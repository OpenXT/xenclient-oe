inherit openxt_image_types
inherit openxt-image-disk

# We want to inherit the FEATURE_PACKAGES but do only want core-boot included into IMAGE_INSTALL
IMAGE_INSTALL ?= "packagegroup-core-boot"
inherit core-image

export STAGING_KERNEL_DIR

# Helper to avoid redundancy in image recipes.
remove_initscript() {
    local script="$1"

    if [ -f "${IMAGE_ROOTFS}${sysconfdir}/init.d/${script}" ]; then
        update-rc.d -f -r "${IMAGE_ROOTFS}" "${script}" remove
    fi
}

#zap any empty root passwords for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_empty_root_password; ", "",d)}'

# Make sysvinit verbose if debug-tweaks is enabled
activate_verbose_sysvinit() {
    if [ -e "${IMAGE_ROOTFS}${sysconfdir}/default/rcS" ]; then
        sed -i -e 's/^VERBOSE=no$/VERBOSE=yes/' "${IMAGE_ROOTFS}${sysconfdir}/default/rcS"
    fi
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks" , "activate_verbose_sysvinit; ", "",d)}'

# A hook function to support extended read-only-rootfs IMAGE_FEATURES.
read_only_rootfs_hook_extend() {
    # If we're using openssh-v4v and the /etc/ssh directory has no pre-generated keys,
    # we should configure openssh to use the configuration file /etc/ssh/sshd_config_readonly
    # and the keys under /var/run/ssh.
    if [ -f "${IMAGE_ROOTFS}/etc/ssh/sshd_config_v4v" ]; then
        echo "SYSCONFDIR=\${SYSCONFDIR:-/var/run/ssh}" >> "${IMAGE_ROOTFS}/etc/default/ssh-v4v"
        echo "SSHD_OPTS='-f /etc/ssh/sshd_config_readonly_v4v'" >> "${IMAGE_ROOTFS}/etc/default/ssh-v4v"
    fi
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "read_only_rootfs_hook_extend; ", "",d)}'

# Add and entry to /etc/inittab to start a tty on hvc0 (debug).
start_tty_on_hvc0() {
	echo 'hvc0:12345:respawn:/bin/start_getty 115200 hvc0 vt102' >> "${IMAGE_ROOTFS}/etc/inittab"
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks", "start_tty_on_hvc0; ", "",d)}'

