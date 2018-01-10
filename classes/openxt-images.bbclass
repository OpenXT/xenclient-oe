IMAGE_TYPES += "ext3.vhd"
IMAGE_CMD_ext3.vhd () {
    # 100M - safe default, overwrite in the recipe
    VHD_MAX_SIZE = "100"

    install -d ${DEPLOY_DIR_IMAGE}/tmp.vhd
    genext2fs -b ${ROOTFS_SIZE} -d ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3 ${EXTRA_IMAGECMD}
    tune2fs -j ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3
    vhd convert ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3 ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3.vhd ${VHD_MAX_SIZE}
    rm -f ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3
    mv ${DEPLOY_DIR_IMAGE}/tmp.vhd/${IMAGE_NAME}.rootfs.ext3.vhd ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3.vhd
}

IMAGE_DEPENDS_ext3.vhd = "hs-vhd-native genext2fs-native e2fsprogs-native"

# raw image - simply copy rootfs tree to deploy directory
IMAGE_TYPES += "raw"
IMAGE_CMD_raw () {
    cp -a ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.raw
}

IMAGE_TYPES += "xc.ext3"
IMAGE_TYPEDEP_xc.ext3 = "ext3"
IMAGE_CMD_xc.ext3 () {
    set -x

    I0=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3
    I=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.xc.ext3
    mv $I0 $I

    tune2fs -c -1 -i 0 $I
    e2fsck -f -y $I || true

    set +x
}

IMAGE_DEPENDS_xc.ext3 = "e2fsprogs-native"

# sadly the vhd stack in XC doesn't seem to [yet] understand libbudgetvhd's vhds
# take rootfs size in KB, convert to bytes for truncate size, convert to MB
# and deal with bash rounding. If odd after rounding, add 1, if even, add 2 since we lost
# some precision; vhd size must also be a multiple of 2 MB.
IMAGE_TYPES += "xc.ext3.vhd"
IMAGE_TYPEDEP_xc.ext3.vhd = "xc.ext3"
IMAGE_CMD_xc.ext3.vhd () {
    set -x

    I0=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.xc.ext3
    I=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.xc.ext3.vhd

    TGT_VHD_SIZE=`expr $ROOTFS_SIZE / 1024`
    if [ `expr $TGT_VHD_SIZE % 2` -eq 1 ]; then
        TGT_VHD_SIZE=`expr $TGT_VHD_SIZE + 1`
    else
        TGT_VHD_SIZE=`expr $TGT_VHD_SIZE + 2`
    fi

    tune2fs -c -1 -i 0 $I0
    e2fsck -f -y $I0 || true

    vhd convert $I0 $I ${TGT_VHD_SIZE}

    rm -f $I0
    set +x
}

IMAGE_DEPENDS_xc.ext3.vhd = "hs-vhd-native e2fsprogs-native"

