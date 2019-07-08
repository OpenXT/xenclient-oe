PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://mountearly.sh \
    file://udev-volatiles.sh \
    file://finish.sh \
    file://volatiles \
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

	install -m 0644    ${WORKDIR}/functions		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/bootmisc.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/checkroot.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/halt		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/hostname.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/mountall.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/reboot		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/rmnologin.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/sendsigs		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/single		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/urandom		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/populate-volatile.sh ${D}${sysconfdir}/init.d
	install -m 0644    ${WORKDIR}/volatiles		${D}${sysconfdir}/default/volatiles/00_core
	install -m 0755    ${WORKDIR}/finish.sh		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/mountearly.sh	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/udev-volatiles.sh	${D}${sysconfdir}/init.d

#
# Install device dependent scripts
#
	install -m 0755 ${WORKDIR}/umountfs	${D}${sysconfdir}/init.d/umountfs

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

pkg_postinst_${PN}_append() {
    if [ -n "$D" ]; then
        $D/etc/init.d/populate-volatile.sh update
    fi
}
