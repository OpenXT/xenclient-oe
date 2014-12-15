PRINC = "2"
# add SE Linux dependency, so selinux is detected and pam selinux module is build
# unfortunately there is no way to enforce failure when libselinux is not present
DEPENDS += "libselinux"

PAM_EXTRAPATH := "${THISDIR}/${PN}"
FILESPATH .= ":${PAM_EXTRAPATH}"
SRC_URI += "file://etc-config-passwd.patch;patch=1"
EXTRA_OECONF += "--disable-nis"

RDEPENDS_${PN}-runtime += " pam-plugin-selinux"
