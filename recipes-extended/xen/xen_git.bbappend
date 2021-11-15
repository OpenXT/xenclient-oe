SRCREV = "${AUTOREV}"
XEN_REL = "4.14"
LIC_FILES_CHKSUM = "file://COPYING;md5=419739e325a50f3d7b4501338e44a4e5"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

require xen-common.inc
require xen-openxt.inc

DEFAULT_PREFERENCE = "1"
