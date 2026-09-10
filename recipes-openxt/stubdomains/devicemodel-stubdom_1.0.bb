SUMMARY = "Recipe for packaging qemu stubdomain image"
HOMEPAGE = "https://openxt.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

COMPATIBLE_MACHINE = "xenclient-dom0"

# Built with MACHINE=xenclient-stubdomain; copy from deploy at install time.
# Do not use file:// SRC_URI: parse-time checksums require the artifact
# to exist before that machine has been built.
STUBDOMAIN_DIR = "${DEPLOY_DIR}/images/${STUBDOMAIN_MACHINE}"
STUBDOMAIN_NAME = "xenclient-stubdomain-initramfs-image"
STUBDOMAIN_IMAGE = "${STUBDOMAIN_DIR}/${STUBDOMAIN_NAME}-${STUBDOMAIN_MACHINE}.cpio.gz"

SRC_URI = ""
S = "${UNPACKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
        install -d ${D}${libdir}/xen/boot
        install -m 0644 ${STUBDOMAIN_IMAGE} \
            ${D}${libdir}/xen/boot/qemu-stubdom-linux-rootfs
}

do_checkimage() {
        if [ ! -e "${STUBDOMAIN_IMAGE}" ]; then
                bbfatal "The stubdomain image, ${STUBDOMAIN_NAME}, must be built first"
        fi
}
addtask checkimage before do_install

FILES:${PN} = "${libdir}/xen/boot/qemu-stubdom-linux-rootfs"

RDEPENDS:${PN} = "${STUBDOMAIN_MACHINE}-kernel"
