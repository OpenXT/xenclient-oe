SECTION = "base"
DESCRIPTION = "Script to manage module configuration files"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "all"
RDEPENDS = "${@base_contains("MACHINE_FEATURES", "kernel26",  "module-init-tools-depmod","modutils-depmod",d)} "
PR = "r9xc1"

SRC_URI = "file://update-modules"

do_install() {
	install -d ${D}${sbindir}
	install ${WORKDIR}/update-modules ${D}${sbindir}
}

# The SlugOS distro is testing the use of the busybox mod* utilities.
# If that works out, we should create a virtual/update-modules, and
# let the distros select if they want busybox, or some other package
# to provide it.  Until then, the following line just removes the
# unwanted dependencies for SlugOS.
RDEPENDS_slugos = ""
