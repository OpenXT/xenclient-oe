# XenClient dom0 image.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
"
IMAGE_FSTYPES = "ext3.gz"
export IMAGE_BASENAME = "xenclient-dom0-image"

COMPATIBLE_MACHINE = "(xenclient-dom0)"


# xserver-xorg should not live in dom0, but UIVM.
BAD_RECOMMENDATIONS += " \
    xserver-xorg \
    avahi-daemon \
    avahi-autoipd \
    ${@bb.utils.contains('IMAGE_FEATURES', 'web-certificates', '', 'ca-certificates', d)} \
"

IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    initscripts \
    modules \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    packagegroup-xenclient-dom0 \
    packagegroup-openxt-test \
    v4v-module \
    xenclient-preload-hs-libs \
    linux-firmware-i915 \
    ${@bb.utils.contains('IMAGE_FEATURES', 'debug-tweaks', 'packagegroup-selinux-policycoreutils audit', '' ,d)} \
"

inherit openxt-selinux-image
inherit xenclient-licences

require xenclient-image-common.inc
require xenclient-version.inc

# zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
    # Change root shell.
    sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd

    mkdir -p ${IMAGE_ROOTFS}/config/etc
    mv ${IMAGE_ROOTFS}/etc/passwd ${IMAGE_ROOTFS}/config/etc
    mv ${IMAGE_ROOTFS}/etc/shadow ${IMAGE_ROOTFS}/config/etc
    ln -s ../config/etc/passwd ${IMAGE_ROOTFS}/etc/passwd
    ln -s ../config/etc/shadow ${IMAGE_ROOTFS}/etc/shadow
    ln -s ../config/etc/.pwd.lock ${IMAGE_ROOTFS}/etc/.pwd.lock
    ln -s ../var/volatile/etc/asound ${IMAGE_ROOTFS}/etc/asound

    rm ${IMAGE_ROOTFS}/etc/hosts
    ln -s /var/run/hosts ${IMAGE_ROOTFS}/etc/hosts
    ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf

    echo 'kernel.printk_ratelimit = 0' >> ${IMAGE_ROOTFS}/etc/sysctl.conf

    # Add initramfs
    cat ${DEPLOY_DIR_IMAGE}/xenclient-initramfs-image-xenclient-dom0.cpio.gz > ${IMAGE_ROOTFS}/boot/initramfs.gz

    # Create mountpoint for /mnt/secure
    mkdir -p ${IMAGE_ROOTFS}/mnt/secure

    # Create mountpoint for /mnt/upgrade
    mkdir -p ${IMAGE_ROOTFS}/mnt/upgrade

    # Create mountpoint for boot/system
    mkdir -p ${IMAGE_ROOTFS}/boot/system

    # Create XL-related files and directories
    mkdir -p ${IMAGE_ROOTFS}/var/lib/xen
    mkdir -p ${IMAGE_ROOTFS}/etc/xen
    touch ${IMAGE_ROOTFS}/etc/xen/xl.conf

    # Remove network modules except netfront
    for x in `find ${IMAGE_ROOTFS}/lib/modules -name *.ko | grep drivers/net | grep -v xen-netfront`; do
        pkg="kernel-module-`basename $x .ko | sed s/_/-/g`"
        opkg ${IPKG_ARGS} -force-depends remove $pkg
    done

    # Write coredumps in /var/cores
    echo 'kernel.core_pattern = /var/cores/%e-%t.%p.core' >> ${IMAGE_ROOTFS}/etc/sysctl.conf
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "

### Stubdomain stuff - temporary
STUBDOMAIN_DEPLOY_DIR_IMAGE = "${DEPLOY_DIR}/images/xenclient-stubdomain"
STUBDOMAIN_IMAGE = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/xenclient-stubdomain-initramfs-image-xenclient-stubdomain.cpio.gz"
STUBDOMAIN_KERNEL = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/bzImage-xenclient-stubdomain.bin"
process_tmp_stubdomain_items() {
    mkdir -p ${IMAGE_ROOTFS}/usr/lib/xen/boot
    cat ${STUBDOMAIN_IMAGE} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-initramfs
    cat ${STUBDOMAIN_KERNEL} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-bzImage
}
ROOTFS_POSTPROCESS_COMMAND += "process_tmp_stubdomain_items; "

# Get rid of unneeded initscripts
remove_initscripts() {
    remove_initscript "rmnologin.sh"
    remove_initscript "finish.sh"
}
ROOTFS_POSTPROCESS_COMMAND += "remove_initscripts; "

# After ensuring that the correct number of xenstored daemon(s) are installed,
# enforce that the init script is active:
activate_xenstored_initscript() {
    update-rc.d -r ${IMAGE_ROOTFS} xenstored defaults 05
}
ROOTFS_POSTPROCESS_COMMAND += "activate_xenstored_initscript; "

# packagegroup-xenclient-dom0 provides lvm2, so have lvmetad running as lvm2
# utilities try to use it and warn in its absence.
activate_lvmetad_initscript() {
    update-rc.d -r ${IMAGE_ROOTFS} lvm2-lvmetad defaults 06
}
ROOTFS_POSTPROCESS_COMMAND += "activate_lvmetad_initscript; "

# Handle required configuration of the rootfs to store persistent files on
# encripted /config partition.
rw_config_partition() {
    # If we are using openssh but want the persistent data to be stored in the
    # encrypted config partition, replace or append SYSCONFDIR in
    # /etc/default/ssh.
    # This should only be done after read_only_rootfs_hook(s) have been done.
    if [ -d ${IMAGE_ROOTFS}${sysconfdir}/ssh ]; then
        sed -i -e '/^SYSCONFDIR=/{h;s/=.*/=\$\{SYSCONFDIR:-\/config\/etc\/ssh\}/};${x;/^$/{s//SYSCONFDIR=\$\{SYSCONFDIR:-\/config\/etc\/ssh\}/;H};x}' ${IMAGE_ROOTFS}${sysconfdir}/default/ssh
        sed -i -e 's/HostKey .*\/ssh\/ssh_host_\(.*\)key/HostKey \/config\/etc\/ssh\/ssh_host_\1key/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
        echo "HostKey /config/etc/ssh/ssh_host_dsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
        echo "HostKey /config/etc/ssh/ssh_host_rsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
        echo "HostKey /config/etc/ssh/ssh_host_ecdsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
        echo "HostKey /config/etc/ssh/ssh_host_ed25519_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
    fi
}
ROOTFS_POSTPROCESS_COMMAND += "rw_config_partition; "
