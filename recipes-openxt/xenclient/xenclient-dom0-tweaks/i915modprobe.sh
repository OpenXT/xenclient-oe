#! /bin/sh
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

# Currently in OpenXT, i915 will always be loaded and surfman will use
# the drm plugin by default.
/sbin/modprobe -i i915
exit 0


# For posterity, leaving the original script that selectively modprobed the
# i915 driver or prevented its loading when we used our own igfx display driver.
usedrm=`/bin/sed -n '/drm-graphics/p' /proc/cmdline`

# If drm-graphics flag set then we want to use DRM plugin unconditionally
if [ -n "$usedrm" ]; then
  /sbin/modprobe -i i915
  exit 0
fi

# Check for Intel IGFX device at standard location
if [ `/bin/cat /sys/devices/pci0000\:00/0000\:00\:02.0/vendor` != 0x8086 ] ||
   [ `/bin/cat /sys/devices/pci0000\:00/0000\:00\:02.0/class` != 0x030000 ]; then
  # Unknown, do the default action
  /sbin/modprobe -i i915
  exit 0
fi

devid=`/bin/cat /sys/devices/pci0000\:00/0000\:00\:02.0/device`

# Filter list of known IGFX devices that are pre-Haswell
masked=$(( $devid & 0xFFF0 ))
masked=0x$(printf "%x\n" $masked)

case $masked in
  0x40)   ;&  # Iron Lake
  0x20e0) ;&  # Cantiga
  0x2a40) ;&
  0x2e10) ;&
  0x100)  ;&  # Cougar Point 
  0x110)  ;&
  0x120)  ;&
  0x160)  ;&  # Panther Point 
  0x150)
    exit 0  # Ignore i915, return success
    ;;
esac

# Else this is Haswell or something new, modprobe i915
/sbin/modprobe -i i915
exit 0

