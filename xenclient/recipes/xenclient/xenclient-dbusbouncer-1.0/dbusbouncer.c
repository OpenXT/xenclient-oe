/*
 * DBUS bouncer for XenClient dom0
 *
 * Copyright (C) 2013 Citrix Systems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

#include <stdio.h>
#include <stdarg.h>
#include <errno.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <sys/un.h>
#include <libv4v.h>
#include <signal.h>
#include <xs.h>

#define V4V     1
#define PORT    5555
#define DBUSSOCK "/var/run/dbus/system_bus_socket"

#define dprintf(arg...)
//#define dprintf(arg...) printf(arg)

const char interface[] = "uivm";

static struct xs_handle *xs = NULL;

void sigchl_handler(int sig)
{
  int status = 0;
  waitpid(-1, &status, 0);
  signal(SIGCHLD, sigchl_handler);
}

char *xenstore_read(const char *format, ...)
{
    char                *buff = NULL;
    char                *ret = NULL;
    va_list             arg;
    xs_transaction_t    t;

    va_start(arg, format);
    vasprintf(&buff, format, arg);
    va_end(arg);
    t = xs_transaction_start(xs);
    ret = xs_read(xs, t, buff, NULL);
    xs_transaction_end(xs, t, false);
    free(buff);
    return ret;
}

int domid_of_saddr(struct sockaddr *addr)
{
    if (addr->sa_family == AF_INET) {
        struct sockaddr_in *in = (struct sockaddr_in*) addr;
        char *buf = strrchr(inet_ntoa(in->sin_addr), '.');
        int domid = (int)strtol(++buf, NULL, 10);
        return domid;
    }
    return -1;
}

int uuid_of_domid(char *buf, int domid)
{
    char *path, *uuid;
    path = xenstore_read( "/local/domain/%d/vm", domid );
    if (!path) {
        return 0;
    }
    uuid = xenstore_read( "%s/uuid", path );
    if (!uuid) {
        free( path );
        return 0;
    }
    free( path );
    strncpy(buf, uuid, 64);
    free( uuid );
    return 1;
}

int allowance_test(struct sockaddr *addr)
{
    int domid;
    char uuid[128] = { 0 };
    domid = domid_of_saddr( addr );
    if ( domid < 0 ) {
        return 0;
    }
    /* allow dom0 */
    if ( domid == 0 ) {
        return 1;
    }

    if ( !uuid_of_domid(uuid, domid) ) {
        return 0;
    }
    return strcmp("00000000-0000-0000-0000-000000000001", uuid) == 0
        || strcmp("00000000-0000-0000-0000-000000000002", uuid) == 0;
}

int forward(int rs, int ws)
{
    char buf[8192];
    ssize_t lr, ls;
    while(1) {
	lr = recv(rs, buf, sizeof(buf), 0);
	if (lr<0) {
	    fprintf(stderr, "recv(%d): %m\n", rs);
	    break;
	} else if (lr==0) {
	    dprintf("eof(%d)\n", rs);
	    break;
	}
	ls = send(ws, buf, lr, 0);
	if (ls < 0) {
	    fprintf(stderr, "send(%d): %m\n", ws);
	} else if (ls < lr) {
	    fprintf(stderr, "send(%d) short: read %d sent %d\n", rs, lr, ls);
	    break;
	}
	dprintf("%d->%d: %d bytes\n", rs,ws,lr);
    }
}

int doit(int client)
{
    struct sockaddr_un name;
    int r;
    int server;
    char buf[8192];
    pid_t pid, parentpid;

    server = socket(PF_LOCAL, SOCK_STREAM, 0);
    if (server<0) {
	perror("socket2");
	exit(1);
    }
    name.sun_family = AF_LOCAL;
    strcpy(name.sun_path, DBUSSOCK);
    if (connect(server, (struct sockaddr*)&name, sizeof(name)) < 0) {
	perror("connect-server");
	exit(1);
    }
    parentpid = getpid();
    pid = fork();
    if (pid<0) {
	perror("fork2");
	exit(1);
    } else if (pid==0) {
	forward(client,server);
	dprintf("client->server forwarder died, killing %d\n", parentpid);
	close(client);
	close(server);
	kill(parentpid, SIGTERM);
    } else {
	forward(server,client);
	dprintf("server->client forwarder died, killing %d\n", pid);
	close(server);
	close(client);
    }
}

int main()
{
    int listensock, clientsock;
    struct sockaddr_in name;
    struct sockaddr_in clientname;
    int size;
    int pid;
    int opt;

    signal(SIGCHLD, sigchl_handler);
    xs = xs_domain_open();
    if (!xs) {
        perror("xs_domain_open");
        exit (EXIT_FAILURE);
    }
#if V4V
    listensock = socket(PF_XENV4V, SOCK_STREAM, 0);
#else
    listensock = socket(PF_INET, SOCK_STREAM, 0);
#endif
    if (listensock < 0) {
	perror ("socket");
	exit (EXIT_FAILURE);
    }

    name.sin_family = AF_INET;
    name.sin_port = htons(PORT);
    name.sin_addr.s_addr = htonl(INADDR_ANY);

    opt=1;
    setsockopt(listensock, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(int));

#if !V4V
    if (setsockopt(listensock, SOL_SOCKET, SO_BINDTODEVICE, &interface, strlen(interface))) {
        perror ("setsockopt(SO_BINDTODEVICE)");
	exit (EXIT_FAILURE);
    }
#endif

    if (bind(listensock, (struct sockaddr *)&name, sizeof(name)) < 0) {
	perror ("bind");
	exit (EXIT_FAILURE);
    }

    if (listen(listensock, 1) < 0) {
	perror("listen");
	exit(EXIT_FAILURE);
    }

    while (1) {
	size = sizeof(clientname);
	clientsock = accept(listensock,
			    (struct sockaddr *) &clientname,
			    &size);
	if (clientsock<0) {
	    if (errno == EAGAIN) {
		fprintf(stderr, "Interrupted\n");
		continue;
	    }
	    perror("accept");
	    exit(EXIT_FAILURE);
	}

	pid = fork();
	if (pid < 0) {
	    perror("fork");
	    exit(EXIT_FAILURE);
	} else if (pid == 0) {
	    doit(clientsock);
	    exit(0);
	} else {
	    close(clientsock);
	    dprintf("Spawned pid %d for connection from %s:%d\n",
		    pid, inet_ntoa(clientname.sin_addr), ntohs(clientname.sin_port));
	}
    }
}
