SRCREV = "${AUTOREV}"
XEN_REL = "4.18"
LIC_FILES_CHKSUM = "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

require xen-common.inc
require xen-openxt.inc

DEFAULT_PREFERENCE = "1"
