FILESEXTRAPATHS:prepend := "${THISDIR}/files:${THISDIR}/${BPN}:${THISDIR}/${BPN}-7:"

SRC_URI += " \
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

do_install:append() {
    install -m 0755 ${UNPACKDIR}/sshargo ${D}${bindir}/sshargo
    install -m 0755 ${UNPACKDIR}/scpargo ${D}${bindir}/scpargo
    install -m 0755 ${UNPACKDIR}/sshd_check_keys_argo ${D}${libexecdir}/openssh/sshd_check_keys_argo

    install -m 0755 ${UNPACKDIR}/init_argo ${D}${sysconfdir}/init.d/sshd-argo
    sed -i -e 's,@LIBEXECDIR@,${libexecdir}/${BPN},g' ${D}${sysconfdir}/init.d/sshd-argo

    install -m 0644 ${UNPACKDIR}/sshd_config_argo ${D}${sysconfdir}/ssh/sshd_config_argo

    install -m 0644 ${UNPACKDIR}/sshd_config_argo ${D}${sysconfdir}/ssh/sshd_config_readonly_argo
    sed -i -e 's|^HostKey /etc/ssh/|HostKey /var/run/ssh/|' \
        ${D}${sysconfdir}/ssh/sshd_config_readonly_argo

    install -m 0644 ${UNPACKDIR}/volatiles.99_ssh-keygen ${D}${sysconfdir}/default/volatiles/99_ssh-keygen

    # CONFIG_IPV6 is not set in every linux-openxt.
    sed -i -e 's/^[#]AddressFamily .\+/AddressFamily inet/' \
        ${D}${sysconfdir}/ssh/sshd_config \
        ${D}${sysconfdir}/ssh/sshd_config_readonly
}

FILES:${PN}-ssh += " \
    ${bindir}/sshargo \
"
FILES:${PN}-sshd += " \
    ${sysconfdir}/init.d/sshd-argo \
    ${sysconfdir}/ssh/sshd_config_argo \
    ${sysconfdir}/ssh/sshd_config_readonly_argo \
    ${libexecdir}/openssh/sshd_check_keys_argo \
    ${sysconfdir}/default/volatiles/99_ssh-keygen \
"
FILES:${PN}-scp += " \
    ${bindir}/scpargo \
"

# Override sshd initscript with sshd-argo.
# The initial sshd initscript will be shipped with sshd-tcp-init
INITSCRIPT_NAME:${PN}-sshd = "sshd-argo"
CONFFILES:${PN}-sshd += " \
    ${sysconfdir}/ssh/sshd_config_argo \
    ${sysconfdir}/ssh/sshd_config_readonly_argo \
"

# sshd-tcp-init
PACKAGES =+ "${PN}-sshd-tcp-init"
FILES:${PN}-sshd-tcp-init = "/etc/init.d/sshd"

INITSCRIPT_PACKAGES += "${PN}-sshd-tcp-init"
INITSCRIPT_NAME:${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS:${PN}-sshd-tcp-init = "defaults 9 91"

RDEPENDS:${PN}-sshd += "libargo"
RDEPENDS:${PN}-ssh += "bash libargo"
RDEPENDS:${PN}-scp += "bash libargo"
