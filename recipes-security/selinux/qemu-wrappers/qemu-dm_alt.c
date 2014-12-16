/*
 * Copyright (c) 2011 Citrix Systems, Inc.
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

#include <selinux/selinux.h>
#include <unistd.h>
#include <error.h>

static char* bin = "/usr/lib/xen/bin/qemu-dm";

int main (int argc, char* argv[])
{
  if (setexeccon("system_u:system_r:qemu_alt_t") != 0)
	perror("qemu-dm_alt");
  argv[0] = bin;
  execve (bin, argv, NULL);
}
