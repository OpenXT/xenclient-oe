DESCRIPTION = "Surface Manager XenClient"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " xen-tools xenfb2 libv4v dbus xenclient-idl xenclient-rpcgen-native libpng libsurfman libxenbackend surfman-sample libxcxenstore libedid libdmbus libpciaccess fbtap libvgaemu libpciemu"

RDEPENDS_${PN} += "fbtap"

PV = "0+git${SRCPV}"

SRCREV = "1161eeba1d4d8bb3ad09da0a2e42001472474ed1"
SRC_URI = "git://github.com/openxt/surfman.git;protocol=https \
           file://surfman.initscript \
           file://surfman.conf"

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

S = "${WORKDIR}/git/surfman"

inherit autotools
inherit xenclient
inherit update-rc.d

INITSCRIPT_NAME = "surfman"
INITSCRIPT_PARAMS = "defaults 72"

pkg_postinst_${PN} () {
    if [ ! -f $D/etc/surfman.conf ]; then
        cp --preserve=xattr $D/usr/share/xenclient/surfman.conf $D/etc/surfman.conf || exit 1
    else
        echo "$D/etc/surfman.conf already exists"
    fi
}


do_install_append() {
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/surfman.initscript ${D}/${sysconfdir}/init.d/surfman
    [ ! -d ${D}/usr/share/xenclient ] && mkdir -p ${D}/usr/share/xenclient
    install -m 0644 ${WORKDIR}/surfman.conf ${D}/usr/share/xenclient/surfman.conf
}

FILES_${PN} += "\
                /usr/share/xenclient/surfman.conf \
		screenshot \
                "
