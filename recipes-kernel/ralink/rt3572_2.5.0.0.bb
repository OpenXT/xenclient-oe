SRC_URI[md5sum] = "492bf72c8ec5cacca7e03bfd22a67008"
SRC_URI[sha256sum] = "1cb144a8d85db2b732bf30ad64dbf89c466c67866312273baea6de35068873be"
DESCRIPTION = "Ralink usb wifi driver"
HOMEPAGE = "http://www.ralinktech.com/support.php?s=2"
LICENSE = "GPL-2.0+"
# Apparently space in filenames are not dealt with properly. So we copy it with a better name.
LIC_FILES_CHKSUM = "file://LICENSE-ralink-GPL.txt;md5=848110d2a28fcf38a63b8c9a604f169c"

SRC_URI = "${OPENXT_MIRROR}/2011_0427_RT3572_Linux_STA_v2.5.0.0.DPO.tar.gz \
           file://config-mk-wpa.patch;patch=1 \
           file://fix-makefile-kernel-version-checks.patch;patch=1 \
           file://other-makefile-fixes.patch;patch=1 \
           file://fix-makefile-6.patch;patch=1 \
           file://add-wifi-usb-device-id.patch;patch=1 \
           file://linux-3.x-uidgid-compat.patch;patch=1 \
           file://RT2870STA.dat"

S = "${WORKDIR}/2011_0427_RT3572_Linux_STA_v2.5.0.0.DPO"

DEPENDS = "virtual/kernel"
inherit module-base

addtask move_lic before do_populate_lic before do_configure after do_unpack

do_move_lic () {
    cp ${S}/LICENSE\ ralink-GPL.txt ${S}/LICENSE-ralink-GPL.txt
}

do_compile() {
    sed -i '/export OSABL/ iLINUX_SRC = "${STAGING_KERNEL_DIR}"' ${S}/Makefile
    make
}

do_install() {

    sed -i '/export OSABL/ iINST_MOD_PATH = "${D}"' ${S}/Makefile
    make install
    install -d ${D}/etc/Wireless/
    install -d ${D}/etc/Wireless/RT2870STA/
    install -m 0644 ${WORKDIR}/RT2870STA.dat ${D}/etc/Wireless/RT2870STA/
}

FILES_${PN} = "/lib/modules /etc/modprobe.d /etc/Wireless/RT2870STA/"

PARALLEL_MAKE = ""

MACHINE_KERNEL_PR_append = "b"
