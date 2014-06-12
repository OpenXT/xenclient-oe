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

DRMC="/boot/system/config/drm_grahics.conf"
GRUBC="/boot/system/grub/grub.cfg"
IVAL=
CVAL=
ACTION=

usage()
{
/bin/cat << EOF
USAGE: $0 options

Script for manipulating DRM graphics option.

OPTIONS:
   -s <true|false> Set the specified value in the DRM option config file.
   -u              Update grub.cfg with the value from the DRM option config file.
   -g              Get value from the DRM option config file.

NOTE: -s and -u can be used together to set the value and update grub.cfg at the
same time.

EOF
  exit -1
}

error()
{
  /bin/echo $1
  exit -1
}

drm_cval()
{
  [ ! -e $DRMC ] && error "Cannot find DRM graphics options file: $DRMC"
  CVAL=`/bin/cat $DRMC | /bin/grep drm_graphics | /usr/bin/awk -F'=' '{ print $2 }'`
  if [ "$CVAL" != "true" ] && [ "$CVAL" != "false" ];then
    error "Invalid value: $CVAL in drm_graphics file: $DRMC"
  fi
}

drm_get_value()
{
  # The g flag is exclusive, fail if other options specified
  [ "$ACTION" != "g" ] && usage
  # The g flag writes the value to stdout
  drm_cval
  /bin/echo $CVAL
  exit
}

drm_set_value()
{
  if [ "$IVAL" != "true" ] && [ "$IVAL" != "false" ];then
    usage
  fi
  # Create or over-write contents of conf file
  /bin/echo "drm_graphics=$IVAL" > $DRMC
}

drm_update_value()
{
  [ ! -e $GRUBC ] && error "Cannot find GRUB cfg file: $GRUBC"
  GVAL=""
  drm_cval
  [ "$CVAL" == "true" ] && GVAL="drm-graphics"
  # Update grub.cfg with DRM option from the conf file
  /bin/sed -i "s/^\(DRM_GRAPHICS_OPTION\s*=\s*\).*\$/\1\"$GVAL\"/" $GRUBC
}

while getopts "s:ug" OPTION
do
  case $OPTION in
  s)
    ACTION=$ACTION"s"
    IVAL="$OPTARG"
    ;;
  u)
    ACTION=$ACTION"u"
    ;;
  g)
    ACTION=$ACTION"g"
    ;;
  ?)
    usage
    ;;
  esac
done

[ -z $ACTION ] && usage
[[ "$ACTION" =~ "g" ]] && drm_get_value
[[ "$ACTION" =~ "s" ]] && drm_set_value
[[ "$ACTION" =~ "u" ]] && drm_update_value
