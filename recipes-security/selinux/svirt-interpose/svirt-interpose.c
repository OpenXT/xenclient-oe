/*
 * Copyright (c) 2012 Citrix Systems, Inc.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

#define _GNU_SOURCE

#include <fcntl.h>
#include <selinux/context.h>
#include <selinux/selinux.h>
#include <stdio.h>
/*  need stdlib.h for size_t used in xs.h  */
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>
#include <unistd.h>
#include <xenstore.h>

#define BUF_SIZE      256
#define LOCAL_DOMAINS "/local/domain"
/* #define QEMU          "/opt/xensource/libexec/qemu-dm-wrapper-old" */
#define QEMU          "/usr/bin/qemu-dm-wrapper"
#define RAND_DEV      "/dev/random"

typedef struct data data_t;
typedef struct xs_handle xs_handle_t;

static int      create_category            (xs_handle_t*);
static char*    create_context             (char*, char*);
static char**   do_directory               (xs_handle_t*, char*, unsigned*);
static char*    do_read                    (xs_handle_t*, char*);
static bool     do_write                   (xs_handle_t*, char*, char*);
static void     exec_cmd                   (char**);
static int      file_con_fixup             (data_t*);
static int      get_default_contexts       (data_t*);
static int      get_domid_by_mcs           (xs_handle_t*, uint16_t);
static char**   get_vbd_nums               (xs_handle_t*, int, int*);
static char*    get_vbd_backend            (xs_handle_t*, char*);
static char*    get_vbd_file               (xs_handle_t*, char*);
static char**   get_writable_files         (xs_handle_t*, int);
static int      read_single_context        (char*, const char*, size_t);
static bool     set_domid_category         (xs_handle_t*, int, uint16_t);
static bool     set_exec_context           (data_t*);
static bool     vbd_is_writable            (xs_handle_t*, char*);

struct data {
        int domid;
        char **files;
        uint16_t category;
        char domain_context [BUF_SIZE];
        xs_handle_t *xsh;
};

int
main (int argc, char **argv)
{
        data_t data = { 0, };
        int retval = EXIT_SUCCESS, i = 0, cat_result = 0;

        openlog (argv[0], LOG_NOWAIT | LOG_PID, LOG_DAEMON);
        if (argc < 2) {
                syslog (LOG_EMERG, "argc is less than 2, that's very wrong");
                retval = EXIT_FAILURE;
                goto exit;
        }
        if (is_selinux_enabled () != 1) {
                syslog (LOG_WARNING, "SELinux is disabled. sVirt will do nothing.");
                goto exit;
        }
        /*  not really parsing parameters, just going on position  */
        data.domid = atoi (argv [2]);
        syslog (LOG_INFO, "domain id: %d", data.domid);

        data.xsh = xs_daemon_open();
        if (data.xsh == NULL) {
                syslog (LOG_CRIT, "ERROR connecting to XenStore. Halting");
                retval = EXIT_FAILURE;
                goto exit;
        }
        /*  get files that we need to relabele  */
        data.files = get_writable_files (data.xsh, data.domid);
        if (data.files == NULL) {
                syslog (LOG_CRIT, "ERROR getting files. Halting");
                retval = EXIT_FAILURE;
                goto exit_session;
        }
        for (i = 0; data.files [i] != NULL; ++i)
                syslog (LOG_INFO, "got file: %s", data.files [i]);
        /*  get category for our domid and save it to xenstore  */
        cat_result = create_category (data.xsh);
        if (cat_result < 0) {
                syslog (LOG_CRIT, "ERROR generating unique category. Halting");
                retval = EXIT_FAILURE;
                goto exit_files;
        }
        data.category = cat_result;
        syslog (LOG_INFO, "got unique mcs: %d", data.category);
        if (set_domid_category (data.xsh, data.domid, data.category) == false) {
                syslog (LOG_CRIT, "ERROR setting category. Halting");
                retval = EXIT_FAILURE;
                goto exit_files;
        }
        /*  SELinux stuff  */
        /*  get SELinux default contexts  */
        if (get_default_contexts (&data) != 0) {
                syslog (LOG_CRIT, "ERROR getting default contexts. Halting");
                retval = EXIT_FAILURE;
                goto exit_files;
        }
        /*  label files  */
        if (file_con_fixup (&data) != 0) {
                syslog (LOG_CRIT,
                        "ERROR setting contexts for VM device files. Halting");
                retval = EXIT_FAILURE;
                goto exit_files;
        }
        /*  Set Execution Context  */
        if (set_exec_context (&data) != true) {
                syslog (LOG_CRIT,
                        "ERROR setting context to %s for qemu execution: %s. Halting",
                        strerror (errno));
                retval = EXIT_FAILURE;
                goto exit_files;
        }
        syslog (LOG_INFO, "Successfully set set MCS label %d for domid %d",
                data.category, data.domid);
exit_files:
        if (data.files != NULL) {
                for (i = 0; data.files [i] != NULL; ++i)
                        free (data.files [i]);
                free (data.files);
        }
exit_session:
        if (data.xsh != NULL) 
                xs_daemon_close (data.xsh);
exit:
        closelog ();
        /*  execute the real qemu if no previous errors prevent it  */
        if (retval != EXIT_FAILURE)
                exec_cmd (argv);
        exit (retval);
}
/*  Build a context from the domain_context and category fields of the data_t
 *  structure.
 *  Use the resultant context as the execution context (setexeccon) for the
 *  next call to exec.
 */
