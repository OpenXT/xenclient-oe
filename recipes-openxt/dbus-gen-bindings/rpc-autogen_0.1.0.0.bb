SUMMARY = "Generated Haskell DBus bindings used by OpenXT components."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SECTION = "devel"

DEPENDS = " \
    libxch-rpc \
"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/eric-ch/dbus-gen-bindings.git;protocol=https;branch=master"

S = "${WORKDIR}/git/haskell"

inherit haskell
