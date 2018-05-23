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

is_tpm_2_0 () {
    # See the TPM chardev driver implementation:
    # https://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git/tree/drivers/char/tpm/tpm-sysfs.c?h=v4.14.34#n296
    # Assuming a TPM has already been detected, absence of the sysfs entry
    # means TPM 2.0.
    # This is still valid on Linux 4.16.
    if [ ! -e /sys/class/tpm/tpm0/device/caps ]; then
        return 0
    fi

    # Sysfs caps entry contains TPM manufacturer and version info.
    # https://www.kernel.org/doc/Documentation/ABI/stable/sysfs-class-tpm
    local tpm_ver="$(awk '/TCG version:/ { print $3 }' /sys/class/tpm/tpm0/device/caps)"

    [ "${tpm_ver}" = "2.0" ]
}

#listpcrs sample output:
#Supported Bank/Algorithm: TPM_ALG_SHA1(0x0004) TPM_ALG_SHA256(0x000b)
#Cuts and for loop isolate "TPM_ALG_<hash_type>" and compare against input
pcr_bank_exists () {
    local alg_in=$1

    banks=$(tpm2_listpcrs -s | cut -d ':' -f 2)
    for bank in $banks; do
        alg=$(echo $bank | cut -d '(' -f 1)
        if [ "$alg" = $alg_in ]; then
            return 0
        fi
    done
    return 1
}

early_setup() {
    # initialize /proc, /sys, /run/lock and /var/lock /mnt /tmp
    mkdir -p /proc /sys /run/lock /var/lock /mnt /tmp
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
}

dev_setup()
{
    if grep -q devtmpfs /proc/filesystems; then
        mkdir -p /dev
        mount -t devtmpfs devtmpfs /dev
    else
        if [ ! -d /dev ]; then
            fatal "ERROR: /dev doesn't exist and kernel doesn't has devtmpfs enabled."
        fi
    fi

    echo "initramfs: Configuring LVM"
    lvm vgscan --mknodes
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
                FBCON=true
                ;;
            break=*)
                BREAK=$optarg ;;
            [0123456Ss])
                RUNLEVEL=$arg ;;
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
    if [ ! -d /root ]; then
        fatal "/root does not exist."
    fi
    if [ ! -x /root/sbin/selinux-load.sh ]; then
        fatal "/sbin/selinux-load.sh does not exist in new root filesystem."
    fi

    [ -z "$ROOT_READONLY" ] && mount -o remount,rw $ROOT_DEVICE /root

    echo "Switching root to '/root'..."

    mount --move /dev /root/dev
    mount --move /proc /root/proc
    mount --move /sys /root/sys

    cd /root
    exec switch_root -c /dev/console /root /sbin/selinux-load.sh ${INIT:-$DEFINIT} ${RUNLEVEL}
}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}

tpm_setup() {
    CMDLINE="ro measured" read_args
    modprobe tpm_tis
    is_tpm_2_0
    if [ $? -eq 0 ];
    then
        echo "Measuring for tpm 2.0"
        #Prefer the more secure alg, but try sha1 as a last resort since most
        #platforms support it for legacy.
        if pcr_bank_exists "TPM_ALG_SHA256"; then
            s=$(sha256sum $ROOT_DEVICE)
            echo $s
            DIGEST=$(echo -n ${s:0:64})
            tpm2_extendpcr -c 15 -g 0xB -s $DIGEST
            return $?
        else
            s=$(sha1sum $ROOT_DEVICE)
            echo $s
            DIGEST=$(echo -n ${s:0:40})
            tpm2_extendpcr -c 15 -g 0x4 -s $DIGEST
            return $?
        fi
    else
        s=$(sha1sum $ROOT_DEVICE)
        echo "done"
        echo -n ${s:0:40} | TCSD_LOG_OFF=yes tpm_extendpcr_sa -p 15
        [ $? -ne 0 ] && fatal "PCR-15 extend failed"
    fi
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
if [ -e /root/boot/system/tpm/enabled ]; then
    echo "Setting up TPM..."
    tpm_setup
fi

maybe_break pivot
boot_root
