KBRANCH ?= "v5.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = " \
    git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA} \
"
SRC_URI_append += " \
    file://openxt-kmeta;type=kmeta;destsuffix=openxt-kmeta \
"

SRCREV_machine = "v5.4.75"
SRCREV_meta = "8d0ed83a864cc91eef4d2abbc90f13d4ecd1c213"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.75"

inherit kernel-module-signing

DEPENDS += " \
    ${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)} \
    openssl-native \
    util-linux-native \
"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-data"
KCONF_BSP_AUDIT_LEVEL = "2"
