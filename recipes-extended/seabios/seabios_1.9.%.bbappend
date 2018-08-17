FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI += " \
    file://halt-if-no-bootable.patch \
    file://init-vgahooks-if-optionroms-deployed.patch \
    file://xci-cpuid-signature.patch \
    file://amd-gpu-support.patch \
    file://only-boot-selected-devices.patch \
    file://gpu-pt-page-align-sections.patch \
    file://avoid-iPXE-rom-init-when-not-required.patch \
    file://defconfig \
"

do_configure() {
    cp "${WORKDIR}/defconfig" ${B}/.config
    oe_runmake oldconfig
}
