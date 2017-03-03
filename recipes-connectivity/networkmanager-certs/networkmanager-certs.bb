DESCRIPTION = "Tool to copy NM certificates"
LICENSE = "MIT"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} += "libtirpc libicbinn libicbinn-client xen-xenstore"

SRC_URI = "file://certs-sync \
           file://populate-certs.sh \
"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "populate-certs.sh"
INITSCRIPT_PARAMS_${PN} = "start 28 2 3 4 5 ."
inherit update-rc.d

do_install () {
        install -d ${D}/usr/bin
        install -m 0755 ${WORKDIR}/certs-sync ${D}/usr/bin

	install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/populate-certs.sh ${D}${sysconfdir}/init.d
}

do_install_append_xenclient-uivm() {
    install -d ${D}/${localstatedir}/lib/NetworkManager
}
