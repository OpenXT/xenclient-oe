DESCRIPTION = "XenClient database daemon and tools."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

DEPENDS = " \
    ocaml-dbus \
    xen-tools \
    openxt-ocaml-libs \
"

require manager.inc

SRC_URI += " \
    file://dbd.initscript \
    file://db.default \
    file://db-exists-dom0 \
    file://db-ls-dom0 \
    file://db-nodes-dom0 \
    file://db-read-dom0 \
    file://db-rm-dom0 \
    file://db-write-dom0 \
    file://db-cat-dom0 \
"

S = "${WORKDIR}/git/dbd"
# brokensep.
B = "${S}"

inherit update-rc.d ocaml findlib xc-rpcgen

do_configure() {
    # generate rpc stubs
    mkdir -p ${B}/autogen
    # Server objects
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -s -o ${B}/autogen ${STAGING_IDLDATADIR}/db.xml
    # Client objects
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o ${B}/autogen ${STAGING_IDLDATADIR}/db.xml
    xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o ${B}/autogen ${STAGING_IDLDATADIR}/dbus.xml
}

do_install() {
    oe_runmake DESTDIR="${D}" install

    # findlib.bbclass will create ${D}${sitelibdir} for generic ocamlfind
    # compliance with bitbake. This does not ship any library though.
    rm -rf ${D}${libdir}

    # dbd
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/dbd.initscript ${D}${sysconfdir}/init.d/dbd
    install -m 0755 -d ${D}${datadir}/xenclient
    install -m 0644 ${WORKDIR}/db.default ${D}${datadir}/xenclient/db.default

    # dbd-tools
    for bin in \
        db-exists db-ls db-nodes db-read db-rm db-write db-cat
    do
        install -m 0755 ${S}/${bin} ${D}${bindir}/${bin}
    done

    # dbd-tools-vm
    for bin in \
        db-exists-dom0 db-ls-dom0 db-nodes-dom0 db-read-dom0 db-rm-dom0 \
        db-write-dom0 db-cat-dom0
    do
        install -m 0755 ${WORKDIR}/${bin} ${D}${bindir}/${bin}
    done
}

PACKAGES =+ " \
    ${PN}-tools \
    ${PN}-tools-vm \
"
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "dbd"
INITSCRIPT_PARAMS_${PN} = "defaults 25 19"

FILES_${PN} += " \
    ${datadir}/xenclient/db.default \
    ${sysconfdir}/init.d/* \
"
RDEPENDS_${PN} += "bash"
RRECOMMENDS_${PN} += "dbd-tools"

FILES_${PN}-tools = " \
    ${bindir}/db-cmd \
    ${bindir}/db-exists \
    ${bindir}/db-ls \
    ${bindir}/db-nodes \
    ${bindir}/db-read \
    ${bindir}/db-rm \
    ${bindir}/db-write \
    ${bindir}/db-cat \
"

FILES_${PN}-tools-vm = " \
    ${bindir}/db-exists-dom0 \
    ${bindir}/db-ls-dom0 \
    ${bindir}/db-nodes-dom0 \
    ${bindir}/db-read-dom0 \
    ${bindir}/db-rm-dom0 \
    ${bindir}/db-write-dom0 \
    ${bindir}/db-cat-dom0 \
"
RDEPENDS_${PN}-tools-vm += " \
    libargo \
    ${PN}-tools \
"
