FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/${BPN}:${THISDIR}/${BPN}-7:"

SRC_URI += " \
    file://fix-parallel-make.patch \
    file://nostrip.patch \
    file://sshd_config \
    file://sshd_config_v4v \
    file://ssh_config \
    file://init_v4v \
    file://sshv4v \
    file://scpv4v \
    "
FILES_${PN}-ssh += " \
    /usr/bin/sshv4v \
    /etc/ssh/sshd_config_v4v \
    "
FILES_${PN}-sshd += " ${sysconfdir}/init.d/sshd_v4v"
RDEPENDS_${PN}-ssh += " bash"
FILES_${PN}-scp += " /usr/bin/scpv4v"
RDEPENDS_${PN}-scp += " bash"

# Disabled to allow ssh users to do anything
EXTRA_OECONF += "--with-selinux"

do_install_append() {
    install -m 0755 -d ${D}/usr/bin
    install -m 0755 ${WORKDIR}/sshv4v ${D}/usr/bin/sshv4v
    install -m 0755 ${WORKDIR}/scpv4v ${D}/usr/bin/scpv4v
    install -m 0755 -d ${D}/etc/ssh

    if [ -f ${WORKDIR}/init_v4v ]; then
        install -m 0755 ${WORKDIR}/init_v4v ${D}${sysconfdir}/init.d/sshd_v4v
    fi
    install -m 0644 ${WORKDIR}/sshd_config_v4v ${D}/etc/ssh/sshd_config_v4v
}

pkg_postinst_${PN}-sshd_append () {
    ssh-keygen -q -f $D/etc/ssh/ssh_host_rsa_key_v4v -N '' -t rsa
    ssh-keygen -q -f $D/etc/ssh/ssh_host_dsa_key_v4v -N '' -t dsa
}

# Override sshd initscript with sshd_v4v.
# The initial sshd initscript will be shipped with sshd-tcp-init
INITSCRIPT_NAME_${PN}-sshd = "sshd_v4v"
# From openssh main recipe:
#INITSCRIPT_PARAMS_${PN}-sshd = "defaults 9"

# sshd-tcp-init
SRC_URI_append_xenclient-dom0 = " file://init "
PACKAGES_prepend_xenclient-dom0 = " ${PN}-sshd-tcp-init "
FILES_${PN}-sshd-tcp-init = "/etc/init.d/sshd"

INITSCRIPT_PACKAGES_append_xenclient-dom0 = " ${PN}-sshd-tcp-init "
INITSCRIPT_NAME_${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd-tcp-init = "defaults 9"

SRC_URI_append_openxt-installer = " file://init "
PACKAGES_prepend_openxt-installer = " ${PN}-sshd-tcp-init "
FILES_${PN}-sshd-tcp-init = "/etc/init.d/sshd"

INITSCRIPT_PACKAGES_append_openxt-installer = " ${PN}-sshd-tcp-init "
INITSCRIPT_NAME_${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd-tcp-init = "defaults 9"

do_install_append_xenclient-dom0() {
    install -m 0755 ${WORKDIR}/init ${D}/etc/init.d/sshd
}

