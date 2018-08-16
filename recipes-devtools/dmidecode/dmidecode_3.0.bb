SUMMARY = "DMI (Desktop Management Interface) table related utilities"
HOMEPAGE = "http://www.nongnu.org/dmidecode/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "${SAVANNAH_NONGNU_MIRROR}/dmidecode/${BP}.tar.xz \
          file://use-oe-vars.patch;patch=1 \
          "
SRC_URI[md5sum] = "281ee572d45c78eca73a14834c495ffd"
SRC_URI[sha256sum] = "7ec35bb193729c1d593a1460b59d82d24b89102ab23fd0416e6cf4325d077e45"

COMPATIBLE_HOST = "(i.86|x86_64|aarch64|arm|powerpc|powerpc64).*-linux"

# biosdecode ownership vpddecode are i386 program, so don't trip the QA
# checker.
INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"

do_install() {
	oe_runmake DESTDIR="${D}" install
}

do_unpack_extra() {
	sed -i -e '/^prefix/s:/usr/local:${exec_prefix}:' ${S}/Makefile
}
addtask unpack_extra after do_unpack before do_patch
