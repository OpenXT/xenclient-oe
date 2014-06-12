/*
 * Utility to create a preallocated file
 *
 * Author: Thomas Horsten <thomas.horsten@citrix.com>
 *
 * Copyright (C) 2010 Citrix Systems, Inc.
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

#define _FILE_OFFSET_BITS 64

#include <sys/types.h>

#ifndef USE_POSIX_FALLOC
#define _GNU_SOURCE
#include <unistd.h>
#include <sys/syscall.h>
#endif
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>
#ifdef USE_POSIX_FALLOC
#define _XOPEN_SOURCE 600
#include <fcntl.h>
#endif
#include <errno.h>


#ifndef USE_POSIX_FALLOC
/* Not defined in current glibc */
long _sys_fallocate(int fd, int mode, loff_t offset, loff_t length)
{
  int r;
  int offset_lo = offset & 0xffffffff;
  int offset_hi = (offset >> 32) & 0xffffffff;
  int length_lo = length & 0xffffffff;
  int length_hi = (length >> 32) & 0xffffffff;
  /* printf("Calling syscall(%d, %d, %d, %lld, %lld)\n", SYS_fallocate, 
    fd, mode, offset, length); */
  r = syscall(SYS_fallocate, fd, mode, offset, length);
  /* printf("It returned %d with errno %d\n", r, errno); */
  if (r)
    return errno;
  return r;
}
#endif

int main(int argc, char **argv)
{
  char *fname;
  off_t size;
  int fd;
  int r;
  if (argc != 3) {
    fprintf(stderr, "Usage: %s FILENAME SIZE-IN-MB\n", argv[0]);
    exit(1);
  }
  fname = argv[1];
  size = atol(argv[2]);
  if (size < 1) {
    fprintf(stderr, "Usage: %s FILENAME SIZE-IN-MB\n", argv[0]);
    exit(1);
  }
  fd = creat(fname, S_IRUSR|S_IWUSR);
  if (fd < 0) {
    perror("creat");
    exit(2);
  }
  size *= 1048576;
  /* Try the syscall first. If it fails we use posix_fallocate() which may be slow */
  r = _sys_fallocate(fd, 0, 0, size);
  if (r) {
    errno = r;
    if (errno == EOPNOTSUPP) {
      printf("SYS_falloc not supported on that filesystem. Falling back to manual creation.\n");
      r = posix_fallocate(fd, 0, size);
      if (r) {
	errno = r;
	perror("posix_fallocate");
	exit(3);
      }
    } else {
	perror("_sys_fallocate");
	exit(3);
    }
  }
  close(fd);
  exit(0);
}
