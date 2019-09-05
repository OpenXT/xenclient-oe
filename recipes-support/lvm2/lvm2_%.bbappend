FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://yocto-initscripts.patch \
    file://0001-lvmetad-fix-segfault-on-i386.patch \
    file://volatiles.99_lvmetad \
"

# meta-oe recipe will already _append the autotools do_install(), and
# do_<something>_append() cannot be overridden...
# So instead, overwrite the files since this is a bbappend it should be done
# after the initial do_install_append()
do_install_append() {
    if ! ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        # Use Yocto compatible initscripts instead of the RHEL ones provided by
        # the tarball.
        oe_runmake 'DESTDIR=${D}' install_initscripts_yocto
        mv -f ${D}${sysconfdir}/rc.d/init.d/* ${D}${sysconfdir}/init.d/
        rm -rf ${D}${sysconfdir}/rc.d
    fi

    install -d ${D}${sysconfdir}/default/volatiles
    install -m 0644 ${WORKDIR}/volatiles.99_lvmetad ${D}${sysconfdir}/default/volatiles/99_lvmetad
}

PACKAGES =+ "${PN}-conf"
RRECOMMENDS_${PN}_append += "${PN}-conf"

FILES_${PN}-conf = " \
    ${sysconfdir}/lvm/lvm.conf \
"
CONFFILES_${PN}-conf = " \
    ${sysconfdir}/lvm/lvm.conf \
"

FILES_${PN} += " \
    ${sysconfdir}/default/volatiles \
"
