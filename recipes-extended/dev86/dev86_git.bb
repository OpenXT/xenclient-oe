DESCRIPTION = "This is a cross development C compiler, assembler and linker environment for the production of 8086 executables (Optionally MSDOS COM)"
HOMEPAGE = "https://codeberg.org/jbruchon/dev86"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"
SECTION = "console/tools"

# meta-virtualization dropped this after github.com/jbruchon/dev86 vanished.
# vgabios 0.8a still needs bcc from here. Use the Codeberg mirror and pin
# the v0.16.21 tag (the last meta-virt SRCREV is not on that history).
BASE_PV = "0.16.21"
PV = "${BASE_PV}+git"
SRCREV = "c6f36cbafa979710b809f117562773dbd6825918"
SRC_URI = "git://codeberg.org/jbruchon/${BPN}.git;protocol=https;branch=master \
    file://0001-cpp-Makefile-respect-LDFLAGS-when-building-bcc-cpp.patch \
    file://0003-cpp-update-token1.tok-to-make-new-gperf-happy-regen..patch \
    file://0004-regen-token2.h-token1.h-with-gperf-3.1.patch \
    file://cross.patch \
    file://0001-cpp-fix-race-writing-token.h-files.patch \
"

DEPENDS = "gperf-native"

BBCLASSEXTEND = "native nativesdk"
EXTRA_OEMAKE = "VERSION=${BASE_PV} PREFIX=${prefix} DIST=${D} LDFLAGS='${LDFLAGS}' INEXE=''"

do_compile() {
	# always regenerate token1.h, token2.h for deterministic behavior
	rm -f ${S}/cpp/token1.h ${S}/cpp/token2.h
	oe_runmake make.fil
	oe_runmake -f make.fil bcc86 as86 ld86
}

do_install() {

	if [ "${prefix}"=="" ] ; then
		export prefix=/usr
	fi

	oe_runmake install-bcc
	ln -s ../lib/bcc/bcc-cpp ${D}${prefix}/bin/bcc-cpp
	ln -s ../lib/bcc/bcc-cc1 ${D}${prefix}/bin/bcc-cc1
}

FILES:${PN} += "${libdir}/bcc"

# http://gecko.lge.com:8000/Errors/Details/832862
# http://errors.yoctoproject.org/Errors/Details/766930/
# cpp.c:548:1: error: type of 'ch' defaults to 'int' [-Wimplicit-int]
# strsave.c:47:42: error: implicit declaration of function 'strcmp' [-Wimplicit-function-declaration]
# strsave.c:55:26: error: implicit declaration of function 'hash' [-Wimplicit-function-declaration]
# strsave.c:66:32: error: implicit declaration of function 'strlen' [-Wimplicit-function-declaration]
# symbol.c:60:42: error: implicit declaration of function 'strcmp' [-Wimplicit-function-declaration]
# symbol.c:72:30: error: implicit declaration of function 'strlen' [-Wimplicit-function-declaration]
# symbol.c:77:13: error: implicit declaration of function 'hash' [-Wimplicit-function-declaration]
# tok_class.c:305:43: error: implicit declaration of function 'strlen' [-Wimplicit-function-declaration]
# tok_io.c:219:20: error: implicit declaration of function 'atoi' [-Wimplicit-function-declaration]
# tok_io.c:488:20: error: implicit declaration of function 'strlen' [-Wimplicit-function-declaration]
# unproto.c:188:51: error: implicit declaration of function 'strcmp' [-Wimplicit-function-declaration]
CFLAGS += "-Wno-error=implicit-int -Wno-error=implicit-function-declaration"

# http://errors.yoctoproject.org/Errors/Details/853302/
CFLAGS += "-std=gnu17"

# http://errors.yoctoproject.org/Errors/Details/853303/
# ar.c:615:23: error: passing argument 1 of 'ctime' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-Wno-error=incompatible-pointer-types"

# GCC 15: unproto/tok_io.c returns without a value from a non-void function
CFLAGS += "-Wno-error=return-mismatch"

EXTRA_OEMAKE += "CFLAGS='${CFLAGS}'"
