SUMMARY = "V4V Linux module."
DESCRIPTION = "V4V implements inter-domain communication on Xen virtualization \
platform relying on the hypervisor to broker all communications. Domains then \
manage their own data rings and no memory is shared between them. V4V module \
defines a stream and a datagram protocol."
HOMEPAGE = "https://github.com/OpenXT/openxt/wiki/V4V"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/v4v.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/v4v"

inherit module
inherit module-signing
