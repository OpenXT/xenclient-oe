DESCRIPTION = "Configuration files for online package repositories aka feeds"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

#RRECOMMENDS_${PN} += "opkg-nogpg-nocurl"

PV = "${XENCLIENT_BUILD}"
PR = "r15"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile() {
	mkdir -p ${S}/${sysconfdir}/opkg

	rm ${S}/${sysconfdir}/opkg/arch.conf || true
	ipkgarchs="${PACKAGE_ARCHS}"
	priority=1
	for arch in $ipkgarchs; do 
		echo "arch $arch $priority" >> ${S}/${sysconfdir}/opkg/arch.conf
		priority=$(expr $priority + 5)
	done

        for i in all ${MACHINE_ARCH} ${TUNE_PKGARCH}; do
            echo "src/gz $i ${XENCLIENT_PACKAGE_FEED_URI}/$i" > ${S}/${sysconfdir}/opkg/$i-feed.conf
        done
}


do_install () {
	install -d ${D}${sysconfdir}/opkg
	install -m 0644  ${S}/${sysconfdir}/opkg/* ${D}${sysconfdir}/opkg/
}

FILES_${PN} = " ${sysconfdir}/opkg/${MACHINE_ARCH}-feed.conf \
					${sysconfdir}/opkg/${TUNE_PKGARCH}-feed.conf \
					${sysconfdir}/opkg/all-feed.conf \
					${sysconfdir}/opkg/arch.conf \
					"

CONFFILES_${PN} += " ${sysconfdir}/opkg/${MACHINE_ARCH}-feed.conf \
					${sysconfdir}/opkg/${TUNE_PKGARCH}-feed.conf \
					${sysconfdir}/opkg/all-feed.conf \
				    ${sysconfdir}/opkg/arch.conf \
					"
