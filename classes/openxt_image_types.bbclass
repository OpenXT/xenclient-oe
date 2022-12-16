# raw image - simply copy rootfs tree to deploy directory
IMAGE_CMD_raw() {
    cp -a ${IMAGE_ROOTFS} ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.raw
}

# OpenXT ext3 tweaks.
# - Disable fscheck.
# - Run fs check after generation.
oe_mkext234fs_append() {
    tune2fs -c -1 -i 0 ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.$fstype
    e2fsck -f -y ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.$fstype
}

# OpenXT vhd.
# Standard vhd image from an existing filesystem.
CONVERSIONTYPES_append = " vhd"
CONVERSION_CMD_vhd = "qemu-img convert -O vpc -o subformat=fixed ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type} ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type}.vhd"
CONVERSION_DEPENDS_vhd = "qemu-system-native"
