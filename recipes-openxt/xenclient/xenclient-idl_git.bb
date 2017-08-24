DESCRIPTION = "XenClient IDL definitions + rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = " \
	git://${OPENXT_GIT_MIRROR}/idl.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
	file://ovmf.patch;patch=1 \
	"
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
