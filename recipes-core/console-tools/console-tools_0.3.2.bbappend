PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
           file://config \
           file://unicode-keysym-with-plus.patch \
"

#CFLAGS_append += " -Wno-attributes "

do_install_append () {
	mv ${D}${bindir}/chvt ${D}${bindir}/chvt.${PN}
	mv ${D}${bindir}/deallocvt ${D}${bindir}/deallocvt.${PN}
	mv ${D}${bindir}/openvt ${D}${bindir}/openvt.${PN}
	mv ${D}${bindir}/showkey ${D}${bindir}/showkey.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${bindir}/chvt chvt chvt.${PN} 100
	update-alternatives --install ${bindir}/deallocvt deallocvt deallocvt.${PN} 100
	update-alternatives --install ${bindir}/openvt openvt openvt.${PN} 100
	update-alternatives --install ${bindir}/showkey showkey showkey.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove chvt chvt.${PN}
	update-alternatives --remove deallocvt deallocvt.${PN}
	update-alternatives --remove openvt openvt.${PN}
	update-alternatives --remove showkey showkey.${PN}
}

