DESCRIPTION = "SeaBIOS"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504      \
                    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"

SRCREV_source = "${AUTOREV}"

PV = "0+git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/seabios.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};name=source \
    file://build-fix.patch;patch=1 \
    file://xenclient-version.patch;patch=1 \
    file://vbe-linesize-align.patch;patch=1 \
    file://vbe-function-15h.patch;patch=1 \
    file://xengfx.patch;patch=1 \
    file://halt-if-no-bootable.patch;patch=1 \
    file://init-vgahooks-if-optionroms-deployed.patch;patch=1 \
    file://xci-cpuid-signature.patch;patch=1 \
    file://amd-gpu-support.patch;patch=1 \
    file://only-boot-selected-devices.patch;patch=1 \
    file://gpu-pt-page-align-sections.patch;patch=1 \
    file://gpu-pt-fixed-debug-port.patch;patch=1 \
    file://defconfig \
"

S= "${WORKDIR}/git"

inherit xenclient

FILES_${PN} = "/usr/share/firmware/*.bin"

PARALLEL_MAKE=""
# Use host toolchain. God please forgive me.
EXTRA_OEMAKE += "CFLAGS= LDFLAGS= CC='${BUILD_CC}' LD='${BUILD_LD}'"

do_configure () {
        install -m 0644 ${WORKDIR}/defconfig .config
        oe_runmake oldconfig
}

do_compile() {
        oe_runmake V=1
}

do_install() {
        install -d ${D}/usr/share/firmware
        install -m 0644 out/bios.bin ${D}/usr/share/firmware/
        install -m 0644 out/vgabios.bin ${D}/usr/share/firmware/
}

