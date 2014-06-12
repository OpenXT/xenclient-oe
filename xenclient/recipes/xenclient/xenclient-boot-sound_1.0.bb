DESCRIPTION = "XenClient set sound on boot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS_${PN} += "alsa-utils-amixer"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://xenclient-boot-sound.initscript \
	   file://update-pcm-config"
PACKAGES = "${PN}"
FILES_${PN} = "/"

inherit update-rc.d xenclient

INITSCRIPT_NAME = "xenclient-boot-sound"
INITSCRIPT_PARAMS = "start 04 5 ."

do_install () {
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/xenclient-boot-sound.initscript \
		${D}/etc/init.d/xenclient-boot-sound
	install -d ${D}/usr/sbin
	install -m 0755 ${WORKDIR}/update-pcm-config \
		${D}/usr/sbin/update-pcm-config
}
