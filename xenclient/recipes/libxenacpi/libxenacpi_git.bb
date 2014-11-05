DESCRIPTION = "Xen ACPI access and utility library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools libtool virtual/kernel"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xctools.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/libxenacpi"

PACKAGE_ARCH = "${MACHINE_ARCH}"
CFLAGS += "-I${STAGING_KERNEL_DIR}/include"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

