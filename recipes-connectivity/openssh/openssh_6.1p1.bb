require recipes-connectivity/openssh/openssh6.inc
DEPENDS += "libselinux"
RDEPENDS_${PN} += "libselinux"
PR = "${INC_PR}.4"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=e326045657e842541d3f35aada442507"

SRC_URI = "ftp://ftp.openbsd.org/pub/OpenBSD/OpenSSH/portable/openssh-${PV}.tar.gz \
           file://nostrip.patch \
           file://fix-parallel-make.patch \
           file://sshd_config \
	   file://sshd_config_v4v \
           file://ssh_config \
           file://init_v4v \
           file://sshv4v \
           file://scpv4v \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

SRC_URI_append_xenclient-dom0 = " file://init "
PACKAGES_prepend_xenclient-dom0 = " ${PN}-sshd-tcp-init "
FILES_${PN}-sshd-tcp-init = "/etc/init.d/sshd"

SRC_URI[md5sum] = "3345cbf4efe90ffb06a78670ab2d05d5"
SRC_URI[sha256sum] = "d1c157f6c0852e90c191cc7c9018a583b51e3db4035489cb262639d337a1c411"

# Disabled to allow ssh users to do anything
EXTRA_OECONF += "--with-selinux"

FILES_${PN}-ssh += " /usr/bin/sshv4v /etc/ssh/sshd_config_v4v "
RDEPENDS_${PN}-ssh += " bash"
FILES_${PN}-scp += " /usr/bin/scpv4v "
RDEPENDS_${PN}-scp += " bash"

do_install_append() {
	install -m 0755 -d ${D}/usr/bin
	install -m 0755 ${WORKDIR}/sshv4v ${D}/usr/bin/sshv4v
	install -m 0755 ${WORKDIR}/scpv4v ${D}/usr/bin/scpv4v
	install -m 0755 -d ${D}/etc/ssh
	install -m 0644 ${WORKDIR}/sshd_config_v4v ${D}/etc/ssh/sshd_config_v4v
}

pkg_postinst_${PN}-sshd_append () {
    ssh-keygen -q -f $D/etc/ssh/ssh_host_rsa_key_v4v -N '' -t rsa
    ssh-keygen -q -f $D/etc/ssh/ssh_host_dsa_key_v4v -N '' -t dsa
}

do_install_append_xenclient-dom0() {
	install -m 0755 ${WORKDIR}/init ${D}/etc/init.d/sshd
}

INITSCRIPT_PACKAGES_append_xenclient-dom0 = " ${PN}-sshd-tcp-init "
INITSCRIPT_NAME_${PN}-sshd-tcp-init = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd-tcp-init = "defaults 9"
