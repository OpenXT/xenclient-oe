DESCRIPTION = "PV linux audio alsa driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

PV = "0+git${SRCPV}"

SRCREV = "c1e92e8b6c6a74e326d96c807ab71c8f52e91179"
SRC_URI = "git://github.com/openxt/pv-linux-drivers.git;protocol=https"
DEPENDS = "virtual/kernel v4v-module"

S = "${WORKDIR}/git/xc-audio"

inherit xenclient 
inherit module

do_compile() {
    make -C ${STAGING_KERNEL_DIR} M="${S}" KSRC="${STAGING_KERNEL_DIR}" EXTRA_CFLAGS="-DXC_HAS_STATIC_XEN"
}

do_install() {
    make -C ${STAGING_KERNEL_DIR} KSRC="${STAGING_KERNEL_DIR}" DEPMOD=echo INSTALL_MOD_PATH="${D}" M="${S}" modules_install
}

FILES_${PN} = "/lib/modules /etc/modprobe.d"
