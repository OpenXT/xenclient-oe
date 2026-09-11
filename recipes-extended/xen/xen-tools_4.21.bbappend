require xen-common.inc
require xen-tools-openxt.inc

# Workaround for setuptools3 overriding autotools-brokensep
B = "${S}"

PACKAGES += "vchan-socket-proxy"
FILES:vchan-socket-proxy = " \
    ${bindir}/vchan-socket-proxy \
"
RDEPENDS:${PN}-libxenlight += "vchan-socket-proxy"
