PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://pci.ids.gz.20180615 \
           "

do_install_append () {
	# "make install" misses the debug file for the library
	oe_libinstall -so -C lib libpci ${D}/${libdir}

	install -m 0644 ${WORKDIR}/pci.ids.gz.20180615 ${D}/${datadir}/pci.ids.gz
}
