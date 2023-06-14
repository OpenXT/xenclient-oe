SUMMARY = "OpenXT bats test scripts."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c93f84859222e5549645b5fee3d87947"

SRC_URI = "git://github.com/OpenXT/bats-suite.git;protocol=https"
SRCREV = "4615057adc2670bc558d9322fa7941363eda3b9c"

S = "${WORKDIR}/git"

inherit allarch

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

}

FILES_${PN} = " \
    ${libexecdir} \
"

RDEPENDS_${PN} = " \
    bats \
"
