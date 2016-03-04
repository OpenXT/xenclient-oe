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

. /etc/init.d/functions
CFG_ETC=/config/etc

umask 077

#------------------------------------------------------------------------------
# Clean up after upgrade from XT 3.x. This section can be removed when direct
# upgrade from XT 3.x is no longer supported. Please add a new section when
# adding cleanup steps for upgrade from newer releases.
#------------------------------------------------------------------------------

# Clean up /config/system/iproute2.
rm -rf /config/system/iproute2

# Clean up /config/etc/lvm.
rm -f ${CFG_ETC}/lvm/cache
rm -f ${CFG_ETC}/lvm/lvm.conf

# Move /config/USB_always.conf to /config/etc.
if [ ! -r ${CFG_ETC}/USB_always.conf -a \
     -r /config/USB_always.conf ] ; then
    mv /config/USB_always.conf ${CFG_ETC}
    restore ${CFG_ETC}/USB_always.conf
fi

#------------------------------------------------------------------------------
# After fresh install, populate /config, /boot/system and /var/log with default
# content. After upgrade, populate them with any newly-added default content.
#------------------------------------------------------------------------------

# bind rootfs to tmp location and rsync dom0 /config on to config partition
# to pick up new files after upgrade
TMP_MNT=$(mktemp --directory)
mount --bind / ${TMP_MNT}
rsync --archive --xattrs --ignore-existing ${TMP_MNT}/config/ /config

# update the passwd file for the upgrade case
cp -p ${TMP_MNT}/config/etc/passwd /config/etc/passwd
umount ${TMP_MNT}
rmdir ${TMP_MNT}

# Copy trousers data file to /boot/system.
PS_FILE_SRC=/usr/share/trousers/system.data.auth
PS_DIR=/boot/system/tpm
PS_FILE=${PS_DIR}/system.data
TSS_USER="tss"
if [ ! -s ${PS_FILE} ]; then
    install -m 700 -o ${TSS_USER} -g ${TSS_USER} -d ${PS_DIR}
    install -m 600 -o ${TSS_USER} -g ${TSS_USER} ${PS_FILE_SRC} ${PS_FILE}
fi

# Make /var/log/wtmp volatile.
if [ ! -L /var/log/wtmp ] ; then
    rm -f /var/log/wtmp
    ln -s /var/volatile/log/wtmp /var/log/wtmp
    restore /var/log/wtmp
fi

#------------------------------------------------------------------------------
# After fresh install, apply configuration from files left by the installer
# in /config/install.
#
# !!! Be careful handling the contents of these files !!!
#------------------------------------------------------------------------------

INSTALL_CONF=/config/install

if [ -r ${INSTALL_CONF}/language.conf ] ; then
    LANGUAGE="$(awk -F\' '/^LANGUAGE=/ { print $2 }' ${INSTALL_CONF}/language.conf)"
    echo "LANGUAGE='${LANGUAGE}'" > /config/language.conf
 
    DEFER_LANGUAGE="$(awk -F\' '/^DEFER_LANGUAGE=/ { print $2 }' ${INSTALL_CONF}/language.conf)"
    if [ "${DEFER_LANGUAGE}" = "true" ]; then
        touch /config/deferred_language
    fi
 
    mv -f ${INSTALL_CONF}/language.conf ${INSTALL_CONF}/language.conf.DONE
fi

if [ -r ${INSTALL_CONF}/keyboard.conf ] ; then
    KEYBOARD="$(awk -F\' '/^KEYBOARD=/ { print $2 }' ${INSTALL_CONF}/keyboard.conf)"
    echo "KEYBOARD='${KEYBOARD}'" > /config/keyboard.conf
    restore /config/keyboard.conf
 
    DEFER_KEYBOARD="$(awk -F\' '/^DEFER_KEYBOARD=/ { print $2 }' ${INSTALL_CONF}/keyboard.conf)"
    if [ "${DEFER_KEYBOARD}" = "true" ]; then
        touch /config/deferred_kb_layout
    fi
 
    mv -f ${INSTALL_CONF}/keyboard.conf ${INSTALL_CONF}/keyboard.conf.DONE
fi

if [ -r ${INSTALL_CONF}/ssh.conf ] ; then
    SSH_ENABLED="$(awk -F\' '/^SSH_ENABLED=/ { print $2 }' ${INSTALL_CONF}/ssh.conf)"
    if [ "$SSH_ENABLED" = "true" ]; then
        mkdir -p /config/etc/ssh
        touch /config/etc/ssh/enabled
    else
        rm -f /config/etc/ssh/enabled
    fi
    mv -f ${INSTALL_CONF}/ssh.conf ${INSTALL_CONF}/ssh.conf.DONE
fi

if [ -r ${INSTALL_CONF}/eula.conf ] ; then
    DEFER_EULA="$(awk -F\' '/^DEFER_EULA=/ { print $2 }' ${INSTALL_CONF}/eula.conf)"
    if [ "${DEFER_EULA}" = "true" ]; then
        touch /config/deferred_eula
    fi
    mv -f ${INSTALL_CONF}/eula.conf ${INSTALL_CONF}/eula.conf.DONE
fi

if [ -r ${INSTALL_CONF}/panda.conf ] ; then
    if [ ! -r /config/system/panda.conf ] ; then
        cp ${INSTALL_CONF}/panda.conf /config/system/panda.conf
    fi
    mv -f ${INSTALL_CONF}/panda.conf ${INSTALL_CONF}/panda.conf.DONE
fi

if [ -r ${INSTALL_CONF}/repo-cert.conf ] ; then
    if [ ! -r /config/repo-cert.conf ] ; then
        ALLOW_DEV_REPO_CERT="$(awk -F\' '/^ALLOW_DEV_REPO_CERT=/ { print $2 }' ${INSTALL_CONF}/repo-cert.conf)"
        echo "ALLOW_DEV_REPO_CERT='${ALLOW_DEV_REPO_CERT}'" > /config/repo-cert.conf
        restore /config/repo-cert.conf
    fi
    mv -f ${INSTALL_CONF}/repo-cert.conf ${INSTALL_CONF}/repo-cert.conf.DONE
fi
restore -r ${INSTALL_CONF} /config/deferred_*

if [ -r ${INSTALL_CONF}/uivm-gconf,aes-xts-plain,256.key ] ; then
    KEY_FOLDER="/config/platform-crypto-keys"
    mkdir -p ${KEY_FOLDER}
    mv ${INSTALL_CONF}/uivm-gconf,aes-xts-plain,256.key ${KEY_FOLDER}
    restore -r ${KEY_FOLDER}
fi
