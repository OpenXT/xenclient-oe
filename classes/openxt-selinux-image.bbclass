DEPENDS += "policycoreutils-native attr-native"
IMAGE_INSTALL += " \
    libselinux-bin \
    policycoreutils-loadpolicy \
    policycoreutils-newrole \
    policycoreutils-runinit \
    policycoreutils-semodule \
    policycoreutils-sestatus \
    policycoreutils-setfiles \
    refpolicy-mcs \
"

#inherit selinux-image
# WORK-AROUND:
# It seems there is a bug in setfiles that makes the binary not do anything
# when using IMAGE_ROOTFS instead of a shorter relative path. This should get
# attention.
selinux_set_labels() {
    POL_TYPE=$(sed -n -e "s&^SELINUXTYPE[[:space:]]*=[[:space:]]*\([0-9A-Za-z_]\+\)&\1&p" ${IMAGE_ROOTFS}/${sysconfdir}/selinux/config)
    pushd "${IMAGE_ROOTFS}/.." > /dev/null
    if ! setfiles -v -r rootfs rootfs${sysconfdir}/selinux/${POL_TYPE}/contexts/files/file_contexts rootfs/
    then
        bbwarn "Unable to set filesystem context, setfiles / restorecon must be run on the live image."
        touch  rootfs/.autorelabel
        popd > /dev/null
        exit 0
    fi
    popd > /dev/null
}

DEPENDS += " \
    policycoreutils-native \
"

IMAGE_PREPROCESS_COMMAND += "selinux_set_labels; "

inherit core-image
