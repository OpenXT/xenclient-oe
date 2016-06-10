#
# Copyright (c) 2012 Citrix Systems, Inc.
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

#
# bootmisc.sh	Miscellaneous things to be done during bootup.
#

. /etc/default/rcS

# Set the system clock from hardware clock
# If the timestamp is 1 day or more recent than the current time,
# use the timestamp instead.
test -x /etc/init.d/hwclock.sh && /etc/init.d/hwclock.sh start
if test -e /etc/timestamp
then
	SYSTEMDATE=`date "+%Y%m%d"`
	TIMESTAMP=`cat /etc/timestamp | awk '{ print substr($0,1,8);}'`
        NEEDUPDATE=`expr \( $TIMESTAMP \> $SYSTEMDATE \)`                                                 
        if [ $NEEDUPDATE -eq 1 ]; then 
		date `cat /etc/timestamp`
		/etc/init.d/hwclock.sh stop
	fi
fi

[ -f /etc/hostname ] && hostname -F /etc/hostname

: exit 0
