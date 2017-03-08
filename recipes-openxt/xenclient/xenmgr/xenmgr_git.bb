require recipes-devtools/ghc/ghc-xcprog.inc

DESCRIPTION = "XenClient xenmgr"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libxenmgr-core libxchutils libxchv4v libxchxenstore libxchdb xenclient-rpcgen-native xenclient-idl xen xen-libxl xenmgr-data ghc-native ghc-json ghc-hsyslog ghc-regex-posix ghc-network libxch-rpc ghc-attoparsec ghc-zlib ghc-parsec ghc-deepseq ghc-text ghc-mtl"
RDEPENDS_${PN} += "glibc-gconv-utf-32 xenclient-eula ghc-runtime xenclient-caps heimdallr"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

SRC_URI += "file://xenmgr_dbus.conf \
            file://xenstore-init-extra \
            file://xenmgr.initscript \
"

S = "${WORKDIR}/git/xenmgr"

inherit xenclient update-rc.d

INITSCRIPT_NAME = "xenmgr"
INITSCRIPT_PARAMS = "start 80 5 . stop 01 0 1 6 ."

FILES_${PN} += "/usr/bin/xenmgr /etc/dbus-1/system.d/xenmgr_dbus.conf /etc/init.d/xenmgr /usr/share/xenmgr-1.0/templates/default/*"
FILES_${PN} += "/usr/share/xenclient"

do_configure_append() {
    # generate rpc stubs
    mkdir -p Rpc/Autogen
    # Server objects
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr.xml
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_vm.xml
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_host.xml
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/vm_nic.xml
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/vm_disk.xml

    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/input_daemon.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/surfman.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/guest.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/dbus.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/network_daemon.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/network.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/ctxusb_daemon.xml
}

do_install() {
    runhaskell Setup.hs copy --destdir=${D}
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

