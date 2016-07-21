SUMMARY = "Libre Secure Socket Layer"
DESCRIPTION = "Libre Secure Socket Layer (SSL) binary and related cryptographic tools."
HOMEPAGE = "http://www.libressl.org/"
BUGTRACKER = "https://github.com/libressl-portable/openbsd/issues"
SECTION = "libs/network"

# openssl | SSLeay dual license + ISC for LibreSSL modifications
LICENSE = "openssl & ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=01f9bb4d275f5eeea905377bef3de622"

SRC_URI = "http://ftp.openbsd.org/pub/OpenBSD/LibreSSL/libressl-${PV}.tar.gz \
          "

S = "${WORKDIR}/libressl-${PV}"

export DIRS = "crypto ssl apps"

inherit autotools pkgconfig multilib_header

PACKAGES =+ "libressl-libcrypto \
             libressl-libssl \
             libressl-openssl-conf \
             libressl-certs \
             "

# libressl provides an API-compatible alternative to openssl
PROVIDES = "openssl libcrypto libssl openssl-conf"

RPROVIDES_libressl += "openssl"
RREPLACES_libressl += "openssl"
RCONFLICTS_libressl += "openssl"

RPROVIDES_libressl-native += "openssl-native"
RREPLACES_libressl-native += "openssl-native"
RCONFLICTS_libressl-native += "openssl-native"

RPROVIDES_libressl-libcrypto += "libcrypto"
RREPLACES_libressl-libcrypto += "libcrypto"
RCONFLICTS_libressl-libcrypto += "libcrypto"

RPROVIDES_libressl-libssl += "libssl"
RREPLACES_libressl-libssl += "libssl"
RCONFLICTS_libressl-libssl += "libssl"

RPROVIDES_libressl-openssl-conf += "openssl-conf"
RREPLACES_libressl-openssl-conf += "openssl-conf"
RCONFLICTS_libressl-openssl-conf += "openssl-conf"

FILES_libressl-libcrypto = "${libdir}/libcrypto${SOLIBS}"
FILES_libressl-libssl = "${libdir}/libssl${SOLIBS}"
FILES_libressl-certs = "${libdir}/ssl/certs/* ${libdir}/ssl/cert.pem"
FILES_${PN} =+ " ${libdir}/ssl/*"
FILES_${PN}-dev =+ "${includedir}/openssl/* ${includedir}/tls.h"
FILES_${PN}-dev =+ "${mandir}/man3/*"

# Add the openssl.cnf file to the libressl-openssl-conf package.
# Make the libcrypto package RRECOMMENDS on this package.
# This will enable the configuration file to be installed for both
# the base ssl package and the libcrypto package since the base ssl
# package depends on the libcrypto package.
FILES_libressl-openssl-conf = "${libdir}/ssl/openssl.cnf"
CONFFILES_libressl-openssl-conf = "${libdir}/ssl/openssl.cnf"
RRECOMMENDS_libressl-libcrypto += "libressl-openssl-conf"

CONFIGUREOPTS = "--prefix=${prefix} --with-openssldir=${libdir}/ssl --libdir=${libdir}"

do_configure_append() {
    mv ${B}/-libtoolT ${B}/-libtool
    chmod 755 ${B}/-libtool
}

do_compile () {
    oe_runmake
}

do_install () {
    oe_runmake DESTDIR="${D}" MANDIR="${mandir}" install

    oe_libinstall -so libcrypto ${D}${libdir}
    oe_libinstall -so libssl ${D}${libdir}

    oe_multilib_header openssl/opensslconf.h
}

do_install_append_virtclass-native() {
    create_wrapper ${D}${bindir}/openssl \
        OPENSSL_CONF=${libdir}/ssl/openssl.cnf \
        SSL_CERT_DIR=${libdir}/ssl/certs \
        SSL_CERT_FILE=${libdir}/ssl/cert.pem
}

BBCLASSEXTEND = "native nativesdk"
