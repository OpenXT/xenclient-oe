DESCRIPTION = "Linux kernel for OpenXT service VMs."

# Use the one from meta-openembedded/meta-oe
require recipes-kernel/linux/linux.inc
require recipes-kernel/linux/linux-openxt.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/defconfigs:"
SRC_URI += "${KERNELORG_MIRROR}/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.xz;name=kernel \
    file://xsa270.patch;patch=1 \
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
    file://konrad-ioperm.patch;patch=1 \
    file://fbcon-do-not-drag-detect-primary-option.patch;patch=1 \
    file://usbback-base.patch;patch=1 \
    file://hvc-kgdb-fix.patch;patch=1 \
    file://pciback-restrictive-attr.patch;patch=1 \
    file://thorough-reset-interface-to-pciback-s-sysfs.patch;patch=1 \
    file://tpm-tis-force-ioremap.patch;patch=1 \
    file://netback-vwif-support.patch;patch=1 \
    file://gem-foreign.patch;patch=1 \
    file://xen-txt-add-xen-txt-eventlog-module.patch;patch=1 \
    file://xsa-155-qsb-023-add-RING_COPY_RESPONSE.patch;patch=1 \
    file://xsa-155-qsb-023-xen-blkfront-make-local-copy-of-response-before-usin.patch;patch=1 \
    file://xsa-155-qsb-023-xen-blkfront-prepare-request-locally-only-then-put-i.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-add-range-check-for-Tx-response-id.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-copy-response-out-of-shared-buffer-befo.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-do-not-use-data-already-exposed-to-back.patch;patch=1 \
    file://0001-mm-handle_mm_fault-xen_pmd_val-radix_tree_lookup_slo.patch;patch=1 \
    file://defconfig \
    "
SRC_URI_append_xenclient-dom0 = "file://efi-tables-for-dom0.patch;patch=1 \ 
	"

SRC_URI[kernel.md5sum] = "7badf9d4911a2c26601f862199243fca"
SRC_URI[kernel.sha256sum] = "4a9540f256b8ee91d49c3d55def90be3af8c0ddb1ff274d20bc56a42dcdbd810"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
