LICENSE = "BSD-3-Clause & AFLv2"
LIC_FILES_CHKSUM = "file://dojo-release-${PV}-src/dijit/LICENSE;md5=420b6319a08678ab5d97203953210ad3    \
                    file://dojo-release-${PV}-src/dojo/LICENSE;md5=420b6319a08678ab5d97203953210ad3     \
                    file://dojo-release-${PV}-src/dojox/LICENSE;md5=420b6319a08678ab5d97203953210ad3    \
                    file://dojo-release-${PV}-src/util/LICENSE;md5=420b6319a08678ab5d97203953210ad3"

SRC_URI[md5sum] = "d070b0809f4ceeebc4fac340dea6a729"
SRC_URI[sha256sum] = "6bd043a07534464c7dc8eb1f5fd2e65e869f058c7461ccc1a30871ac38c8cc6f"
require recipes-devtools/dojo/dojosdk-native.inc
