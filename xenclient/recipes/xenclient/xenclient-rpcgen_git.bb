require recipes/ghc/ghc-pkg.inc
require xenclient-rpcgen.inc
BBCLASSEXTEND = "native"

S = "${WORKDIR}/git/rpcgen"

PACKAGES = " ${PN} ${PN}-dev ${PN}-dbg "

RDEPENDS_${PN}-dev = ""

FILES_${PN}-dev  = " /usr/bin/* /usr/share/* " 

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/git/rpcgen/dist/build/xc-rpcgen/xc-rpcgen ${D}/${bindir}
    install -m 0755 -d ${D}/${datadir}/${TEMPLATES_DIR} # share
    install -m 0644 ${WORKDIR}/git/rpcgen/templates/* ${D}/${datadir}/${TEMPLATES_DIR}/
}
