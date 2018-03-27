SECTION = "base"
DESCRIPTION = "Script to manage module configuration files"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PACKAGE_ARCH = "all"
RDEPENDS_${PN} = "${@bb.utils.contains("MACHINE_FEATURES", "kernel26",  "module-init-tools-depmod","modutils-depmod",d)} "
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
RDEPENDS_${PN}_slugos = ""
