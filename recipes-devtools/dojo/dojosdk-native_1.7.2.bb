LICENSE = "BSD-3-Clause & AFLv2"
LIC_FILES_CHKSUM = "file://dojo-release-${PV}-src/dijit/LICENSE;md5=3d06b14ab533a5e4f9815b5b8761eb84    \
                    file://dojo-release-${PV}-src/dojo/LICENSE;md5=3d06b14ab533a5e4f9815b5b8761eb84     \
                    file://dojo-release-${PV}-src/dojox/LICENSE;md5=3d06b14ab533a5e4f9815b5b8761eb84    \
                    file://dojo-release-${PV}-src/util/LICENSE;md5=3d06b14ab533a5e4f9815b5b8761eb84"

FILESEXTRAPATHS_prepend := "${THISDIR}/dojosdk-native-${PV}:"

SRC_URI[md5sum] = "60d1f8f7a33437a7e055aa4a0131d305"
SRC_URI[sha256sum] = "912d30010010a35ba59439f51d27662b98fe9c185af508a7b237f6f8988a2464"
require recipes-devtools/dojo/dojosdk-native.inc

SRC_URI += "file://use-java-native-copy-rather-than-forking.patch;patch=1"
SRC_URI += "file://0001-Fix-for-dojo-ticket-15057.patch;patch=1"
