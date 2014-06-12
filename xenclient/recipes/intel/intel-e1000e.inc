DESCRIPTION = "Intel wired ethernet driver"
LICENSE = "GPLv2"

SRC_URI = "http://downloads.sourceforge.net/project/e1000/e1000e%20stable/${PV}/e1000e-${PV}.tar.gz "

DEPENDS = "virtual/kernel"

S = "${WORKDIR}/e1000e-${PV}/src"

inherit module

do_compile() {
    make -C ${STAGING_KERNEL_DIR} M="${S}" KSRC="${STAGING_KERNEL_DIR}" K_VERSION="2.6"
}

do_install() {
    make -C ${STAGING_KERNEL_DIR} KSRC="${STAGING_KERNEL_DIR}" DEPMOD=echo INSTALL_MOD_PATH="${D}" M="${S}" modules_install
}

FILES_${PN} = "/lib/modules /etc/modprobe.d"
