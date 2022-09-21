DESCRIPTION = "Linux kernel for OpenXT service VMs."

# Use the one from meta-openembedded/meta-oe
require recipes-kernel/linux/linux.inc
require recipes-kernel/linux/linux-openxt.inc
DEPENDS += "rsync-native"

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/defconfigs:"
SRC_URI += "${KERNELORG_MIRROR}/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.xz;name=kernel \
    file://bridge-carrier-follow-prio0.patch \
    file://dont-suspend-xen-serial-port.patch \
    file://extra-mt-input-devices.patch \
    file://blktap2.patch \
    file://pci-pt-move-unaligned-resources.patch \
    file://pci-pt-flr.patch \
    file://usbback-base.patch \
    file://hvc-kgdb-fix.patch \
    file://pciback-restrictive-attr.patch \
    file://thorough-reset-interface-to-pciback-s-sysfs.patch \
    file://netback-vwif-support.patch \
    file://xen-txt-add-xen-txt-eventlog-module.patch \
    file://xenpv-no-tty0-as-default-console.patch \
    file://tpm_tis-work-around-status-register-bug-in-STMicroelectronics-TPM.patch \
    file://v2-0001-xen-pcifront-Handle-missed-Connected-state.patch \
    file://defconfig \
    "

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
SRC_URI[kernel.md5sum] = "fb1777e62d8123d95e4e588b9dbfd659"
SRC_URI[kernel.sha256sum] = "e32839ca761e5251f25708f7939b37b101d28fc29515a97bfc0c838a21efdf34"
