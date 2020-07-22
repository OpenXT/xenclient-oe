XEN_TARGET_ARCH = "x86_64"

SRC_URI_append = "\
    file://xenconsoled.initscript \
    file://xenstored.initscript \
    file://xen-init-dom0.initscript \
    file://xen-vbd3-backend.rules \
    file://block-vbd3 \
    file://xl.conf \
"

RDEPENDS_${PN}-xl += " ${PN}-scripts-block"
RDEPENDS_${PN}-scripts-common += " ${PN}-cmp-fd-file-inode"
RDEPENDS_${PN}-scripts-block += " udev"

PACKAGES += " \
    ${PN}-cmp-fd-file-inode \
    ${PN}-toolstack-headers \
    ${PN}-xenstored-c \
    "

PROVIDES =+ "virtual/xenstored"

RPROVIDES_${PN}-xenstored-c += " virtual/xenstored"

FILES_${PN}-cmp-fd-file-inode = "${libdir}/xen/bin/cmp-fd-file-inode"

# OpenXT uses init scripts rather than systemd
FILES_${PN}-console += "${sysconfdir}/init.d/xenconsoled"

FILES_${PN}-scripts-block += " \
    ${sysconfdir}/udev/rules.d/xen-vbd3-backend.rules \
    ${sysconfdir}/xen/scripts/block-vbd3 \
"

# Export local headers for external toolstack
FILES_${PN}-toolstack-headers = "\
    ${includedir}/xc_dom.h \
    ${includedir}/xen/libelf/libelf.h \
    ${includedir}/xen/libelf/elfstructs.h \
    "

FILES_${PN}-xenstored-c = " \
    ${sbindir}/xenstored.${PN}-xenstored-c \
    ${localstatedir}/lib/xenstored \
    ${sysconfdir}/init.d/xenstored.${PN}-xenstored-c \
    ${sysconfdir}/xen/xenstored.conf \
    "

# This init script is added by OpenXT and runs the xen-init-dom0 script
# that meta-virtualization has packaged in the ${PN}-xl package.
FILES_${PN}-xl += " ${sysconfdir}/init.d/xen-init-dom0"

INITSCRIPT_PACKAGES =+ " \
    ${PN}-console \
    ${PN}-xenstored-c \
    ${PN}-xl \
"
INITSCRIPT_NAME_${PN}-console = "xenconsoled"
INITSCRIPT_PARAMS_${PN}-console = "defaults 20"
INITSCRIPT_NAME_${PN}-xenstored-c = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored-c = "defaults 05"
INITSCRIPT_NAME_${PN}-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_${PN}-xl = "defaults 21"
INITSCRIPT_NAME_${PN}-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_${PN}-xl = "defaults 21"

EXTRA_OEMAKE += "ETHERBOOT_ROMS=${STAGING_DIR_HOST}/usr/share/firmware/intel.rom"

pkg_postinst_${PN}-xenstored-c () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.${PN}-xenstored-c 200
    update-alternatives --install ${sysconfdir}/init.d/xenstored xenstored-initscript xenstored.${PN}-xenstored-c 200
}
pkg_prerm_${PN}-xenstored-c () {
    update-alternatives --remove xenstored xenstored.${PN}-xenstored-c
    update-alternatives --remove xenstored-initscript xenstored.${PN}-xenstored-c
}

do_install_append() {
    install -d ${D}${datadir}/pkgconfig

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xenconsoled.initscript \
                    ${D}${sysconfdir}/init.d/xenconsoled
    install -m 0755 ${WORKDIR}/xen-init-dom0.initscript \
                    ${D}${sysconfdir}/init.d/xen-init-dom0

    install -d ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/xl.conf \
                    ${D}${sysconfdir}/xen/xl.conf

    install -d ${D}${includedir}/xen/libelf
    install -m 0755 ${S}/tools/libxc/include/xc_dom.h \
                    ${D}${includedir}/xc_dom.h
    install -m 0755 ${S}/tools/include/xen/libelf/libelf.h \
                    ${D}${includedir}/xen/libelf/libelf.h
    install -m 0755 ${S}/tools/include/xen/libelf/elfstructs.h \
                    ${D}${includedir}/xen/libelf/elfstructs.h

    mv ${D}${sbindir}/xenstored ${D}${sbindir}/xenstored.${PN}-xenstored-c
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${sysconfdir}/init.d/xenstored.${PN}-xenstored-c

    # The C xenstored uses one additional command line argument:
    sed 's/EXECUTABLE --/EXECUTABLE --internal-db --/' \
        -i ${D}${sysconfdir}/init.d/xenstored.${PN}-xenstored-c

    # Since we don't have a xenstore stubdomain, remove the
    # xenstore stubdomain init program (libdir == /usr/lib)
    rm -f ${D}/${libdir}/xen/bin/init-xenstore-domain

    # install vbd3 work-around block script and udev rule.
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0755 ${WORKDIR}/xen-vbd3-backend.rules ${D}${sysconfdir}/udev/rules.d/xen-vbd3-backend.rules
    install -m 0755 ${WORKDIR}/block-vbd3 ${D}${sysconfdir}/xen/scripts/block-vbd3

    rm -rf ${D}/${sysconfdir}/init.d/xencommons
    rm -rf ${D}/${sysconfdir}/init.d/xendriverdomain
    rm -rf ${D}/${sysconfdir}/xen/scripts/colo-proxy-setup
    rm -rf ${D}/${sysconfdir}/xen/scripts/launch-xenstore
    rm -rf ${D}/${sysconfdir}/xen/scripts/block-dummy
    rm -rf ${D}/${sysconfdir}/default/xencommons
}
