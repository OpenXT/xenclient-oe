inherit openxt_image_types
inherit openxt-image-disk
inherit image-qa-module-sigs

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
ROOTFS_POSTPROCESS_COMMAND += '${@oe.utils.conditional("DISTRO_TYPE", "release", "zap_empty_root_password; ", "",d)}'

# Make sysvinit verbose if debug-tweaks is enabled
activate_verbose_sysvinit() {
    if [ -e "${IMAGE_ROOTFS}${sysconfdir}/default/rcS" ]; then
        sed -i -e 's/^VERBOSE=no$/VERBOSE=yes/' "${IMAGE_ROOTFS}${sysconfdir}/default/rcS"
    fi
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks" , "activate_verbose_sysvinit; ", "",d)}'

# A hook function to support extended read-only-rootfs IMAGE_FEATURES.
read_only_rootfs_hook_extend() {
    # If we're using openssh-argo and the /etc/ssh directory has no pre-generated keys,
    # we should configure openssh to use the configuration file /etc/ssh/sshd_config_readonly
    # and the keys under /var/run/ssh.
    if [ -f "${IMAGE_ROOTFS}/etc/ssh/sshd_config_argo" ]; then
        echo "SYSCONFDIR=\${SYSCONFDIR:-/var/run/ssh}" >> "${IMAGE_ROOTFS}/etc/default/ssh-argo"
        echo "SSHD_OPTS='-f /etc/ssh/sshd_config_readonly_argo'" >> "${IMAGE_ROOTFS}/etc/default/ssh-argo"
    fi
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "read_only_rootfs_hook_extend; ", "",d)}'

# A hook function to only accept sshargo from dom0.  sshd-argo will bind a
# single-source ring to dom0 instead of a wildcard ring.
read_only_rootfs_no_sshd_argo_wildcard() {
    # Aside from dom0, we only want to allow sshargo from dom0.
    if [ -f "${IMAGE_ROOTFS}/etc/ssh/sshd_config_argo" ]; then
        echo "export ARGO_ACCEPT_DOM0_ONLY=1" >> "${IMAGE_ROOTFS}/etc/default/ssh-argo"
    fi
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "wildcard-sshd-argo", "", "read_only_rootfs_no_sshd_argo_wildcard; ",d)}'
IMAGE_FEATURES[validitems] += "wildcard-sshd-argo"

# Add and entry to /etc/inittab to start a tty on hvc0 (debug).
start_tty_on_hvc0() {
	echo 'hvc0:12345:respawn:/bin/su - root -c "/sbin/getty 115200 hvc0 vt102"' >> "${IMAGE_ROOTFS}/etc/inittab"
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks", "start_tty_on_hvc0; ", "",d)}'

# Forcibly remove packages disregarding if it creates a broken dependency
force_package_removal() {
    if [ -n "${PACKAGE_REMOVE}" ]; then
        opkg -f "${IPKGCONF_TARGET}" -o "${IMAGE_ROOTFS}" ${OPKG_ARGS} --force-depends remove ${PACKAGE_REMOVE};
    fi
}
ROOTFS_POSTPROCESS_COMMAND += "force_package_removal; "

# Change root shell.
root_bash_shell() {
    sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' "${IMAGE_ROOTFS}/etc/passwd"
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "root-bash-shell", "root_bash_shell; ", "",d)}'
IMAGE_FEATURES[validitems] += "root-bash-shell"

# Remove initscripts pulled-in by dependencies or not required for operation.
remove_nonessential_initscripts() {
    if [ -n "${INITSCRIPT_REMOVE}" ]; then
        for i in ${INITSCRIPT_REMOVE}; do
            remove_initscript "${i}"
        done
    fi
}
ROOTFS_POSTPROCESS_COMMAND += "remove_nonessential_initscripts; "

# Xenstore reboot
ctrlaltdel_reboot() {
    # PV driver synthesize ctrl+alt+del in response to a xenstore reboot
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;
}
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "ctrlaltdel-reboot", "ctrlaltdel_reboot; ", "",d)}'
IMAGE_FEATURES[validitems] += "ctrlaltdel-reboot"
