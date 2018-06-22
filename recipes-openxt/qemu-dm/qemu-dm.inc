LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = " \
    xen \
    alsa-lib \
    pciutils \
    libpng \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap', 'blktap3', d)} \
    libv4v \
    zlib \
    libcap-ng \
    libdmbus \
    pixman \
"

# Leave this in it's own definition so that it can be replaced with a local
# tarball (the QEMU download speed is terrible). If this is combined with the
# other patches, it doesn't seem to work
SRC_URI = "http://wiki.qemu-project.org/download/qemu-${PV}.tar.bz2"

# QEMU backports
SRC_URI += " \
    file://backports/0001-char-serial-check-divider-value-against-baud-base.patch \
    file://backports/0002-audio-intel-hda-check-stream-entry-count-during-tran.patch \
    file://backports/0003-net-rtl8139-limit-processing-of-ring-descriptors.patch \
    file://backports/0004-char-Fix-Possible-use-after-free-CVE-2016-9923.patch \
    file://backports/0005-audio-ac97-add-exit-function.patch \
    file://backports/0006-serial-fix-memory-leak-in-serial-exit.patch \
    file://backports/0007-tcg-i386-Check-the-size-of-instruction-being-transla.patch \
    file://backports/0008-xen-fix-ioreq-handling.patch \
    file://backports/0009-input-limit-kbd-queue-depth.patch \
    file://backports/0010-audio-release-capture-buffers.patch \
    file://backports/0011-slirp-check-len-against-dhcp-options-array-end.patch \
    file://backports/0012-bitmap-add-bitmap_copy_and_clear_atomic.patch \
    file://backports/0013-memory-add-support-getting-and-using-a-dirty-bitmap-.patch \
    file://backports/0014-vga-add-vga_scanline_invalidated-helper.patch \
    file://backports/0015-vga-make-display-updates-thread-safe.patch \
    file://backports/0016-vga-fix-display-update-region-calculation.patch \
    file://backports/0017-vga-fix-display-update-region-calculation-split-scre.patch \
    file://backports/0018-vga-stop-passing-pointers-to-vga_draw_line-functions.patch \
    file://backports/0019-multiboot-validate-multiboot-header-address-values.patch \
    file://backports/0020-vga-drop-line_offset-variable.patch \
    file://backports/0021-vga-handle-cirrus-vbe-mode-wraparounds.patch \
    file://backports/0022-vga-check-the-validation-of-memory-addr-when-draw-te.patch \
    file://backports/0023-vga-add-ram_addr_t-cast.patch \
    file://backports/0024-vga-fix-region-calculation.patch \
    file://backports/0025-memory-remove-unnecessary-masking-of-MemoryRegion-ra.patch \
    file://backports/0026-exec-hide-mr-ram_addr-from-qemu_get_ram_ptr-users.patch \
    file://backports/0027-exec-introduce-address_space_extend_translation.patch \
    file://backports/0028-xen-mapcache-store-dma-information-in-revmapcache-en.patch \
    file://backports/0029-exec-use-qemu_ram_ptr_length-to-access-guest-ram.patch \
    file://backports/0030-exec-Add-lock-parameter-to-qemu_ram_ptr_length.patch \
    file://backports/0031-IDE-Do-not-flush-empty-CDROM-drives.patch \
    file://backports/0032-slirp-fix-clearing-ifq_so-from-pending-packets.patch \
    file://backports/0033-ps2-check-PS2Queue-pointers-in-post_load-routine.patch \
    file://backports/0034-osdep-Fix-ROUND_UP-64-bit-32-bit.patch \
    file://backports/0035-ide-core-add-cleanup-function.patch \
    file://backports/0036-ide-ahci-call-cleanup-function-in-ahci-unit.patch \
    file://backports/0037-slirp-correct-size-computation-while-concatenating-m.patch \
    file://backports/0038-multiboot-Check-validity-of-mh_header_addr.patch \
    file://backports/0039-xen-disk-don-t-leak-stack-data-via-response-ring.patch \
"

# QEMU Patch Queue
SRC_URI += " \
    file://compile-time-stubdom-flag.patch \
    file://generic-xenstore-extensions.patch \
    file://readonly-ide.patch \
    file://hvm-param-dm-domain.patch \
    file://logging-syslog.patch \
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
    file://qmp-v4v-char-driver.patch \
    file://use-relative-xenstore-nodes.patch \
    file://exit-mainloop-on-reset.patch \
    file://write-acpi-state-to-xenstore.patch \
    file://set-blockdev-ro.patch \
    file://block-remove-unused-block-format-support.patch \
    file://net-Remove-unused-network-options.patch \
"

SRC_URI[md5sum] = "bdf1f3d0c177ebeb35a079a4bc3fc74e"
SRC_URI[sha256sum] = "9a7ec64f147b9e1e570c410e00ebe271869b5d4c85392ba047db7617c297faa3"

S = "${WORKDIR}/qemu-${PV}"

inherit xenclient autotools-brokensep pkgconfig

# Include a minimal amount of blobs by default.
FILES_${PN} += "/usr/share/qemu/keymaps/en-us       \
                /usr/share/qemu/keymaps/commons     \
                /usr/share/qemu/keymaps/modifiers   \
                "
FILES_${PN}-dbg += "/usr/lib/.debug/* /usr/libexec/.debug/*"
FILES_${PN}-extra-keymaps = "/usr/share/qemu/keymaps/*"
FILES_${PN}-extra-roms = "/usr/share/qemu/*"
INSANE_SKIP_${PN}-extra-roms = "arch"
FILES_${PN}-utils = "/usr/libexec/*"

PACKAGES += "${PN}-extra-keymaps ${PN}-extra-roms ${PN}-utils"

EXTRA_OECONF += " \
    --enable-xen \
    --target-list=i386-softmmu \
    --with-system-pixman \
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
    --disable-uuid \
    --disable-vhdx \
    --disable-qom-cast-debug \
    --disable-tcg-interpreter \
    --disable-vhost-net \
    --disable-vhost-scsi \
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
    --enable-atapi-pt-v4v \
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
	dev_disable CONFIG_PARALLEL
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
	dev_disable CONFIG_USB_OHCI
	dev_disable CONFIG_USB_XHCI
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
