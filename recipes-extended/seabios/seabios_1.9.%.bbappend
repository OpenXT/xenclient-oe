FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
# meta-virtualization recipe for seabios will fetch using the http:// source
# which is 308 to the https://, throwing the fetcher off.
PREMIRRORS_prepend += " \
    http://code.coreboot.org/p/seabios/downloads/.* https://code.coreboot.org/p/seabios/downloads/${PN}-${PV}.tar.gz \
"
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
