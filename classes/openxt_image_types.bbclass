# raw image - simply copy rootfs tree to deploy directory
IMAGE_CMD_raw() {
    cp -a ${IMAGE_ROOTFS} ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.raw
}

# OpenXT ext3 tweaks.
# - Disable fscheck.
# - Run fs check after generation.
IMAGE_CMD_ext3_append() {
    ;
    tune2fs -c -1 -i 0 ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext3
    e2fsck -f -y ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext3
}

# OpenXT vhd.
# Standard vhd image from an existing filesystem.
# vhd size must be a multiple of 2 MB.
CONVERSIONTYPES_append = "vhd"

CONVERSION_CMD_vhd() {
    local ALIGN=`expr 2 \* 1024 \* 1024`
    local TARGET_VHD_SIZE=`expr \( \( ${ROOTFS_SIZE} + ${ALIGN} + 1 \) / ${ALIGN} \) \* ${ALIGN}`
    local TARGET_VHD_SIZE_KB=`expr \( ${TARGET_VHD_SIZE} / 1024 \)`
    vhd convert ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type} ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type}.vhd ${TARGET_VHD_SIZE_KB}
}

CONVERSION_DEPENDS_vhd = "hkg-vhd-native"

IMAGE_TYPES += " \
    ext3.vhd ext3.vhd.gz \
"
