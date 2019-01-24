DESCRIPTION = "blktap3"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a9e8593dad23434929bc6218a17b5634"
DEPENDS = "xen libicbinn"

PV = "0+git${SRCPV}"

SRCREV = "a7832564b4d7e540d2d5a85e2556f571b7f9d89b"
SRC_URI = "git://github.com/xapi-project/blktap.git;protocol=https \
    file://tapback.initscript \
    file://fix-format-specifier-errors.patch \
    file://compiler-errors-fix.patch \
    file://remove-inline-function-declarations.patch \
    file://OXT-specific-errors-fix.patch \
    file://fix-run-time-errors-and-memory-leaks.patch \
    file://fix-segfault-if-startup-fails.patch \
    file://remove-creation-of-unused-log-files.patch \
    file://add-device-string-support-to-tap-destroy.patch \
    file://fix-error-checks.patch \
    file://add-missing-files-to-gitignore.patch \
    file://blktap3-vhd-icbinn-support.patch \
"

S = "${WORKDIR}/git"

inherit autotools-brokensep xenclient update-rc.d

INITSCRIPT_NAME = "tapback-daemon"
INITSCRIPT_PARAMS = "defaults 61"

TARGET_CPPFLAGS += "-DTAP_CTL_NO_DEFAULT_CGROUP_SLICE"

do_configure_prepend() {
	touch ${S}/EXTRAVERSION
}

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
		rm -rf ${D}/usr/lib/systemd
	fi
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/tapback.initscript \
		${D}/etc/init.d/tapback-daemon
}

# QA dev-elf: libvhdio-3.5.0.so does not honor the SOLIBSDEV format.
FILES_SOLIBSDEV = ""
FILES_${PN}-dev += " \
    ${libdir}/libblktapctl.so \
    ${libdir}/libvhd.so \
    ${libdir}/libvhdio.so \
    ${libdir}/libblockcrypto.so \
"
FILES_${PN} += " \
    ${libdir}/libvhdio-*.so \
"
RDEPENDS_${PN} += "glibc-gconv-utf-16"
RCONFLICTS_${PN} = "xen-blktap xen-libblktap xen-libblktapctl xen-libvhd"
