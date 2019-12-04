FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://halt-if-no-bootable.patch \
    file://xci-cpuid-signature.patch \
    file://amd-gpu-support.patch \
    file://only-boot-selected-devices.patch \
    file://gpu-pt-page-align-sections.patch \
    file://avoid-iPXE-rom-init-when-not-required.patch \
    file://hvmloader-predeployed-optionrom.patch \
    file://defconfig \
"

do_configure() {
    echo "${PV}" > .version
    cp "${WORKDIR}/defconfig" ${B}/.config
    oe_runmake oldconfig
}

INSANE_SKIP_${PN} = "src-uri-bad"
