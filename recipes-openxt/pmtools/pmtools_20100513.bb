LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

MIRRORS_append += " \
    http://.*/.*      https://openxt.ainfosec.com/mirror/ \
"
SRC_URI = " \
    ${OPENXT_MIRROR}/pmtools-${PV}.tar.bz2 \
"
SRC_URI[md5sum] = "613d596275528a4c41e5dbf7a48f3ed2"
SRC_URI[sha256sum] = "bffe9985f3079c01a534a408a3d0ccd38965b3e607fd2cc6a384648027a17f18"

S = "${WORKDIR}/pmtools"

do_compile() {
        export CFLAGS="${CFLAGS} ${LDFLAGS}"
        oe_runmake	
}

do_install() {
	install -d ${D}${bindir}
	install ${S}/acpidump/acpidump ${D}${bindir}
        install ${S}/acpixtract/acpixtract ${D}${bindir}
        install ${S}/turbostat/turbostat ${D}${bindir}
        install ${S}/pmtest/pmtest ${D}${bindir}
}