static bool
set_exec_context (data_t* data)
{
        char mcs_str [9] = { 0, }, *context = NULL;
        int p_ret = 0;

        p_ret = snprintf (mcs_str, sizeof (mcs_str), "s0:c%d", data->category);
        if (p_ret < 0 || p_ret > 9) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return false;
        }
        context = create_context (data->domain_context, mcs_str);
        if (context == NULL) {
                syslog (LOG_CRIT, "error creating context from %s and %s",
                        data->domain_context, mcs_str);
                return false;
        }
        syslog (LOG_INFO, "Setting execution context to %s", context);
        if (setexeccon(context) == -1) {
                syslog (LOG_CRIT, "setexeccon: %s", strerror (errno));
                return false;
        }
        free (context);
        return true;
}
/*  Set the context of all files associated with this VM to the new context
 *  complete with the unique generated category.
 */
static int
file_con_fixup (data_t *data)
{
        security_context_t sec_con = { 0, };
        context_t con = { 0, };
        char mcs_str[9] = { 0, };
        int ret = 0, p_ret = 0, i = 0;;
        
        p_ret = snprintf (mcs_str, sizeof (mcs_str), "s0:c%d", data->category);
        if (p_ret < 0 || p_ret > 9) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return -1;
        }
        for (i = 0; data->files [i] != NULL; ++i) {
                if (getfilecon (data->files [i], &sec_con) == -1) {
                        syslog (LOG_CRIT,
                                "error getting context from file: %s, error %s",
                                data->files [i], strerror (errno));
                        continue;
                }
                con = context_new (sec_con);
                if (con == NULL) {
                        syslog (LOG_CRIT, 
                                "Error creating new context from string: %s",
                                sec_con);
                        ret = -1;
                        goto err_freecon;
                }
                if (context_range_set (con, mcs_str) == -1) {
                        syslog (LOG_CRIT, 
                                "Error setting context range to %s, "
                                "error: %s", mcs_str, strerror (errno));
                        ret = -1;
                        goto err_confree;
                }
                syslog (LOG_INFO, "Setting context for file %s to %s",
                        data->files [i], context_str (con));
                ret = setfilecon (data->files [i], context_str (con));
                if (ret != 0)
                        syslog (LOG_CRIT, "setfilecon error:%s",
                                strerror (errno));
                context_free (con);
                freecon (sec_con);
        }
        return ret;

 err_confree:
        context_free (con);
 err_freecon:
        freecon (sec_con);
        return ret;
}
/*  Gets the default context for virtualization processes and populates
 *  the data_t structure accordingly.
 */
static int
get_default_contexts (data_t *data)
{
        int ret = 0;

        ret = read_single_context (data->domain_context,
                                   selinux_virtual_domain_context_path (),
                                   sizeof (data->domain_context));
        if (ret != 0) {
                syslog (LOG_CRIT, "read single failed. ret: %d", ret);
                return ret;
        }
        return 0;
}
static bool
set_domid_category (xs_handle_t *xsh, int domid, uint16_t mcs)
{
        char path_buf [BUF_SIZE] = { 0, }, data_buf [BUF_SIZE] = { 0, };
        int ret = 0;

        ret = snprintf (path_buf,
                        sizeof (path_buf),
                        "%s/%d/selinux-mcs",
                        LOCAL_DOMAINS,
                        domid);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return false;
        }
        ret = snprintf (data_buf,
                        sizeof (data_buf),
                        "%d",
                        mcs);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return false;
        }
        return do_write (xsh, path_buf, data_buf);
}
/*  returns a unique integer representing the SELinux category
 *  Value will be between 1 and 1023.  0 is reserved for the system to assign
 *    to files beloning to VMs that are not curretly running (default category).
 *  A negative result indicates an error.
 */
