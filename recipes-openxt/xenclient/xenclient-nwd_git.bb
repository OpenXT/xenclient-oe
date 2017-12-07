DESCRIPTION = "XenClient Network Daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxchutils \
    hkg-hsyslog \
    libxch-rpc \
    hkg-regex-posix \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-network \
    libxchdb \
    libxchxenstore \
"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/network.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/nwd"

inherit haskell update-rc.d xc-rpcgen-haskell-1.0

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "network-daemon"
INITSCRIPT_PARAMS_${PN} = "defaults 28 15"

FILES_${PN} += "/usr/bin/network-daemon"

do_configure_append() {
    # generate rpc stubs
    mkdir -p ${S}/Rpc/Autogen

    # Server objects
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network_daemon.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network_domain.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network_nm.xml

    # Client objects
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network_slave.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/network_nm.xml

    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} -o Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/dbus.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} -o Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/xenmgr.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} -o Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/xenmgr_vm.xml
    xc-rpcgen --haskell --templates-dir=${rpcgendatadir} -o Rpc/Autogen --module-prefix=Rpc.Autogen ${idldatadir}/vm_nic.xml
}

do_install_append() {
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/nwd.initscript ${D}${sysconfdir}/init.d/network-daemon

    install -m 0755 -d ${D}${sysconfdir}/network-daemon
}
