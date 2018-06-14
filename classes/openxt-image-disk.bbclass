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

BOOTDD_EXTRA_SPACE ?= "2048"
KERNEL_CMDLINE     ?= "root=/dev/xvda2 ro console=hvc0"

CONVERSIONTYPES_append = " disk"

CONVERSION_DEPENDS_disk = "syslinux \
                           syslinux-native \
                           dosfstools-native \
                           virtual/kernel \
                           parted-native \
                           mtools-native \
                           "

CONVERSION_CMD_disk() {
	BOOTDIR="${S}/bootp/boot"
	BOOTIMG="${S}/bootp.image"
	ROOTIMG=${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type}
	IMAGE=$ROOTIMG.disk

	# Populate the boot directory
	install -d $BOOTDIR
	bbnote "Trying to install ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} as $BOOTDIR/vmlinuz"
	if [ -e ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ]; then
		install -m 0644 ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} $BOOTDIR/
	else
		bbwarn "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} doesn't exist"
	fi

	# initrd is made of concatenation of multiple filesystem images
	if [ -n "${INITRD}" ]; then
		rm -f $BOOTDIR/initrd
		for fs in ${INITRD}
		do
			if [ -s "$fs" ]; then
				cat $fs >> $BOOTDIR/initrd
			else
				bbfatal "$fs is invalid. initrd image creation failed."
			fi
		done
		chmod 0644 $BOOTDIR/initrd
	fi

	# Create syslinux.cfg
	CFGNAME="syslinux.cfg"
	install -d ${BOOTDIR}
	# Create syslinux config
	echo "ALLOWOPTIONS 1"  > ${BOOTDIR}/${CFGNAME}
	#echo "SERIAL 0 115200" >> ${BOOTDIR}/${CFGNAME}
	echo "DEFAULT boot" >> ${BOOTDIR}/${CFGNAME}
	echo "TIMEOUT 0" >> ${BOOTDIR}/${CFGNAME}
	echo "PROMPT 0" >> ${BOOTDIR}/${CFGNAME}
	echo "LABEL boot" >> ${BOOTDIR}/${CFGNAME}
	echo "KERNEL /${KERNEL_IMAGETYPE}" >> ${BOOTDIR}/${CFGNAME}
	echo "APPEND LABEL=boot ${KERNEL_CMDLINE}" >> ${BOOTDIR}/${CFGNAME}
	if [ -n "${INITRD}" ]; then
		echo "INITRD /initrd" >> ${BOOTDIR}/${CFGNAME}
	fi

	BOOTKB=$( du -bks $BOOTDIR | cut -f 1 )
	BOOTKB=$( expr $BOOTKB + ${BOOTDD_EXTRA_SPACE} )

	BOOTDD_VOLUME_ID="BOOT"
	# Remove it since mkdosfs would fail when it exists
	rm -f $BOOTIMG
	mkdosfs -n ${BOOTDD_VOLUME_ID} -S 512 -C $BOOTIMG $BOOTKB
	mcopy -i $BOOTIMG -s $BOOTDIR/* ::/

	# syslinux installs ldlinux.sys & ldlinux.c32
	syslinux $BOOTIMG
	chmod 644 $BOOTIMG

	ROOTKB=$( du -Lbks $ROOTIMG | cut -f 1 )
	TOTALSIZE=$( expr $BOOTKB + $ROOTKB )
	END1=$( expr $BOOTKB \* 1024 )
	START2=$( expr $END1 + 512 )
	END2=$( expr \( $ROOTKB \* 1024 \) + $END1 )

	echo $ROOTKB $TOTALSIZE $END1 $START2 $END2
	rm -rf $IMAGE
	dd if=/dev/zero of=$IMAGE bs=1024 seek=$TOTALSIZE count=1

	parted $IMAGE mklabel msdos
	parted $IMAGE mkpart primary fat16 0 ${END1}B
	parted $IMAGE unit B mkpart primary ext2 ${START2}B ${END2}B
	parted $IMAGE set 1 boot on

	parted $IMAGE print

	# write a disk signature
	echo -n "disk" | dd of=$IMAGE bs=1 seek=440 conv=notrunc

	OFFSET=$( expr $START2 / 512 )

	dd if=${STAGING_DATADIR}/syslinux/mbr.bin of=$IMAGE conv=notrunc
	dd if=$BOOTIMG of=$IMAGE conv=notrunc seek=1 bs=512
	dd if=$ROOTIMG of=$IMAGE conv=notrunc seek=$OFFSET bs=512
}
