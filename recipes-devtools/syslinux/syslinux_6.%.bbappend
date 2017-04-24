PACKAGES =+ " \
	     ${PN}-isohybrid \"
             ${PN}-mboot \ 
	    "

do_install_append() {
   install -m 755 ${S}/bios/utils/isohybrid ${D}${bindir}/
}


FILES_${PN}-isohybrid = "${bindir}/isohybrid"

FILES_${PN}-mboot = "${datadir}/${BPN}/mboot.c32"
