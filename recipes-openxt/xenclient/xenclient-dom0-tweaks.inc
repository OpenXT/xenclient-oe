DESCRIPTION = "Various XenClient tweaks"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

# This should go away with everything put into the packages it really
# belongs to. For now it's just a convenient place to stash certain things.

SRC_URI = "file://enter-s3.sh \
	   file://install-bootloader \
	   file://install-bootloader-images \
	   file://xenstore-init \
	   file://netcon \
	   file://grub.cfg \
	   file://unifont.pf2 \
	   file://firstboot.sh \
	   file://firstboot.initscript \
	   file://dom0_add_vif.sh \
	   file://blacklist-nvidiafb.conf \
	   file://r8168.conf \
	   file://create-ndvm \
	   file://status-report \
	   file://status-tool \
	   file://null.iso \
	   file://set_platform_username \
	   file://USB_fakenames.conf \
	   file://usb-state \
	   file://selinux-state \
	   file://99-kvm.rules \
	   file://no-kvm-autosuspend.sh \
"

FILES_${PN} = "/"

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}"

INITSCRIPT_NAME_${PN} = "firstboot"
INITSCRIPT_PARAMS_${PN} = "start 70 S ."

do_install () {
	install -d ${D}/etc
	install -d ${D}/etc/modprobe.d
	install -m 0644 ${WORKDIR}/blacklist-nvidiafb.conf \
		${D}/etc/modprobe.d/blacklist-nvidiafb.conf
	install -m 0644 ${WORKDIR}/r8168.conf \
		${D}/etc/modprobe.d/r8168.conf

	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/firstboot.initscript \
		${D}/etc/init.d/firstboot

	install -d ${D}/usr/share/xenclient
	install -m 0755 ${WORKDIR}/enter-s3.sh \
		${D}/usr/share/xenclient/enter-s3.sh
	install -m 0755 ${WORKDIR}/install-bootloader \
		${D}/usr/share/xenclient/install-bootloader
	install -m 0755 ${WORKDIR}/install-bootloader-images \
		${D}/usr/share/xenclient/install-bootloader-images
	install -m 0755 ${WORKDIR}/xenstore-init \
		${D}/usr/share/xenclient/xenstore-init
	install -m 0755 ${WORKDIR}/dom0_add_vif.sh \
		${D}/usr/share/xenclient/dom0_add_vif.sh
	install -m 0755 ${WORKDIR}/firstboot.sh \
		${D}/usr/share/xenclient/firstboot.sh

	install -d ${D}/usr/bin
	install -m 0755 ${WORKDIR}/create-ndvm \
		${D}/usr/bin/create-ndvm

	# Useless cruft should be in installer
	install -d ${D}/usr/share/xenclient/bootloader
	install -m 0644 ${WORKDIR}/grub.cfg \
		${D}/usr/share/xenclient/bootloader
	install -m 0644 ${WORKDIR}/unifont.pf2 \
		${D}/usr/share/xenclient/bootloader

	mkdir -p ${D}/opt/xensource
	ln -sf /usr/bin ${D}/opt/xensource/libexec

        install -d ${D}/root/.ssh

    install -m 0755 -d ${D}${localstatedir}/lib/ovf

	install -d ${D}/config${sysconfdir}
	install -m 0644 ${WORKDIR}/USB_fakenames.conf \
		${D}/config${sysconfdir}/USB_fakenames.conf

	install -d ${D}/usr/share
	install -m 0644 ${WORKDIR}/null.iso \
		${D}/usr/share/null.iso

	install -d ${D}/usr/bin
	install -m 0755 ${WORKDIR}/status-report \
		${D}/usr/bin/status-report
	install -m 0755 ${WORKDIR}/status-tool \
		${D}/usr/bin/status-tool
	install -m 0755 ${WORKDIR}/set_platform_username \
		${D}/usr/bin/set_platform_username
	install -m 0755 ${WORKDIR}/usb-state \
		${D}/usr/bin/usb-state
	install -m 0755 ${WORKDIR}/selinux-state \
		${D}/usr/bin/selinux-state

    # Disable Avocent KVM autosleep
    install -d ${D}/lib/udev
    install -d ${D}/etc/udev/rules.d
    install -D -m 644 -o root -g root ${WORKDIR}/99-kvm.rules ${D}/etc/udev/rules.d/99-kvm.rules
    install -D -m 755 -o root -g root ${WORKDIR}/no-kvm-autosuspend.sh ${D}/usr/bin/no-kvm-autosuspend.sh
}

INC_PR="r4"
