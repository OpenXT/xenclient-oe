inherit task
DESCRIPTION = "Installs packages that are needed to compile software on target"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
INSANE_SKIP_${PN} = "dev-deps"
RDEPENDS += "binutils-symlinks gcc-symlinks g++-symlinks cpp-symlinks libc6-dev libstdc++-dev libtool libtool-dev git quilt autoconf automake make pkgconfig-dev refpolicy-mcs-dev"
