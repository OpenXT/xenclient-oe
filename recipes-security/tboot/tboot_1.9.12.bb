require tboot.inc

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=7730ab1e15a162ca347bcc1722486d89"

S = "${WORKDIR}/${PN}-${PV}"

SRC_URI = " \
    https://downloads.sourceforge.net/project/${BPN}/${BPN}/${BPN}-${PV}.tar.gz \
    file://0001-Do-not-try-to-read-EFI-mem-map-when-booted-with-mult.patch \
    file://0002-grub2-Adjust-module-placement-locations-when-changin.patch \
    file://0003-tboot-Propagate-failure-to-map_tboot_pages.patch \
    file://0004-tboot-TB_POLTYPE_WARN_ON_FAILURE-with-pre-post.patch \
    file://0005-tboot-Mark-TPM-region-reserved-if-not-already.patch \
    file://0006-pcr-calc-Add-pcr-calculator-tool.patch \
    file://0007-tpm2.0-Perform-orderly-shutdown.patch \
    file://0008-tboot-Export-TPM-event-log-to-VMM-Kernel.patch \
    file://0009-Find-e820-regions-that-include-the-limit.patch \
    file://0010-Add-support-for-launching-64-bit-PE-kernels.patch \
"
SRC_URI[md5sum] = "29cc2524e48eaba12249d3476be219b2"
SRC_URI[sha256sum] = "007212deacab8eb159d71449766f9b2e0523439f1c4fd64d1932eb38cb60e053"

inherit deploy

# safestringlib/safeclib/mem_primitives_lib.c has a lot of fallthrough.
CFLAGS_append = "-Wno-implicit-fallthrough"
EXTRA_OEMAKE = "INSTALL_STRIP=''"

do_compile() {
    oe_runmake SUBDIRS="tboot" CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}"
    if [ "${TBOOT_TARGET_ARCH}" != "x86_32" ]; then
        # Safestringlib is built statically by tboot right before and
        # TBoot is always 32bit (-m32 -march=i686).
        # Clean and rebuild for now.
        oe_runmake SUBDIRS="safestringlib" clean
    fi
    oe_runmake SUBDIRS="safestringlib lcptools lcptools-v2 tb_polgen utils pcr-calc" TARGET_ARCH="${TBOOT_TARGET_ARCH}"
}

do_deploy() {
    install -m 0644 "${D}/boot/tboot.gz" "${DEPLOYDIR}/tboot.gz"
}
addtask do_deploy after do_install before do_build
