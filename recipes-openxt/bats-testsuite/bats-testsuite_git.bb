SUMMARY = "OpenXT bats test scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9cfc4a8eac3fe9bdc740b8e3760c5ade"

SRC_URI = "git://github.com/apertussolutions/openxt-bats-suite.git;protocol=git"
SRCREV = "c69273d5b57d5fe47a8b40a7df8fa1d4bf14599f"

S = "${WORKDIR}/git"

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
	${libexecdir}/*"

RDEPENDS_${PN} = "bats"
