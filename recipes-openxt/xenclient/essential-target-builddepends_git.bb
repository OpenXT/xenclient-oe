inherit packagegroup
DESCRIPTION = "Installs packages that are needed to compile software on target"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
INSANE_SKIP_${PN} = "dev-deps"
RDEPENDS_${PN} += "binutils-symlinks gcc-symlinks g++-symlinks cpp-symlinks libc6-dev libstdc++-dev libtool libtool-dev git quilt autoconf automake make pkgconfig-dev refpolicy-mcs-dev"
