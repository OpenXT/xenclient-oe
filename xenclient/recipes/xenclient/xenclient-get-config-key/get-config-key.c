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

#include <err.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define PRODUCT_UUID_NULL "00000000-0000-0000-0000-000000000000"
#define PRODUCT_UUID_LEN (sizeof(PRODUCT_UUID_NULL) - 1)

#define SHIFT 0x60
#define E(x) (x - SHIFT)
static char obfuscated_uuid_path[] = {
  E('/'), E('s'), E('y'), E('s'),
  E('/'), E('c'), E('l'), E('a'), E('s'), E('s'),
  E('/'), E('d'), E('m'), E('i'),
  E('/'), E('i'), E('d'),
  E('/'), E('p'), E('r'), E('o'), E('d'), E('u'), E('c'), E('t'),
  E('_'), E('u'), E('u'), E('i'), E('d'),
};

int
get_product_uuid(char **uuid)
{
  FILE *f;
  char uuid_path[sizeof(obfuscated_uuid_path)];
  int i, ret;

  *uuid = malloc(PRODUCT_UUID_LEN + 1);
  if (*uuid == NULL) {
    warnx("calloc");
    return 1;
  }

  for (i = 0; i < sizeof(obfuscated_uuid_path); i++)
    uuid_path[i] = obfuscated_uuid_path[i] + SHIFT;
  uuid_path[sizeof(obfuscated_uuid_path)] = 0;

  f = fopen(uuid_path, "r");
  if (f == NULL)
    goto fail;

  ret = fread(*uuid, PRODUCT_UUID_LEN, 1, f);
  if (ret != 1)
    goto fail;

  fclose(f);
  (*uuid)[PRODUCT_UUID_LEN] = 0;

  return 0;

 fail:
  if (f)
    fclose(f);
  strcpy(*uuid, PRODUCT_UUID_NULL);

  return 0;
}


int
main(int argc, char **argv)
{
  char *uuid;
  int ret;

  ret = get_product_uuid(&uuid);
  if (ret)
    errx(1, "failed");

  puts(uuid);

  return 0;
}
