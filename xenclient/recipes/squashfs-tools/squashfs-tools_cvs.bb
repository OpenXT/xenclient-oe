require squashfs-tools.inc
PR = "${INC_PR}.1"

#SRC_URI = "cvs://squashfs.cvs.sourceforge.net/cvsroot/squashfs;method=pserver;module=squashfs"
SRC_URI = "file://squashfs-tools_cvs-20100914.tar.gz"
SRC_URI += " file://Makefile.patch;patch=1"
SRC_URI += " file://fixes.patch;patch=1"

S = "${WORKDIR}/squashfs/squashfs-tools"

do_compile_append() {
	oe_runmake unsquashfs
}

do_install_append() {
	install -m 0755 unsquashfs ${D}${sbindir}/
}