static int
create_category (xs_handle_t *xsh)
{
        int fd = 0, ret = 0, domid = 0;
        char *val = NULL;
        /*  current SELinux MCS uses 1024 categories: 0 - 1023  */
        uint16_t random = 0;

        /*  generate random category number  */
        fd = open (RAND_DEV, O_RDONLY);
        if (fd == -1) {
                syslog (LOG_CRIT, "error opening %s: %s", RAND_DEV, strerror (errno));
                return -1;
        }
        do {
                if (val)
                        free (val);
                ret = read (fd, &random, sizeof (random));
                if (ret != sizeof (random)) {
                        if (ret == -1) {
                                syslog (LOG_CRIT,
                                        "error reading from %s: %s",
                                        RAND_DEV,
                                        strerror (errno));
                                return -1;
                        } else
                                continue;
                }
                /*  bound value between 1 and 1023 inclusive  */
                /*  Useful for test-cases.  Generates numbers that will cause
                 *    collisions.
                 *  random = (rand () % 1023) + 1;
                 */
                random = (random % 1023) + 1;
                ret = get_domid_by_mcs (xsh, random);
                if (ret == -2) {
                        syslog (LOG_CRIT,
                                "unrecoverable error from get_domid_by_mcs");
                        return -1;
                }
        } while (ret != -1);
        close (fd);
        /*  return integer value  */
        return random;
}
/*  find domain assigned the parameter mcs category
 *  return >= 0 (0 is valuid domid) on success
 *  return -1 if nothing found
 *  return -2 on error
 */
static int
get_domid_by_mcs (xs_handle_t *xsh, uint16_t cat)
{
        int ret = 0, i = 0, cat_int = 0, domid = -1;
        char data [BUF_SIZE] = { 0, }, **domids = NULL, *cat_str = NULL;
        unsigned len = 0;
        uint16_t cat_uint16 = 0;

        /*  cycle through /local/domain/# */
        syslog (LOG_DEBUG, "searching for domid with mcs: %d", cat);
        domids = do_directory (xsh, LOCAL_DOMAINS, &len);
        if (!domids) {
                syslog (LOG_CRIT, "do_directory failed on: %s", LOCAL_DOMAINS);
                return -2;
        }
        for (i = 0; i < len; ++i) {
                /*  reading contents of /local/domain/#/selinux-mcs  */
                ret = snprintf (data,
                                sizeof (data),
                                "%s/%s/selinux-mcs",
                                LOCAL_DOMAINS,
                                domids [i]);
                if (ret < 0 || ret > BUF_SIZE) {
                        syslog (LOG_CRIT, "insufficient buffer size");
                        return -2;
                }
                cat_str = do_read (xsh, data);
                /*  if read fails assume domain has no mcs set and continue
                 *    search
                 */
                if (!cat_str) {
                        syslog (LOG_WARNING,
                                "do_read failed on: %s.  Carrying on.",
                                data);
                        continue;
                }
                cat_int = atoi (cat_str);
                if (cat_int < 1 || cat_int > 1023) {
                        syslog (LOG_CRIT,
                                "value at %s is inconsistent: %d < 1 || %d > 1023",
                                data,
                                cat_int,
                                cat_int);
                        free (cat_str);
                        return -2;
                }
                cat_uint16 = cat_int;
                /*    if value returned == cat return domain ID (# in outer loop)  */
                if (cat_uint16 == cat) {
                        free (cat_str);
                        domid = atoi (domids [i]);
                        syslog (LOG_INFO,
                                "found dom with mcs %d: %s",
                                cat_uint16,
                                data);
                        break;
                }
                free (cat_str);
        }
free_ids:
        if (domids)
                free (domids);
        return domid;
}
/*  returns all files that need to have labels set
 */
