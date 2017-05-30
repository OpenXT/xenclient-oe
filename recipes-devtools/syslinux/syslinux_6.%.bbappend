PACKAGES =+ " \
    ${PN}-isohybrid \
    ${PN}-mboot \
"
do_install_append() {
   install -m 755 ${S}/bios/utils/isohybrid ${D}${bindir}/
}

# Since version 5.00, all Syslinux variants require an additional module,
# ldlinux, to be loaded too.
# (http://www.syslinux.org/wiki/index.php?title=Library_modules).
FILES_${PN} += " ${datadir}/${BPN}/ldlinux.c32"

FILES_${PN}-isohybrid = "${bindir}/isohybrid"

# mboot.c32 requires libcom32.c32 library
# (http://www.syslinux.org/wiki/index.php?title=Library_modules).
FILES_${PN}-mboot = " \
    ${datadir}/${BPN}/mboot.c32 \
    ${datadir}/${BPN}/libcom32.c32 \
"
