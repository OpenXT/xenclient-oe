SRC_URI[md5sum] = "4a476b4f036dd20a764fb54fc24edbec"
SRC_URI[sha256sum] = "ce50713a261d14b735ec9ccd97609f0ad5ce69540af560e8c3ce9eb5f2d28f47"
DEPENDS = "openssl"
LICENSE = "CPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8031b2ae48ededc9b982c08620573426"

PR = "r1"

SRC_URI = " \
    http://downloads.sourceforge.net/${BPN}/${BPN}-${PV}.tar.gz;subdir=${BPN}-${PV} \
    file://trousers-standalone.patch \
    file://trousers-tcsd-dont-pthread-exit.patch \
    file://trousers.initscript \
    file://45-trousers.rules \
    file://tcsd.conf \
"
INSANE_SKIP_${PN} = "src-uri-bad"

S = "${WORKDIR}/${BPN}-${PV}"

EXTRA_OECONF += " --disable-usercheck"
CFLAGS_append = " -Wno-error=unused-parameter -Wno-error=strict-aliasing -std=gnu89"

inherit update-rc.d useradd autotools-brokensep pkgconfig


INITSCRIPT_PACKAGES = "${PN}"

INITSCRIPT_NAME_${PN} = "trousers"
INITSCRIPT_PARAMS_${PN} = "defaults 85"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--gid 421 tss"
USERADD_PARAM_${PN} = "--system --home-dir /boot/system/tpm --shell /bin/false --gid tss --uid 421 tss"

do_install_append() {
    install -o tss -g tss -m 0644 ${WORKDIR}/tcsd.conf ${D}${sysconfdir}/tcsd.conf

    install -o tss -g tss -m 0755 -d ${D}${datadir}/trousers
    install -o tss -g tss -m 0644 ${B}/dist/system.data.auth ${D}${datadir}/trousers/
    install -o tss -g tss -m 0644 ${B}/dist/system.data.noauth ${D}${datadir}/trousers/

    install -m 0755 -d ${D}/etc/init.d
    install -m 0755 ${WORKDIR}/trousers.initscript ${D}/etc/init.d/trousers

    install -m 0755 -d ${D}/etc/udev/rules.d
    install -m 0644 ${WORKDIR}/45-trousers.rules ${D}/etc/udev/rules.d

    # HACK: system.data is supposed to be created when taking ownership of the
    # TPM. It then serves as a system scope persistent storage for keys that
    # are not stored on the TPM.
    # TCSD will not start if the file does not exist and it will try to change
    # it when not to its liking, which is a problem for installer->dom0-ro or
    # even initramfs->dom0-ro.
    # OpenXT does not use that storage though. Historically the file is stored
    # in the boot partition, so tcsd finds system.data.auth on first boot,
    # although the installer took ownership of the TPM...
    # The standalone tools somewhat mitigate that, so initramfs gets away with
    # it (even if the PS file isn't available weirdly).
    # This is spread across the platform far and wide. It should be overhauled.
    install -d ${D}/boot/system
    install -d -o tss -g tss -m 700 ${D}/boot/system/tpm
    install -o tss -g tss -m 600 ${D}${datadir}/trousers/system.data.auth ${D}/boot/system/tpm/system.data
}

PACKAGES =+ " \
    ${PN}-data \
    ${PN}-conf \
"
FILES_${PN} += " \
    /boot/system/tpm \
"
FILES_${PN}-data = " \
    ${datadir}/trousers/system.data.auth \
    ${datadir}/trousers/system.data.noauth \
"
FILES_${PN}-conf = " \
    ${sysconfdir}/tcsd.conf \
"
CONFFILES_${PN}-conf = " \
    ${sysconfdir}/tcsd.conf \
"

RRECOMMENDS_${PN}_append += "${PN}-conf"