static char**
get_writable_files (xs_handle_t *xsh, int domid)
{
        char **vbd_paths_front = NULL, **writable_files = NULL, *vbd_back_tmp = NULL;
        unsigned vbd_front_count = 0, i = 0, j = 0;
        /*  get paths to vbds basically list /local/domain/$domid/backend/vbd  */
        vbd_paths_front = get_vbd_nums (xsh, domid, &vbd_front_count);
        if (vbd_paths_front == NULL)
                return NULL;
        writable_files = calloc (vbd_front_count + 1, sizeof (char*));
        /*  iterate over vbds to find those that are writable  */
        for (i = 0, j = 0; i < vbd_front_count; ++i) {
                vbd_back_tmp = get_vbd_backend (xsh, vbd_paths_front [i]);
                if (vbd_is_writable (xsh, vbd_back_tmp)) {
                        syslog (LOG_INFO, "%s is writable", vbd_back_tmp);
                        /*  if vbd is writable, get the file on disk that's
                         *  backing it
                         */
                        writable_files [j] = get_vbd_file (xsh, vbd_back_tmp);
                        if (writable_files [j] == NULL)
                                syslog (LOG_CRIT,
                                        "Error getting file backing vbd backend %s",
                                        vbd_back_tmp);
                        else
                                ++j;
                }
                free (vbd_back_tmp);
                free (vbd_paths_front [i]);
        }
        free (vbd_paths_front);
        return writable_files;
}
/*  QEMU doesn't access files directly.  VHDs are accessed through blktap
 *    devices.  Raw swap disks used by service VMs are accessed through loopback
 *    devices.  This function returns the path to the device that the QEMU
 *    instances is accessing the block device through.
 *  The path parameter is a xenstore path to a VBD:
 *    /local/domain/#/backend/#/vbd3/# where 
 *    The first # is the domid of the backend hosting the device.
 *    The second # is the domid of the frontend using the device.
 *    The third # is the identifier given to the vbd (vbd number).
 *  In this VBD path the entry "loop-device" will hold the path to a loopback
 *    device accessed by QEMU.  If this isn't present the "params" entry will
 *    contain the path to either a tapdev or a raw file.
 */
static char*
get_vbd_file (xs_handle_t *xsh, char *path)
{
        char path_buf [BUF_SIZE] = { 0, }, *ret_buf = NULL;
        int ret = 0;
	/*  check first for loopback device  */
	ret = snprintf (path_buf, BUF_SIZE, "%s/loop-device", path);
	if (ret < 0 || ret > BUF_SIZE) {
		syslog (LOG_CRIT, "insufficient buffer size");
		return NULL;
	}
	ret_buf = do_read (xsh, path_buf);
	if (ret_buf)
		return ret_buf;
	/*  if not loop-back look for file/tapdev in params field  */
        ret = snprintf (path_buf, BUF_SIZE, "%s/params", path);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return NULL;
        }
        return do_read (xsh, path_buf);
}
/*  return the path to the vbd backend for the parameter vbd  */
static char*
get_vbd_backend (xs_handle_t *xsh, char *path)
{
        char path_buf [BUF_SIZE] = { 0, };
        int ret = 0;

        ret = snprintf (path_buf, BUF_SIZE, "%s/backend", path);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return NULL;
        }
        return do_read (xsh, path_buf);
}
/*  determine whether a vbd device assigned to a vm is writable
 */
static bool
vbd_is_writable (xs_handle_t *xsh, char *path)
{
        char path_buf [BUF_SIZE] = { 0, }, *val = NULL;
        int ret = 0;

        syslog (LOG_INFO, "checking mode of %s", path);
        ret = snprintf (path_buf, BUF_SIZE, "%s/mode", path);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                return false;
        }
        val = do_read (xsh, path_buf);
        if (val == NULL)
                return false;
        ret = strncmp (val, "w", strlen ("w"));
        free (val);
        if (ret == 0)
                return true;
        else
                return false;
}
/*  Get paths to vbd numbers for all backend devices  */
static char**
get_vbd_nums (xs_handle_t *xsh, int domid, int *vbd_num)
{
        char path_buf [BUF_SIZE] = { 0, }, **vbd_nums = NULL, **vbd_paths = NULL;
        int ret = 0, i = 0;

        ret = snprintf (path_buf,
                        BUF_SIZE,
                        "%s/%d/device/vbd",
                        LOCAL_DOMAINS,
                        domid);
        if (ret < 0 || ret > BUF_SIZE) {
                syslog (LOG_CRIT, "insufficient buffer size");
                goto exit;
        }
        vbd_nums = do_directory (xsh, path_buf, vbd_num);
        if (vbd_nums == NULL)
                goto exit;
        vbd_paths = calloc (*vbd_num, sizeof (char*));
        if (vbd_paths == NULL) {
                syslog (LOG_CRIT, "calloc failed: %s", strerror (errno));
                goto exit_free;
        }
        for (i = 0; i < *vbd_num; ++i) {
                vbd_paths [i] =
                        calloc (1,
                                strlen (path_buf) +
                                strlen (vbd_nums [i]) + 1);
                sprintf (vbd_paths [i], "%s/%s", path_buf, vbd_nums [i]);
        }
exit_free:
        free (vbd_nums);
exit:
        return vbd_paths;
}
/*  Wrapper around fopen/getline calls to read a single line from a file.  This
 *  is necessary in order to read default contexts from virtualization specific
 *  files in /etc/selinux/policy/contexts/
 */
