DESCRIPTION = "XenClient set sound on boot"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
RDEPENDS:${PN} += "alsa-utils-amixer"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://xenclient-boot-sound.initscript \
	   file://update-pcm-config"
PACKAGES = "${PN}"
FILES:${PN} = "/"

inherit update-rc.d

INITSCRIPT_NAME = "xenclient-boot-sound"
INITSCRIPT_PARAMS = "defaults 75 25"

do_install () {
	install -d ${D}/etc/init.d
	install -m 0755 ${UNPACKDIR}/xenclient-boot-sound.initscript \
		${D}/etc/init.d/xenclient-boot-sound
	install -d ${D}/usr/sbin
	install -m 0755 ${UNPACKDIR}/update-pcm-config \
		${D}/usr/sbin/update-pcm-config
}
