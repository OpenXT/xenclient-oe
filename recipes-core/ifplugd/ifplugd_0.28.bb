# ifplugd is included in busybox 1.15 onwards, so this recipe can be removed
# (and ifplug.conf moved to another recipe) when we switch to a more recent
# version of busybox.

DESCRIPTION = "ifplugd is a Linux daemon which will automatically configure \
your ethernet device when a cable is plugged in and automatically unconfigure \
it if the cable is pulled."
HOMEPAGE = "http://0pointer.de/lennart/projects/ifplugd/"
SECTION = "network"
DEPENDS = "libdaemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r1"


MIRRORS_append += " \
    http://.*/.*      https://openxt.ainfosec.com/mirror/ \
"
SRC_URI = "${OPENXT_MIRROR}/openxt/ifplugd-0.28.tar.gz \
           file://kernel-types.patch \
           file://nobash.patch \
           file://ifplugd.conf"

inherit autotools update-rc.d pkgconfig

EXTRA_OECONF = "--disable-lynx"

INITSCRIPT_NAME = "ifplugd"
INITSCRIPT_PARAMS = "defaults 30"

CONFFILES_${PN} = "${sysconfdir}/ifplugd/ifplugd.conf"

SRC_URI[md5sum] = "df6f4bab52f46ffd6eb1f5912d4ccee3"
SRC_URI[sha256sum] = "474754ac4ab32d738cbf2a4a3e87ee0a2c71b9048a38bdcd7df1e4f9fd6541f0"

do_install_append() {
    install -m 0644 ${WORKDIR}/ifplugd.conf ${D}/etc/ifplugd/
}
