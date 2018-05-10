SUMMARY = "V4V Linux module headers."
DESCRIPTION = "V4V UAPI available to user-land programs to implement V4V \
communications."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/v4v.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/v4v"

inherit allarch

do_install() {
    oe_runmake INSTALL_HDR_PATH=${D}${prefix} headers_install
}

# Skip build steps.
do_compile[noexec] = "1"
do_configure[noexec] = "1"
