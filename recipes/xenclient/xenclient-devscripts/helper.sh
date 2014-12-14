#!/bin/bash
#
# Copyright (c) 2014 Citrix Systems, Inc.
# 
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#

usage()
{
    echo -e "XenClient dev helper"
    echo -e "Usage: helper.sh option [arguments...]"
    echo -e "  Options :"
    echo -e "    -h                   - this help screen"
    echo -e "    -w <windows_version> - downloads st_<windows_version>.vhd in /storage"
    echo -e "    -f [rows [columns]]  - fixes serial terminal (defaut: 65x239)"
    echo -e "    -e [size in GB]      - expands rootfs (default: 1GB)"
    exit $1
}

wget_windows()
{
    VERSION=$1
    mkdir -p /storage/disks
    cd /storage/disks
    wget http://www/xc_vhds/dev_vhds/sans_pvtools/st_${VERSION}.vhd
}

fix_term()
{
    ROWS=$1
    COLUMNS=$2
    stty rows $ROWS columns $COLUMNS
    kill -WINCH $PPID
}

resize_rootfs()
{
    SIZE=$1
    lvresize -L+${SIZE}G /dev/mapper/xenclient-root
    resize2fs /dev/mapper/xenclient-root
}

[ $# -ge 1 ] || usage 1

case $1 in
    "-h")
	usage 0
	;;
    "-w")
	[ $# -eq 2 ] || usage 1
	wget_windows $2
	;;
    "-f")
	[ $# -le 3 ] || usage 1
	ROWS=65
	COLUMNS=239
	[ $# -ge 2 ] && ROWS=$2
	[ $# -eq 3 ] && COLUMNS=$3
	fix_term $ROWS $COLUMNS
	;;
    "-e")
	[ $# -le 2 ] || usage 1
	SIZE=1
	[ $# -eq 2 ] && SIZE=$2
	resize_rootfs $SIZE
	;;
    *)
	usage 1
	;;
esac

exit 0

