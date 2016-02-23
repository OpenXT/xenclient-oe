require recipes-devtools/ghc/ghc-pkg.inc

GHC_PN  = "${PN}"
SRC_URI = "http://hackage.haskell.org/package/${GHC_PN}-${PV}/${GHC_PN}-${PV}.tar.gz"
S       = "${WORKDIR}/${GHC_PN}-${PV}"
PR      = "r6"

# generate so exclude list for -dev package, .so -dev package is not used as .so provider
def gen_dev_so_excludes(d):
    excludes = []
    pkgdest = d.getVar('PKGDEST', True)
    pkg = d.getVar("PN", True)

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
    pn = d.getVar("PN", True)
    pl = d.getVar("PRIVATE_LIBS_" + pn + "-dev", True)
}



FILES_${PN} = "/usr/lib/libH*.so"
FILES_${PN}-dbg = "/usr/lib/ghc-local/*/.debug/ /usr/lib/.debug/* /usr/src/debug/*"
FILES_${PN}-dev = "/usr/lib/ghc-local/"

#do_stage() {
#	${RUNSETUP} install
#}

do_install() {
    ${RUNSETUP} copy --destdir=${D} --verbose
    ${RUNSETUP} register --gen-pkg-config
    # The config might get generated with the full sysroot path in the library or
    # include path.  Getting rid of it with sed as I am not sure how to get GHC
    # to not do that.  Room for improvement!
    for file in ${S}/*.conf
    do
        if [ -e "$file" ]; then
            echo "seding $file"
            sed -i "s#${STAGING_LIBDIR}#/usr/lib#" "$file"
            sed -i "s#${STAGING_INCDIR}#/usr/include#" "$file"
        fi
    done
    cp ${S}/*.conf ${D}/${prefix}/lib/ghc-local
	for x in `find ${D}/${prefix}/lib/ghc-local -name libHS*.so`; do install -m 755 $x ${D}/${prefix}/lib; done
}
