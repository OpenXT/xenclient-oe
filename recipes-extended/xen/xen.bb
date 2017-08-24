require recipes-extended/xen/xen.inc
require xen-common.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = "\
    file://xenconsoled.initscript \
    file://xenstored.initscript \
"

PACKAGES += " \
    ${PN}-toolstack-headers \
    ${PN}-xenstored-c \
    "

PACKAGES_remove = " \
    ${PN}-efi \
    ${PN}-hypervisor \
    ${PN}-blktap \
    ${PN}-libblktap \
    ${PN}-libblktapctl \
    ${PN}-libblktapctl-dev \
    ${PN}-libblktap-dev \
    ${PN}-libvhd \
    ${PN}-libvhd-dev \
    ${PN}-libxenlight \
    ${PN}-libxenlight-dev \
    ${PN}-libxlutil \
    ${PN}-libxlutil-dev \
    ${PN}-xl \
    ${PN}-xenstored \
    ${PN}-xencommons \
    ${PN}-base \
    ${PN}-devd \
    "

PROVIDES =+ "${PN}-toolstack-headers"
PROVIDES_${PN}-toolstack-headers = "${PN}-toolstack-headers"

# OpenXT packages both the C and OCaml versions of XenStored.
# This recipe packages the C daemon; xen-libxl packages the Ocaml one.
PROVIDES =+ "${PN}-xenstored ${PN}-xenstored-c"
RPROVIDES_${PN}-xenstored-c = "${PN}-xenstored ${PN}-xenstored-c"

FILES_${PN}-staticdev_remove = " \
    ${libdir}/libblktapctl.a \
    ${libdir}/libblktap.a \
    ${libdir}/libvhd.a \
    "

# OpenXT uses init scripts rather than systemd.
FILES_${PN}-console += "${sysconfdir}/init.d/xenconsoled"

# Export local headers for external toolstack
FILES_${PN}-toolstack-headers = "\
    ${includedir}/xc_dom.h \
    ${includedir}/xen/libelf/libelf.h \
    ${includedir}/xen/libelf/elfstructs.h \
    "

FILES_${PN}-xenstored-c = " \
    ${sbindir}/xenstored.xen-xenstored-c \
    ${localstatedir}/lib/xenstored \
    ${sysconfdir}/init.d/xenstored.xen-xenstored-c \
    ${sysconfdir}/xen/xenstored.conf \
    "

INITSCRIPT_PACKAGES =+ "${PN}-console ${PN}-xenstored-c"
INITSCRIPT_NAME_${PN}-console = "xenconsoled"
INITSCRIPT_PARAMS_${PN}-console = "defaults 20"
INITSCRIPT_NAME_${PN}-xenstored-c = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored-c = "defaults 05"

EXTRA_OEMAKE += "ETHERBOOT_ROMS=${STAGING_DIR_HOST}/usr/share/firmware/e1000.rom"

pkg_postinst_${PN}-xenstored-c () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.${PN}-xenstored-c 200
    update-alternatives --install ${sysconfdir}/init.d/xenstored xenstored-initscript xenstored.${PN}-xenstored-c 200
}
pkg_prerm_${PN}-xenstored-c () {
    update-alternatives --remove xenstored xenstored.${PN}-xenstored-c
    update-alternatives --remove xenstored-initscript xenstored.${PN}-xenstored-c
}

do_compile() {
    oe_runmake -C tools subdir-all-include
    oe_runmake -C tools subdir-all-libxc
    oe_runmake -C tools subdir-all-flask
    oe_runmake -C tools subdir-all-xenstore
    oe_runmake -C tools subdir-all-misc
    oe_runmake -C tools subdir-all-hotplug
    oe_runmake -C tools subdir-all-xentrace
    oe_runmake -C tools subdir-all-xenmon
    oe_runmake -C tools subdir-all-console
    oe_runmake -C tools subdir-all-xenstat
    oe_runmake -C tools subdir-all-firmware
}

do_install() {
    oe_runmake DESTDIR=${D} -C tools subdir-install-include
    oe_runmake DESTDIR=${D} -C tools subdir-install-libxc
    oe_runmake DESTDIR=${D} -C tools subdir-install-flask
    oe_runmake DESTDIR=${D} -C tools subdir-install-xenstore
    oe_runmake DESTDIR=${D} -C tools subdir-install-misc
    oe_runmake DESTDIR=${D} -C tools subdir-install-hotplug
    oe_runmake DESTDIR=${D} -C tools subdir-install-xentrace
    oe_runmake DESTDIR=${D} -C tools subdir-install-xenmon
    oe_runmake DESTDIR=${D} -C tools subdir-install-console
    oe_runmake DESTDIR=${D} -C tools subdir-install-xenstat
    oe_runmake DESTDIR=${D} -C tools subdir-install-firmware

    install -m 0755 ${WORKDIR}/xenconsoled.initscript \
                    ${D}${sysconfdir}/init.d/xenconsoled

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
}

DEPENDS_append = "\
    ovmf \
    "

RDEPENDS_${PN}-base_remove = "\
    ${PN}-blktap \
    ${PN}-libblktapctl \
    ${PN}-libvhd \
    "

RRECOMMENDS_${PN}-base_remove = " \
    ${PN}-libblktap \
    "

RPROVIDES_${PN}-toolstack-headers = "${PN}-toolstack-headers"
