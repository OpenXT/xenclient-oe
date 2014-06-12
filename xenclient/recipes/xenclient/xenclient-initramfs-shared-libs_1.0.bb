PR="r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "eglibc openssl trousers"
PROVIDES = ""
RPROVIDES_${PN} = ""
DEFAULT_PREFERENCE = "-99"

# Allow empty, this is just to build the dependencies for initramfs
ALLOW_EMPTY = "1"

do_install() {
    :
}
