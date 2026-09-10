DESCRIPTION = "Various XenClient tweaks"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://enter-s3.sh \
	   file://install-bootloader \
	   file://install-bootloader-images \
	   file://xenstore-init \
	   file://netcon \
	   file://grub.cfg \
	   file://openxt.cfg \
	   file://BOOT.CSV \
	   file://unifont.pf2 \
	   file://firstboot.sh \
	   file://firstboot.initscript \
	   file://dom0_add_vif.sh \
	   file://create-ndvm \
	   file://status-report \
	   file://status-tool \
	   file://null.iso \
	   file://set_platform_username \
	   file://USB_fakenames.conf \
	   file://usb-state \
	   file://selinux-state \
	   file://inputrc \
"

RDEPENDS:${PN} += "bash"

FILES:${PN} = "/"

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}"

INITSCRIPT_NAME:${PN} = "firstboot"
INITSCRIPT_PARAMS:${PN} = "start 70 S ."

do_install () {
	install -d ${D}/etc/init.d
	install -m 0755 ${UNPACKDIR}/firstboot.initscript \
		${D}/etc/init.d/firstboot

	install -d ${D}/usr/share/xenclient
	install -m 0755 ${UNPACKDIR}/enter-s3.sh \
		${D}/usr/share/xenclient/enter-s3.sh
	install -m 0755 ${UNPACKDIR}/install-bootloader \
		${D}/usr/share/xenclient/install-bootloader
	install -m 0755 ${UNPACKDIR}/install-bootloader-images \
		${D}/usr/share/xenclient/install-bootloader-images
	install -m 0755 ${UNPACKDIR}/xenstore-init \
		${D}/usr/share/xenclient/xenstore-init
	install -m 0755 ${UNPACKDIR}/dom0_add_vif.sh \
		${D}/usr/share/xenclient/dom0_add_vif.sh
	install -m 0755 ${UNPACKDIR}/firstboot.sh \
		${D}/usr/share/xenclient/firstboot.sh

	install -d ${D}/usr/bin
	install -m 0755 ${UNPACKDIR}/create-ndvm \
		${D}/usr/bin/create-ndvm

	# Useless cruft should be in installer
	install -d ${D}/usr/share/xenclient/bootloader
	install -m 0644 ${UNPACKDIR}/grub.cfg \
		${D}/usr/share/xenclient/bootloader
	install -m 0644 ${UNPACKDIR}/openxt.cfg \
		${D}/usr/share/xenclient/bootloader
	install -m 0644 ${UNPACKDIR}/BOOT.CSV \
		${D}/usr/share/xenclient/bootloader
	install -m 0644 ${UNPACKDIR}/unifont.pf2 \
		${D}/usr/share/xenclient/bootloader

	mkdir -p ${D}/opt/xensource
	ln -sf /usr/bin ${D}/opt/xensource/libexec

	install -d ${D}/root/.ssh
	install -m 0644 ${UNPACKDIR}/inputrc ${D}/root/.inputrc

	install -m 0755 -d ${D}${localstatedir}/lib/ovf

	install -d ${D}/config${sysconfdir}
	install -m 0644 ${UNPACKDIR}/USB_fakenames.conf \
		${D}/config${sysconfdir}/USB_fakenames.conf

	install -d ${D}/usr/share
	install -m 0644 ${UNPACKDIR}/null.iso \
		${D}/usr/share/null.iso

	install -d ${D}/usr/bin
	install -m 0755 ${UNPACKDIR}/status-report \
		${D}/usr/bin/status-report
	install -m 0755 ${UNPACKDIR}/status-tool \
		${D}/usr/bin/status-tool
	install -m 0755 ${UNPACKDIR}/set_platform_username \
		${D}/usr/bin/set_platform_username
	install -m 0755 ${UNPACKDIR}/usb-state \
		${D}/usr/bin/usb-state
	install -m 0755 ${UNPACKDIR}/selinux-state \
		${D}/usr/bin/selinux-state
}

INC_PR="r4"
