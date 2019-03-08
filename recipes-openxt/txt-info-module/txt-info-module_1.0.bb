SUMMARY = "Out-of-tree module to expose TXT resources to user-land."
DESCRIPTION = "TXT exposes configuration registers documented in its Software \
Development Guide. Accessing these registers in sometimes necessary for \
userland software to perform checks and validate compatibility with software \
resources."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://sources/Kbuild \
    file://sources/Makefile \
    file://sources/txt_info.c \
"

S = "${WORKDIR}/sources"

inherit module
inherit module-signing
