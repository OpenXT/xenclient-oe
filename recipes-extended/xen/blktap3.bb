DESCRIPTION = "blktap3"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a9e8593dad23434929bc6218a17b5634"
DEPENDS = "xen-tools libicbinn"

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
    file://Revert-CP-9798-Update-cgroups-path.patch \
    file://fix-encryption.patch \
    file://gcc9-compilation.patch \
    file://openssl-1.1.x.patch \
    file://0001-Set-libvhdio-libtool-version-info.patch \
"

S = "${WORKDIR}/git"

inherit autotools-brokensep update-rc.d

PACKAGES =+ "tapback"

INITSCRIPT_PACKAGES = "tapback"
INITSCRIPT_NAME_tapback = "tapback"
INITSCRIPT_PARAMS_tapback = "defaults 61 39"

TARGET_CPPFLAGS += "-DTAP_CTL_NO_DEFAULT_CGROUP_SLICE -DOPEN_XT"

do_configure_prepend() {
	touch ${S}/EXTRAVERSION
}

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        rm -rf ${D}/usr/lib/systemd
    fi
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/tapback.initscript \
                    ${D}${INIT_D_DIR}/tapback
}

FILES_${PN}-dev += " \
    ${libdir}/libblktapctl.so \
    ${libdir}/libvhd.so \
    ${libdir}/libvhdio.so \
    ${libdir}/libblockcrypto.so \
"
FILES_${PN}-cpumond = " \
    ${bindir}/cpumond \
"

FILES_${PN}-doc += " \
    /etc/xensource/bugtool/tapdisk-logs.xml \
    /etc/xensource/bugtool/tapdisk-logs/description.xml \
"

FILES_${PN} += " \
    ${libdir}/libvhdio-*.so \
"
FILES_tapback += " \
    ${bindir}/tapback \
    ${INIT_D_DIR}/tapback \
"
RDEPENDS_${PN} += "glibc-gconv-utf-16"
RCONFLICTS_${PN} = "xen-blktap xen-libblktap xen-libblktapctl xen-libvhd"
