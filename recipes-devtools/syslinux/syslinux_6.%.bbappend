FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://pxelinux.cfg \
    file://isolinux.cfg \
    file://bootmsg.txt \
"

inherit deploy

do_deploy() {
    # Netboot modules
    install -m 0755 -d "${DEPLOYDIR}/netboot"
    for bin in pxelinux.0 ldlinux.c32 mboot.c32 libcom32.c32; do
        install -m 0644 "${D}${datadir}/syslinux/${bin}" "${DEPLOYDIR}/netboot"
    done
    # Netboot configuration
    install -m 0644 "${WORKDIR}/pxelinux.cfg" "${DEPLOYDIR}/netboot/pxelinux.cfg"

    # ISO installer modules
    install -m 0755 -d "${DEPLOYDIR}/iso"
    for bin in isolinux.bin ldlinux.c32 mboot.c32 libcom32.c32; do
        install -m 0644 "${D}${datadir}/syslinux/${bin}" "${DEPLOYDIR}/iso"
    done
    # ISO-hybrid packaging (see build-scripts).
    install -m 0644 "${D}${datadir}/syslinux/isohdpfx.bin" "${DEPLOYDIR}/"
    # ISO configuration
    install -m 0644 "${WORKDIR}/isolinux.cfg" "${DEPLOYDIR}/iso/isolinux.cfg"
    install -m 0644 "${WORKDIR}/bootmsg.txt" "${DEPLOYDIR}/iso/bootmsg.txt"
}
do_deploy_class-native() {
    :
}

addtask do_deploy after do_install before do_build

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
