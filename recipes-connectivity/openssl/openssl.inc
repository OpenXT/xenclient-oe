SUMMARY = "Secure Socket Layer"
DESCRIPTION = "Secure Socket Layer (SSL) binary and related cryptographic tools."
HOMEPAGE = "http://www.openssl.org/"
BUGTRACKER = "http://www.openssl.org/news/vulnerabilities.html"
SECTION = "libs/network"

# "openssl | SSLeay" dual license
LICENSE = "openssl"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f9a8f968107345e0b75aa8c2ecaa7ec8"

DEPENDS = "makedepend-native"
DEPENDS_append_class-target = " openssl-native"

SRC_URI = "http://www.openssl.org/source/openssl-${PV}.tar.gz \
          "
S = "${WORKDIR}/openssl-${PV}"

PACKAGECONFIG[perl] = ",,,"

TERMIO_libc-musl = "-DTERMIOS"
TERMIO ?= "-DTERMIO"
# Avoid binaries being marked as requiring an executable stack since it 
# doesn't(which causes and this causes issues with SELinux
CFLAG = "${@base_conditional('SITEINFO_ENDIANNESS', 'le', '-DL_ENDIAN', '-DB_ENDIAN', d)} \
	 ${TERMIO} ${CFLAGS} -Wall -Wa,--noexecstack"

export DIRS = "crypto ssl apps"
export EX_LIBS = "-lgcc -ldl"
export AS = "${CC} -c"

inherit pkgconfig siteinfo multilib_header

PACKAGES =+ "libcrypto libssl ${PN}-misc openssl-conf"
FILES_libcrypto = "${libdir}/libcrypto${SOLIBS}"
FILES_libssl = "${libdir}/libssl${SOLIBS}"
FILES_${PN}-dbg =+ "${libdir}/ssl/engines/.debug"
FILES_${PN} =+ " ${libdir}/ssl/*"
FILES_${PN}-misc = "${libdir}/ssl/misc"
RDEPENDS_${PN}-misc = "${@bb.utils.contains('PACKAGECONFIG', 'perl', 'perl', '', d)}"

PROVIDES += "openssl10"

# Add the openssl.cnf file to the openssl-conf package.  Make the libcrypto
# package RRECOMMENDS on this package.  This will enable the configuration
# file to be installed for both the base openssl package and the libcrypto
# package since the base openssl package depends on the libcrypto package.
FILES_openssl-conf = "${sysconfdir}/ssl/openssl.cnf"
CONFFILES_openssl-conf = "${sysconfdir}/ssl/openssl.cnf"
RRECOMMENDS_libcrypto += "openssl-conf"

# Remove this to enable SSLv3. SSLv3 is defaulted to disabled due to the POODLE
# vulnerability
EXTRA_OECONF = " -no-ssl3"

do_configure_prepend_darwin () {
	sed -i -e '/version-script=openssl\.ld/d' Configure
}

