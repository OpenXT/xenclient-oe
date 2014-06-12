#!/bin/sh
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

VENDOR_ID=$(setpci -s 0:0.0 VENDOR_ID)
if [ "${VENDOR_ID}" = "8086" ]; then
    BRIDGE_ID=$(setpci -s 0:0.0 DEVICE_ID)
    case "${BRIDGE_ID}" in
        015?)
            MANUFACTURER=$(dmidecode --type 1 | sed -n -re 's/.*Manufacturer: (.*)$/\1/p' | cut -d ' ' -f 1)
            VERSION=$(dmidecode --type 1 | sed -n -re 's/.*Version: (.*)$/\1/p')
            ;;
    esac
fi
