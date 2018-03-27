PACKAGES =+ " \
    ${PN}-ldlinux \
    ${PN}-mboot \
"

# Since version 5.00, all Syslinux variants require an additional module,
# ldlinux, to be loaded too.
# (http://www.syslinux.org/wiki/index.php?title=Library_modules).
FILES_${PN}-ldlinux += "\
	${datadir}/${BPN}/ldlinux.c32 \
"

# mboot.c32 requires libcom32.c32 library
# (http://www.syslinux.org/wiki/index.php?title=Library_modules).
FILES_${PN}-mboot = " \
    ${datadir}/${BPN}/mboot.c32 \
    ${datadir}/${BPN}/libcom32.c32 \
"

# Don't let the sanity checker trip on the 32 bit real mode BIOS binaries
INSANE_SKIP_${PN}-ldlinux = "arch"
INSANE_SKIP_${PN}-mboot = "arch"

inherit deploy

do_deploy() {
   install -m 644 ${S}/efi64/mbr/isohdpfx.bin ${DEPLOYDIR}/
}

addtask do_deploy after do_compile before do_build
