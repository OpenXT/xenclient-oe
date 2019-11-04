# Portions from openembedded-core/meta/syslinux.bbclass
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
#
# Portions from openembedded-core/meta/image-vm.bbclass
# (loosly based off image-live.bbclass Copyright (C) 2004, Advanced Micro
# Devices, Inc.)

# The "disk" conversion creates a syslinux bootable disk with a small FAT
# partition and a second rootfs partition.  It is basically a CONVERSION_CMD
# implmentation of image-vm & syslinux bbclasses inlined together, but limited
# in functionality for the limited use case.

inherit openxt-vm-common

EFI = "${@bb.utils.contains("MACHINE_FEATURES", "efi", "1", "0", d)}"
EFI_PROVIDER ?= "grub-efi"
EFI_CLASS = "${@bb.utils.contains("MACHINE_FEATURES", "efi", "openxt-efi", "", d)}"

# Include legacy boot if MACHINE_FEATURES includes "pcbios" or if it does not
# contain "efi". This way legacy is supported by default if neither is
# specified, maintaining the original behavior.
def pcbios(d):
    pcbios = bb.utils.contains("MACHINE_FEATURES", "pcbios", "1", "0", d)
    if pcbios == "0":
        pcbios = bb.utils.contains("MACHINE_FEATURES", "efi", "0", "1", d)
    return pcbios

PCBIOS = "${@pcbios(d)}"
PCBIOS_CLASS = "${@['','openxt-syslinux'][d.getVar('PCBIOS') == '1']}"

inherit ${EFI_CLASS}
inherit ${PCBIOS_CLASS}

CONVERSIONTYPES_append = " disk"

CONVERSION_DEPENDS_disk = "syslinux \
                           syslinux-native \
                           dosfstools-native \
                           virtual/kernel \
                           parted-native \
                           mtools-native \
                           "

APPEND ?= "root=/dev/xvda2 ro"
BOOTDD_VOLUME_ID   ?= "boot"
BOOTDD_EXTRA_SPACE ?= "2048"

# Default value is hex that translates to "XTHD"
DISK_SIGNATURE ?= "44485458"

SYSLINUX_CFG ?= "${S}/syslinux.cfg"
GRUB_CFG ?= "${S}/grub.cfg"

build_boot_dd() {
	HDDDIR="${S}/hdd/boot"
	HDDIMG="${S}/hdd.image"
	ROOTFS_TYPE="${1}"
	ROOTFS="${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${ROOTFS_TYPE}"
	IMAGE="${ROOTFS}.disk"

	populate_kernel $HDDDIR
	populate_initrd $HDDDIR

	if [ "${PCBIOS}" = "1" ]; then
		syslinux_hddimg_populate $HDDDIR
	fi
	if [ "${EFI}" = "1" ]; then
		efi_hddimg_populate $HDDDIR
	fi

	BLOCKS=`du -bks $HDDDIR | cut -f 1`
	BLOCKS=`expr $BLOCKS + ${BOOTDD_EXTRA_SPACE}`

	# Remove it since mkdosfs would fail when it exists
	rm -f $HDDIMG
	mkdosfs -n ${BOOTDD_VOLUME_ID} -S 512 -C $HDDIMG $BLOCKS
	mcopy -i $HDDIMG -s $HDDDIR/* ::/

	if [ "${PCBIOS}" = "1" ]; then
		syslinux $HDDIMG
	fi
	chmod 644 $HDDIMG

	ROOTFSBLOCKS=`du -Lbks ${ROOTFS} | cut -f 1`
	TOTALSIZE=`expr $BLOCKS + $ROOTFSBLOCKS`
	END1=`expr $BLOCKS \* 1024`
	END2=`expr $END1 + 512`
	END3=`expr \( $ROOTFSBLOCKS \* 1024 \) + $END1`

	echo $ROOTFSBLOCKS $TOTALSIZE $END1 $END2 $END3
	rm -rf $IMAGE
	dd if=/dev/zero of=$IMAGE bs=1024 seek=$TOTALSIZE count=1

	parted $IMAGE mklabel msdos
	parted $IMAGE mkpart primary fat16 0 ${END1}B
	parted $IMAGE unit B mkpart primary ext2 ${END2}B ${END3}B
	parted $IMAGE set 1 boot on

	parted $IMAGE print

	awk "BEGIN { printf \"$(echo ${DISK_SIGNATURE} | sed 's/\(..\)\(..\)\(..\)\(..\)/\\x\4\\x\3\\x\2\\x\1/')\" }" | \
		dd of=$IMAGE bs=1 seek=440 conv=notrunc

	OFFSET=`expr $END2 / 512`
	if [ "${PCBIOS}" = "1" ]; then
		dd if=${STAGING_DATADIR}/syslinux/mbr.bin of=$IMAGE conv=notrunc
	fi

	dd if=$HDDIMG of=$IMAGE conv=notrunc seek=1 bs=512
	dd if=${ROOTFS} of=$IMAGE conv=notrunc seek=$OFFSET bs=512
}

validate_disk_signature() {
	if [ $(/bin/echo -n ${DISK_SIGNATURE}|wc -c) -le 8 ]; then
		if [ $(expr "x${DISK_SIGNATURE}" : "x[A-Za-z0-9]*$") -gt 0 ]; then
			return
		fi
	fi

        bb_fatal "DISK_SIGNATURE ${DISK_SIGNATURE} must be an 8 digit hex string"
}

CONVERSION_CMD_disk() {
	validate_disk_signature
	if [ "${PCBIOS}" = "1" ]; then
		build_syslinux_cfg
	fi
	if [ "${EFI}" = "1" ]; then
		build_efi_cfg
	fi
	build_boot_dd "${type}"
}
