DESCRIPTION = "scripts to aid in the configuration and maintenance of measured launch"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

SRC_URI = " \
    file://ml-functions \
    file://seal-system \
    file://recovery-method \
"

FILES_${PN} = "\
    ${libdir}/openxt/ml-functions \
    ${sbindir}/seal-system \
    ${sbindir}/recovery-method \
    "

do_install() {
    install -d ${D}${libdir}/openxt
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/ml-functions ${D}${libdir}/openxt
    install -m 0755 ${WORKDIR}/seal-system ${D}${sbindir}
    install -m 0755 ${WORKDIR}/recovery-method ${D}${sbindir}
}

RDEPENDS_${PN} = " \
    bash \
    tboot-lcptools \
    tboot-lcptools-v2 \
    tboot-utils \
    tboot-pcr-calc \
    openxt-keymanagement \
"
