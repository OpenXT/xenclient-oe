#!/bin/sh
#
# Copyright (c) 2014 Citrix Systems, Inc.
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

MODULE_DIR=/initrd.d
ROOT_DEVICE=
ROOT_READONLY=
DEFINIT=/sbin/init
FIRSTBOOT_FLAG=/boot/system/firstboot

early_setup() {
    mkdir -p /proc /sys /mnt /tmp
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
}

dev_setup()
{
    mount -t devtmpfs none /dev

    echo "initramfs: Configuring LVM"
    lvm vgscan
    lvm vgchange -a y
}

read_args() {
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                ROOT_DEVICE=$optarg ;;
            rootfstype=*)
                ROOT_FSTYPE=$optarg ;;
            boot=*)
                BOOT_DEVICE=$optarg ;;
            bootfstype=*)
                BOOT_FSTYPE=$optarg ;;
            init=*)
                INIT=$optarg ;;
            rootdelay=*)
                rootdelay=$optarg ;;
            ro)
                ROOT_READONLY=-r ;;
            measured)
                : ${INIT:=/sbin/init.root-ro} ;;
            debug)
                set -x ;;
            fbcon)
                depmod -a
                modprobe fbcon
                FBCON=true
                ;;
            break=*)
                BREAK=$optarg ;;
        esac
    done
}

maybe_break() {
    if [ ! -z "${FBCON}" ]; then
        modprobe usbhid
        modprobe ehci-hcd
        modprobe ehci-pci
        modprobe uhci-hcd
        modprobe ohci-hcd
        modprobe xhci-hcd
        modprobe hid
        modprobe hid-generic
        
        exec 0<&-
        exec 1<&-
        exec 2<&-
        exec 0< /dev/tty0
        exec 1> /dev/tty0
        exec 2> /dev/tty0
    fi

    if [ "${BREAK:-}" = "$1" ]; then
        echo "Spawning shell in initramfs..."
        PS1='XT: ' /bin/sh
    fi
}

mount_root() {
    MOUNTOPT="-r -o user_xattr"
    [ -n "$ROOT_FSTYPE" ] && MOUNTOPT="$MOUNTOPT -t $ROOT_FSTYPE"
    
    TIMEOUT=30
    while [ ! -e "$ROOT_DEVICE" ] && [ $TIMEOUT -gt 0 ]; do
	    echo "Root device $ROOT_DEVICE not found, waiting $TIMEOUT seconds ..."
	    TIMEOUT=$(($TIMEOUT - 1))
	    sleep 1
	    dev_setup
    done
    [ -e "$ROOT_DEVICE" ] || fatal "Timed out waiting for root device $ROOT_DEVICE"

    [ -e "$ROOT_DEVICE.s" ] && {
        echo "initramfs squashfs $ROOT_DEVICE.s present"
        insmod /lib/modules/$(uname -r)/kernel/fs/squashfs/squashfs.ko
        echo "initramfs mounting squashfs root: mount $MOUNTOPT $ROOT_DEVICE.s /root"
        mount $MOUNTOPT $ROOT_DEVICE.s /root && {
            CMDLINE=ro read_args
            ROOT_DEVICE=$ROOT_DEVICE.s
            return
        }
        echo "initramfs squashfs $ROOT_DEVICE.s mount failed, falling back to $ROOT_DEVICE"
    }

    echo "initramfs mounting root: mount $MOUNTOPT $ROOT_DEVICE /root"
    mount $MOUNTOPT $ROOT_DEVICE /root || fatal "Failed to mount root device"
}

mount_boot() {
    MOUNTOPT=
    [ -n "$BOOT_FSTYPE" ] && MOUNTOPT="$MOUNTOPT -t $BOOT_FSTYPE"

    echo "initramfs mounting boot: mount $MOUNTOPT $BOOT_DEVICE /root/boot/system"

    TIMEOUT=30
    while [ ! -e "$BOOT_DEVICE" ] && [ $TIMEOUT -gt 0 ]; do
            echo "Boot device $BOOT_DEVICE not found, waiting $TIMEOUT seconds ..."
            TIMEOUT=$(($TIMEOUT - 1))
            sleep 1
            dev_setup
    done
    [ -e "$BOOT_DEVICE" ] || fatal "Timed out waiting for boot device $BOOT_DEVICE"

    mount $MOUNTOPT $BOOT_DEVICE /root/boot/system || fatal "Failed to mount boot device"
}

boot_root() {
    [ -z "$ROOT_READONLY" ] && mount -o remount,rw $ROOT_DEVICE /root
    mount --bind /dev /root/dev
    mount --bind /proc /root/proc
    mount --move /sys /root/sys

    exec switch_root -c /dev/console /root /sbin/selinux-load.sh ${INIT:-$DEFINIT}
}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}

tpm_setup() {
    CMDLINE="ro measured" read_args
    insmod /lib/modules/$(uname -r)/kernel/drivers/char/tpm/tpm_bios.ko
    insmod /lib/modules/$(uname -r)/kernel/drivers/char/tpm/tpm.ko
    insmod /lib/modules/$(uname -r)/kernel/drivers/char/tpm/tpm_tis.ko
    mknod /dev/tpm0 c 10 224
    echo -n "initramfs measuring $ROOT_DEVICE: "
    s=$(sha1sum $ROOT_DEVICE)
    echo $s
    echo -n ${s:0:40} | TCSD_LOG_OFF=yes tpm_extendpcr_sa -p 15
}


echo "Starting initramfs boot..."
early_setup

[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

read_args

[ -n "$ROOT_DEVICE" ] || fatal "No valid root device was specified.  Please add root=/dev/something to the kernel command-line and try again."

if [ -n "$rootdelay" ]; then
    echo "Waiting $rootdelay seconds for devices to settle..." >$CONSOLE
    sleep $rootdelay
fi

dev_setup
maybe_break mount
echo "Mounting root file system..."
mount_root

[ -n "$BOOT_DEVICE" ] && mount_boot

maybe_break tpm

[ -e /root/boot/system/tpm/enabled ] && {
    echo "Setting up TPM..."
    tpm_setup
}

maybe_break pivot
boot_root
