COMPATIBLE_MACHINE = "openxt-ndvm"

KMACHINE = "intel-x86-64"
LINUX_KERNEL_TYPE = "ndvm"

KERNEL_FEATURES += " \
    cgl/features/selinux/selinux.scc \
    features/netfilter/netfilter.scc \
    features/netfilter/netfilter-physdev.scc \
    \
    cfg/openxt-common.scc \
    \
    bsp/common-pc/common-pc-eth-extended.scc \
    bsp/common-pc/common-pc-wifi-extended.scc \
    \
    patches/backports/backports.scc \
    patches/openxt-service-vms/openxt-service-vms.scc \
    patches/openxt-bridge-quirks/openxt-bridge-quirks.scc \
    patches/openxt-vwif/openxt-vwif.scc \
    patches/xsa-155/xsa-155.scc \
"

require linux-yocto-openxt-5.4.inc
