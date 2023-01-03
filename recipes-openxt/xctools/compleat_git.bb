DESCRIPTION = "bash completion for human beings"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=acbef28775875450fdedef37d178f5c4"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

require xctools.inc

S = "${WORKDIR}/git/compleat"

HPV = "0.1"
inherit haskell

FILES_${PN} += " \
    ${datadir}/${PN}*/compleat_setup \
"

do_install_append() {
	install -m 0755 -d ${D}/etc/compleat.d
	install -m 0755 -d ${D}/etc/profile.d
	install -m 0755 ${S}/compleat_setup ${D}/etc/profile.d/compleat_setup.sh
}

