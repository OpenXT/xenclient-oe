DESCRIPTION = "XenClient Synchronizer client"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

# FIXME: xen-tools-xenstore can be removed when xenmgr supports disks
# with icbinn paths.

RDEPENDS_${PN} += "python3-core \
                   python3-daemon \
                   python3-dbus \
                   python3-fcntl \
                   python3-io \
                   python3-json \
                   python3-logging \
                   python3-netclient \
                   python3-pickle \
                   python3-syslog \
                   pyicbinn \
                   curl \
                   xen-tools-xenstore"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/sync-client.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           file://sync-client-daemon.initscript"

INITSCRIPT_NAME = "sync-client-daemon"
INITSCRIPT_PARAMS = "defaults 85"

S = "${WORKDIR}/git"

inherit distutils3
inherit update-rc.d

FILES_${PN} += "/etc/init.d/${INITSCRIPT_NAME}"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME}.initscript \
            ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}
