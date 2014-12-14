/*
 * Copyright (c) 2010 Citrix Systems, Inc.
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

#define _XOPEN_SOURCE 600
#define _LARGEFILE64_SOURCE
#define _GNU_SOURCE

#include <err.h>
#include <unistd.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define DEF_BSIZE 256
#define AUNIT (8*512)
#define BUNIT 512
static char *buffer;

static char *_progname;

void
usage(void)
{
  fprintf(stderr,
	  "usage: %s -s src -d dest -o offset -c count [-b buffer size]\n",
	  _progname);
  fprintf(stderr, "Note: all units are in 512 byte sectors\n");
  exit(1);
}

int
main(int argc, char **argv)
{
  uint32_t bsize = DEF_BSIZE * BUNIT, boff;
  int64_t offset = -1, count = -1;
  char *srcname = NULL, *destname = NULL;
  int src, dest;
  uint32_t c, rc, wc;
  int ret;

  _progname = argv[0];

  if (argc < 1)
    usage();

  optind = 0;
  while ((c = getopt(argc, argv, "s:d:o:c:b:h")) != -1) {
    switch (c) {
    case 's':
      srcname = optarg;
      break;
    case 'd':
      destname = optarg;
      break;
    case 'o':
      offset = atoll(optarg) * BUNIT;
      break;
    case 'c':
      count = atoll(optarg) * BUNIT;
      break;
    case 'b':
      bsize = atol(optarg) * BUNIT;
      break;
    case 'h':
    default:
      usage();
    }
  }

  if (srcname == NULL)
    errx(1, "no source specified");
  if (destname == NULL)
    errx(1, "no destination specified");
  if (offset == -1)
    errx(1, "no offset specified");
  if (count == -1)
    errx(1, "no count specified");

  ret = posix_memalign((void **)&buffer, AUNIT, bsize);
  if (ret == -1)
    err(1, "posix_memalign");

  src = open(srcname, O_RDONLY | O_LARGEFILE | O_DIRECT);
  if (src == -1)
    err(1, "open src");
  dest = open(destname, O_WRONLY | O_LARGEFILE | O_DIRECT);
  if (dest == -1)
    err(1, "open dest");

  ret = lseek64(src, offset, SEEK_SET);
  if (ret == -1)
    err(1, "lseek src");
  ret = lseek64(dest, offset, SEEK_SET);
  if (ret == -1)
    err(1, "lseek dest");

  c = bsize - (offset % bsize);
  if (c > count)
    c = count;

  while (count > 0) {
    rc = read(src, buffer, c);
    if (rc == -1)
      err(1, "read");
    if (rc == 0)
      errx(1, "read oops");
    boff = 0;
    do {
      wc = write(dest, buffer + boff, rc);
      if (wc == -1)
	err(1, "write");
      if (wc == 0)
	errx(1, "write oops");
      count -= wc;
      rc -= wc;
      boff += wc;
    } while (rc > 0);
    c = bsize;
    if (c > count)
      c = count;
  }

  return 0;
}
