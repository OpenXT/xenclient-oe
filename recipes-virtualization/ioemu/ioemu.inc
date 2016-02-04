
inherit xenclient
inherit pkgconfig

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"

DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v libdmbus openssl seabios ipxe"

PV = "0+git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/ioemu.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};name=source \
    file://qemu-log-syslog.patch;patch=1 \
    file://ioemu-compil.patch;patch=1 \
    file://tapdisk-disable-readonly.patch;patch=1 \
    file://set_timeoffset.patch;patch=1 \
    file://alsa-fix.patch;patch=1 \
    file://alsa-config.patch;patch=1 \
    file://alsa-fix-resume.patch;patch=1 \
    file://audio-volume-control.patch;patch=1 \
    file://add-config-audio.patch;patch=1 \
    file://dm-ready.patch;patch=1 \
    file://suspend-by-signal.patch;patch=1 \
    file://battery-management.patch;patch=1 \
    file://oem-features.patch;patch=1 \
    file://thermal-management.patch;patch=1 \
    file://xenstore-notify-pm-events.patch;patch=1 \
    file://vbestate.patch;patch=1 \
    file://pci-to-pci-bridge-for-calpella.patch;patch=1 \
    file://igfx-passthrough-1f-caps.patch;patch=1 \
    file://switcher.patch;patch=1 \
    file://fix-imobile-mouse.patch;patch=1 \
    file://atapi-pass-through.patch;patch=1 \
    file://pv_driver_throttling_disabled.patch;patch=1 \
    file://guest-os-controlled-brightness-support.patch;patch=1 \
    file://kbd-led.patch;patch=1 \
    file://disable-audio-recording.patch;patch=1 \
    file://fix-keyboard-mouse-simultaneous-use-problems.patch;patch=1 \
    file://fix-base-for-16meg-vram.patch;patch=1 \
    file://absolute-event-support.patch;patch=1 \
    file://fix-blktaplib.h-include-path.patch;patch=1 \
    file://dummy-ider.patch;patch=1 \
    file://uhci-inject-after-hid-event.patch;patch=1 \
    file://xc-emulated-nic-link-state-propagation.patch;patch=1 \
    file://vnc-always-use-absolute-mouse.patch;patch=1 \
    file://vnc-use-domid-if-port-doesnt-exist.patch;patch=1 \
    file://switcher-export-enabled-abs.patch;patch=1 \
    file://xenmouse.patch;patch=1 \
    file://fix-raw-serial-device.patch;patch=1 \
    file://disable-blktap.patch;patch=1 \
    file://atapi-pt-exclusive-access.patch;patch=1 \
    file://intel-hda.patch;patch=1 \
    file://intel-hda-disable-audio-recording.patch;patch=1 \
    file://cdrom-fix-cdrom-media-change-detection.patch;patch=1 \
    file://lpc.patch;patch=1 \
    file://applesmc.patch;patch=1 \
    file://vuart.patch;patch=1 \
    file://fix-vuart.patch;patch=1 \
    file://fix-absolute-mouse-clicks.patch;patch=1 \
    file://switcher-resend-abs-config.patch;patch=1 \
    file://xc-rtl8139-dont-receive-data-until-frame-sent.patch;patch=1 \
    file://es1370-supervol.patch;patch=1 \
    file://fix-static-linking.patch;patch=1 \
    file://switch-jap-key-quirk.patch;patch=1 \
    file://dmbus.patch;patch=1 \
    file://passthrough-dont-bind-irq-with-msitranslate.patch;patch=1 \
    file://use_dmbus_for_input_server_communication.patch;patch=1 \
    file://xenclient-vga.patch;patch=1 \
    file://surfman.patch;patch=1 \
    file://msi-rebind-after-host-sleep.patch;patch=1 \
    file://stubdoms.patch;patch=1 \
    file://stubdomain-passthrough.patch;patch=1 \
    file://stubdom-media-change.patch;patch=1 \
    file://stubdoms-drivemapping.patch;patch=1 \
    file://xen-machine-max-vcpus.patch;patch=1 \
    file://pci-pt-from-xenstore-entry-legacy.patch;patch=1 \
    file://igfx-quirks.patch;patch=1 \
    file://vcpus-alloc-ioreq-local-port-once.patch;patch=1 \
    file://fix-gpe0-size.patch;patch=1 \
    file://no-vkbd-pv-domain.patch;patch=1 \
    file://event-tweaks.patch;patch=1 \
    file://cleanup-volume.patch;patch=1 \
    file://opregion-mapping.patch;patch=1 \
    file://pt-lpc-bridge.patch;patch=1 \
    file://pt-ioport-map-invalid-address.patch;patch=1 \
    file://pt-pci-host-clean-values.patch;patch=1 \
    file://prevent-vm-clear-cmdreg-first-bits.patch;patch=1 \
    file://deactivate-msi-TEMP.patch;patch=1 \
    file://acpi-reset-reg.patch;patch=1 \
    file://xenmou2.patch;patch=1 \
    file://ioreq-server.patch;patch=1 \
    file://fix-unregister-iomem.patch;patch=1 \
    file://ati-pt.patch;patch=1 \
    file://disable-vt100.patch;patch=1 \
    file://no-default-device.patch;patch=1 \
    file://vtpm-support.patch;patch=1 \
    file://fix-ioreq-pio-move.patch;patch=1 \
    file://support-multiple-gpu.patch;patch=1 \
    file://vga-restrict-modes.patch;patch=1 \
    file://honour-empty-boot-device-list.patch;patch=1 \
    file://cd-lock-control-and-report.patch;patch=1 \
    file://vga-restrict-resolution.patch;patch=1 \
    file://qemu-ifup-stubdom \
    file://fix-concatenation.patch \
"

