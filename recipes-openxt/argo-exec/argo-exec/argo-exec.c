#define _GNU_SOURCE
#include <errno.h>
#include <fcntl.h>
#include <getopt.h>
#include <poll.h>
#include <signal.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <unistd.h>

#include <libargo.h>
#include <xen/xen.h>

/* child to remote and remote to child buffers */
static char c2r_buf[4096], r2c_buf[4096];
ssize_t c2r_sz, r2c_sz;

pid_t my_pid;
pid_t child, waited_child;
static int child_running;

static
void sig_child()
{
	int wstatus;

	waited_child = waitpid(-1, &wstatus, WNOHANG);
	if (waited_child == 0 && WIFEXITED(wstatus)) {
		child_running--;
	}
}

static
pid_t exec_cmd(char *argv[], int fds[2])
{
	int fd_stdin[2], fd_stdout[2];
	pid_t pid;
	int ret;

	ret = pipe2(fd_stdin, O_CLOEXEC);
	if (ret) {
		perror("pipe2 stdin");
		return -1;
	}
	fds[1] = fd_stdin[1];

	ret = pipe2(fd_stdout, O_CLOEXEC);
	if (ret) {
		perror("pipe2 stdout");
		return -1;
	}
	fds[0] = fd_stdout[0];

	pid = fork();
	switch (pid) {
	case 0:
		/* child */
		my_pid = getpid();
		if (fd_stdin[0] != 0) {
			dup2(fd_stdin[0], 0);
			close(fd_stdin[0]);
		}
		if (fd_stdout[1] != 1) {
			dup2(fd_stdout[1], 1);
			close(fd_stdout[1]);
		}
		fprintf(stderr, "%d: execvp(%s..)\n", my_pid, argv[0]);
		ret = execvp(argv[0], argv);
		perror("execvp");
		return ret;
		break;
	case -1:
		perror("fork exec_cmd");
		return -1;
		break;
	default:
		close(fd_stdin[0]);
		close(fd_stdout[1]);
		printf("%d: Forked child %d running %s\n", my_pid, pid,
		       argv[0]);
		child_running++;
		return pid;
		break;
	}
}

static
ssize_t write_all(int fd, void *buf, ssize_t len)
{
	ssize_t sz = 0;
	ssize_t ret;

	while (sz < len) {
		ret = write(fd, buf + sz, len - sz);
		if ( ret < 0 ) {
			return ret;
		}
		sz += ret;
	}

	return 0;
}

static
ssize_t argo_send_all(int s, void *buf, ssize_t len)
{
	ssize_t sz = 0;
	ssize_t ret;

	while (sz < len) {
		ret = argo_send(s, buf + sz, len - sz, 0);
		if ( ret < 0 ) {
			return ret;
		}
		sz += ret;
	}

	return 0;
}

static
int shuffle(int s, int c2r, int r2c)
{
	fd_set _fdset_base, *fdset_base = &_fdset_base;
	fd_set _fdset,      *fdset      = &_fdset;
	int nfds = 2;
	int ret;

	FD_ZERO(fdset_base);
	FD_SET(s, fdset_base);
	FD_SET(c2r, fdset_base);

	while (child_running && nfds) {
		int maxfd;

		*fdset = *fdset_base;

		maxfd = (s > c2r ? s : c2r) + 1;

		ret = select(maxfd, fdset, NULL, NULL, NULL);
		if (ret == -1) {
			if (errno != EINTR) {
				perror("select");
				return -1;
			} else {
				printf("%d: EINTR - child_running=%d\n", my_pid,
				       child_running);
				continue;
			}
		}

		if (ret == 0) {
			printf("select timeout\n");
			continue;
		}

		if ( FD_ISSET(s, fdset) ) {
			r2c_sz = argo_recv(s, r2c_buf, sizeof(r2c_buf), 0);
			if (r2c_sz == 0) {
				//maxfd = c2r + 1;
				fprintf(stderr, "%d: closing fd s=%d\n", my_pid,
				        c2r);
				FD_CLR(s, fdset_base);
				nfds--;
				return 0;
			}

			ret = write_all(r2c, r2c_buf, r2c_sz);
			if (ret < 0) {
				perror("write_all");
				return -1;
			}
		}

		if ( FD_ISSET(c2r, fdset) ) {
			c2r_sz = read(c2r, c2r_buf, sizeof(c2r_buf));
			if (c2r_sz == 0) {
				//maxfd = s + 1;
				fprintf(stderr, "%d: closing fd c2r=%d\n",
				        my_pid, c2r);
				FD_CLR(c2r, fdset_base);
				nfds--;
				return 0;
			}

			ret = argo_send_all(s, c2r_buf, c2r_sz);
			if (ret < 0) {
				perror("argo_send_all");
				return -1;
			}
		}
	}

	return 0;
}

