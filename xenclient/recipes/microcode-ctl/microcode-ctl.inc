DESCRIPTION = "Intel microcode utility"
LICENSE = "GPLv2"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://microcode_ctl.c;endline=11;md5=4ea1ddef6ca07e5567d3ef7f81553494   \
                    file://intel-ia32microcode-23April2007.txt;endline=10;md5=5ef6f326e9cb035222cd586e2445531a"

SRC_URI = "${OPENXT_MIRROR}/microcode_ctl-${PV}.tar.gz \
           file://fix-startup-script.diff;patch=1"

S="${WORKDIR}/microcode_ctl-${PV}"

RDEPENDS_${PN}+=" intel-microcode "

inherit update-rc.d

do_compile() {
        ${CC} ${TARGET_CFLAGS} ${TARGET_LDFLAGS} microcode_ctl.c -o microcode_ctl
}

do_install() {
        install -d ${D}/${sbindir}
        install -m 0755 ${S}/microcode_ctl ${D}/${sbindir}

        install -d ${D}/etc/init.d
        install -m 0755 ${S}/microcode_ctl.start \
            ${D}/etc/init.d/microcode_ctl
}

INITSCRIPT_NAME = "microcode_ctl"
INITSCRIPT_PARAMS = "start 01 5 ."
