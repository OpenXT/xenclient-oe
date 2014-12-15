FILESEXTRAPATHS := "${THISDIR}/${PN}"
SRC_URI += "file://pseudo-xattr-support.patch"
DEPENDS += "attr"
