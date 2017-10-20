FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "\
    git://github.com/james-antill/ustr.git;protocol=https;branch=master \
    file://ustr-makefile-fix.patch;patch=1 \
    file://ustr-fix__va_copy-not-defined.patch;patch=2 \
    file://ustr-c99-inline.patch;patch=3 \
    file://ustr-gnu-inline.diff;patch=4 \
"

SRCREV = "97b6e00d83464bc77378073c210724e10012b770"

S = "${WORKDIR}/git"
