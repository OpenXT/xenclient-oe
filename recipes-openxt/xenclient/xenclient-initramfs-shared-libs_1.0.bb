PR="r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "virtual/libc openssl trousers"
PROVIDES = ""
RPROVIDES_${PN} = ""
DEFAULT_PREFERENCE = "-99"

# Allow empty, this is just to build the dependencies for initramfs
ALLOW_EMPTY_${PN} = "1"

do_install() {
    :
}
