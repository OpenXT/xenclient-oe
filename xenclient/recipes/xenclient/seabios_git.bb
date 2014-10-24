DESCRIPTION = "SeaBIOS"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504      \
                    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"

SRCREV_FORMAT = "source_patchqueue"
SRCREV_source = "d7323eab4817886c9e20c32968f3f5dc9fe553d6"
SRCREV_patchqueue = "3ae92fb7b617a2e17547244411a75ffa0ebaf5a4"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/openxt/seabios.git;protocol=https;name=source \
           git://github.com/openxt/seabios-pq.git;protocol=https;destsuffix=patchqueue;name=patchqueue \
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

