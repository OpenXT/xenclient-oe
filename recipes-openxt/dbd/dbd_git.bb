DESCRIPTION = "XenClient database daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    ocaml-dbus \
    xen-ocaml-libs \
    xenclient-toolstack \
"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
    file://dbd.initscript \
    file://db.default \
"

S = "${WORKDIR}/git/dbd"

inherit update-rc.d ocaml findlib xc-rpcgen


do_configure() {
    # generate rpc stubs
    mkdir -p autogen
    # Server objects
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o autogen ${STAGING_IDLDATADIR}/db.xml
    # Client objects
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o autogen ${STAGING_IDLDATADIR}/db.xml
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o autogen ${STAGING_IDLDATADIR}/dbus.xml
}

do_compile() {
    oe_runmake V=1 XEN_DIST_ROOT="${STAGING_DIR}" all
}

do_install() {
    # findlib.bbclass will create ${D}${sitelibdir} for generic ocamlfind
    # compliance with bitbake. This does not ship any library though.
    rm -rf ${D}${libdir}
    oe_runmake V=1 DESTDIR="${D}" install
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/dbd.initscript ${D}${sysconfdir}/init.d/dbd
    install -m 0755 -d ${D}/usr/share/xenclient
    install -m 0644 ${WORKDIR}/db.default ${D}/usr/share/xenclient/db.default
}

INITSCRIPT_NAME = "dbd"
INITSCRIPT_PARAMS = "defaults 25"

FILES_${PN} += " \
    ${datadir}/xenclient/db.default \
    ${sysconfdir}/init.d/* \
"
RDEPENDS_${PN} += "bash"
