DESCRIPTION = "XenClient Synchronizer client"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

# FIXME: Add sync-client-daemon to bootage.conf when adding this package
# to xenclient-dom0-image recipe.

# FIXME: xen-tools-xenstore-utils can be removed when xenmgr supports disks
# with icbinn paths.

RDEPENDS_${PN} += "python \
                   python-argparse \
                   python-daemon \
                   python-dbus \
                   python-fcntl \
                   python-io \
                   python-json \
                   python-lang \
                   python-logging \
                   python-netclient \
                   python-subprocess \
                   python-syslog \
                   pyicbinn \
                   curl \
                   xen-tools-xenstore-utils"

SRC_URI = "${OPENXT_GIT_MIRROR}/sync-client.git;protocol=git;tag=${OPENXT_TAG} \
           file://sync-client-daemon.initscript"

INITSCRIPT_NAME = "sync-client-daemon"
INITSCRIPT_PARAMS = "defaults 85"

S = "${WORKDIR}/git"

inherit distutils
inherit xenclient
inherit update-rc.d

FILES_${PN} += "/etc/init.d/${INITSCRIPT_NAME}"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME}.initscript \
            ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}
