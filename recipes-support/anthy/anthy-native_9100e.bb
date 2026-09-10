require anthy_${PV}.bb

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS = ""
PACKAGES = ""
PR = "r1"

S = "${WORKDIR}/anthy-${PV}"

inherit native
