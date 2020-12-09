COMPATIBLE_MACHINE = "openxt-live-installer"

KMACHINE = "intel-x86-64"
LINUX_KERNEL_TYPE = "livecd-xen"

KERNEL_FEATURES += " \
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
    bsp/laptop/laptop-dell.scc \
    bsp/laptop/laptop-hp.scc \
    bsp/laptop/laptop-thinkpad.scc \
    \
    bsp/common-pc/common-pc-hid.scc \
    features/usb/touchscreen-composite.scc \
    \
    bsp/gpu/nouveau.scc \
    bsp/gpu/radeon.scc \
    \
    features/usb/ehci-hcd.scc \
    features/usb/uhci-hcd.scc \
    features/usb/ohci-hcd.scc \
    features/usb/xhci-hcd.scc \
    \
    bsp/common-pc/common-pc-eth-extended.scc \
    bsp/common-pc/common-pc-wifi-extended.scc \
    \
    cfg/openxt-common.scc \
    cfg/luks-lvm.cfg \
    cfg/initrd-gz.scc \
    cfg/livecd-fs.scc \
    \
    patches/backports/backports.scc \
    patches/openxt-input-quirks/openxt-input-quirks.scc \
    patches/openxt-tpm/openxt-tpm.scc \
"

require linux-yocto-openxt-5.4.inc
