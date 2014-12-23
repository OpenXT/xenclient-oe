#!/bin/sh
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

# Called when GUI is ready. Xenmgr will defer starting other VM's until
# this script returns.

# Give uivm time to actually finish booting (but only  when profiling)
if ssh -o StrictHostKeyChecking=false -o UserKnownHostsFile=/dev/null root@uivm pidof ureadahead; then
  sleep 10
fi

# Kill ureadahead:
/etc/init.d/ureadahead stop
ssh -o StrictHostKeyChecking=false -o UserKnownHostsFile=/dev/null root@uivm /etc/init.d/ureadahead stop

# Lower memory of dom0 and uivm
UIVM=`xenvm-cmd 00000000-0000-0000-0000-000000000001 get_domid`
#xenstore-write /local/domain/$UIVM/memory/target 196608
#xenstore-write /local/domain/0/memory/target 262136
touch /tmp/boot-complete
