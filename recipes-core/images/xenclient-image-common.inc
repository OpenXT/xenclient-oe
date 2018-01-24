IMAGE_CLASSES += " \
    openxt_image_types \
"

export STAGING_KERNEL_DIR

activate_verbose_sysvinit() {
    sed -i -e 's/^VERBOSE=no$/VERBOSE=yes/' ${IMAGE_ROOTFS}${sysconfdir}/default/rcS
}
# Make sysvinit verbose if debug-tweaks is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks" , "activate_verbose_sysvinit; ", "",d)}'
