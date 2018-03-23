# initramfs image allowing to boot from location as specified on kernel
# command line, from teh choices of block device, loop back images (including
# recursive) and NFS.

COMPATIBLE_MACHINE = "(xenclient-dom0)"

SRC_URI = "file://initramfs-tcsd.conf \
           file://initramfs-passwd \
           file://initramfs-group \
           file://initramfs-nsswitch.conf \
           file://initramfs-lvm.conf \
"

IMAGE_FSTYPES = "cpio.gz"
IMAGE_INSTALL = "busybox lvm2 initramfs-xenclient"
IMAGE_INSTALL += "kernel-module-tpm kernel-module-tpm-tis kernel-module-tpm-tis-core"
IMAGE_INSTALL += "kernel-module-tpm-tis"
IMAGE_INSTALL += "tpm-tools-sa xenclient-initramfs-shared-libs"
IMAGE_INSTALL += "kernel-module-usbhid"
IMAGE_INSTALL += "kernel-module-ehci-hcd"
IMAGE_INSTALL += "kernel-module-ehci-pci"
IMAGE_INSTALL += "kernel-module-uhci-hcd"
IMAGE_INSTALL += "kernel-module-ohci-hcd"
IMAGE_INSTALL += "kernel-module-hid"
IMAGE_INSTALL += "kernel-module-hid-generic"
IMAGE_INSTALL += "module-init-tools-depmod module-init-tools"
IMAGE_INSTALL += "policycoreutils-setfiles"
IMAGE_INSTALL += "libtss2 libtctisocket libtctidevice"
IMAGE_INSTALL += "tpm2-tools"
IMAGE_LINGUAS = ""
IMAGE_DEV_MANAGER = ""
IMAGE_BOOT = "${IMAGE_DEV_MANAGER}"
# Install only ${IMAGE_INSTALL}, not even deps
PACKAGE_INSTALL_NO_DEPS = "1"

# Remove any kernel-image that the kernel-module-* packages may have pulled in.
PACKAGE_REMOVE = "kernel-image-* update-modules udev sysvinit opkg* mdev*"

# Pull in required shared libraries. Having them in a package shared with dom0 causes
# other packages to depend on it no matter what we put in its recipe...
EXTRA_INITRAMFS_LIBS = "\
    lib/ld-linux.so.2 \
    lib/libc.so.6 \
    lib/libdl.so.2 \
    lib/libnss_files.so.2 \
    usr/lib/libcrypto.so.1.0.2 \
    usr/lib/libssl.so.1.0.2 \
    usr/lib/libtspi_sa.so.1"

post_rootfs_shell_commands() {
	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};
	install -d ${IMAGE_ROOTFS}/lib;
	for a in ${EXTRA_INITRAMFS_LIBS}; do
		install -m 0755 ${STAGING_DIR_HOST}/$a ${IMAGE_ROOTFS}/lib;
		${STRIP} ${IMAGE_ROOTFS}/lib/`basename $a`;
	done;
}

write_config_files() {
	install -m 0600 -o tss -g tss ${WORKDIR}/initramfs-tcsd.conf ${IMAGE_ROOTFS}${sysconfdir}/tcsd.conf
	chown tss:tss ${IMAGE_ROOTFS}${sysconfdir}/tcsd.conf
	install -m 0644 ${WORKDIR}/initramfs-passwd ${IMAGE_ROOTFS}${sysconfdir}/passwd
	install -m 0644 ${WORKDIR}/initramfs-group ${IMAGE_ROOTFS}${sysconfdir}/group
	install -m 0644 ${WORKDIR}/initramfs-nsswitch.conf ${IMAGE_ROOTFS}${sysconfdir}/nsswitch.conf
	install -m 0644 ${WORKDIR}/initramfs-lvm.conf ${IMAGE_ROOTFS}/${sysconfdir}/lvm/lvm.conf
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; write_config_files; "

strip_files () {
	rm -rvf ${IMAGE_ROOTFS}/usr/lib/opkg;
	rm -vf ${IMAGE_ROOTFS}/usr/bin/tpm_sealdata_sa;
	rm -vf ${IMAGE_ROOTFS}/usr/bin/tpm_unsealdata_sa;
	rm -vf ${IMAGE_ROOTFS}/etc/init.d/hwclock.sh;
	rm -vf ${IMAGE_ROOTFS}/etc/init.d/mdev;
	rm -vf ${IMAGE_ROOTFS}/etc/rcS.d/S06mdev;
	rm -vf ${IMAGE_ROOTFS}/etc/rcS.d/S98configure;
	rm -vf ${IMAGE_ROOTFS}/usr/bin/opkg-cl;
	rm -vf ${IMAGE_ROOTFS}/usr/lib/ipkg;
	rm -vrf ${IMAGE_ROOTFS}/var/lib;
	rm -vrf ${IMAGE_ROOTFS}/usr/share/opkg;
	rm -vrf ${IMAGE_ROOTFS}/etc/ipkg;
	rm -vrf ${IMAGE_ROOTFS}/etc/opkg;
}

IMAGE_PREPROCESS_COMMAND += "strip_files; "

inherit image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

addtask rootfs after do_unpack

python () {
	# Ensure we run these usually noexec tasks
	d.delVarFlag("do_fetch", "noexec")
	d.delVarFlag("do_unpack", "noexec")
}
