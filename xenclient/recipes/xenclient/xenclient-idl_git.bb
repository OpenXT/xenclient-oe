DESCRIPTION = "XenClient IDL definitions + rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

PV = "0+git${SRCPV}"

SRCREV = "652898375d541ac2c63b3bf1350986297c4b269c"
SRC_URI = "git://github.com/openxt/idl.git;protocol=https"
S = "${WORKDIR}/git"

inherit xenclient

RDEPENDS_${PN}-dev = ""

PACKAGES = "${PN}-dev ${PN}-dbg ${PN}"
FILES_${PN}-dev += " /usr/share/* "

#
# NO NO NO 
#
# JMM - the concept of a staging binary makes no sense at all, this should be 
# compiled as a native tool
#

do_configure() {
}

do_compile() {
}

#do_stage() {
#    # Install the interface files in the staging area
#    install -m 0755 -d ${STAGING_DATADIR}/idl/
#    install -m 0644 ${WORKDIR}/git/interfaces/* ${STAGING_DATADIR}/idl/
#}

do_install() {
    install -m 0755 -d ${D}/usr/share/idl
    install -m 0644 ${WORKDIR}/git/interfaces/* ${D}/usr/share/idl/
}
