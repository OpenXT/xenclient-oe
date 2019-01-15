DESCRIPTION = "Various ndvm tweaks"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://enter-s3.sh \
	   file://xenstore-init \
	   file://netcon \
	   file://iwlwifi.conf \
"

PACKAGES = "${PN}"

FILES_${PN} = "/"

do_install () {
	install -d ${D}/etc
	install -d ${D}/etc/modprobe.d
	install -m 0644 ${WORKDIR}/iwlwifi.conf \
		${D}/etc/modprobe.d/iwlwifi.conf

	install -d ${D}/usr/share/xenclient
	install -m 0755 ${WORKDIR}/enter-s3.sh \
		${D}/usr/share/xenclient/enter-s3.sh
	install -m 0755 ${WORKDIR}/xenstore-init \
		${D}/usr/share/xenclient/xenstore-init

	install -d ${D}/etc/NetworkManager/system-connections
	install -m 0644 ${WORKDIR}/netcon \
		"${D}/etc/NetworkManager/system-connections/Wired Ethernet Connection"
}
