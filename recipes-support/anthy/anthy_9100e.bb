SRC_URI[md5sum] = "1c328986005e61f503adc118909e12ac"
SRC_URI[sha256sum] = "369fd18d406783acc92e6ced2b3536999aa0d33940398af2b4fb11dba46cbfa3"
DESCRIPTION = "A Japanese input method"
DESCRIPTION_anthy = "A Japanese input method (backend, dictionary and utility)"
DESCRIPTION_libanthy0 = "Anthy runtime library"
DESCRIPTION_libanthy-dev = "Anthy static library, headers and documets for developers"
AUTHOR = "Anthy Developers <anthy-dev@lists.sourceforge.jp>"
HOMEPAGE = "http://anthy.sourceforge.jp"
SECTION = "inputmethods"
SECTION_libanthy0 = "libs/inputmethods"
SECTION_libanthy-dev = "devel/libs"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://alt-cannadic/COPYING;md5=c93c0550bd3173f4504b2cbd8991e50b    \
                    file://COPYING;md5=11f384074d8e93e263b5664ef08a411a"

DEPENDS = "anthy-native"
PR = "r3"

SRC_URI = "${OPENXT_MIRROR}/anthy-9100e.tar.gz \
           file://not_build_elc.patch;patch=1 \
           file://2ch_t.patch;patch=1 \
           file://native-helpers.patch;patch=1 \
           file://format-security-fixes.patch \
"

inherit autotools pkgconfig

# gettext

LEAD_SONAME = "libanthy.so.0"
RDEPENDS_anthy = "libanthy0"

PACKAGES += "${PN}-el libanthy0 libanthy-dev"
FILES_${PN}-dbg += "${libdir}/.debug"
FILES_libanthy0 = "${libdir}/libanthy.so.*	\
           		   ${libdir}/libanthydic.so.*	\
		           ${libdir}/libanthyinput.so.*"
FILES_libanthy-dev = "${libdir}/libanthy*.la \
                      ${libdir}/libanthy*.a \
                      ${libdir}/libanthy*.so \
	 	              ${includedir}/anthy	\
		              ${libdir}/pkgconfig/anthy.pc"
FILES_${PN}-el = "${datadir}/emacs/*"
FILES_${PN} = "${datadir}/* \
               ${bindir}/* \
               ${sysconfdir}/anthy-conf"
