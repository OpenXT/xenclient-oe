POLICY_NAME = "xc_policy"
POLICY_DISTRO = "debian"
POLICY_UBAC = "y"
POLICY_DIRECT_INITRC = "y"
POLICY_QUIET = "n"
POLICY_MLS_CATS = "256"

# Custom name to reflect modifications.
EXTRA_OEMAKE += "PKGNAME=${POLICY_NAME}-${PV}"

do_srctree_copy() {
    cp -nr "${WORKDIR}/policy/modules" "${S}/policy"
}
addtask do_srctree_copy after do_unpack before do_patch
do_srctree_copy[doc] = "Copy layer provided policy files in work directory."
do_srctree_copy[dirs] = "${B}"

do_policy_conf() {
    for conf in modules booleans; do
        rm -f "${S}/policy/${conf}.conf"
        for e in ${WORKDIR}/policy/${conf}*.conf; do
            if [ -e "${e}" ]; then
                cat "${e}" >> "${S}/policy/${conf}.conf"
            fi
        done
    done
}
addtask do_policy_conf after do_srctree_copy before do_configure
do_policy_conf[doc] = "Generate configuration files (booleans.conf, \
modules.conf) from the configuration elements passed by the layer. \
See 'make conf' of the refpolicy for more information."
do_policy_conf[dirs] = "${B}"

do_install_append() {
    install -d ${D}${sysconfdir}/selinux
    install -m 644 ${WORKDIR}/config ${D}${sysconfdir}/selinux/config
}

sysroot_stage_all_append () {
    sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}

pkg_postinst_${PN} () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /
    fi
}

pkg_postinst_${PN}_append_xenclient-dom0 () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /config /storage
    fi
}

FILES_${PN} += " \
    ${sysconfdir}/selinux \
"
