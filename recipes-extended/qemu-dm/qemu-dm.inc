LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = " \
    xen \
    alsa-lib \
    pciutils \
    libpng \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap', 'blktap3', d)} \
    libargo \
    zlib \
    libcap-ng \
    libdmbus \
    pixman \
"

PV = "3.1.0"

# Leave this in it's own definition so that it can be replaced with a local
# tarball (the QEMU download speed is terrible). If this is combined with the
# other patches, it doesn't seem to work
SRC_URI = "http://wiki.qemu-project.org/download/qemu-${PV}.tar.bz2"

# QEMU Patch Queue
SRC_URI += "file://0001-configure-use-valid-args-testing-sem_timedwait.patch \
            file://0002-qemu-compiler-Define-QEMU_NONSTRING.patch \
            file://0003-block-sheepdog-Use-QEMU_NONSTRING-for-non-NUL-termin.patch \
            file://0004-sockets-avoid-string-truncation-warnings-when-copyin.patch \
            file://0005-hw-usb-avoid-format-truncation-warning-when-formatti.patch \
            file://0006-spice-set-device-address-and-device-display-ID-in-QX.patch \
            file://0007-migration-Fix-stringop-truncation-warning.patch \
            file://0008-migration-Use-strnlen-for-fixed-size-string.patch \
            file://0009-crypto-block-remove-redundant-struct-packing-to-fix-.patch \
            file://0010-block-nvme-Remove-QEMU_PACKED-from-naturally-aligned.patch \
            file://0011-qxl-avoid-unaligned-pointer-reads-writes.patch \
            file://0012-usb-mtp-fix-alignment-of-access-of-ObjectInfo-filena.patch \
            file://0013-virtio-scsi-Fix-build-with-gcc-9.patch \
            file://0014-block-vdi-Don-t-take-address-of-fields-in-packed-str.patch \
            file://0015-uuid-Make-qemu_uuid_bswap-take-and-return-a-QemuUUID.patch \
            file://0016-block-vpc-Don-t-take-address-of-fields-in-packed-str.patch \
            file://0017-hw-acpi-Use-QEMU_NONSTRING-for-non-NUL-terminated-ar.patch \
            file://compile-time-stubdom-flag.patch \
            file://generic-xenstore-extensions.patch \
            file://readonly-ide.patch \
            file://hvm-param-dm-domain.patch \
            file://dmbus.patch \
            file://switcher.patch \
            file://acpi.patch \
            file://xenmou.patch \
            file://atapi-pass-through.patch \
            file://vbe-xt-extensions.patch \
            file://vga-spinlock.patch \
            file://vga-shadow-bda.patch \
            file://surfman-dcl.patch \
            file://audio-policy.patch \
            file://msix-cap-disable.patch \
            file://openxtaudio.patch \
            file://nic-link-state-propagation.patch \
            file://stubdom-read-gsi-from-device-config-space.patch \
            file://acpi-pm-feature.patch \
            file://maintain-time-offset.patch \
            file://acpi-wakeup.patch \
            file://openxt-misc-fixes.patch \
            file://qmp-argo-char-driver.patch \
            file://exit-mainloop-on-reset.patch \
            file://write-acpi-state-to-xenstore.patch \
            file://set-blockdev-ro.patch \
            file://block-remove-unused-block-format-support.patch \
            file://net-Remove-unused-network-options.patch \
            file://round-pci-region-sizes.patch \
            "

SRC_URI[md5sum] = "8a75cfd18460143d1b352234e437ca8d"
SRC_URI[sha256sum] = "98fee0c86b299ebaf08587ba8df1dc8bb7152708d62937a0631875d95cc8d921"

S = "${WORKDIR}/qemu-${PV}"

inherit autotools-brokensep pkgconfig

# Include a minimal amount of blobs by default.
FILES_${PN} += "/usr/share/qemu/keymaps/en-us       \
                /usr/share/qemu/keymaps/commons     \
                /usr/share/qemu/keymaps/modifiers   \
                "
FILES_${PN}-dbg += "/usr/lib/.debug/* /usr/libexec/.debug/*"
FILES_${PN}-extra-keymaps = "/usr/share/qemu/keymaps/*"
FILES_${PN}-extra-roms = "/usr/share/qemu/*"
INSANE_SKIP_${PN}-extra-roms = "arch"
FILES_${PN}-utils = "/usr/libexec/* \
                     /usr/bin/qemu-pr-helper \
"

PACKAGES += "${PN}-extra-keymaps ${PN}-extra-roms"
PACKAGES =+ "${PN}-utils"

EXTRA_OECONF += " \
    --enable-xen \
    --target-list=i386-softmmu \
    --disable-sdl \
    --disable-vnc \
    --disable-docs \
    --disable-strip \
    --disable-virtfs \
    --disable-bluez \
    --disable-guest-agent \
    --disable-curses \
    --disable-slirp \
    --disable-curl \
    --disable-kvm \
    --disable-tools \
    --disable-fdt \
    --disable-qom-cast-debug \
    --disable-tcg-interpreter \
    --disable-tcg \
    --disable-vhost-net \
    --disable-vhost-crypto \
    --disable-vhost-scsi \
    --disable-vhost-vsock \
    --disable-vhost-user \
    --disable-bzip2 \
    --disable-libusb \
    --disable-usb-redir \
    --disable-lzo \
    --disable-gnutls \
    --disable-nettle \
    --disable-gcrypt \
    --disable-tpm \
    --enable-pie \
    --enable-werror \
    --enable-surfman \
    --enable-atapi-pt \
    --enable-atapi-pt-argo \
    --enable-readonly-ide \
    --enable-debug-info \
    --enable-trace-backends=nop \
    --extra-cflags=-DXC_WANT_COMPAT_DEVICEMODEL_API \
"

