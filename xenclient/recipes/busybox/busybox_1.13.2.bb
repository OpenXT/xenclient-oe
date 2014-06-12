require recipes/busybox/busybox.inc
PR = "${INC_PR}.3"

SRC_URI = "\
  http://www.busybox.net/downloads/busybox-${PV}.tar.gz \
  file://busybox-1.13.2-awk.patch;patch=1 \
  file://busybox-1.13.2-depmod.patch;patch=1 \
  file://busybox-1.13.2-init.patch;patch=1 \
  file://busybox-1.13.2-killall.patch;patch=1 \
  file://busybox-1.13.2-mdev.patch;patch=1 \
  file://busybox-1.13.2-modprobe.patch;patch=1 \
  file://busybox-1.13.2-printf.patch;patch=1 \
  file://busybox-1.13.2-tar.patch;patch=1 \
  file://busybox-1.13.2-top24.patch;patch=1 \
  file://busybox-1.13.2-unzip.patch;patch=1 \
  file://busybox-1.13.2-wget.patch;patch=1 \
  file://busybox-1.13.2-chpasswd-fix-for-no-shadow-file.patch;patch=1 \
  file://udhcpscript.patch;patch=1 \
  file://udhcpc-fix-nfsroot.patch;patch=1 \
  file://B921600.patch;patch=1 \
  file://get_header_tar.patch;patch=1 \
  file://busybox-appletlib-dependency.patch;patch=1 \
  file://busybox-1.13.2-sysinfo-build-fix.patch \
  file://find-touchscreen.sh \
  file://busybox-cron \
  file://busybox-udhcpd \
  file://default.script file://simple.script \
  file://hwclock.sh \
  file://mount.busybox \
  file://mountall \
  file://umount.busybox \
  file://defconfig \
  file://mdev \
  file://mdev.conf \
"
SRC_URI[md5sum] = "aeb526108f13b91c02b115c8d86f9659"
SRC_URI[sha256sum] = "03fc9dbdc6f44afd2da55c0ab36646d2d063708cc35f3f4569b913b064f11d83"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"

do_configure_prepend () {
	if [ "${TARGET_ARCH}" = "avr32" ] ; then
		sed -i s:CONFIG_FEATURE_OSF_LABEL=y:CONFIG_FEATURE_OSF_LABEL=n: ${WORKDIR}/defconfig
	fi

        # Enable ftpget and ftpput.
        for i in CONFIG_FTPGET \
                 CONFIG_FTPPUT \
                 CONFIG_FEATURE_FTPGETPUT_LONG_OPTIONS ; do
            sed -i "s/^# $i is not set\$/$i=y/" ${WORKDIR}/defconfig
        done
}

do_install_append() {
    install -m 0644 ${WORKDIR}/mdev.conf ${D}${sysconfdir}/
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/mdev
    install -m 0755 ${WORKDIR}/find-touchscreen.sh ${D}${sysconfdir}/mdev/
    install -m 0755 ${WORKDIR}/mdev ${D}${sysconfdir}/init.d/
    # SELinux doesn't like busybox links
    install -d ${D}${base_sbindir}
    cp -a ${D}${base_bindir}/busybox ${D}${base_sbindir}/udhcpc
    grep -v '/udhcpc' ${D}${sysconfdir}/busybox.links > ${S}/busybox.links.tmp
    install -m 0644 ${S}/busybox.links.tmp ${D}${sysconfdir}/busybox.links
}
