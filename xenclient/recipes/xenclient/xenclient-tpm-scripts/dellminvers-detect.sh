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

# Dell E6410 and E6510
product_name=`dmidecode --type 1 | sed -n -re 's/.*Product Name: (.*)$/\1/p'`
case "${product_name}" in
    'Latitude E6410'|'Latitude E6510')
        tpm_min_version="1.2.7.14"
        tpm_version=`tpm_version | sed -n -re 's/.*Chip Version: *(.*)/\1/p'`

        if [ `expr "${tpm_version}" \< "${tpm_min_version}"` -eq 1 ]
        then
            printf "%-15s: %s\n" "${product_name}" "TPM Firmware version too old (${tpm_version}). The minimum version required is ${tpm_min_version}.  Please upgrade your TPM Firmware (Dell ControlVault) before configuring XenClient Measured Launch." >&2
            exit 2
        fi

        bios_min_version="A10"
        bios_version=`dmidecode --type 0 | sed -n -re 's/.*Version: *(.*)$/\1/p'`
        if [ `expr "${bios_version}" \< "${bios_min_version}"` -eq 1 ]
        then
            printf "%-15s: %s\n" "${product_name}" "BIOS version too old (${bios_version}). The minimum version required is ${bios_min_version}.  Please upgrade your BIOS to the latest version before configuring XenClient Measured Launch." >&2
            exit 2
        fi
        ;;
esac
