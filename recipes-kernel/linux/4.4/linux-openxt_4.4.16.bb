DESCRIPTION = "Linux kernel for OpenXT service VMs."

# Use the one from meta-openembedded/meta-oe
require recipes-kernel/linux/linux.inc
require recipes-kernel/linux/linux-openxt.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/defconfigs:"
SRC_URI += "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel \
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
    file://fbcon-do-not-drag-detect-primary-option.patch;patch=1 \
    file://usbback-base.patch;patch=1 \
    file://hvc-kgdb-fix.patch;patch=1 \
    file://xenstore-no-read-vs-write-atomicity.patch;patch=1 \
    file://pciback-restrictive-attr.patch;patch=1 \
    file://thorough-reset-interface-to-pciback-s-sysfs.patch;patch=1 \
    file://xsa-155-qsb-023-add-RING_COPY_RESPONSE.patch;patch=1 \
    file://xsa-155-qsb-023-xen-blkfront-make-local-copy-of-response-before-usin.patch;patch=1 \
    file://xsa-155-qsb-023-xen-blkfront-prepare-request-locally-only-then-put-i.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-add-range-check-for-Tx-response-id.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-copy-response-out-of-shared-buffer-befo.patch;patch=1 \
    file://xsa-155-qsb-023-xen-netfront-do-not-use-data-already-exposed-to-back.patch;patch=1 \
    file://cve-2016-0758-KEYS-Fix-ASN.1-indefinite-length-object-parsing.patch \
    file://cve-2016-3135-netfilter-x_tables-check-for-size-overflow.patch \
    file://cve-2016-3672-x86-mm-32-Enable-full-randomization-on-i386-and-X86_.patch \
    file://cve-2016-3951-cdc_ncm-do-not-call-usbnet_link_change-from-cdc_ncm_.patch \
    file://cve-2016-4482-usbfs-fix-potential-infoleak-in-devio.patch \
    file://cve-2016-4568-media-videobuf2-v4l2-Verify-planes-array-in-buffer-d.patch \
    file://cve-2016-4569-ALSA-timer-Fix-leak-in-SNDRV_TIMER_IOCTL_PARAMS.patch \
    file://cve-2016-4578-1-ALSA-timer-Fix-leak-in-events-via-snd_timer_user_cca.patch \
    file://cve-2016-4578-2-ALSA-timer-Fix-leak-in-events-via-snd_timer_user_tin.patch \
    file://cve-2016-5244-rds-fix-an-infoleak-in-rds_inc_info_copy.patch \
    file://defconfig \
    "

SRC_URI[kernel.md5sum] = "3252f673da6a77de161da3bfafb022a3"
SRC_URI[kernel.sha256sum] = "b48d1160e3c181138f6ae9efe433e204ac3da821939780c1b115b02fc3c92dda"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

PR = "r2"

