#!/bin/sh
#
# Copyright (c) 2013 Citrix Systems, Inc.
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

export PATH="/sbin:$PATH"

mount -t devtmpfs none /dev

exec 0<&-                      
exec 1<&-              
exec 2<&-

exec 0< /dev/hvc0
exec 1> /dev/hvc0
exec 2> /dev/hvc0

## the modprobe of busybox-static is broken
## so we have to use insmod directly
insmod /lib/modules/`uname -r`/extra/v4v.ko

sync
mkdir -p /proc /sys /mnt /tmp
mount -t proc proc /proc
mount -t xenfs none /proc/xen
mount -t sysfs sysfs /sys

lsmod

echo "0" > /sys/bus/pci/drivers_autoprobe
for pci_dev in `ls /sys/bus/pci/devices/`
do
 if [ -e /sys/bus/pci/devices/$pci_dev/driver/unbind ]
 then
    echo pci device $pci_dev is bound, unbounding it!
    echo "$pci_dev" > /sys/bus/pci/devices/$pci_dev/driver/unbind
 fi
done

ln -s /proc/self/fd/2 /dev/stderr

echo $*
echo 1 > /proc/sys/net/ipv4/ip_forward
echo 0 > /proc/sys/net/ipv4/conf/all/rp_filter

mkdir -p /var/run
export USE_INTEL_SB=1
export INTEL_DBUS=1

rsyslogd -f /etc/rsyslog.conf -c4

# Agent cmdline parsing.
KERNEL_CMDLINE=`cat /proc/cmdline`
for arg in $KERNEL_CMDLINE; do
    case "$arg" in
        guest_agent=*) AGENT=${arg##*=} ;;
        guest_domid=*)  DOMID=${arg##*=} ;;
        *) continue ;;
    esac
done

# Start requested agent.
case "$AGENT" in
    dmagent)
        echo "Start dmagent..."
        exec /usr/bin/dm-agent -q -n -t ${DOMID}
        ;;

    *)
        echo "No agent specified. Assume retro-compatibility by falling back to the deprecated behaviour."
        if [ "${KERNEL_CMDLINE/dmagent/}" != "${KERNEL_CMDLINE}" ]; then
            # Assumes we are stubbing for the domid passed at the end of the cmdline.
            DOMID=${KERNEL_CMDLINE##* }
            echo "Start dmagent..."
            exec /usr/bin/dm-agent -q -n -t $DOMID
        else
            QEMU_CMDLINE=`cat /proc/cmdline | cut -d' ' -f4- `
            DOMID=`echo $QEMU_CMDLINE | cut -d' ' -f2 `
            echo "Start qemu-dm-wrapper..."
            exec /usr/bin/qemu-dm-wrapper $DOMID -stubdom -name qemu-$DOMID $QEMU_CMDLINE
        fi
        ;;
esac

