# Extended existing
FILES:${PN}-bnx2 += " \
    ${nonarch_base_libdir}/firmware/bnx2/bnx2-mips*.fw \
    ${nonarch_base_libdir}/firmware/bnx2/bnx2-rv2p*.fw \
"
RDEPENDS:${PN}-bnx2 = "${PN}-bnx2-mips"

FILES:${PN}-iwlwifi-misc += " \
    ${nonarch_base_libdir}/firmware/iwlwifi-*pnvm* \
"

# oe-core already packages ${PN}-bnx2x and ${PN}-bnx2.
