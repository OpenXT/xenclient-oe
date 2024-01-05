SRCREV = "${AUTOREV}"
XEN_REL = "4.18"
LIC_FILES_CHKSUM = "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

require xen-common.inc
require xen-tools-openxt.inc

# Workaround for setuptools3 overriding autotools-brokensep
B = "${S}"

DEFAULT_PREFERENCE = "1"

PACKAGES += "vchan-socket-proxy"
FILES_vchan-socket-proxy = " \
    ${bindir}/vchan-socket-proxy \
"
RDEPENDS_${PN}-libxenlight += "vchan-socket-proxy"
