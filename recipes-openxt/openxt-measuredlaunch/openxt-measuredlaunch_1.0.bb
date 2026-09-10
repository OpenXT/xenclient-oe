DESCRIPTION = "scripts to aid in the configuration and maintenance of measured launch"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://ml-functions \
    file://seal-system \
    file://recovery-method \
    file://seal-system.conf \
"

FILES:${PN} = "\
    ${libdir}/openxt/ml-functions \
    ${sbindir}/seal-system \
    ${sbindir}/recovery-method \
    ${sysconfdir}/openxt/seal-system.conf \
    "

do_install() {
    install -d ${D}${libdir}/openxt
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/ml-functions ${D}${libdir}/openxt
    install -m 0755 ${UNPACKDIR}/seal-system ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/recovery-method ${D}${sbindir}
    install -d ${D}${sysconfdir}/openxt
    install -m 0644 ${UNPACKDIR}/seal-system.conf ${D}${sysconfdir}/openxt/seal-system.conf
}

RDEPENDS:${PN} = " \
    bash \
    tboot-lcptools \
    tboot-lcptools-v2 \
    tboot-utils \
    tboot-pcr-calc \
    openxt-keymanagement \
"
