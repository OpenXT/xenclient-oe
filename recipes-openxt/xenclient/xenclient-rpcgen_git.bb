RPCGEN_VERSION = "1.0"
require xenclient-rpcgen.inc

PV = "0+git${SRCPV}"

BBCLASSEXTEND = "native"

S = "${WORKDIR}/git/rpcgen"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/git/rpcgen/dist/build/xc-rpcgen/xc-rpcgen ${D}/${bindir}
    install -m 0755 -d ${D}/${datadir}/${TEMPLATES_DIR} # share
    install -m 0644 ${WORKDIR}/git/rpcgen/templates/* ${D}/${datadir}/${TEMPLATES_DIR}/
}
