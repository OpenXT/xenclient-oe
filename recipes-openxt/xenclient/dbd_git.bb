inherit findlib
require recipes-devtools/ghc/ghc-pkg.inc

DESCRIPTION = "XenClient database daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xenclient-idl ocaml-cross ocaml-dbus ocaml-libs xen-ocaml-libs xenclient-rpcgen-native"

# Ocaml stuff is built with the native compiler with "-m32".

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

SRC_URI += "file://dbd.initscript \
            file://db.default \
"

FILES_${PN} += "/usr/share/xenclient/db.default"
RDEPENDS_${PN} += "bash"

S = "${WORKDIR}/git/dbd"

inherit xenclient update-rc.d

INITSCRIPT_NAME = "dbd"
INITSCRIPT_PARAMS = "defaults 25"

do_configure() {
    # generate rpc stubs
    mkdir -p autogen
    # Server objects
    xc-rpcgen --camel -s -o autogen ${STAGING_DATADIR}/idl/db.xml
    # Client objects
    xc-rpcgen --camel -c -o autogen ${STAGING_DATADIR}/idl/db.xml
    xc-rpcgen --camel -c -o autogen ${STAGING_DATADIR}/idl/dbus.xml
}

do_compile() {
    # hack
    touch ${STAGING_LIBDIR}/ocaml/ld.conf
    # dbd
    make V=1 XEN_DIST_ROOT="${STAGING_DIR}" TARGET_PREFIX="${TARGET_PREFIX}" STAGING_DIR="${STAGING_DIR}" STAGING_BINDIR_CROSS="${STAGING_BINDIR_CROSS}" STAGING_LIBDIR="${STAGING_LIBDIR}" STAGING_INCDIR="${STAGING_INCDIR}" all
}

do_install() {
    make DESTDIR=${D} V=1 install
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/dbd.initscript ${D}${sysconfdir}/init.d/dbd
    install -m 0755 -d ${D}/usr
    install -m 0755 -d ${D}/usr/share
    install -m 0755 -d ${D}/usr/share/xenclient
    install -m 0644 ${WORKDIR}/db.default ${D}/usr/share/xenclient/db.default
}

# Avoid GNU_HASH check for the ocaml binaries
INSANE_SKIP_${PN} = "1"
