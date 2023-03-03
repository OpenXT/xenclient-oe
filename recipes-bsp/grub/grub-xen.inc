# Hack to work around grub2.inc settings SRC_URI
# OE_CORE_PATH defaults to a bordel build layout
OE_CORE_PATH ?= "${TOPDIR}/layers/openembedded-core"
FILESEXTRAPATHS_prepend := "${OE_CORE_PATH}/meta/recipes-bsp/grub/files:"
require recipes-bsp/grub/grub2.inc

require conf/image-uefi.conf

# Need to be disabled otherwise there are wint_t type not defined errors
SECURITY_CFLAGS = ""

DEPENDS_append_class-target = " grub-efi-native"
RDEPENDS_${PN}_class-target = "grub-common virtual/grub-bootconf"

SRC_URI += " \
	file://grub-xen.cfg \
"

S = "${WORKDIR}/grub-${PV}"

CACHED_CONFIGUREVARS += "ac_cv_path_HELP2MAN="
EXTRA_OECONF += "--enable-efiemu=no"

GRUBPLATFORM ?= "xen"
GRUB_TARGET ?= "x86_64"
GRUBEXT ?= "${GRUBPLATFORM}-pvh"
GRUB_BINARY = "grub-${GRUBEXT}"

PACKAGECONFIG="device-mapper"

GRUB_BUILDIN ?= "linux ext2 fat part_msdos part_gpt normal \
                 iso9660 configfile search loadenv test \
                 echo legacycfg memdisk tar gzio"

do_mkimage() {
	cd ${B}
	# The early.cfg is limited, so it loads a full config from memdisk
	echo "normal (memdisk)/grub-xen.cfg" > grub-bootstrap.cfg
	# The memdisk config searches for a grub.cfg on the local boot media,
	# or falls back to loading bzImage
	tar -c -C ${WORKDIR} -f ${B}/memdisk.tar grub-xen.cfg
	tar tf memdisk.tar
	grub-mkimage -c grub-bootstrap.cfg -p / -d ./grub-core/ \
	               -O ${GRUB_TARGET}-${GRUBPLATFORM} \
	               -o ./${GRUB_BINARY} \
	               -m memdisk.tar \
	               ${GRUB_BUILDIN}
}

addtask mkimage before do_install after do_compile

do_mkimage_class-native() {
	:
}

do_install_class-target() {
    install -d "${D}${libdir}/xen/boot"
    install -m 644 "${GRUB_BINARY}" "${D}${libdir}/xen/boot"
    ln -s "${GRUB_BINARY}" "${D}${libdir}/xen/boot/${GRUB_BINARY}.bin"
}

FILES_${PN} = "${libdir}/xen/boot"

# grub.xen_pvh is a 32bit binary, so it may not match.
INSANE_SKIP_${PN} += "arch"
