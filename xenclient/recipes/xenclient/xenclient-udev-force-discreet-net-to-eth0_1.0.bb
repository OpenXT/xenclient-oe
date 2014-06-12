DESCRIPTION = "XenClient udev rules to force the discreet network card to be eth0"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS_${PN} += "udev"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://xenclient-udev-force-discreet-net-to-eth0.rules"
FILES_${PN} = "/"

inherit xenclient

do_install () {
        install -D -m 0755 ${WORKDIR}/xenclient-udev-force-discreet-net-to-eth0.rules \
                ${D}/etc/udev/rules.d/96-xenclient-udev-force-discreet-net-to-eth0.rules
}
