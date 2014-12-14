require busybox.inc
PR = "${INC_PR}.1"

SRC_URI = "\
  http://www.busybox.net/downloads/busybox-${PV}.tar.gz \
  \
  file://udhcpscript.patch;patch=1 \
  file://B921600.patch;patch=1 \
  file://fdisk_lineedit_segfault.patch;patch=1 \
  file://iptunnel.patch;patch=1 \
  file://busybox-appletlib-dependency.patch;patch=1 \
  file://busybox-sysinfo-build-fix.patch \
  file://busybox-cron \
  file://busybox-httpd \
  file://busybox-udhcpd \
  file://default.script file://simple.script \
  file://hwclock.sh \
  file://mount.busybox \
  file://mountall \
  file://syslog \
  file://syslog.conf \
  file://umount.busybox \
  file://defconfig \
  file://mdev \
  file://mdev.conf \
"
SRC_URI[md5sum] = "d113f2777e4f508faa8b674ece87a37b"
RC_URI[sha256sum] = "f2b433270f22aad86f61cef61d95338aaa63dbe2f9468b9ed5defd1c01c9645f"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"

do_install_append() {
    install -m 0644 ${WORKDIR}/mdev.conf ${D}${sysconfdir}/
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/mdev ${D}${sysconfdir}/init.d/
}

