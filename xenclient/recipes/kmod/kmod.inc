# Copyright (C) 2014 Citrix Systems Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "KMOD - Handle kernel modules."
HOMEPAGE = "https://www.kernel.org/pub/linux/utils/kernel/kmod/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=801f80980d171dd6425610833a22dbe6"
SECTION = "kernel/modules"
DEPENDS = ""
PR = "r0"

SRC_URI = "https://www.kernel.org/pub/linux/utils/kernel/${PN}/${PN}-${PV}.tar.gz;name=tarball"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools

