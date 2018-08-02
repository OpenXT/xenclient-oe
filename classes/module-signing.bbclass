# For module signing
DEPENDS += "openssl-native"

# Let linux build strip modules before signing
export INSTALL_MOD_STRIP="1"
# Don't let OE strip packages since that will remove the signature
INHIBIT_PACKAGE_STRIP = "1"

# Needed to build sign-tool
export HOST_EXTRACFLAGS = "${BUILD_CFLAGS} ${BUILD_LDFLAGS}"
