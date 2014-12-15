require recipes-devtools/ghc/ghc-pkg.inc

GHC_PN  = "${PN}"
SRC_URI = "http://hackage.haskell.org/package/${GHC_PN}-${PV}/${GHC_PN}-${PV}.tar.gz"
S       = "${WORKDIR}/${GHC_PN}-${PV}"
PR      = "r6"

# generate so exclude list for -dev package, .so -dev package is not used as .so provider
def gen_dev_so_excludes(d):
    excludes = []
    pkgdest = d.getVar('PKGDEST', True)
    pkg = bb.data.getVar("PN", d, True)
    top = os.path.join(pkgdest, pkg + "-dev")
    if not os.path.exists(top):
       return ""
    for root, dirs, files in os.walk(top):
        for file in files:
            if file.startswith("libH"):
                excludes.append(file)
    return " ".join(excludes)

PRIVATE_LIBS_${PN}-dev = "${@gen_dev_so_excludes(d)}"

python() {
    pn = bb.data.getVar("PN", d, True)
    pl = bb.data.getVar("PRIVATE_LIBS_" + pn + "-dev", d, True)
}



FILES_${PN} = "/usr/lib/libH*.so"
FILES_${PN}-dbg = "/usr/lib/ghc-local/*/.debug/"
FILES_${PN}-dev = "/usr/lib/ghc-local/"

#do_stage() {
#	${RUNSETUP} install
#}

do_install() {
    ${RUNSETUP} copy --destdir=${D} --verbose
    ${RUNSETUP} register --gen-pkg-config
    cp ${S}/*.conf ${D}/${prefix}/lib/ghc-local
	for x in `find ${D}/${prefix}/lib/ghc-local -name libHS*.so`; do install -m 755 $x ${D}/${prefix}/lib; done
}
