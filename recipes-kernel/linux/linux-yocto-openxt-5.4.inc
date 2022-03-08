KBRANCH ?= "v5.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = " \
    git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA} \
"
SRC_URI_append += " \
    file://openxt-kmeta;type=kmeta;destsuffix=openxt-kmeta \
"

SRCREV_machine = "v5.4.106"
SRCREV_meta = "6144556fdee152870d8e792d41948ddcf9a684f1"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.106"

inherit kernel-module-signing

DEPENDS += " \
    ${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)} \
    openssl-native \
    util-linux-native \
"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-data"
KCONF_BSP_AUDIT_LEVEL = "2"
