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

selinux_policy_link () {
	SEROOT=${IMAGE_ROOTFS}/etc/selinux
	POL_TYPE=$(sed -n -e "s&SELINUXTYPE[:space:]*=[:space:]*\([0-9A-Za-z_]\+\)&\1&p" ${SEROOT}/config)
	cp ${SEROOT}/semanage.conf ${SEROOT}/semanage.conf.bak
	touch ${SEROOT}/${POL_TYPE}/contexts/files/file_contexts.local
	cat <<-EOF > ${SEROOT}/semanage.conf
module-store = direct
[setfiles]
path = ${STAGING_DIR_NATIVE}${base_sbindir_native}/setfiles
args = -q -c \$@ \$<
[end]
[sefcontext_compile]
path = ${STAGING_DIR_NATIVE}${sbindir_native}/sefcontext_compile
args = \$@
[end]
EOF
	semodule -p ${IMAGE_ROOTFS} -s ${POL_TYPE} -n -B
	mv -f ${SEROOT}/semanage.conf.bak ${SEROOT}/semanage.conf
}

selinux_set_labels () {
	POL_TYPE=$(sed -n -e "s&SELINUXTYPE[:space:]*=[:space:]*\([0-9A-Za-z_]\+\)&\1&p" ${IMAGE_ROOTFS}/etc/selinux/config)
	setfiles -r ${IMAGE_ROOTFS} ${IMAGE_ROOTFS}/etc/selinux/xc_policy/contexts/files/file_contexts ${IMAGE_ROOTFS} || exit 1;
}

inherit image

ROOTFS_POSTPROCESS_COMMAND += "selinux_policy_link ;"
# Do SELinux file labeling after all rootfs postprocessing.
IMAGE_PREPROCESS_COMMAND_append = " selinux_set_labels; "
