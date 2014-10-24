DESCRIPTION = "libbudgetvhd"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"

PV = "git${SRCPV}"

SRCREV = "52356fd1fe5af97e37213dbbc0e87f738db74b13"
SRC_URI = "git://github.com/openxt/gene3fs.git;protocol=https"

S = "${WORKDIR}/git/libbudgetvhd"

inherit autotools
inherit pkgconfig
inherit xenclient

BBCLASSEXTEND="native"
