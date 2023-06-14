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

RDEPENDS_sync-cmd += " \
    python3-core \
    python3-dbus \
    pyicbinn \
"

PV = "0+git${SRCPV}"

SRCREV = "17df6d78397f5b9af4a6e8604c351b0df202edc3"
SRC_URI = "git://github.com/OpenXT/sync-client.git;protocol=https \
           file://sync-client-daemon.initscript"

INITSCRIPT_NAME = "sync-client-daemon"
INITSCRIPT_PARAMS = "defaults 85 15"

S = "${WORKDIR}/git"

inherit distutils3
inherit update-rc.d

FILES_${PN} += "/etc/init.d/${INITSCRIPT_NAME}"

PACKAGE_BEFORE_PN = "sync-cmd"

FILES_sync-cmd = " \
       ${bindir}/sync-cmd \
       ${PYTHON_SITEPACKAGES_DIR}/pysynchronizer \
"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME}.initscript \
            ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}
