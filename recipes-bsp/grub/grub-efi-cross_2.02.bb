SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "https://ftp.gnu.org/gnu/grub/grub-${PV}.tar.gz"
SRC_URI[md5sum] = "1116d1f60c840e6dbd67abbc99acb45d"
SRC_URI[sha256sum] = "660ee136fbcee08858516ed4de2ad87068bfe1b6b8b37896ce3529ff054a726d"

DEPENDS = "flex-native bison-native"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*)-(linux.*|freebsd.*)'

# configure.ac has code to set this automagically from the target tuple
# but the OE freeform one (core2-foo-bar-linux) don't work with that.

PACKAGECONFIG ??= ""
PACKAGECONFIG[grub-mount] = "--enable-grub-mount,--disable-grub-mount,fuse"
PACKAGECONFIG[device-mapper] = "--enable-device-mapper,--disable-device-mapper,lvm2"

# grub2 creates its own set of -nostdinc / -isystem / -ffreestanding CFLAGS and
# OE's default BUILD_CFLAGS (assigned to CFLAGS for native builds) etc, conflict
# with that. Note that since BUILD_CFLAGS etc are not used by grub2 target
# builds, it's safe to clear them unconditionally for both target and native.
BUILD_CPPFLAGS = ""
BUILD_CFLAGS = ""
BUILD_CXXFLAGS = ""
BUILD_LDFLAGS = ""

# ldm.c:114:7: error: trampoline generated for nested function 'hook' [-Werror=trampolines]
# and many other places in the grub code when compiled with some native gcc compilers (specifically, gentoo)
CFLAGS_append_class-native = " -Wno-error=trampolines"

EXTRA_OECONF = "--with-platform=efi \
		--target=x86_64 \
		--disable-grub-mkfont \
		--program-prefix="" \
		--enable-liblzma=no \
		--enable-libzfs=no \
		--enable-largefile=no \
		--enable-efiemu=no \
"

do_configure_prepend() {
	# The grub2 configure script uses variables such as TARGET_CFLAGS etc
	# for its own purposes. Remove the OE versions from the environment to
	# avoid conflicts.
	unset TARGET_CPPFLAGS TARGET_CFLAGS TARGET_CXXFLAGS TARGET_LDFLAGS
	( cd ${S}
	${S}/autogen.sh )
}

# grub and grub-efi's sysroot/${datadir}/grub/grub-mkconfig_lib are
# conflicted, remove it since no one uses it.
SYSROOT_DIRS_BLACKLIST += "${datadir}/grub/grub-mkconfig_lib"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

S = "${WORKDIR}/grub-${PV}"

SRC_URI += " \
    file://grub-efi-early.cfg \
    file://0001-Only-check-for-large-file-support-when-needed.patch \
    "

GRUB_BUILDIN = "all_video boot btrfs cat chain configfile echo \
                efinet ext2 fat font gfxmenu gfxterm gzio halt \
                hfsplus iso9660 jpeg loadenv loopback lvm mdraid09 mdraid1x \
                minicmd normal part_apple part_msdos part_gpt \
                password_pbkdf2 png \
                reboot search search_fs_uuid search_fs_file search_label \
                serial sleep test tftp video xfs \
                linux backtrace usb usbserial_common \
                usbserial_pl2303 usbserial_ftdi \
                multiboot multiboot2"

inherit autotools
inherit gettext
inherit native
inherit deploy

do_deploy() {
        # Search for the grub.cfg on the local boot media by using the
        # built in cfg file provided via this recipe
        ${B}/grub-mkimage -c ${WORKDIR}/grub-efi-early.cfg -p /EFI/BOOT -d ${B}/grub-core/ \
                     -O x86_64-efi -o ${B}/grubx64.efi \
                     ${GRUB_BUILDIN}
        install -m 644 ${B}/grubx64.efi ${DEPLOYDIR}/
}

addtask deploy after do_install before do_build

do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
BBCLASSEXTEND = "native"
