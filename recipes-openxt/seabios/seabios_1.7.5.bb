require seabios.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504         \
                    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6  \
                    "
DEPENDS = "iasl-native"

SRC_URI += "file://openxt-version.patch;patch=1                         \
            file://halt-if-no-bootable.patch;patch=1                    \
            file://init-vgahooks-if-optionroms-deployed.patch;patch=1   \
            file://xci-cpuid-signature.patch;patch=1                    \
            file://amd-gpu-support.patch;patch=1                        \
            file://only-boot-selected-devices.patch;patch=1             \
            file://gpu-pt-page-align-sections.patch;patch=1             \
            file://gcc5.patch;patch=1                                   \
            file://avoid-iPXE-rom-init-when-not-required.patch;patch=1  \
            "

SRC_URI += "file://defconfig"
SRC_URI[tarball.md5sum] = "3f1e17485ca327b245ae5938d9aa02d9"
SRC_URI[tarball.sha256sum] = "858d9eda4ad91efa1c45a5a401d560ef9ca8dd172f03b0a106f06661c252dc51"

PR = "r1"

FILES_${PN} = "/usr/share/firmware"

# Use x87 inst/regs for scalar code (HVM triple faulting).
EXTRA_OEMAKE = '                        \
    CPP="${CROSS_COMPILE}cpp"           \
    CC="${CC} -mno-sse -mfpmath=387"    \
'
PARALLEL_MAKE=""

do_configure() {
    install -m 0644 "${WORKDIR}/defconfig" .config
    oe_runmake oldconfig
}

do_install() {
    install -d ${D}/usr/share/firmware
    install -m 0644 out/bios.bin ${D}/usr/share/firmware/
}

