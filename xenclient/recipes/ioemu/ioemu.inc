
inherit xenclient
inherit xenclient-pq
inherit pkgconfig

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"

DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v libdmbus openssl seabios ipxe"

SRCREV_FORMAT = "source"

PV = "0+git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/ioemu.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};name=source \
    file://qemu-log-syslog;patch=1 \
    file://ioemu-compil;patch=1 \
    file://tapdisk-disable-readonly;patch=1 \
    file://set_timeoffset;patch=1 \
    file://alsa-fix;patch=1 \
    file://alsa-config;patch=1 \
    file://alsa-fix-resume;patch=1 \
    file://audio-volume-control;patch=1 \
    file://add-config-audio;patch=1 \
    file://dm-ready;patch=1 \
    file://suspend-by-signal;patch=1 \
    file://battery-management;patch=1 \
    file://oem-features;patch=1 \
    file://thermal-management;patch=1 \
    file://xenstore-notify-pm-events;patch=1 \
    file://vbestate;patch=1 \
    file://pci-to-pci-bridge-for-calpella;patch=1 \
    file://igfx-passthrough-1f-caps;patch=1 \
    file://switcher;patch=1 \
    file://fix-imobile-mouse;patch=1 \
    file://atapi-pass-through;patch=1 \
    file://pv_driver_throttling_disabled;patch=1 \
    file://guest-os-controlled-brightness-support;patch=1 \
    file://kbd-led;patch=1 \
    file://disable-audio-recording;patch=1 \
    file://fix-keyboard-mouse-simultaneous-use-problems;patch=1 \
    file://fix-base-for-16meg-vram;patch=1 \
    file://absolute-event-support;patch=1 \
    file://fix-blktaplib.h-include-path;patch=1 \
    file://dummy-ider;patch=1 \
    file://uhci-inject-after-hid-event;patch=1 \
    file://xc-emulated-nic-link-state-propagation;patch=1 \
    file://vnc-always-use-absolute-mouse;patch=1 \
    file://vnc-use-domid-if-port-doesnt-exist;patch=1 \
    file://switcher-export-enabled-abs;patch=1 \
    file://xenmouse;patch=1 \
    file://fix-raw-serial-device;patch=1 \
    file://disable-blktap;patch=1 \
    file://atapi-pt-exclusive-access;patch=1 \
    file://intel-hda;patch=1 \
    file://cdrom-fix-cdrom-media-change-detection.patch;patch=1 \
    file://lpc;patch=1 \
    file://applesmc;patch=1 \
    file://vuart;patch=1 \
    file://fix-vuart;patch=1 \
    file://fix-absolute-mouse-clicks;patch=1 \
    file://switcher-resend-abs-config;patch=1 \
    file://xc-rtl8139-dont-receive-data-until-frame-sent;patch=1 \
    file://es1370-supervol;patch=1 \
    file://fix-static-linking;patch=1 \
    file://switch-jap-key-quirk;patch=1 \
    file://dmbus;patch=1 \
    file://passthrough-dont-bind-irq-with-msitranslate;patch=1 \
    file://use_dmbus_for_input_server_communication;patch=1 \
    file://xenclient-vga;patch=1 \
    file://surfman;patch=1 \
    file://msi-rebind-after-host-sleep;patch=1 \
    file://stubdoms;patch=1 \
    file://stubdomain-passthrough;patch=1 \
    file://stubdom-media-change;patch=1 \
    file://stubdoms-drivemapping;patch=1 \
    file://xen-machine-max-vcpus;patch=1 \
    file://pci-pt-from-xenstore-entry-legacy;patch=1 \
    file://igfx-quirks;patch=1 \
    file://vcpus-alloc-ioreq-local-port-once;patch=1 \
    file://fix-gpe0-size;patch=1 \
    file://no-vkbd-pv-domain;patch=1 \
    file://event-tweaks;patch=1 \
    file://cleanup-volume;patch=1 \
    file://opregion-mapping;patch=1 \
    file://pt-lpc-bridge;patch=1 \
    file://pt-ioport-map-invalid-address;patch=1 \
    file://pt-pci-host-clean-values;patch=1 \
    file://prevent-vm-clear-cmdreg-first-bits;patch=1 \
    file://deactivate-msi-TEMP;patch=1 \
    file://acpi-reset-reg;patch=1 \
    file://xenmou2;patch=1 \
    file://ioreq-server;patch=1 \
    file://fix-unregister-iomem;patch=1 \
    file://ati-pt;patch=1 \
    file://disable-vt100;patch=1 \
    file://no-default-device;patch=1 \
    file://vtpm-support;patch=1 \
    file://fix-ioreq-pio-move;patch=1 \
    file://support-multiple-gpu;patch=1 \
    file://vga-restrict-modes;patch=1 \
    file://honour-empty-boot-device-list;patch=1 \
    file://cd-lock-control-and-report;patch=1 \
    file://vga-restrict-resolution;patch=1 \
    file://qemu-ifup-stubdom \
"

S = "${WORKDIR}/git"

FILES_${PN}-dbg += "/usr/lib/xen/bin/.debug"
FILES_${PN} += "/usr/lib/xen/bin /etc/xen /usr/share/xen/qemu"

# hack, sneak --sysroot in CFLAGS and LDFLAGS as qemu build scripts do not deal to good with crosscompilation
CFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} "
LDFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} "

# export some variable for IOEMU compilation
export STAGING_INCDIR
export STAGING_LIBDIR
export IDLDIR = "${STAGING_IDLDIR}"
export RPCGEN_TEMPLATES = "${STAGING_DATADIR}/xc-rpcgen"

do_configure() {
	# fixme these extras should be a patch in configure
        export CFLAGS="${CFLAGS} -msse4.1"
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
