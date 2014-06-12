#! /bin/sh
#
# Copyright (c) 2010 Citrix Systems, Inc.
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

chvt 12
echo -n mem > /sys/power/state
chvt 1 

# Stop network manager
killall NetworkManager

#remove below sleep after end-to-end S3 testing
sleep 2 

# Restart network manager after S3 resume to force wireless rescan
export NM_IFACE_FORCE="brbridged"
NetworkManager

xenstore-write "/pm/events/resumefromsleep" 1
