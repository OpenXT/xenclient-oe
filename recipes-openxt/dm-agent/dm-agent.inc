DESCRIPTION = "Daemon which spawns Device Models in a domain"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xen libv4v libdmbus dbus libevent libxcxenstore"

PV = "0+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/dm-agent:"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/dm-agent.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
            file://dm-agent.initscript \
           "

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools pkgconfig xenclient

do_configure_prepend(){
     export VERSION="${VERSION}"
}
