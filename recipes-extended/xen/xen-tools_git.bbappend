SRCREV = "${AUTOREV}"
XEN_REL = "4.12"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbb4b1bdc2c3b6743da3c39d03249095"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

require xen-common.inc
require ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-tools-blktap2.inc', 'xen-tools-blktap3.inc', d)}
require xen-tools-openxt.inc
