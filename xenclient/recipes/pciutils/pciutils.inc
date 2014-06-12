DESCRIPTION = 'The PCI Utilities package contains a library for portable access \
to PCI bus configuration space and several utilities based on this library.'
DESCRIPTION_pciutils-ids = 'The list of PCI IDs for pciutils'
SECTION = "console/utils"
HOMEPAGE = "http://atrey.karlin.mff.cuni.cz/~mj/pciutils.shtml"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
DEPENDS = "zlib"

SRC_URI = "ftp://ftp.kernel.org/pub/software/utils/pciutils/pciutils-${PV}.tar.bz2"
SRC_URI += "file://pci.ids.gz.20110801 \
"

SRC_URI_append_nylon = "file://gcc-3-compatibility.patch;patch=1 "

PARALLEL_MAKE = ""

PR = "r6"

EXTRA_OEMAKE += "'STRIP = ' PREFIX=${prefix} LIBDIR=${libdir}"
export SHARED = "yes"
export DESTDIR = "${D}"
export PREFIX = "${prefix}"
export SBINDIR = "${sbindir}"
export SHAREDIR = "${datadir}"
export MANDIR = "${mandir}"
export IDSDIR = "${datadir}"

do_configure () {
	export ZLIB=yes
	(cd lib && ./configure ${datadir} ${PV} ${TARGET_OS} 2.4.21 ${TARGET_ARCH})
}

do_install () {
	oe_runmake install install-lib

	# "make install" misses the debug file for the library
	oe_libinstall -so -C lib libpci ${D}/${libdir}

	install -m 0644 ${WORKDIR}/pci.ids.gz.20110801 ${D}/${datadir}/pci.ids.gz
}

#do_stage () {
#	oe_runmake install-lib DESTDIR=${STAGING_DIR_HOST}
#
#	# "make install-lib" misses the symlink libpci.so.3
#	oe_libinstall -so -C lib libpci ${STAGING_LIBDIR}
#
#        install -d -m 0755 ${STAGING_DATADIR}
#	install -m 0644 ${WORKDIR}/pci.ids.gz.20110801 ${STAGING_DATADIR}/pci.ids.gz
#}


PACKAGES =+ "${PN}-ids"
FILES_${PN}-ids = "${datadir}/pci.ids.gz"
