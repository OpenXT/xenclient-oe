SUMMARY = "Recipe for packaging stubdomain kernel"
HOMEPAGE = "https://openxt.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

COMPATIBLE_MACHINE = "xenclient-dom0"

STUBDOMAIN_DIR = "${DEPLOY_DIR}/images/${STUBDOMAIN_MACHINE}"

FILESEXTRAPATHS_prepend := "${STUBDOMAIN_DIR}:"

SRC_URI = " \
        file://${STUBDOMAIN_KERNEL}-${STUBDOMAIN_MACHINE}.bin \
"

do_install() {
        install -d ${D}${libdir}/xen/boot
        install -m 0644 ${WORKDIR}/${STUBDOMAIN_KERNEL}-${STUBDOMAIN_MACHINE}.bin \
            ${D}${libdir}/xen/boot/qemu-stubdom-linux-kernel
}

do_checkimage() {
        if [ ! -e "${STUBDOMAIN_DIR}/${STUBDOMAIN_KERNEL}-${STUBDOMAIN_MACHINE}.bin" ]; then
                bbfatal "The stubdomain kernel must be built first"
        fi
}
addtask checkimage before do_fetch

FILES_${PN} = "${libdir}/xen/boot/qemu-stubdom-linux-kernel"

