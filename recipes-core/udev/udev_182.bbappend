PR .= ".1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OECONF += " \
        --enable-static \
        --with-selinux \
        --with-usb-ids-path=/usr/share/usb.ids \
        --with-pci-ids-path=/usr/share/pci.ids \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

inherit update-rc.d

INITSCRIPT_PARAMS_udev += "start 04 5 ."

DEPENDS += " libselinux "

SRC_URI += " \
        file://${PACKAGE_ARCH}-init \
        file://0001-mtd_probe.h-Add-stdint.h-as-it-was-removed-from-mtd-.patch \
        file://0002-configure.ac-Makefile.am-Check-for-input.h-and-input.patch \
        file://usb-hid-no-autosleep.patch;patch=1 \
        file://disable-cdrom-locking-by-dom0.patch;patch=2 \
        file://05-db.rules \
        "

do_install_append () {
        mkdir ${D}/${base_sbindir}
        (cd ${D}${base_sbindir}; ln -s ..${base_libdir}/udev/udevd .)

        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/${PACKAGE_ARCH}-init ${D}${sysconfdir}/init.d/udev

        install -d ${D}/etc
        install -d ${D}/etc/udev
        install -d ${D}/etc/udev/rules.d
        install ${WORKDIR}/05-db.rules ${D}/etc/udev/rules.d
}
