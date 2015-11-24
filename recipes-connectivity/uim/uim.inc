DESCRIPTION = "A multilingual user input method library"
HOMEPAGE = "http://uim.freedesktop.org/"
LICENSE = "BSD-3-Clause & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c372efbeca58ce22017a29c1ebf8bf0"
SECTION = "inputmethods"

EXTRA_OECONF += "--disable-applet \
                 --disable-emacs \
                 --without-scim \
                 --without-m17nlib \
                 --without-prime \
                 --without-canna \
                 --without-mana \
                 --without-eb"
#                 --without-qt \
#
SRC_URI = "http://uim.googlecode.com/files/uim-${PV}.tar.bz2"

FILES_${PN}-dbg += "${libdir}/*/*/*/.debug ${libdir}/*/*/.debug"
FILES_${PN}-dev += "${libdir}/uim/plugin/*.la"

# https://github.com/uim/uim/issues/44
PARALLEL_MAKE = ""
