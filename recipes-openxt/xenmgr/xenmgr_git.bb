DESCRIPTION = "XenClient xenmgr"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    xen \
    xen-libxl \
    libxenmgr-core \
    libxchutils \
    libxchargo \
    libxchxenstore \
    libxchdb \
    libxch-rpc \
    hkg-json \
    hkg-hsyslog \
    hkg-regex-posix \
    hkg-network \
    hkg-attoparsec \
    hkg-zlib \
    hkg-parsec \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    xenmgr-data \
"
RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
    xenclient-eula \
    xenclient-caps \
    heimdallr \
    bash \
"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
    file://xenmgr_dbus.conf \
    file://xenstore-init-extra \
    file://xenmgr.initscript \
"

S = "${WORKDIR}/git/xenmgr"

inherit haskell update-rc.d xc-rpcgen

INITSCRIPT_NAME = "xenmgr"
INITSCRIPT_PARAMS = "defaults 80"

FILES_${PN} += " \
    ${datadir}/xenmgr-1.0/templates/default/* \
    ${datadir}/xenclient \
    /etc/dbus-1/system.d/xenmgr_dbus.conf \
    /etc/init.d/xenmgr \
"

do_configure_append() {
    # generate rpc stubs
    mkdir -p Rpc/Autogen
    # Server objects
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/xenmgr.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/xenmgr_vm.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/xenmgr_host.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/vm_nic.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/vm_disk.xml

    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/input_daemon.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/surfman.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/guest.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/dbus.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/network_daemon.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/network.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/ctxusb_daemon.xml
}

do_install_append() {
    install -m 0755 ${S}/setup-ica-vm ${D}/usr/bin/setup-ica-vm
    install -m 0755 -d ${D}/etc
    install -m 0755 -d ${D}/etc/dbus-1/system.d
    install -m 0644 ${WORKDIR}/xenmgr_dbus.conf ${D}/etc/dbus-1/system.d/
    install -m 0755 -d ${D}/usr/share/xenclient
    install -m 0755 ${WORKDIR}/xenstore-init-extra ${D}/usr/share/xenclient/
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xenmgr.initscript ${D}${sysconfdir}/init.d/xenmgr
    install -m 0755 -d ${D}/usr/share/xenmgr-1.0/templates
    install -m 0755 -d ${D}/usr/share/xenmgr-1.0/templates/default
    install -m 0644 ${S}/../templates/default/* ${D}/usr/share/xenmgr-1.0/templates/default/
}

