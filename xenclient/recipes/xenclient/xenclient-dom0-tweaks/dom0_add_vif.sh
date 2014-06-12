#! /bin/sh
#
# Copyright (c) 2011 Citrix Systems, Inc.
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

local_mac="$1"

while true; do
        backend=`xenstore-read "backend-domid"`
        [ "$?" -eq 0 ] && break
done

while true; do
        timeout 5 xenops add_vif -backend-domid "$backend" -domid 0 -mac "$local_mac" -devid 0
        [ "$?" -eq 0 ] && break;
        xenops del_vif -backend-domid "$backend" -domid 0 -mac "$local_mac" -devid 0
done

ifconfig eth0 inet 0.0.0.0
if [ ! -e /config/system/dom0-networking-disabled ]; then
        ifconfig eth0 up
        exec udhcpc -i eth0
fi
