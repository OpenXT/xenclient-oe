SUMMARY = "Argo Linux module headers."
DESCRIPTION = "Argo UAPI available to user-land programs to implement Argo \
communications."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require argo.inc

S = "${WORKDIR}/git/argo-linux"

inherit multilib-allarch

do_install() {
    oe_runmake INSTALL_HDR_PATH=${D}${prefix} headers_install
}

# Skip build steps.
do_compile[noexec] = "1"
do_configure[noexec] = "1"
