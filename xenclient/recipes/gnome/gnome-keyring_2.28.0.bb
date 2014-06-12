DESCRIPTION = "GNOME security credential management"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
SECTION = "x11/gnome"
DEPENDS = " libpam gconf gtk+ libtasn1 libtasn1-native libgcrypt"

PR="xc1"

inherit autotools gnome pkgconfig

EXTRA_OECONF = "--disable-gtk-doc --enable-pam --with-pam-dir=${libdir}/security/"

SRC_URI += "file://tasn.m4 file://org.gnome.keyring.service \
	file://gnome-keyring-daemon \
	file://make-peter-happy.patch;patch=1 \
	file://translations/de.po \
	file://translations/es.po \
	file://translations/fr.po \
	file://translations/ja.po \
	file://translations/zh_CN.po"
SRC_URI[archive.md5sum] = "07fa253d8506c22640d74eb4fc90a092"
SRC_URI[archive.sha256sum] = "1b3234f1feac6a619a9a61a0b5f67ab8cd89d94aeeec9a5cc2d78b81d9d8cab4"

do_configure_prepend() {
	cp ${WORKDIR}/tasn.m4 acinclude.m4
}

do_compile_prepend() {
    # Run these commands in a subshell. Otherwise changing directory affects
    # the rest of do_compile.
    (
        cd po

        # Bring the message files up to date with the source code.
        make update-po

        # Merge in the translated messages from the translation team.
        for i in de es fr ja zh_CN ; do
            mv $i.po $i.po.orig
            tr -d '\r' < ${WORKDIR}/translations/$i.po |
                msgmerge - $i.po.orig -o $i.po
        done
    )
}

do_install_append () {
	install -d ${D}${datadir}/dbus-1/services
	install -m 0644 ${WORKDIR}/org.gnome.keyring.service ${D}${datadir}/dbus-1/services

	mv ${D}${bindir}/gnome-keyring-daemon ${D}${bindir}/gnome-keyring-daemon.real
        install -m 0755 ${WORKDIR}/gnome-keyring-daemon ${D}${bindir}/gnome-keyring-daemon
}

FILES_${PN} += "${datadir}/dbus-1/services ${datadir}/gcr"
PACKAGES =+ "gnome-keyring-pam-plugin"
FILES_gnome-keyring-pam-plugin = "${libdir}/security/*.so"
FILES_${PN}-dbg += "${libdir}/gnome-keyring/*/.debug ${libdir}/security/.debug"

#do_stage() {
#        autotools_stage_all
#}
