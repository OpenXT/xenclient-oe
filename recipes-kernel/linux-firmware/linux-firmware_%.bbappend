# Extended existing
FILES:${PN}-bnx2 += " \
    ${nonarch_base_libdir}/firmware/bnx2/bnx2-mips*.fw \
    ${nonarch_base_libdir}/firmware/bnx2/bnx2-rv2p*.fw \
"
RDEPENDS:${PN}-bnx2 = "${PN}-bnx2-mips"

FILES:${PN}-iwlwifi-misc += " \
    ${nonarch_base_libdir}/firmware/iwlwifi-*pnvm* \
"

# New
FILES:${PN}-bnx2x = " \
    ${nonarch_base_libdir}/firmware/bnx2x/bnx2x*.fw \
"
LICENSE:${PN}-bnx2x = "WHENCE"

PACKAGE_BEFORE_PN += "${PN}-bnx2x ${PN}-bnx2"
