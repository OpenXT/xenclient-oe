inherit grub-efi

get_rootfs_uuid() {
	UUID=$( tune2fs -l "${ROOTFS}" | awk -F: '/Filesystem UUID:/ { print $2 }')

	[ -z "$UUID" ] && bbfatal "Cannot determine filesystem UUID of ${ROOTFS}"

	echo "$UUID"
}

replace_rootfs_uuid() {
	CFG="${1}"

	sed -i "s/<<uuid-of-rootfs>>/${UUID}/g" "${CFG}"
}

build_efi_cfg() {
	if [ -z "${GRUB_CFG}" ]; then
		bbfatal "Unable to read GRUB_CFG"
	fi

	cat /dev/null > "${GRUB_CFG}"

	echo "${GRUB_OPTS}"| tr ';' '\n' | while read opt; do
		echo "${opt}" >> "${GRUB_CFG}"
	done

	echo "default=boot" >> "${GRUB_CFG}"

	if [ -n "${GRUB_TIMEOUT}" ]; then
		echo "timeout=${GRUB_TIMEOUT}" >> "${GRUB_CFG}"
	else
		echo "timeout=10" >> "${GRUB_CFG}"
	fi

	echo -e "\nmenuentry 'boot' {" >> "${GRUB_CFG}"
	echo -ne "linux /${KERNEL_IMAGETYPE} LABEL=boot" >> "${GRUB_CFG}"
	echo -ne " ${GRUB_ROOT}" >> "${GRUB_CFG}"

	if [ -n "${APPEND}" ]; then
		echo -e " ${APPEND}" >> "${GRUB_CFG}"
	else
		echo "" >> "${GRUB_CFG}"
	fi
	if [ -n "${INITRD}" ]; then
		echo "initrd /initrd" >> "${GRUB_CFG}"
	fi

	echo "}" >> "${GRUB_CFG}"

	replace_rootfs_uuid "${GRUB_CFG}"
}
