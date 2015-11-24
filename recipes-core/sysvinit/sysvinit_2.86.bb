DESCRIPTION = "System-V like init."
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=10a994bcb2e5b17959a7f02c5693c9d2"
HOMEPAGE = "http://freshmeat.net/projects/sysvinit/"
PR = "r61"

# USE_VT and SERIAL_CONSOLE are generally defined by the MACHINE .conf.
# Set PACKAGE_ARCH appropriately.
PACKAGE_ARCH_${PN}-inittab = "${MACHINE_ARCH}"

DEPENDS += "libselinux"
RDEPENDS_${PN} = "${PN}-inittab"

PACKAGES =+ "bootlogd ${PN}-inittab"
FILES_bootlogd = "/etc/init.d/bootlogd /etc/init.d/stop-bootlogd /etc/rc?.d/S*bootlogd /sbin/bootlogd"
FILES_${PN}-inittab = "${sysconfdir}/inittab"
CONFFILES_${PN}-inittab = "${sysconfdir}/inittab"

USE_VT ?= "1"
SYSVINIT_ENABLED_GETTYS ?= "1"

SRC_URI = "ftp://ftp.cistron.nl/pub/people/miquels/sysvinit/sysvinit-${PV}.tar.gz \
           file://dont-use-an-illegal-errno.patch;patch=1 \
           file://enable_selinux.patch;patch=1 \
           file://install.patch;patch=1 \
           file://need \
           file://provide \
           file://inittab \
           file://rcS-default \
           file://rc \
           file://rcS \
           file://bootlogd.init"
SRC_URI[md5sum] = "7d5d61c026122ab791ac04c8a84db967"
SRC_URI[sha256sum] = "035f98fae17d9cff002993c564ccc83dc4ed136127172caeff872b6abdb679d8"

S = "${WORKDIR}/sysvinit-${PV}"
B = "${S}/src"

ASNEEDED = ""

inherit update-alternatives

#ALTERNATIVE_NAME = "init"
#ALTERNATIVE_LINK = "${base_sbindir}/init"
#ALTERNATIVE_PATH = "${base_sbindir}/init.sysvinit"
#ALTERNATIVE_PRIORITY = "60"

ALTERNATIVE_${PN} = "init halt reboot runlevel shutdown poweroff"

ALTERNATIVE_PRIORITY = "200"

ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_PRIORITY[init] = "50"

ALTERNATIVE_LINK_NAME[halt] = "${base_sbindir}/halt"
ALTERNATIVE_LINK_NAME[reboot] = "${base_sbindir}/reboot"
ALTERNATIVE_LINK_NAME[runlevel] = "${base_sbindir}/runlevel"
ALTERNATIVE_LINK_NAME[shutdown] = "${base_sbindir}/shutdown"
ALTERNATIVE_LINK_NAME[poweroff] = "${base_sbindir}/poweroff"

PACKAGES =+ "sysvinit-utils sysvinit-pidof sysvinit-sulogin"
FILES_${PN} += "${base_sbindir}/* ${base_bindir}/*"
FILES_sysvinit-pidof = "${base_bindir}/pidof.sysvinit"
FILES_sysvinit-sulogin = "${base_sbindir}/sulogin"
FILES_sysvinit-utils = "${bindir}/last.${PN} ${bindir}/mesg.${PN} ${bindir}/wall.${PN} ${base_sbindir}/shutdown.${PN} ${bindir}/lastb ${bindir}/utmpdump ${base_sbindir}/runlevel"
RRECOMMENDS_${PN} = "sysvinit-utils"
RRECOMMENDS_${PN}_micro = ""

CFLAGS_prepend = "-D_GNU_SOURCE "
LDFLAGS_append = " -lselinux"
export LCRYPT = "-lcrypt"
EXTRA_OEMAKE += "'INSTALL=install' \
		 'bindir=${base_bindir}' \
		 'sbindir=${base_sbindir}' \
		 'usrbindir=${bindir}' \
		 'usrsbindir=${sbindir}' \
		 'includedir=${includedir}' \
		 'mandir=${mandir}' \
		 DISTRO='' \
		 WITH_SELINUX='yes'"

