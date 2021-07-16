COMPATIBLE_MACHINE = "openxt-stubdom"

KMACHINE = "intel-x86-64"
LINUX_KERNEL_TYPE = "stubdom"

KERNEL_FEATURES += " \
    features/xen/xen-blk-fe.scc \
    features/xen/xen-net-fe.scc \
    features/xen/xen-pci-fe.scc \
    \
    cfg/openxt-common.scc \
    cfg/initrd-gz.scc \
    \
    patches/backports/backports.scc \
    patches/openxt-service-vms/openxt-service-vms.scc \
    patches/xsa-155/xsa-155.scc \
"

require linux-yocto-openxt-5.4.inc
