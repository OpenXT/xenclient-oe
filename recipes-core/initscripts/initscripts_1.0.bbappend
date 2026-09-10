PR .= ".1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://functions-selinux \
    file://functions-dbus \
    file://mountearly.sh \
    file://udev-volatiles.sh \
    file://finish.sh \
    file://volatiles \
"

SRC_URI:append:openxt-installer += " \
    file://mountefi.sh \
"

# Override to reduce the number of scripts installed
do_install () {
#
# Create directories and install device independent scripts
#
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -d ${D}${sysconfdir}/rc0.d
	install -d ${D}${sysconfdir}/rc1.d
	install -d ${D}${sysconfdir}/rc2.d
	install -d ${D}${sysconfdir}/rc3.d
	install -d ${D}${sysconfdir}/rc4.d
	install -d ${D}${sysconfdir}/rc5.d
	install -d ${D}${sysconfdir}/rc6.d
	install -d ${D}${sysconfdir}/default
	install -d ${D}${sysconfdir}/default/volatiles

	install -m 0644    ${UNPACKDIR}/functions		${D}${sysconfdir}/init.d
	install -m 0644    ${UNPACKDIR}/functions-selinux	${D}${sysconfdir}/init.d
	install -m 0644    ${UNPACKDIR}/functions-dbus	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/bootmisc.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/checkroot.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/halt		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/hostname.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/mountall.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/reboot		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/rmnologin.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/sendsigs		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/single		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/urandom		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/populate-volatile.sh ${D}${sysconfdir}/init.d
	install -m 0644    ${UNPACKDIR}/volatiles		${D}${sysconfdir}/default/volatiles/00_core
	install -m 0755    ${UNPACKDIR}/finish.sh		${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/mountearly.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${UNPACKDIR}/udev-volatiles.sh	${D}${sysconfdir}/init.d

	if ${@bb.utils.contains('DISTRO_FEATURES','selinux','true','false',d)}; then
		install -d ${D}/${base_sbindir}
		install -m 0755 ${UNPACKDIR}/sushell ${D}/${base_sbindir}
	fi

#
# Install device dependent scripts
#
	install -m 0755 ${UNPACKDIR}/umountfs	${D}${sysconfdir}/init.d/umountfs

#
# Create runlevel links
#
	update-rc.d -r ${D} rmnologin.sh start 99 2 3 4 5 .
	update-rc.d -r ${D} sendsigs start 20 0 6 .
	update-rc.d -r ${D} urandom start 33 S 0 6 .
	update-rc.d -r ${D} umountfs start 40 0 6 .
	update-rc.d -r ${D} reboot start 90 6 .
	update-rc.d -r ${D} halt start 90 0 .
	update-rc.d -r ${D} checkroot.sh start 06 S .
	update-rc.d -r ${D} mountall.sh start 35 S .
	update-rc.d -r ${D} hostname.sh start 39 S .
	update-rc.d -r ${D} bootmisc.sh start 55 S .
	update-rc.d -r ${D} populate-volatile.sh start 37 S .
	update-rc.d -r ${D} finish.sh start 99 S .
	update-rc.d -r ${D} mountearly.sh start 01 S .
	update-rc.d -r ${D} udev-volatiles.sh start 03 S .
}

do_install:append:openxt-installer() {
	install -m 0755    ${UNPACKDIR}/mountefi.sh	${D}${sysconfdir}/init.d
	update-rc.d -r ${D} mountefi.sh start 36 S .
}

pkg_postinst:${PN}:append() {
    if [ -n "$D" ]; then
        $D/etc/init.d/populate-volatile.sh update
    fi
}
