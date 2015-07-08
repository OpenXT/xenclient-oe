inherit xenclient

require recipes-kernel/linux/linux.inc

DEPENDS += "bc-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
KERNEL_IMAGETYPE = "bzImage"

PV = "3.11.10.4"

SRC_URI = "http://mirror.openxt.org/linux-3.11.10.4.tar.gz;name=kernel \
    file://bp-msix-passthru-fix-0e4ccb1505-v2.patch;patch=1 \
    file://xen-acpi-wmi.patch;patch=1 \
    file://bridge-carrier-follow-prio0.patch;patch=1 \
    file://privcmd-mmapnocache-ioctl.patch;patch=1 \
    file://usb-mass-storage-no-autobind.patch;patch=1 \
    file://xenkbd-tablet-resolution.patch;patch=1 \
    file://guest-os-controlled-brightness-support.patch;patch=1 \
    file://acpi-video-delay-init.patch;patch=1 \
    file://skb-forward-copy-bridge-param.patch;patch=1 \
    file://dont-suspend-xen-serial-port.patch;patch=1 \
    file://break-8021d.patch;patch=1 \
    file://extra-mt-input-devices.patch;patch=1 \
    file://tpm-log-didvid.patch;patch=1 \
    file://alsa-driver.patch;patch=1 \
    file://blktap2.patch;patch=1 \
    file://export-for-xenfb2.patch;patch=1 \
    file://allow-service-vms.patch;patch=1 \
    file://intel-amt-support.patch;patch=1 \
    file://disable-csum-xennet.patch;patch=1 \
    file://enable-pat.patch;patch=1 \
    file://pci-pt-move-unaligned-resources.patch;patch=1 \
    file://pci-pt-flr.patch;patch=1 \
    file://realmem-mmap.patch;patch=1 \
    file://netback-skip-frontend-wait-during-shutdown.patch;patch=1 \
    file://xenbus-move-otherend-watches-on-relocate.patch;patch=1 \
    file://netfront-support-backend-relocate.patch;patch=1 \
    file://konrad-microcode.patch;patch=1 \
    file://konrad-ioperm.patch;patch=1 \
    file://stubdom-allow-foreign-lowmem-map.patch;patch=1 \
    file://gem-userptr.patch;patch=1 \
    file://gem-foreign.patch;patch=1 \
    file://gem-foreign-debug.patch;patch=1 \
    file://atmel-tpm-timeouts.patch;patch=1 \
    file://fbcon-do-not-drag-detect-primary-option.patch;patch=1 \
    file://usbback-base.patch;patch=1 \
    file://usbback-rolled-up-patches.patch;patch=1 \
    file://usbback-req-padding.patch;patch=1 \
    file://usbback-optional-configuration.patch;patch=1 \
    file://usbback-async-urb-free.patch;patch=1 \
    file://usbback-map-ring-valloc.patch;patch=1 \
    file://usbback-cancel-urb-support.patch;patch=1 \
    file://blkback-3.18.13-backport.patch;patch=1 \
    file://backport-config-Enable-NEED_DMA_MAP_STATE-by-default-when-SWIOTLB-is-selected.patch;patch=1 \
    file://defconfig"

SRC_URI[kernel.md5sum] = "de35143a3d9bc37c87a13c2d3760e522"
SRC_URI[kernel.sha256sum] = "2aa4a14a022a7ad92db81888b0a4dde9b0d713c07da9d1e1e07c8152df0d1cf5"

S = "${WORKDIR}/linux-3.11.10.4"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
