LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://README;md5=54cfc88015d3ce83f7156e63c6bb1738 \
                    file://wpa_supplicant/wpa_supplicant.c;beginline=1;endline=17;md5=acdc5a4b0d6345f21f136eace747260e"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "openssl"
 
PR .= ".1"

SRC_URI += " \
            file://dbus-scan-results.patch;patch=1 \
            file://libnl3_2.patch \
"

S = "${WORKDIR}/wpa_supplicant-${PV}"

CFLAGS_append += " -Wno-unused-but-set-variable "

do_configure () {
    install -m 0755 ${S}/wpa_supplicant/defconfig ${S}/wpa_supplicant/.config
    echo "CONFIG_CTRL_IFACE_DBUS=y" >> ${S}/wpa_supplicant/.config
    echo "CONFIG_CTRL_IFACE_DBUS_NEW=y" >> ${S}/wpa_supplicant/.config 
    echo "CONFIG_TLS=openssl" >> ${S}/wpa_supplicant/.config
    echo "CONFIG_DRIVER_NL80211=y" >> ${S}/wpa_supplicant/.config
    echo "CONFIG_LIBNL32=y" >> ${S}/wpa_supplicant/.config
    echo "CONFIG_BGSCAN=y" >> ${S}/wpa_supplicant/.config
    echo "CONFIG_BGSCAN_SIMPLE=y" >> ${S}/wpa_supplicant/.config
    echo "CFLAGS += -I${STAGING_INCDIR}/libnl3" >> ${S}/wpa_supplicant/.config
}

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS
	sed -e "s:CFLAGS\ =.*:& \$(EXTRA_CFLAGS):g" -i ${S}/src/lib.rules
	oe_runmake -C wpa_supplicant
}

do_install () {
	install -d ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_supplicant ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_passphrase ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_cli        ${D}${sbindir}

	install -d ${D}${docdir}/wpa_supplicant
	install -m 644 wpa_supplicant/README ${WORKDIR}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

	install -d ${D}${sysconfdir}/default
	install -m 600 ${WORKDIR}/defaults-sane ${D}${sysconfdir}/default/wpa
	install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf

	install -d ${D}${sysconfdir}/network/if-pre-up.d/
	install -d ${D}${sysconfdir}/network/if-post-down.d/
	install -d ${D}${sysconfdir}/network/if-down.d/
	install -m 644 ${WORKDIR}/wpa_supplicant.conf ${D}${sysconfdir}
	install -m 755 ${WORKDIR}/wpa-supplicant.sh ${D}${sysconfdir}/network/if-pre-up.d/wpa-supplicant
	cd ${D}${sysconfdir}/network/ && \
	ln -sf ../if-pre-up.d/wpa-supplicant if-post-down.d/wpa-supplicant

	install -d ${D}/${sysconfdir}/dbus-1/system.d
	install -m 644 ${S}/wpa_supplicant/dbus/dbus-wpa_supplicant.conf ${D}/${sysconfdir}/dbus-1/system.d
	install -d ${D}/${datadir}/dbus-1/system-services
	install -m 644 ${S}/wpa_supplicant/dbus/*.service ${D}/${datadir}/dbus-1/system-services
	sed -i -e s:${base_sbindir}:${sbindir}:g ${D}/${datadir}/dbus-1/system-services/*.service

	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_wpa_supplicant ${D}/etc/default/volatiles
}
