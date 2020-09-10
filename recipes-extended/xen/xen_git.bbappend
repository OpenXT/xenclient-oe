SRCREV = "${AUTOREV}"
XEN_REL = "4.12"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbb4b1bdc2c3b6743da3c39d03249095"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

require xen-common.inc
require xen-openxt.inc

DEFAULT_PREFERENCE = "1"
