FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/${BPN}:${THISDIR}/${BPN}-7:"

SRC_URI += " \
    file://fix-parallel-make.patch \
    file://nostrip.patch \
    file://sshd_config \
    file://sshd_config_argo \
    file://ssh_config \
    file://init_argo \
    file://sshargo \
    file://scpargo \
    file://sshd_check_keys_argo \
    file://volatiles.99_ssh-keygen \
    file://init \
"

do_install_append() {
    install -m 0755 ${WORKDIR}/sshargo ${D}${bindir}/sshargo
    install -m 0755 ${WORKDIR}/scpargo ${D}${bindir}/scpargo
    install -m 0755 ${WORKDIR}/sshd_check_keys_argo ${D}${libexecdir}/openssh/sshd_check_keys_argo

    install -m 0755 ${WORKDIR}/init_argo ${D}${sysconfdir}/init.d/sshd-argo
    sed -i -e 's,@LIBEXECDIR@,${libexecdir}/${BPN},g' ${D}${sysconfdir}/init.d/sshd-argo

    install -m 0644 ${WORKDIR}/sshd_config_argo ${D}${sysconfdir}/ssh/sshd_config_argo

    install -m 0644 ${WORKDIR}/sshd_config_argo ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    sed -i -e '/HostKey/d' ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    echo "HostKey /var/run/ssh/ssh_host_rsa_key_argo" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    echo "HostKey /var/run/ssh/ssh_host_dsa_key_argo" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    echo "HostKey /var/run/ssh/ssh_host_ecdsa_key_argo" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    echo "HostKey /var/run/ssh/ssh_host_ed25519_key_argo" >> ${D}${sysconfdir}/ssh/sshd_config_readonly_argo

    install -m 0644 ${WORKDIR}/volatiles.99_ssh-keygen ${D}${sysconfdir}/default/volatiles/99_ssh-keygen

    # CONFIG_IPV6 is not set in every linux-openxt.
    sed -i -e 's/^[#]AddressFamily .\+/AddressFamily inet/' \
        ${D}${sysconfdir}/ssh/sshd_config \
        ${D}${sysconfdir}/ssh/sshd_config_readonly
}

FILES_${PN}-ssh += " \
    ${bindir}/sshargo \
"
FILES_${PN}-sshd += " \
    ${sysconfdir}/init.d/sshd-argo \
    ${sysconfdir}/ssh/sshd_config_argo \
    ${sysconfdir}/ssh/sshd_config_readonly_argo \
    ${libexecdir}/openssh/sshd_check_keys_argo \
    ${sysconfdir}/default/volatiles/99_ssh-keygen \
"
FILES_${PN}-scp += " \
    ${bindir}/scpargo \
"

# Override sshd initscript with sshd-argo.
# The initial sshd initscript will be shipped with sshd-tcp-init
INITSCRIPT_NAME_${PN}-sshd = "sshd-argo"
CONFFILES_${PN}-sshd += " \
    ${sysconfdir}/ssh/sshd_config_argo \
    ${sysconfdir}/ssh/sshd_config_readonly_argo \
"

# sshd-tcp-init
PACKAGES =+ "${PN}-sshd-tcp-init"
FILES_${PN}-sshd-tcp-init = "/etc/init.d/sshd"

INITSCRIPT_PACKAGES += "${PN}-sshd-tcp-init"
INITSCRIPT_NAME_${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd-tcp-init = "defaults 9"

RDEPENDS_${PN}-sshd += "libargo"
RDEPENDS_${PN}-ssh += "bash libargo"
RDEPENDS_${PN}-scp += "bash libargo"
