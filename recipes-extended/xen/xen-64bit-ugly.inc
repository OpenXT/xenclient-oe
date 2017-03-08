# This file reconfigures build options in order to use the (64-bit target) host
# compiler rather than the (32-bit target) native compiler.
# Make is directed to use settings from its environment.
# Autotools is disabled.

HOSTCC = ""
CROSS_COMPILE = ""

EXTRA_OEMAKE += " \
    CFLAGS='' \
    LDFLAGS='' \
    XEN_TARGET_ARCH=x86_64 \
    CC='${BUILD_CC}' \
    LD='${BUILD_LD}' \
    XEN_VENDORVERSION=-xc \
    "
EXTRA_OEMAKE =+ "-e MAKEFLAGS= "

DEPENDS_remove = " \
    autoconf-native \
    automake-native \
    libtool-native \
    libtool-cross \
    gnu-config-native \
    "
do_configure[prefuncs] = ""
do_configure[postfuncs] = ""
