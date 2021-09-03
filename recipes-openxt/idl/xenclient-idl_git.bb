DESCRIPTION = "XenClient IDL definitions + rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

require idl.inc

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -m 0755 -d ${D}${idldatadir}
    install -m 0644 ${S}/interfaces/* ${D}${idldatadir}
}
