PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://login.selinux"

do_install_append() {
	# If shadown-native is building the destination will not exist.  Now that shadow-native is
	# a BBCLASSEXTEND this file applies to it also.  Not sure if there is a better way to 
	# handle this.
	if [ -e ${D}${sysconfdir}/pam.d ]; then
		install -m 0644 ${WORKDIR}/login.selinux ${D}${sysconfdir}/pam.d/login
	fi
}
