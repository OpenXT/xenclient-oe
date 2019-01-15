DESCRIPTION = "XenClient set sound on boot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
RDEPENDS_${PN} += "alsa-utils-amixer"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://xenclient-boot-sound.initscript \
	   file://update-pcm-config"
PACKAGES = "${PN}"
FILES_${PN} = "/"

inherit update-rc.d xenclient

INITSCRIPT_NAME = "xenclient-boot-sound"
INITSCRIPT_PARAMS = "defaults 75"

do_install () {
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/xenclient-boot-sound.initscript \
		${D}/etc/init.d/xenclient-boot-sound
	install -d ${D}/usr/sbin
	install -m 0755 ${WORKDIR}/update-pcm-config \
		${D}/usr/sbin/update-pcm-config
}
