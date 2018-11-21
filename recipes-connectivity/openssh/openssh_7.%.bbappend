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
    file://sshd_check_keys_v4v \
    file://volatiles.99_ssh-keygen \
    file://init \
"

do_install_append() {
    install -m 0755 ${WORKDIR}/sshv4v ${D}${bindir}/sshv4v
    install -m 0755 ${WORKDIR}/scpv4v ${D}${bindir}/scpv4v
    install -m 0755 ${WORKDIR}/sshd_check_keys_v4v ${D}${libexecdir}/openssh/sshd_check_keys_v4v

    install -m 0755 ${WORKDIR}/init_v4v ${D}${sysconfdir}/init.d/sshd-v4v
    sed -i -e 's,@LIBEXECDIR@,${libexecdir}/${BPN},g' ${D}${sysconfdir}/init.d/sshd-v4v

    install -m 0644 ${WORKDIR}/sshd_config_v4v ${D}${sysconfdir}/ssh/sshd_config_v4v

    install -m 0644 ${WORKDIR}/sshd_config_v4v ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v
    sed -i -e '/HostKey/d' ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v
    echo "HostKey /var/run/ssh/ssh_host_rsa_key_v4v" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v
    echo "HostKey /var/run/ssh/ssh_host_dsa_key_v4v" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v
    echo "HostKey /var/run/ssh/ssh_host_ecdsa_key_v4v" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v
    echo "HostKey /var/run/ssh/ssh_host_ed25519_key_v4v" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_v4v

    install -m 0644 ${WORKDIR}/volatiles.99_ssh-keygen ${D}${sysconfdir}/default/volatiles/99_ssh-keygen

    # CONFIG_IPV6 is not set in every linux-openxt.
    sed -i -e 's/^[#]AddressFamily .\+/AddressFamily inet/' \
        ${D}${sysconfdir}/ssh/sshd_config \
        ${D}${sysconfdir}/ssh/sshd_config_readonly
}

FILES_${PN}-ssh += " \
    ${bindir}/sshv4v \
"
FILES_${PN}-sshd += " \
    ${sysconfdir}/init.d/sshd-v4v \
    ${sysconfdir}/ssh/sshd_config_v4v \
    ${sysconfdir}/ssh/sshd_config_readonly_v4v \
    ${libexecdir}/openssh/sshd_check_keys_v4v \
    ${sysconfdir}/default/volatiles/99_ssh-keygen \
"
FILES_${PN}-scp += " \
    ${bindir}/scpv4v \
"

# Override sshd initscript with sshd-v4v.
# The initial sshd initscript will be shipped with sshd-tcp-init
INITSCRIPT_NAME_${PN}-sshd = "sshd-v4v"
CONFFILES_${PN}-sshd += " \
    ${sysconfdir}/ssh/sshd_config_v4v \
    ${sysconfdir}/ssh/sshd_config_readonly_v4v \
"

# sshd-tcp-init
PACKAGES =+ "${PN}-sshd-tcp-init"
FILES_${PN}-sshd-tcp-init = "/etc/init.d/sshd"

INITSCRIPT_PACKAGES += "${PN}-sshd-tcp-init"
INITSCRIPT_NAME_${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd-tcp-init = "defaults 9"

RDEPENDS_${PN}-sshd += "libv4v"
RDEPENDS_${PN}-ssh += "bash libv4v"
RDEPENDS_${PN}-scp += "bash libv4v"
