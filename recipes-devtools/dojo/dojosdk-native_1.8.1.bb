LICENSE = "BSD-3-Clause & AFLv2"
LIC_FILES_CHKSUM = "file://dojo-release-${PV}-src/dijit/LICENSE;md5=7a697b953bf9e661d81e8c23725526c8    \
                    file://dojo-release-${PV}-src/dojo/LICENSE;md5=7a697b953bf9e661d81e8c23725526c8     \
                    file://dojo-release-${PV}-src/dojox/LICENSE;md5=7a697b953bf9e661d81e8c23725526c8    \
                    file://dojo-release-${PV}-src/util/LICENSE;md5=7a697b953bf9e661d81e8c23725526c8"

FILESEXTRAPATHS_prepend := "${THISDIR}/dojosdk-native-${PV}:"
                    
SRC_URI[md5sum] = "9b80b9a736b81c336accd832f3c3aea2"
SRC_URI[sha256sum] = "a1a9d315ab3322539d997351b403325c7ab53164b18675a415020aa1525c2f83"
require recipes-devtools/dojo/dojosdk-native.inc

SRC_URI += "file://use-java-native-copy-rather-than-forking.patch;patch=1"
