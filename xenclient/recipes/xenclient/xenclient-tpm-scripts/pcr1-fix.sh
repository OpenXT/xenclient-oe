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

root="$1"
config_pcrs="${root}/config/config.pcrs"

[ ! -f "${config_pcrs}" ] && {
    echo "Cannot locate config.pcrs: ${config_pcrs}" >&2
    exit 1
}

grep '\-p 1' "${config_pcrs}"
case $? in
    0)  sed -i -e 's&-p 1 &&' "${config_pcrs}"
        if [ $? -gt 0 ]; then
            echo "Unable to remove PCR[1] from measurement list." >&2
            exit 1
        fi
        ;;
    1)  # PCR[1] not in measurement list
        ;;
    *)  echo "Error occurred while searching for PCR[1] in measurement list." >&2
        ;;
esac

exit 0