S = "${WORKDIR}/git"

FILES_${PN}-dbg += "/usr/lib/xen/bin/.debug"
FILES_${PN} += "/usr/lib/xen/bin /etc/xen /usr/share/xen/qemu"

# hack, sneak --sysroot in CFLAGS and LDFLAGS as qemu build scripts do not deal to good with crosscompilation
CFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} -msse4.1"
LDFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} "

# export some variable for IOEMU compilation
export STAGING_INCDIR
export STAGING_LIBDIR
export IDLDIR = "${STAGING_IDLDIR}"
export RPCGEN_TEMPLATES = "${STAGING_DATADIR}/xc-rpcgen"

do_configure() {
	# fixme these extras should be a patch in configure
        #export CFLAGS="${CFLAGS} -msse4.1"
        ./xen-setup --cross-prefix=${HOST_PREFIX} --extra-ldflags="${TARGET_LDFLAGS}"
}

do_compile(){
    # BitBake run make with -j8 but it doesn't work with ioemu
    make
}

#do_stage(){
#        mkdir -p ${STAGING_DIR_TARGET}/usr/src
#        rsync --exclude-from '${S}/.gitignore' --exclude '.git' -qr ${S}/ ${STAGING_DIR_TARGET}/usr/src/ioemu
#}

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/i386-dm/qemu-dm ${D}/usr/lib/xen/bin/qemu-dm
        install -d ${D}/etc/xen
        install -d ${D}/etc/xen/scripts
        install -m 755 ${S}/i386-dm/qemu-ifup-Linux ${D}/etc/xen/scripts/qemu-ifup

	install -d ${D}/usr/share/xen/qemu
	BLOBS="bios.bin vgabios.bin vgabios-cirrus.bin \
		video.x \
		pxe-ne2k_pci.bin pxe-rtl8139.bin pxe-pcnet.bin pxe-e1000.bin \
		bamboo.dtb"
	for f in $BLOBS ; do
		install -m 0644 ${S}/pc-bios/$f ${D}/usr/share/xen/qemu
	done

        # install the keymaps (for vnc)
        install -d ${D}/usr/share/xen/qemu/keymaps
        for i in ${S}/keymaps/* ; do
                install -m 0644 ${i} ${D}/usr/share/xen/qemu/keymaps
        done
}

do_install_append_xenclient-stubdomain() {
        rm -f ${D}/etc/xen/scripts/qemu-ifup
        install -d ${D}/etc/xen
        install -m 755 ${WORKDIR}/qemu-ifup-stubdom ${D}/etc/xen/scripts/qemu-ifup
}
