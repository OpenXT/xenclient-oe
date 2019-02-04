#!/bin/sh
#
# Copyright (c) 2013 Citrix Systems, Inc.
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

# Forbid Montevina platforms
if [ "$(setpci -s 0:2.0 VENDOR_ID)" = "8086" ] ; then
    IGFX_DEVICE_ID="$(setpci -s 0:2.0 DEVICE_ID)"
    case ${IGFX_DEVICE_ID} in
        20e4|2a42|2e12)
            echo "This system is a Montevina platform which is not supported.">&2
            exit 2
        ;;
    esac
fi
