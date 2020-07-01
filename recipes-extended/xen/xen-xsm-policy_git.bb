DESCRIPTION = "XSM Policy"
LICENSE = "GPLv2"
DEPENDS += "checkpolicy-native"

XEN_REL ?= "4.12"
PV = "${XEN_REL}+git${SRCPV}"

SRCREV = "${AUTOREV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/xsm-policy.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

S = "${WORKDIR}/git"

FILES_${PN} += "/etc/xen/refpolicy/policy/policy.24"

EXTRA_OEMAKE = " -j 1 "

do_compile(){
	oe_runmake DESTDIR=${D} BINDIR=${STAGING_BINDIR_NATIVE}
}

do_install(){
	mkdir -p ${D}/etc/xen/xenrefpolicy/users/
	touch ${D}/etc/xen/xenrefpolicy/users/system.users
	touch ${D}/etc/xen/xenrefpolicy/users/local.users
	oe_runmake DESTDIR=${D} BINDIR=${STAGING_BINDIR_NATIVE} install
}