static
int spawn_child(char *argv[], int s)
{
	int wstatus;
	int fds[2] = { -1, -1 };
	int ret;

	signal(SIGCHLD, sig_child);

	child = exec_cmd(argv, fds);

	ret = shuffle(s, fds[0], fds[1]);
	if (ret) {
		perror("shuffle");
	}

	if (child_running) {
		ret = kill(child, SIGTERM);
	}

	close(fds[0]);
	close(fds[1]);
	argo_close(s);

	waitpid(child, &wstatus, 0);

	printf("%d: child %d exited %d\n", my_pid, child, WIFEXITED(wstatus));

	return 0;
}

static
int accept_loop(int s, xen_argo_addr_t addr, domid_t domid) {
	int ret;

	int fd;
	xen_argo_addr_t peer;

	addr.domain_id = XEN_ARGO_DOMID_ANY;

	ret = argo_bind(s, &addr, domid);
	if (ret == -1) {
		perror("argo_bind");
		return -1;
	}

	ret = argo_listen(s, 1);
	if (ret == -1) {
		perror("argo_listen");
		return -1;
	}

	signal(SIGCHLD, sig_child);

	while (1) {
		fd = argo_accept(s, &peer);
		if (fd < 0) {
			if (errno == EINTR) {
				if (waited_child) {
					printf("%d: EINTR - child %d exited\n",
					       my_pid, waited_child);
					waited_child = 0;
				}
				continue;
			}

			perror("argo_accept");
			return -1;
		}

		printf("%d: accepted connection from dom %d:%u\n", my_pid,
		       peer.domain_id, peer.aport);

		child = fork();
		switch (child) {
		case -1:
			perror("fork accept_loop");
			exit(1);
			break;
		case 0:
			/* child */
			my_pid = getpid();
			argo_close(s);
			s = fd;
			return s;
			break;
		default:
			/* parent */
			argo_close(fd);
			printf("%d: spawned child %d\n", my_pid, child);
			child_running++;
			break;
		}
	}
}

static
void usage(char *prog)
{
	printf(
"%s: -p port <-l | -d domid> -- cmd args ...\n"
"\t-p port - for argo comms\n"
"\t-l - listen for an argo connection on port\n"
"\t-d domid - Remote domid for argo connect\n"
"\tcmd args - command and arguments to exec.\n"
"\t stdin and stdout will be transfered over the argo connection.\n", prog);
}

int main(int argc, char *argv[])
{
	int s;
	int opt;
	int ret;
	int port = 0;
	int index;
	int listen = 0;
	domid_t domid = XEN_ARGO_DOMID_ANY;
	xen_argo_addr_t addr = {};

	struct option opts[] = {
		{"domid",  required_argument, NULL, 'd'},
		{"listen",       no_argument, NULL, 'l'},
		{"port",   required_argument, NULL, 'p'},
		{}
	};

	my_pid = getpid();
	setbuf(stdout, NULL);

	while ( (opt = getopt_long(argc, argv, "d:lp:", opts, &index)) != -1 )
	{
		switch (opt)
		{
		case 'd':
			domid = strtoul(optarg, NULL, 0);
			break;
		case 'l':
			listen = 1;
			break;
		case 'p':
			port = strtoul(optarg, NULL, 0);
			break;
		case '?':
			printf("unknown option %c\n", optopt);
			return -1;
		}
	}

	if (port == 0 ||
	    (listen == 0 && domid == XEN_ARGO_DOMID_ANY)) {
		usage(argv[0]);
		return -1;
	}

	s = argo_socket(SOCK_STREAM | SOCK_CLOEXEC);
	if (s < 0) {
		perror("argo_socket");
		return -1;
	}

	if (listen) {
		addr.aport = port;
		s = accept_loop(s, addr, domid);
	} else {
		addr.domain_id = XEN_ARGO_DOMID_ANY;
		/* bind local ring to random port */
		addr.aport = 0;

		ret = argo_bind(s, &addr, domid);
		if (ret == -1) {
			perror("argo_bind");
			return -1;
		}

		addr.domain_id = domid;
		addr.aport = port;

		ret = argo_connect(s, &addr);
		if (ret == -1) {
			perror("argo_connect");
			return -1;
		}
	}

	return spawn_child(&argv[optind], s);
}
