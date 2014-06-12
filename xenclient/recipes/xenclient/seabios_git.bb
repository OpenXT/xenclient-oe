DESCRIPTION = "SeaBIOS"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504      \
                    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"

SRC_URI = "${OPENXT_GIT_MIRROR}/seabios.git;protocol=git;tag=${OPENXT_TAG} \
           ${OPENXT_GIT_MIRROR}/seabios-pq.git;protocol=git;tag=${OPENXT_TAG} \
           file://defconfig \
          "

S= "${WORKDIR}/git"

inherit xenclient
inherit xenclient-pq

FILES_${PN} = "/usr/share/firmware/*.bin"

PARALLEL_MAKE=""
# Use host toolchain. God please forgive me.
EXTRA_OEMAKE += "CFLAGS= LDFLAGS= CC='${BUILD_CC}' LD='${BUILD_LD}'"

do_configure () {
        install -m 0644 ${WORKDIR}/defconfig .config
        oe_runmake oldconfig
}

do_compile() {
        oe_runmake V=1
}

do_install() {
        install -d ${D}/usr/share/firmware
        install -m 0644 out/bios.bin ${D}/usr/share/firmware/
        install -m 0644 out/vgabios.bin ${D}/usr/share/firmware/
}

