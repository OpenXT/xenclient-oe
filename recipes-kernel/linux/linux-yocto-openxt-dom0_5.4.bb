COMPATIBLE_MACHINE = "openxt-dom0"

KMACHINE = "intel-x86-64"
LINUX_KERNEL_TYPE = "dom0"

KERNEL_FEATURES += " \
    \
    features/rfkill/rfkill.scc \
    \
    features/mmc/mmc-sdhci.scc \
    \
    features/scsi/disk.scc \
    features/scsi/cdrom.scc \
    \
    features/input/input.scc \
    \
    cfg/vesafb.scc \
    cfg/usb-mass-storage.scc \
    cfg/efi-ext.scc \
    \
    features/power/intel.scc \
    \
    features/i915/i915.scc \
    features/drm-gma500/drm-gma500.scc \
    features/sound/snd_hda_intel.scc \
    \
    features/tpm/tpm.scc \
    \
    features/xen/xen-net-fe.scc \
    features/xen/xen-balloon.scc \
    features/xen/xen-blk-be.scc \
    features/xen/xen-pci-be.scc \
    \
    cgl/features/selinux/selinux.scc \
    \
    bsp/laptop/laptop-dell.scc \
    bsp/laptop/laptop-hp.scc \
    bsp/laptop/laptop-thinkpad.scc \
    \
    bsp/common-pc/common-pc-hid.scc \
    \
    bsp/gpu/nouveau.scc \
    bsp/gpu/radeon.scc \
    \
    features/usb/touchscreen-composite.scc \
    features/usb/ehci-hcd.scc \
    features/usb/uhci-hcd.scc \
    features/usb/ohci-hcd.scc \
    features/usb/xhci-hcd.scc \
    \
    cfg/openxt-common.scc \
    cfg/luks-lvm.cfg \
    cfg/initrd-gz.scc \
    \
    patches/backports/backports.scc \
    patches/blktap2/blktap2.scc \
    patches/openxt-input-quirks/openxt-input-quirks.scc \
    patches/openxt-pci-quirks/openxt-pci-quirks.scc \
    patches/openxt-serial-quirks/openxt-serial-quirks.scc \
    patches/openxt-service-vms/openxt-service-vms.scc \
    patches/openxt-tpm/openxt-tpm.scc \
    patches/openxt-usbback/openxt-usbback.scc \
    patches/openxt-video-quirks/openxt-video-quirks.scc \
    patches/xen-txt/xen-txt.scc \
    patches/xsa-155/xsa-155.scc \
"

require linux-yocto-openxt-5.4.inc
