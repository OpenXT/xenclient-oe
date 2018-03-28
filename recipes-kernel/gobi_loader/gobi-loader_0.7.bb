SRC_URI[md5sum] = "c3aca13541be3b29e3700149256aadb6"
SRC_URI[sha256sum] = "78bdc255451cde1caa406e146b01a88828480c9c43272de8cffdb61627be754a"
DESCRIPTION = "gobi_loader"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;beginline=56;md5=9cee455f5312b78f2e739e9bc68dfd38"
DEPENDS = ""

PR = "r1"

inherit autotools-brokensep

SRC_URI = "http://www.codon.org.uk/~mjg59/gobi_loader/download/gobi_loader-0.7.tar.gz \
		file://separate_firmware_directories.patch;patch=1 "


S = "${WORKDIR}/gobi_loader-${PV}"

do_configure() {
	:
}

do_compile() {
	${CC} -c ${CFLAGS} ${LDFLAGS} gobi_loader.c
	${CC} ${LDFLAGS} -o gobi_loader gobi_loader.o
}

do_install () {
        install -d ${D}/lib/udev
        install -d ${D}/etc/udev/rules.d
        install -m 755 -c -D ${S}/gobi_loader ${D}/lib/udev/gobi_loader
        install -m 644 -c -D ${S}/60-gobi.rules ${D}/etc/udev/rules.d/60-gobi.rules
}

FILES_${PN} += " \
    /lib/udev/* \
"