static int
read_single_context (char* buf, const char* file_path, size_t size)
{
        FILE* fstream = { 0, };
	char* tmp;

        fstream = fopen(file_path, "r");
        if (fstream == NULL) {
                syslog (LOG_CRIT, "error opening file %s: %s", file_path, strerror (errno));
                return -1;
        }
        if (getline(&buf, &size, fstream) == -1) {
                syslog (LOG_CRIT, "error getting line from %s: %s", file_path, strerror (errno));
                fclose (fstream);
                return -1;
        }
        fclose (fstream);
        /*  Contents of file may have trailing new line after context.
         *  The context_* functions require that this be removed.
         */
	tmp = strchrnul (buf, '\n');
        *tmp = '\0';
        return 0;
}
/*  wrapper around xs_write
 */
static bool
do_write (xs_handle_t *xsh, char *path, char *data)
{
        static struct expanding_buffer ebuf = { 0, };
        unsigned len;

        expanding_buffer_ensure (&ebuf, strlen (data) + 1);
        unsanitise_value (ebuf.buf, &len, data);
        if (!xs_write (xsh, 0, path, ebuf.buf, len)) {
                syslog (LOG_WARNING,
                        "could not write %s to path %s",
                        data,
                        path);
                return false;
        }
        return true;
}
/*  wrapper around xs_read
 */
static char*
do_read (xs_handle_t *xsh, char* path)
{
        char *val = NULL, *san_val = NULL, *tmp = NULL;
        static struct expanding_buffer ebuf = { 0, };
        unsigned len = 0;

        val = xs_read (xsh, 0, path, &len);
        if (val == NULL) {
                syslog (LOG_WARNING,
                        "xs_read on %s returned null",
                        path);
                return NULL;
        }
        san_val = sanitise_value (&ebuf, val, len);
        if (san_val == NULL) {
                syslog (LOG_CRIT, "sanitise_value returned NULL");
                free (val);
                return NULL;
        }
        tmp = strdup (san_val);
        /*  don't free san_val  */
        free (val);
        return tmp;
}
/*  wrapper around directory listing  
 *  not sure if these values need to be sanitized
 */
static char**
do_directory (xs_handle_t *xsh, char* path, unsigned *len)
{
        char **dir_vals = NULL;

        dir_vals = xs_directory (xsh, 0, path, len);
        if (dir_vals == NULL) {
                syslog (LOG_WARNING, "xs_directory failed on %s\n", path);
                return NULL;
        }
        return dir_vals;
}
/*  Final function called to execute QEMU.  Does some basic cleanup as well.
 */
static void
exec_cmd (char** argv)
{
        argv [0] = QEMU;
        execve (QEMU, argv, NULL);
        perror ("exec");
}
/*  Basic function to build string representation of context from
 *  user:role:type component and sensitivity:category.
 *  Returned string must be free'd by caller.
 */
static char*
create_context (char *oldcontext, char *mcs)
{
        char *newcontext = NULL, *scontext = NULL;
        context_t con = { 0, };

        scontext = strdup(oldcontext);
        if (!scontext)
                return scontext;
        con = context_new(scontext);
        if (!con) {
                perror ("context_new");
                return NULL;
        }

        context_range_set(con, mcs);
        newcontext = strdup(context_str(con));
        context_free(con);
        return (newcontext);
}
