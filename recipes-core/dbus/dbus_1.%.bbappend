DEPENDS_${PN} += " \
    libselinux \
    libv4v \
    xen \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://0001-Make-the-default-DBus-reply-timeout-configurable.patch \
    file://add-domid-authentication.patch \
    file://system.conf \
    file://v4v.patch \
"

do_install_append() {
	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}/etc/dbus-1
	install -m 0644 ${WORKDIR}/system.conf ${D}/etc/dbus-1
}
