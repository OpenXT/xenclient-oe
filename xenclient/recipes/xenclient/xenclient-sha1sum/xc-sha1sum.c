/*
 * Copyright (C) 2010 Citrix Systems, Inc.
 * All Rights Reserved.
 *
 */

/*
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


#include <sys/types.h>
#include <sys/stat.h>
#include <err.h>
#include <fcntl.h>
#include <unistd.h>

#include <openssl/evp.h>

static unsigned char *line;
static int linelen, linemax;

int
main(int argc, char **argv)
{
  unsigned char msg[EVP_MAX_MD_SIZE];
  unsigned int msglen;
  EVP_MD_CTX ctx;
  int fdin;
  int i;

  if (argc > 1) {
    fdin = open(argv[1], O_RDONLY);
    if (fdin < 0)
      err(1, "open failed");
  } else
    fdin = 0;

  linemax = EVP_MD_block_size(EVP_sha1()) * 16 * 1024 * 8;
  line = malloc(linemax);
  if (line == NULL)
    err(1, "mallo");

  EVP_DigestInit(&ctx, EVP_sha1());
  while ((linelen = read(fdin, line, linemax)) > 0)
    EVP_DigestUpdate(&ctx, line, linelen);
  EVP_DigestFinal(&ctx, msg, &msglen);

  for (i = 0; i <msglen; i++)
    printf("%02x", msg[i]);
  printf("\n");

  return 0;
}
