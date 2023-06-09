SUMMARY = "Recipe for packaging qemu stubdomain image"
HOMEPAGE = "https://openxt.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

COMPATIBLE_MACHINE = "xenclient-dom0"

STUBDOMAIN_DIR = "${DEPLOY_DIR}/images/${STUBDOMAIN_MACHINE}"
STUBDOMAIN_NAME = "xenclient-stubdomain-initramfs-image"

FILESEXTRAPATHS_prepend := "${STUBDOMAIN_DIR}:"

SRC_URI = " \
        file://${STUBDOMAIN_NAME}-${STUBDOMAIN_MACHINE}.cpio.gz;unpack=0 \
"

do_install() {
        install -d ${D}${libdir}/xen/boot
        install -m 0644 ${WORKDIR}/${STUBDOMAIN_NAME}-${STUBDOMAIN_MACHINE}.cpio.gz \
            ${D}${libdir}/xen/boot/qemu-stubdom-linux-rootfs
}

do_checkimage() {
        if [ ! -e "${STUBDOMAIN_DIR}/${STUBDOMAIN_NAME}-${STUBDOMAIN_MACHINE}.cpio.gz" ]; then
                bbfatal "The stubdomain image, ${STUBDOMAIN_NAME}, must be built first"
        fi
}
addtask checkimage before do_fetch

FILES_${PN} = "${libdir}/xen/boot/qemu-stubdom-linux-rootfs"

RDEPENDS_${PN} = "${STUBDOMAIN_MACHINE}-kernel"

