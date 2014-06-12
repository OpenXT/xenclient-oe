inherit gettext
DESCRIPTION = "GRand Unified Bootloader"
HOMEPAGE = "http://www.gnu.org/software/grub"
SECTION = "bootloaders"
PRIORITY = "optional"
DEPENDS += "bison-native flex-native"
RDEPENDS_${PN}-install = "diffutils"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

INSANE_SKIP_${PN} = "arch"

S = "${WORKDIR}/${PN}-${PV}"

##SRC_URI = "svn://svn.savannah.gnu.org/grub/trunk;module=${PN};proto=http \
##           file://configure_grub_disable_manpages.patch;patch=1"

# Avoid make conflict for bison
PARALLEL_MAKE = ""

SRC_URI = "http://alpha.gnu.org/gnu/grub/grub-1.98.tar.gz \
	file://grub-add-sector-offset.patch;patch=1 \
	file://grub-1.98-refresh-on-background-change.patch;patch=1 \
	file://grub-1.98-branding.patch;patch=1 \
	file://grub-1.98-video-mbi-green-isnt-red.patch;patch=1 \
	file://remove-editing-and-shell.patch;patch=1 \
	file://accept-video-always.patch;patch=1 \
"

SRC_URI[md5sum] = "c0bcf60e524739bb64e3a2d4e3732a59"
SRC_URI[sha256sum] = "bef2c1892e052967b65aab6aa62ac702c0e50ef8848506eacf3c0b2f5007c614"

# commented out as it's not applying:
#          file://configure_grub_disable_manpages.patch;patch=0

inherit autotools xenclient

EXTRA_OECONF = "--disable-manpages \
		--enable-graphics \
		--disable-auto-linux-mem-opt \
		--disable-werror \
                --disable-grub-mkfont \
"

TARGET_CFLAGS += "-Os"

do_configure() {
        oe_runconf
}


do_install_append() {
	rm -rf ${D}/usr/share/grub
}

#do_stage() {
#}

#PACKAGES =+ "${PN}-install ${PN}-eltorito"

FILES_${PN} += " \
        ${libdir}/grub/grub-mkconfig_lib \
        ${libdir}/grub/update-grub_lib \
        ${libdir}/grub/i386-pc/* \
"

COMPATIBLE_HOST = "i.86.*-linux"
