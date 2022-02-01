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

sync
mkdir -p /proc /sys /mnt /tmp
mount -t proc proc /proc
mount -t xenfs none /proc/xen
mount -t sysfs sysfs /sys

# Cmdline parsing.
KERNEL_CMDLINE=`cat /proc/cmdline`
for arg in $KERNEL_CMDLINE; do
    case "$arg" in
        debug) LOGLVL="debug";;
        *) continue ;;
    esac
done

modprobe xen-argo
modprobe ivc

if [ "${LOGLVL}" = "debug" ]; then
    cut -f1,2,3,4,5 -d ' ' /proc/modules
fi

echo "0" > /sys/bus/pci/drivers_autoprobe
for pci_dev in `ls /sys/bus/pci/devices/`
do
 if [ -e /sys/bus/pci/devices/$pci_dev/driver/unbind ]
 then
    echo "pci device $pci_dev is bound, unbind it!"
    echo "$pci_dev" > /sys/bus/pci/devices/$pci_dev/driver/unbind
 fi
done

ln -s /proc/self/fd/2 /dev/stderr

echo 1 > /proc/sys/net/ipv4/ip_forward
echo 0 > /proc/sys/net/ipv4/conf/all/rp_filter

mkdir -p /var/run
export USE_INTEL_SB=1
export INTEL_DBUS=1

echo "Starting qemu directly."
target="$( xenstore-read target )"
vm_uuid="$( xenstore-read /local/domain/${target}/vm )"

dmargs=$( xenstore-read $(xenstore-list -p "$vm_uuid/image/dm-argv" | sort ))
echo "target $target vm_uuid $vm_uuid"
echo "Invoking qemu with dmargs       = ${dmargs}"

qemu_ifdown() {
    nic_num=$1
    echo "vif-$nic_num" > /sys/bus/xen/drivers/vif/unbind
    xenstore-rm "device/vif/$nic_num"
}

mkdir /tmp/qmp
mkfifo /tmp/qmp/qemu.in /tmp/qmp/qemu.out
(
  echo '{"execute": "qmp_capabilities"}' | tee /proc/self/fd/2
) >/tmp/qmp/qemu.in &
(
set +x
cat /tmp/qmp/qemu.out | tee /proc/self/fd/2 | \
while IFS= read -r line; do
    if [ $(echo "\\${line}" | grep -cim1 '"event": "DEVICE_DELETED", "data": {"device": "nic') -eq 1 ]; then
        nic_num=$(echo "\\${line}" | sed 's/.*"event": "DEVICE_DELETED", "data": {"device": "nic\([[:digit:]]*\)",.*/\1/')
        qemu_ifdown "$nic_num"
    fi
done
) &

qemu_ifup() {
    # vifX.Y-emu  X=domid Y=devid
    vif="$1"
    num="${vif%-emu}"
    num="${num#vif}"
    devid="${num#*.}"
    domid="${num%.*}"
    br="br$devid"

    my_domid=$( xenstore-read domid )
    xs_mac=$( xenstore-read "/local/domain/$my_domid/device/vif/$devid/mac" )

    for netif in $( ls /sys/class/net/ ); do
        if [ "$xs_mac" == "$( cat "/sys/class/net/$netif/address" )" ]; then
            eth="$netif"
            break
        fi
    done

    if [ -z "$eth" ] ; then
        echo "qemu_ifup: Could not find interface with mac $xenstore_mac"
        ls /sys/class/net

        return 1
    fi

    if [ ! -e "/sys/class/net/$eth" ] ; then
        echo "qemu_ifup: PV interface $eth does not exist."

        return 1
    fi

    if [ -e "/sys/class/net/$br" ] ; then
        echo "qemu_ifup: Bridge $br already exists"

        return 1
    fi

    echo "qemu_ifup: Adding $eth and $vif to $br"

    a=$( printf "%02x" $(( domid / 256 )) )
    b=$( printf "%02x" $(( domid % 256 )) )
    c=$( printf "%02x" $(( devid / 256 )) )
    d=$( printf "%02x" $(( devid % 256 )) )
    # Config ethX
    ip link set "$eth" address "fe:$a:$b:$c:$d:fe"
    ip addr flush "$eth"
    ip link set "$eth" up

    brctl addbr "$br"
    # Forward 802.1d link-local packets like 802.1x.
    echo 0xfff8 > "/sys/class/net/$br/bridge/group_fwd_mask"
    brctl stp "$br" off
    brctl setfd "$br" 0
    brctl addif "$br" "$eth"
    ip link set "$br" up

    ip link set "$vif" down
    ip link set "$vif" address "fe:$a:$b:$c:$d:fd"
    ip addr flush "$vif"
    brctl addif "$br" "$vif"
    ip link set "$vif" up
}

(
for arg in $dmargs; do
if [ $(echo "$arg" | grep -cim1 "ifname=") -eq 1 ]; then
    (
    VIFNAME=$(echo "$arg" | sed -n -e 's/^.*ifname=//p' | sed -n -e 's/,.*$//p')
    echo Waiting for \"$VIFNAME\" >&2
    while true; do
        if [ $(ip link | grep -cim1 "$VIFNAME") -eq 1 ]; then
            break
        fi
        sleep 0.1
    done
    qemu_ifup "$VIFNAME"
    echo Finished \"$VIFNAME\" >&2
    ) &
fi
done
) &

# $dm_args and $kernel are separated with \n to allow for spaces in arguments.
OIFS="$IFS"
IFS=$'\n'
set -f
/usr/bin/qemu-system-i386 ${dmargs} \
	-chardev pipe,path=/tmp/qmp/qemu,id=m \
	-mon chardev=m,mode=control \
	-chardev socket,server,nowait,path=/tmp/qemu.qmp,id=m2 \
	-mon chardev=m2,mode=control \
	-chardev socket,server,nowait,path=/tmp/qemu-cdrom.qmp,id=m8675 \
	-mon chardev=m8675,mode=control &
set +f
IFS="$OIFS"

echo /sbin/mdev > /proc/sys/kernel/hotplug
mdev -s

device_model="device-model/$target"

echo "Starting vchan-socket-proxy"
vchan-socket-proxy -m server 0 $device_model/qmp-vchan /tmp/qemu.qmp &

while true; do
    printf '==== Press enter for shell ====\n'
    read
    setsid /bin/cttyhack /bin/sh
done
