LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"
DEPENDS = " \
    xen \
    alsa-lib \
    pciutils \
    libpng \
    xen-tools \
    libargo \
    zlib \
    libcap-ng \
    pixman \
"

PV = "4.1.0"

# Leave this in it's own definition so that it can be replaced with a local
# tarball (the QEMU download speed is terrible). If this is combined with the
# other patches, it doesn't seem to work
SRC_URI = "http://wiki.qemu-project.org/download/qemu-${PV}.tar.bz2"

# QEMU Patch Queue
SRC_URI += "file://compile-time-stubdom-flag.patch \
            file://generic-xenstore-extensions.patch \
            file://readonly-ide.patch \
            file://hvm-param-dm-domain.patch \
            file://acpi.patch \
            file://argo-configure.patch \
            file://atapi-pass-through.patch \
            file://audio-policy.patch \
            file://msix-cap-disable.patch \
            file://openxtaudio.patch \
            file://nic-link-state-propagation.patch \
            file://acpi-pm-feature.patch \
            file://maintain-time-offset.patch \
            file://openxt-misc-fixes.patch \
            file://qmp-argo-char-driver.patch \
            file://write-acpi-state-to-xenstore.patch \
            file://set-blockdev-ro.patch \
            file://block-remove-unused-block-format-support.patch \
            file://net-Remove-unused-network-options.patch \
            file://round-pci-region-sizes.patch \
            file://ac97-compatibility.patch \
	    file://0001-vnc-stubs-Allow-vnc-none.patch \
            file://0001-xen-hvm-Allow-disabling-buffer.patch \
            "

SRC_URI[md5sum] = "5862fc984bb955c4c24427d3bceddca5"
SRC_URI[sha256sum] = "49f0de77410d4d0f7d0321ff2c2888b281381f06e1e2dac9ec4d061e3934f4ae"

S = "${WORKDIR}/qemu-${PV}"

inherit autotools-brokensep pkgconfig python3native

require recipes-extended/qemu-dm/qemu-dm-vglass.inc

# Include a minimal amount of blobs by default.
FILES_${PN} += "/usr/share/qemu/keymaps/en-us       \
                /usr/share/qemu/keymaps/commons     \
                /usr/share/qemu/keymaps/modifiers   \
                "
FILES_${PN}-dbg += "/usr/lib/.debug/* /usr/libexec/.debug/*"
FILES_${PN}-extra-keymaps = "/usr/share/qemu/keymaps/*"
FILES_${PN}-extra-roms = "/usr/share/qemu/*"
INSANE_SKIP_${PN}-extra-roms = "arch"
FILES_${PN}-utils = "/usr/libexec \
                     /usr/bin/qemu-pr-helper \
"

PACKAGES += "${PN}-extra-keymaps ${PN}-extra-roms"
PACKAGES =+ "${PN}-utils"

EXTRA_OECONF += " \
    --target-list=i386-softmmu \
    --disable-auth-pam \
    --disable-bluez \
    --disable-bochs \
    --disable-bzip2 \
    --disable-cap-ng \
    --disable-capstone \
    --disable-cloop \
    --disable-curl \
    --disable-curses \
    --disable-dmg \
    --disable-docs \
    --disable-fdt \
    --disable-gcrypt \
    --disable-gnutls \
    --disable-guest-agent \
    --disable-iconv \
    --disable-kvm \
    --disable-libusb \
    --disable-live-block-migration \
    --disable-lzo \
    --disable-nettle \
    --disable-opengl \
    --disable-parallels \
    --disable-pvrdma \
    --disable-qcow1 \
    --disable-qed \
    --disable-qom-cast-debug \
    --disable-rdma \
    --disable-replication \
    --disable-sdl \
    --disable-sheepdog \
    --disable-slirp \
    --disable-spice \
    --disable-strip \
    --disable-tcg \
    --disable-tcg-interpreter \
    --disable-tools \
    --disable-tpm \
    --disable-usb-redir \
    --disable-vdi \
    --disable-vhost-crypto \
    --disable-vhost-net \
    --disable-vhost-scsi \
    --disable-vhost-user \
    --disable-vhost-vsock \
    --disable-virtfs \
    --disable-vnc \
    --disable-vvfat \
    --enable-atapi-pt \
    --enable-atapi-pt-argo \
    --enable-debug-info \
    --enable-pie \
    --enable-readonly-ide \
    --enable-trace-backends=nop \
    --enable-werror \
    --enable-xen \
    --extra-cflags="${CFLAGS}" \
"

do_configure(){
    ${S}/configure --prefix=${prefix} --sysconfdir=${sysconfdir} ${EXTRA_OECONF}
}

EXTRA_OEMAKE += "AUTOCONF_HOST='--host=`arch`' DESTDIR=${D} STRIP=''"

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
	dev_disable CONFIG_VIRTIO_PMEM
	dev_disable CONFIG_VIRTIO_PMEM_SUPPORTED
	dev_disable CONFIG_VIRTIO_BLK
	dev_disable CONFIG_VIRTIO_CRYPTO
	dev_disable CONFIG_VIRTIO_GPU
	dev_disable CONFIG_VIRTIO_INPUT
	dev_disable CONFIG_VIRTIO_NET
	dev_disable CONFIG_VIRTIO_RNG
	dev_disable CONFIG_VIRTIO_SCSI
	dev_disable CONFIG_VIRTIO_SERIAL
	dev_disable CONFIG_VIRTIO_INPUT_HOST
	dev_disable CONFIG_VFIO
	dev_disable CONFIG_VFIO_PCI
	dev_disable CONFIG_USB_OHCI
	dev_disable CONFIG_USB_OHCI_PCI
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
	dev_disable CONFIG_SDHCI_PCI
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