do_install () {
	oe_runmake 'ROOT=${D}' install
	install -d ${D}${sysconfdir}
	install -d ${D}${sysconfdir}/default
	install	-d ${D}${sysconfdir}/init.d
	install -m 0644 ${WORKDIR}/inittab ${D}${sysconfdir}/inittab
	if [ ! -z "${SERIAL_CONSOLE}" ]; then
		echo "S:2345:respawn:${base_sbindir}/getty ${SERIAL_CONSOLE}" >> ${D}${sysconfdir}/inittab
	fi
	if [ "${USE_VT}" = "1" ]; then
		cat <<EOF >>${D}${sysconfdir}/inittab
# ${base_sbindir}/getty invocations for the runlevels.
#
# The "id" field MUST be the same as the last
# characters of the device (after "tty").
#
# Format:
#  <id>:<runlevels>:<action>:<process>
#

EOF

		for n in ${SYSVINIT_ENABLED_GETTYS}
		do
			echo "$n:2345:respawn:${base_sbindir}/getty 38400 tty$n" >> ${D}${sysconfdir}/inittab
		done
		echo "" >> ${D}${sysconfdir}/inittab
	fi
	install -m 0644    ${WORKDIR}/rcS-default	${D}${sysconfdir}/default/rcS
	install -m 0755    ${WORKDIR}/rc		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/rcS		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/bootlogd.init     ${D}${sysconfdir}/init.d/bootlogd
	ln -sf bootlogd ${D}${sysconfdir}/init.d/stop-bootlogd
	install -d ${D}${sysconfdir}/rcS.d
	ln -sf ../init.d/bootlogd ${D}${sysconfdir}/rcS.d/S07bootlogd
	for level in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc$level.d
		ln -sf ../init.d/stop-bootlogd ${D}${sysconfdir}/rc$level.d/S99stop-bootlogd
	done
	mv                 ${D}${base_sbindir}/init               ${D}${base_sbindir}/init.${PN}
	mv ${D}${base_bindir}/pidof ${D}${base_bindir}/pidof.${PN}
	mv ${D}${base_sbindir}/halt ${D}${base_sbindir}/halt.${PN}
	mv ${D}${base_sbindir}/reboot ${D}${base_sbindir}/reboot.${PN}
	mv ${D}${base_sbindir}/shutdown ${D}${base_sbindir}/shutdown.${PN}
	mv ${D}${base_sbindir}/poweroff ${D}${base_sbindir}/poweroff.${PN}	
	mv ${D}${bindir}/last ${D}${bindir}/last.${PN}
	mv ${D}${bindir}/mesg ${D}${bindir}/mesg.${PN}
	mv ${D}${bindir}/wall ${D}${bindir}/wall.${PN}
}

pkg_postinst_${PN} () {
#!/bin/sh
update-alternatives --install ${base_sbindir}/halt halt halt.${PN} 200
update-alternatives --install ${base_sbindir}/reboot reboot reboot.${PN} 200
update-alternatives --install ${base_sbindir}/poweroff poweroff poweroff.${PN} 200
}

pkg_postinst_sysvinit-utils () {
#!/bin/sh
update-alternatives --install ${bindir}/last last last.${PN} 200
update-alternatives --install ${bindir}/mesg mesg mesg.${PN} 200
update-alternatives --install ${bindir}/wall wall wall.${PN} 200
update-alternatives --install ${base_sbindir}/shutdown shutdown shutdown.${PN} 200
}

pkg_prerm_${PN} () {
#!/bin/sh
update-alternatives --remove halt halt.${PN}
update-alternatives --remove reboot reboot.${PN}
}

pkg_prerm_sysvinit-utils () {
#!/bin/sh
update-alternatives --remove last last.${PN}
update-alternatives --remove mesg mesg.${PN}
update-alternatives --remove wall wall.${PN}
update-alternatives --remove shutdown shutdown.${PN}
}

pkg_postinst_sysvinit-pidof () {
#!/bin/sh
update-alternatives --install ${base_bindir}/pidof pidof pidof.${PN} 200
}

pkg_prerm_sysvinit-pidof () {
#!/bin/sh
update-alternatives --remove pidof pidof.${PN}
}