do_configure () {
	cd util
	perl perlpath.pl ${STAGING_BINDIR_NATIVE}
	cd ..
	ln -sf apps/openssl.pod crypto/crypto.pod ssl/ssl.pod doc/

	os=${HOST_OS}
	case $os in
	linux-uclibc |\
	linux-uclibceabi |\
	linux-gnueabi |\
	linux-uclibcspe |\
	linux-gnuspe |\
	linux-musl*)
		os=linux
		;;
		*)
		;;
	esac
	target="$os-${HOST_ARCH}"
	case $target in
	linux-arm)
		target=linux-armv4
		;;
	linux-armeb)
		target=linux-elf-armeb
		;;
	linux-aarch64*)
		target=linux-aarch64
		;;
	linux-sh3)
		target=debian-sh3
		;;
	linux-sh4)
		target=debian-sh4
		;;
	linux-i486)
		target=debian-i386-i486
		;;
	linux-i586 | linux-viac3)
		target=debian-i386-i586
		;;
	linux-i686)
		target=debian-i386-i686/cmov
		;;
	linux-gnux32-x86_64)
		target=linux-x32
		;;
	linux-gnu64-x86_64)
		target=linux-x86_64
		;;
	linux-gnun32-mips*el)
		target=debian-mipsn32el
		;;
	linux-gnun32-mips*)
		target=debian-mipsn32
		;;
	linux-mips*64*el)
		target=debian-mips64el
		;;
	linux-mips*64*)
		target=debian-mips64
		;;
	linux-mips*el)
		target=debian-mipsel
		;;
	linux-mips*)
		target=debian-mips
		;;
	linux-microblaze*|linux-nios2*)
		target=linux-generic32
		;;
	linux-powerpc)
		target=linux-ppc
		;;
	linux-powerpc64)
		target=linux-ppc64
		;;
	linux-supersparc)
		target=linux-sparcv8
		;;
	linux-sparc)
		target=linux-sparcv8
		;;
	darwin-i386)
		target=darwin-i386-cc
		;;
	esac
	# inject machine-specific flags
	sed -i -e "s|^\(\"$target\",\s*\"[^:]\+\):\([^:]\+\)|\1:${CFLAG}|g" Configure
        useprefix=${prefix}
        if [ "x$useprefix" = "x" ]; then
                useprefix=/
        fi        
	perl ./Configure ${EXTRA_OECONF} shared --prefix=$useprefix --openssldir=${libdir}/ssl --libdir=`basename ${libdir}` $target
}

do_compile_prepend_class-target () {
    sed -i 's/\((OPENSSL=\)".*"/\1"openssl"/' Makefile
}

# Override AR with the required option (required for -native):
EXTRA_OEMAKE = "AR='${AR} r'"
do_compile () {
	oe_runmake
}

do_install () {
	# Create ${D}/${prefix} to fix parallel issues
	mkdir -p ${D}/${prefix}/

	oe_runmake INSTALL_PREFIX="${D}" MANDIR="${mandir}" install

	oe_libinstall -so libcrypto ${D}${libdir}
	oe_libinstall -so libssl ${D}${libdir}

	install -d ${D}${includedir}
	cp --dereference -R include/openssl ${D}${includedir}

	install -Dm 0755 ${WORKDIR}/openssl-c_rehash.sh ${D}${bindir}/c_rehash
	sed -i -e 's,/etc/openssl,${sysconfdir}/ssl,g' ${D}${bindir}/c_rehash

	oe_multilib_header openssl/opensslconf.h
	if [ "${@bb.utils.contains('PACKAGECONFIG', 'perl', 'yes', 'no', d)}" = "yes" ]; then
		sed -i -e '1s,.*,#!${bindir}/env perl,' ${D}${libdir}/ssl/misc/CA.pl
		sed -i -e '1s,.*,#!${bindir}/env perl,' ${D}${libdir}/ssl/misc/tsget
	else
		rm -f ${D}${libdir}/ssl/misc/CA.pl ${D}${libdir}/ssl/misc/tsget
	fi

	# Create SSL structure
	install -d ${D}${sysconfdir}/ssl/
	mv ${D}${libdir}/ssl/openssl.cnf \
	   ${D}${libdir}/ssl/certs \
	   ${D}${libdir}/ssl/private \
	   \
	   ${D}${sysconfdir}/ssl/
	ln -sf ${sysconfdir}/ssl/certs ${D}${libdir}/ssl/certs
	ln -sf ${sysconfdir}/ssl/private ${D}${libdir}/ssl/private
	ln -sf ${sysconfdir}/ssl/openssl.cnf ${D}${libdir}/ssl/openssl.cnf
}

do_install_append_class-native() {
	create_wrapper ${D}${bindir}/openssl \
	    OPENSSL_CONF=${libdir}/ssl/openssl.cnf \
	    SSL_CERT_DIR=${libdir}/ssl/certs \
	    SSL_CERT_FILE=${libdir}/ssl/cert.pem \
	    OPENSSL_ENGINES=${libdir}/ssl/engines
}

BBCLASSEXTEND = "native nativesdk"
