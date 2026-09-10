FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://functions \
    file://lvm \
    file://bootfs \
    file://tpm \
    file://tpm2 \
    file://selinux \
    "

do_install:append() {
    install -d ${D}/init.d

    # functions
    install -m 0755 ${UNPACKDIR}/functions ${D}/init.d/00-functions

    # lvm (override oe-core do_install step)
    rm ${D}/init.d/09-lvm
    install -m 0755 ${UNPACKDIR}/lvm ${D}/init.d/89-lvm

    # bootfs
    install -m 0755 ${UNPACKDIR}/bootfs ${D}/init.d/91-bootfs

    # tpm
    install -m 0755 ${UNPACKDIR}/tpm ${D}/init.d/92-tpm

    # tpm2
    install -m 0755 ${UNPACKDIR}/tpm2 ${D}/init.d/92-tpm2

    # selinux
    install -m 0755 ${UNPACKDIR}/selinux ${D}/init.d/93-selinux
}

PACKAGES += " \
            initramfs-module-functions \
            initramfs-module-bootfs \
            initramfs-module-tpm \
            initramfs-module-tpm2 \
            initramfs-module-selinux \
            "

SUMMARY:initramfs-module-functions = "initramfs support for functions"
RDEPENDS:initramfs-module-functions = "${PN}-base"
FILES:initramfs-module-functions = "/init.d/00-functions"

SUMMARY:initramfs-module-lvm = "initramfs support for lvm"
RDEPENDS:initramfs-module-lvm = "${PN}-base lvm2"
FILES:initramfs-module-lvm = "/init.d/89-lvm"

SUMMARY:initramfs-module-bootfs = "initramfs support for bootfs"
RDEPENDS:initramfs-module-bootfs = "${PN}-base initramfs-module-rootfs"
FILES:initramfs-module-bootfs = "/init.d/91-bootfs"

SUMMARY:initramfs-module-tpm = "initramfs support for tpm"
RDEPENDS:initramfs-module-tpm = "${PN}-base initramfs-module-bootfs tpm-tools-sa"
FILES:initramfs-module-tpm = "/init.d/92-tpm"

SUMMARY:initramfs-module-tpm2 = "initramfs support for tpm2"
RDEPENDS:initramfs-module-tpm2 = "${PN}-base initramfs-module-bootfs tpm2-tools-pcr tpm2-tss-pcr"
FILES:initramfs-module-tpm2 = "/init.d/92-tpm2"

SUMMARY:initramfs-module-selinux = "initramfs support for selinux"
RDEPENDS:initramfs-module-selinux = "${PN}-base"
FILES:initramfs-module-selinux = "/init.d/93-selinux"
