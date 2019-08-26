SUMMARY = "OpenXT bats test scripts."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c93f84859222e5549645b5fee3d87947"

SRC_URI = " \
    git://${OPENXT_GIT_MIRROR}/bats-suite.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
    file://openxt-bats-suite.initscript \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit allarch update-rc.d

do_install () {
    if [ -e "${S}/dom0" ]; then
        install -d ${D}/${libexecdir}/dom0
        install ${S}/dom0/* ${D}/${libexecdir}/dom0
    fi

    if [ -e "${S}/ndvm" ]; then
        install -d ${D}/${libexecdir}/ndvm
        install ${S}/ndvm/* ${D}/${libexecdir}/ndvm
    fi

    if [ -e "${S}/uivm" ]; then
        install -d ${D}/${libexecdir}/uivm
        install ${S}/uivm/* ${D}/${libexecdir}/uivm
    fi

    install -d ${D}/${sysconfdir}/init.d
    install ${WORKDIR}/openxt-bats-suite.initscript  ${D}/${sysconfdir}/init.d/openxt-bats-suite
}

PACKAGES =+ " \
    ${PN}-initscript \
"

FILES_${PN}-initscript = " \
    ${sysconfdir}/init.d \
"

INITSCRIPT_PACKAGES = "${PN}-initscript"
INITSCRIPT_NAME_${PN}-initscript = "openxt-bats-suite"
INITSCRIPT_PARAMS_${PN}-initscript = "defaults 99"

RDEPENDS_${PN}-initscript = " \
    sysvinit \
    initscripts-functions \
    ${BPN} \
"

RDEPENDS_${PN} = " \
    bats \
"
