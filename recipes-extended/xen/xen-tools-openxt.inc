XEN_TARGET_ARCH = "x86_64"

SRC_URI_append = "\
    file://xenconsoled.initscript \
    file://xenstored.initscript \
    file://xen-init-dom0.initscript \
    file://xl.conf \
"

RDEPENDS_${PN}-xl += " ${PN}-scripts-block"
RDEPENDS_${PN}-libxenlight += " varstored-watch"

PACKAGES += " \
    ${PN}-libxenhypfs \
    ${PN}-libxenhypfs-dev \
    "

PROVIDES =+ "virtual/xenstored"

# OpenXT uses init scripts rather than systemd
FILES_${PN}-console += "${INIT_D_DIR}/xenconsoled"

FILES_${PN}-xenstored = " \
    ${sbindir}/xenstored.${PN}-xenstored \
    ${localstatedir}/lib/xenstored \
    ${INIT_D_DIR}/xenstored.${PN}-xenstored \
    ${sysconfdir}/xen/xenstored.conf \
    "

FILES_${PN}-libxenhypfs = " \
    ${libdir}/libxenhypfs.so.* \
"
RDPENDS_${PN} += "${PN}-libxenhypfs"

FILES_${PN}-libxenhypfs-dev = " \
    ${libdir}/libxenhypfs.so \
    ${libdir}/pkgconfig/xenhypfs.pc \
    ${datadir}/pkgconfig/xenhypfs.pc \
"
FILES_${PN}-xenhypfs = " \
    ${sbindir}/xenhypfs \
"
PACKAGES += "${PN}-xenhypfs"

FILES_${PN}-xen-ucode = " \
    ${sbindir}/xen-ucode \
"
PACKAGES += "${PN}-xen-ucode"

FILES_${PN}-tests = " \
    ${libdir}/xen/bin/test-* \
"
PACKAGES += "${PN}-tests"

FILES_${PN}-misc += " \
    ${sbindir}/xen-access \
    ${sbindir}/xen-mceinj \
    ${sbindir}/xen-memshare \
    ${sbindir}/xen-vmtrace \
"

# Python programs we don't want inby default, but they are
# actually libxl runtime dependencies.
FILES_${PN}-migration-helpers = " \
    ${libdir}/xen/bin/convert-legacy-stream \
    ${libdir}/xen/bin/verify-stream-v2 \
"
PACKAGES =+ "${PN}-migration-helpers"

FILES_${PN}-bash-complete = " \
    ${sysconfdir}/bash_completion.d \
"
PACKAGES += "${PN}-bash-complete"

FILES_${PN}-xl-examples += " \
    ${sysconfdir}/xen/xlexample.pvhlinux \
"

# This init script is added by OpenXT and runs the xen-init-dom0 script
# that meta-virtualization has packaged in the ${PN}-xl package.
FILES_${PN}-xl += " ${INIT_D_DIR}/xen-init-dom0"

INITSCRIPT_PACKAGES =+ " \
    ${PN}-console \
    ${PN}-xenstored \
    ${PN}-xl \
"
INITSCRIPT_NAME_${PN}-console = "xenconsoled"
INITSCRIPT_PARAMS_${PN}-console = "defaults 20 80"
INITSCRIPT_NAME_${PN}-xenstored = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored = "defaults 05 95"
INITSCRIPT_NAME_${PN}-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_${PN}-xl = "defaults 21 79"
INITSCRIPT_NAME_${PN}-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_${PN}-xl = "defaults 21 79"

EXTRA_OEMAKE += "ETHERBOOT_ROMS=${STAGING_DIR_HOST}/usr/share/firmware/intel.rom"

pkg_postinst_${PN}-xenstored () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.${PN}-xenstored 200
    update-alternatives --install ${INIT_D_DIR}/xenstored xenstored-initscript xenstored.${PN}-xenstored 200
}
pkg_prerm_${PN}-xenstored () {
    update-alternatives --remove xenstored xenstored.${PN}-xenstored
    update-alternatives --remove xenstored-initscript xenstored.${PN}-xenstored
}

do_install_append() {
    install -d ${D}${datadir}/pkgconfig

    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/xenconsoled.initscript \
                    ${D}${INIT_D_DIR}/xenconsoled
    install -m 0755 ${WORKDIR}/xen-init-dom0.initscript \
                    ${D}${INIT_D_DIR}/xen-init-dom0

    install -d ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/xl.conf \
                    ${D}${sysconfdir}/xen/xl.conf

    mv ${D}${sbindir}/xenstored ${D}${sbindir}/xenstored.${PN}-xenstored
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${INIT_D_DIR}/xenstored.${PN}-xenstored

    # The C xenstored uses one additional command line argument:
    sed 's/EXECUTABLE --/EXECUTABLE --internal-db --/' \
        -i ${D}${INIT_D_DIR}/xenstored.${PN}-xenstored

    # Since we don't have a xenstore stubdomain, remove the
    # xenstore stubdomain init program (libdir == /usr/lib)
    rm -f ${D}/${libdir}/xen/bin/init-xenstore-domain

    rm -rf ${D}/${INIT_D_DIR}/xencommons
    rm -rf ${D}/${INIT_D_DIR}/xendriverdomain
    rm -rf ${D}/${sysconfdir}/xen/scripts/colo-proxy-setup
    rm -rf ${D}/${sysconfdir}/xen/scripts/launch-xenstore
    rm -rf ${D}/${sysconfdir}/xen/scripts/block-dummy
    rm -rf ${D}/${sysconfdir}/default/xencommons
}
