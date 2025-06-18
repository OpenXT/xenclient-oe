FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://functions \
    file://lvm \
    file://bootfs \
    file://tpm \
    file://tpm2 \
    file://selinux \
    "

do_install_append() {
    install -d ${D}/init.d

    # functions
    install -m 0755 ${WORKDIR}/functions ${D}/init.d/00-functions

    # lvm (override oe-core do_install step)
    rm ${D}/init.d/09-lvm
    install -m 0755 ${WORKDIR}/lvm ${D}/init.d/89-lvm

    # bootfs
    install -m 0755 ${WORKDIR}/bootfs ${D}/init.d/91-bootfs

    # tpm
    install -m 0755 ${WORKDIR}/tpm ${D}/init.d/92-tpm

    # tpm2
    install -m 0755 ${WORKDIR}/tpm2 ${D}/init.d/92-tpm2

    # selinux
    install -m 0755 ${WORKDIR}/selinux ${D}/init.d/93-selinux
}

PACKAGES += " \
            initramfs-module-functions \
            initramfs-module-bootfs \
            initramfs-module-tpm \
            initramfs-module-tpm2 \
            initramfs-module-selinux \
            "

SUMMARY_initramfs-module-functions = "initramfs support for functions"
RDEPENDS_initramfs-module-functions = "${PN}-base"
FILES_initramfs-module-functions = "/init.d/00-functions"

SUMMARY_initramfs-module-lvm = "initramfs support for lvm"
RDEPENDS_initramfs-module-lvm = "${PN}-base lvm2"
FILES_initramfs-module-lvm = "/init.d/89-lvm"

SUMMARY_initramfs-module-bootfs = "initramfs support for bootfs"
RDEPENDS_initramfs-module-bootfs = "${PN}-base initramfs-module-rootfs"
FILES_initramfs-module-bootfs = "/init.d/91-bootfs"

SUMMARY_initramfs-module-tpm = "initramfs support for tpm"
RDEPENDS_initramfs-module-tpm = "${PN}-base initramfs-module-bootfs tpm-tools-sa"
FILES_initramfs-module-tpm = "/init.d/92-tpm"

SUMMARY_initramfs-module-tpm2 = "initramfs support for tpm2"
RDEPENDS_initramfs-module-tpm2 = "${PN}-base initramfs-module-bootfs tpm2-tools-pcr tpm2-tss-pcr"
FILES_initramfs-module-tpm2 = "/init.d/92-tpm2"

SUMMARY_initramfs-module-selinux = "initramfs support for selinux"
RDEPENDS_initramfs-module-selinux = "${PN}-base"
FILES_initramfs-module-selinux = "/init.d/93-selinux"
