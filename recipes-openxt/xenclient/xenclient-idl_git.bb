DESCRIPTION = "XenClient IDL definitions + rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/idl.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -m 0755 -d ${D}${idldatadir}
    install -m 0644 ${WORKDIR}/git/interfaces/* ${D}${idldatadir}
}
