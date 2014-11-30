DESCRIPTION = "XenClient splash images"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://black.png \
	   file://booting-en-us.png \
	   file://reboot-en-us.png \
	   file://please-reboot-en-us.png \
	   file://shutdown-en-us.png \
	   file://hibernate-en-us.png \
	   file://startup-en-us.png \
"

PACKAGES = "${PN}"

FILES_${PN} = "/"

do_install () {
	install -d ${D}/usr/share/xenclient/bootloader/images
	install -m 0644 ${WORKDIR}/black.png \
			${D}/usr/share/xenclient/bootloader/images/

	for l in en-us ; do
	    install -d ${D}/usr/share/xenclient/bootloader/images/$l
	    for f in booting reboot please-reboot shutdown hibernate startup ; do
		install -m 0644 ${WORKDIR}/$f-$l.png \
			${D}/usr/share/xenclient/bootloader/images/$l/$f.png
		ln -sf ../black.png ${D}/usr/share/xenclient/bootloader/images/$l/
	    done
	done
}