do_configure(){
    ${S}/configure --prefix=${prefix} --sysconfdir=${sysconfdir} ${EXTRA_OECONF}
}

EXTRA_OEMAKE += "AUTOCONF_HOST='--host=`arch`' DESTDIR=${D} STRIP=''"

CFLAGS_append = " -Wno-unused-parameter -Wno-unused-local-typedefs --param ssp-buffer-size=4 -ftrapv -D_FORTIFY_SOURCE=2"

INC_PR = "r17"

do_configure_append() {
	sed -i -s '/^CONFIG_L2TPV3=y/d' config-host.mak
}

do_compile_prepend() {
	rm -f i386-softmmu/config-devices.mak
	oe_runmake i386-softmmu/config-devices.mak

	dev_disable() {
		sed -i -e 's/\('$1'=\).*$/\1n/' i386-softmmu/config-devices.mak
	}

	dev_disable CONFIG_VGA_ISA
	dev_disable CONFIG_VGA_CIRRUS
	dev_disable CONFIG_VMWARE_VGA
	dev_disable CONFIG_VIRTIO_VGA
	dev_disable CONFIG_VMMOUSE
	dev_disable CONFIG_IPMI
	dev_disable CONFIG_IPMI_LOCAL
	dev_disable CONFIG_IPMI_EXTERN
	dev_disable CONFIG_ISA_IPMI_KCS
	dev_disable CONFIG_ISA_IPMI_BT
	# SuperIO needs this
	#dev_disable CONFIG_PARALLEL
	dev_disable CONFIG_NE2000_ISA
	dev_disable CONFIG_APPLESMC
	#dev_disable CONFIG_PFLASH_CFI01
	dev_disable CONFIG_WDT_IB700
	dev_disable CONFIG_ISA_DEBUG
	dev_disable CONFIG_ISA_TESTDEV
	dev_disable CONFIG_VMPORT
	dev_disable CONFIG_SGA
	#dev_disable CONFIG_PVPANIC
	#dev_disable CONFIG_NVDIMM
	#dev_disable CONFIG_ACPI_NVDIMM
	dev_disable CONFIG_XIO3130
	dev_disable CONFIG_IOH3420
	dev_disable CONFIG_I82801B11
	dev_disable CONFIG_HYPERV_TESTDEV
	dev_disable CONFIG_VIRTIO_PCI
	dev_disable CONFIG_VIRTIO
	dev_disable CONFIG_VIRTIO_9P
	dev_disable CONFIG_VIRTIO_BALLOON
	dev_disable CONFIG_VIRTIO_BLK
	dev_disable CONFIG_VIRTIO_CRYPTO
	dev_disable CONFIG_VIRTIO_GPU
	dev_disable CONFIG_VIRTIO_INPUT
	dev_disable CONFIG_VIRTIO_NET
	dev_disable CONFIG_VIRTIO_RNG
	dev_disable CONFIG_VIRTIO_SCSI
	dev_disable CONFIG_VIRTIO_SERIAL
	dev_disable CONFIG_USB_OHCI
	# Things blow up without xhci
	#dev_disable CONFIG_USB_XHCI
	dev_disable CONFIG_NE2000_PCI
	dev_disable CONFIG_EEPRO100_PCI
	dev_disable CONFIG_PCNET_PCI
	dev_disable CONFIG_PCNET_COMMON
	dev_disable CONFIG_ES1370
	dev_disable CONFIG_LSI_SCSI_PCI
	dev_disable CONFIG_VMW_PVSCSI_SCSI_PCI
	dev_disable CONFIG_MEGASAS_SCSI_PCI
	dev_disable CONFIG_MPTSAS_SCSI_PCI
	dev_disable CONFIG_VMXNET3_PCI
	dev_disable CONFIG_ESP
	dev_disable CONFIG_ESP_PCI
	dev_disable CONFIG_IPACK
	dev_disable CONFIG_WDT_IB6300ESB
	dev_disable CONFIG_PCI_TESTDEV
	dev_disable CONFIG_NVME_PCI
	dev_disable CONFIG_SD
	dev_disable CONFIG_SDHCI
	dev_disable CONFIG_EDU
	dev_disable CONFIG_IVSHMEM
	dev_disable CONFIG_ROCKER
	dev_disable CONFIG_SB16
	dev_disable CONFIG_ADLIB
	dev_disable CONFIG_GUS
	dev_disable CONFIG_CS4231A
	dev_disable CONFIG_USB_STORAGE_BOT
	dev_disable CONFIG_USB_STORAGE_UAS
	dev_disable CONFIG_USB_STORAGE_MTP
	dev_disable CONFIG_USB_SMARTCARD
	dev_disable CONFIG_USB_AUDIO
	dev_disable CONFIG_USB_SERIAL
	dev_disable CONFIG_USB_NETWORK
	dev_disable CONFIG_USB_BLUETOOTH
}
