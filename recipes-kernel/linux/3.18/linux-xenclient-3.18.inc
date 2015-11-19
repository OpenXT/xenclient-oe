# Use the one from meta-openembedded/meta-oe
require recipes-kernel/linux/linux.inc
require recipes-kernel/linux/linux-xenclient.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel \
    file://bp-xen-pv-guest-pat-support.patch;patch=1 \
    file://bridge-carrier-follow-prio0.patch;patch=1 \
    file://privcmd-mmapnocache-ioctl.patch;patch=1 \
    file://xenkbd-tablet-resolution.patch;patch=1 \
    file://acpi-video-delay-init.patch;patch=1 \
    file://skb-forward-copy-bridge-param.patch;patch=1 \
    file://dont-suspend-xen-serial-port.patch;patch=1 \
    file://break-8021d.patch;patch=1 \
    file://extra-mt-input-devices.patch;patch=1 \
    file://tpm-log-didvid.patch;patch=1 \
    file://blktap2.patch;patch=1 \
    file://export-for-xenfb2.patch;patch=1 \
    file://allow-service-vms.patch;patch=1 \
    file://intel-amt-support.patch;patch=1 \
    file://disable-csum-xennet.patch;patch=1 \
    file://pci-pt-move-unaligned-resources.patch;patch=1 \
    file://pci-pt-flr.patch;patch=1 \
    file://realmem-mmap.patch;patch=1 \
    file://netback-skip-frontend-wait-during-shutdown.patch;patch=1 \
    file://xenbus-move-otherend-watches-on-relocate.patch;patch=1 \
    file://netfront-support-backend-relocate.patch;patch=1 \
    file://konrad-microcode.patch;patch=1 \
    file://konrad-ioperm.patch;patch=1 \
    file://gem-foreign.patch;patch=1 \
    file://gem-foreign-debug.patch;patch=1 \
    file://fbcon-do-not-drag-detect-primary-option.patch;patch=1 \
    file://usbback-base.patch;patch=1 \
    file://hvc-kgdb-fix.patch;patch=1 \
    file://xenstore-no-read-vs-write-atomicity.patch;patch=1 \
    file://pciback-restrictive-attr.patch;patch=1 \
    file://0002-Add-thorough-reset-interface-to-pciback-s-sysfs.patch;patch=1 \
    "

SRC_URI[kernel.md5sum] = "9ac668fd5d47f7755fbf81d025112796"
SRC_URI[kernel.sha256sum] = "57649fc21be6929b5bd1a7241a711eae6671b8296b42135d0982661d4d1e224d"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

