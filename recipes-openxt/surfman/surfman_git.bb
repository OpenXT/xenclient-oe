DESCRIPTION = "Surface Manager XenClient"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " xen xenfb2 libargo dbus xenclient-idl xenclient-rpcgen-native libpng libsurfman libxenbackend surfman-sample libxcxenstore libedid libdmbus libpciaccess fbtap"

RDEPENDS_${PN} += "fbtap"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/surfman.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           file://surfman.initscript \
           file://surfman.conf"

CFLAGS_append = " -Wno-unused-parameter "

S = "${WORKDIR}/git/surfman"

ASNEEDED = ""

inherit autotools xenclient update-rc.d pkgconfig

INITSCRIPT_NAME = "surfman"
INITSCRIPT_PARAMS = "start 72 5 . stop 72 0 1 2 3 4 6 ."

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
