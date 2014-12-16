SRC_URI += "\
            file://icedtea-ecj-fix-compil-gcc-4.7.patch;apply=no \
           "
# Ask bitbake to use icedtea-native-1.8.11 to copy the patch
FILESEXTRAPATHS := "${THISDIR}/${PN}:"

export DISTRIBUTION_ECJ_PATCHES += " \
                              patches/icedtea-ecj-fix-compil-gcc-4.7.patch \
                             "
