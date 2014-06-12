
SHADOW_EXTRAPATH := "${THISDIR}/files"
FILESPATH .= ":${SHADOW_EXTRAPATH}"

SRC_URI += "file://login.selinux"

do_install_append() {
	install -m 0644 ${WORKDIR}/login.selinux ${D}${sysconfdir}/pam.d/login
}
