PR .= ".2"

DEPENDS = "zlib enchant libsoup-2.4 curl libxml2 cairo libxslt libxt libidn gnutls \
           gtk+ gstreamer1.0 gstreamer1.0-plugins-base flex-native gperf-native perl-native sqlite3 ${ICU_LIB}"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
  file://enable_hybi_by_default.patch;patch=1 \
  file://ambiguous-overloaded-abs.patch \
  "

EXTRA_OECONF = "\
                --enable-debug=no \
                --enable-svg \
                --enable-icon-database=yes \
                --enable-fullscreen-api \
                --enable-image-resizer \
                --enable-link-prefetch \
		--enable-video=no \
                --enable-webgl=no \
                --with-gtk=2.0 \
                --disable-geolocation \
                ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '--enable-webgl', '--disable-webgl', d)} \
                UNICODE_CFLAGS=-D_REENTRANT \
               "
EXTRA_OEMAKE += ' \
                CFLAGS="${CFLAGS} -std=gnu++98" \
                CXXFLAGS="${CFLAGS} -std=gnu++98" \
                CPPFLAGS="${CFLAGS} -std=gnu++98" \
                '
EXTRA_OECONF += "\
                --enable-video=no \
                --enable-webgl=no \
               "

# Still need this?
do_install_prepend() {
	cp ${S}/Programs/.libs/jsc ${S}/Programs/jsc-1 || true
}


