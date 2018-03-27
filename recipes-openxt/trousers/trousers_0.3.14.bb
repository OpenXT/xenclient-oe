SRC_URI[md5sum] = "4a476b4f036dd20a764fb54fc24edbec"
SRC_URI[sha256sum] = "ce50713a261d14b735ec9ccd97609f0ad5ce69540af560e8c3ce9eb5f2d28f47"
DEPENDS = "openssl"
LICENSE = "CPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8031b2ae48ededc9b982c08620573426"

PR = "r0"

SRC_URI = "http://downloads.sourceforge.net/${PN}/${PN}-${PV}.tar.gz;subdir=${PN}-${PV} \
           file://trousers-tcsd-conf.patch;patch=1 \
           file://trousers-standalone.patch;patch=1 \
           file://trousers-tcsd-dont-pthread-exit.patch;patch=1 \
           file://trousers.initscript \
           file://45-trousers.rules \
"

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECONF += " --disable-usercheck"
CFLAGS_append = " -Wno-error=unused-parameter -Wno-error=strict-aliasing -std=gnu89"

inherit update-rc.d useradd autotools-brokensep pkgconfig


INITSCRIPT_PACKAGES = "${PN}"

INITSCRIPT_NAME_${PN} = "trousers"
INITSCRIPT_PARAMS_${PN} = "defaults 85"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--gid 421 tss"
USERADD_PARAM_${PN} = "--system --home-dir /boot/system/tpm --shell /bin/false --gid tss --uid 421 tss"

pkg_postinst_${PN}() {
        chown tss:tss $D/etc/tcsd.conf
        mkdir -p $D/boot/system/tpm
        chown tss:tss $D/boot/system/tpm
        install -o tss -g tss -m 600 $D/usr/share/trousers/system.data.auth $D/boot/system/tpm/system.data
}

do_install_append() {
	install -m 0755 -d ${D}${datadir}/trousers
	install -m 0644 ${B}/dist/system.data.auth ${D}${datadir}/trousers/
	install -m 0644 ${B}/dist/system.data.noauth ${D}${datadir}/trousers/
        install -m 0755 -d ${D}/etc/init.d
        install -m 0755 ${WORKDIR}/trousers.initscript ${D}/etc/init.d/trousers
        install -m 0755 -d ${D}/etc/udev/rules.d
        install -m 0644 ${WORKDIR}/45-trousers.rules ${D}/etc/udev/rules.d
}

RDEPENDS_${PN} = "libgcc"
PACKAGES =+ "${PN}-data"
FILES_${PN}-data = "${datadir}/trousers/system.data.auth \
	${datadir}/trousers/system.data.noauth \
"
