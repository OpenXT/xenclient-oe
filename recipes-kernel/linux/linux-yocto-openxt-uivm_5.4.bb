COMPATIBLE_MACHINE = "openxt-uivm"

KMACHINE = "intel-x86-64"
LINUX_KERNEL_TYPE = "uivm"

KERNEL_FEATURES += " \
    features/xen/xen-blk-fe.scc \
    features/xen/xen-net-fe.scc \
    features/xen/xen-vkbd-fe.scc \
    features/xen/xen-vfb-fe.scc \
    \
    cfg/openxt-common.scc \
    \
    patches/backports/backports.scc \
    patches/openxt-pv-video-quirks/openxt-pv-video-quirks.scc \
    patches/openxt-service-vms/openxt-service-vms.scc \
    patches/xsa-155/xsa-155.scc \
"

require linux-yocto-openxt-5.4.inc
