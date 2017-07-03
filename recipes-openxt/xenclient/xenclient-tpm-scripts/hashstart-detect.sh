#!/bin/sh
#
# Copyright (c) 2017 Apertus Solutions, LLC
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

. /usr/lib/tpm-scripts/tpm-functions
[ $? -eq 0 ] || {
    echo "failed to load tpm-functions"
    exit 1
}

[ -d /sys/kernel/security/txt ] || exit 0

pcr17="$(tpm_get_pcr 17)"

is_tpm_2_0
if [ $? -eq 0 ]; then
    args="-2 -a sha256 -c"
else
    args="-a sha1 -c"
fi

nonquirk="$(pcr-calc $args|grep ^17|cut -f2 -d:|tr -d \ )"
quirk="$(pcr-calc $args -q|grep ^17|cut -f2 -d:|tr -d \ )"

case $pcr17 in
$nonquirk)
    exit 0
;;
$quirk)
    exit 1
;;
*)
    # Either the pcr value was invalid or an unknown sequence was used
    exit 3
;;
esac

exit 3
