SUMMARY = "Software stack for TPM2."
DESCRIPTION = "tpm2-tss for interfacing with tpm2.0 device"
SECTION = "tpm"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0b1d631c4218b72f6b05cb58613606f4"

DEPENDS = "autoconf-archive autoconf pkgconfig libgcrypt gnome-common"

SRCREV = "ced20c209397f58d81da79810f49976ba2d36566"

SRC_URI = " \
    git://github.com/01org/tpm2-tss.git;protocol=git;branch=master \
    "

S = "${WORKDIR}/git"

# https://lists.yoctoproject.org/pipermail/yocto/2013-November/017042.html

PROVIDES = "${PACKAGES}"
PACKAGES = " \
    ${PN}-dbg \
    libtss2 \
    libtss2-dev \
    libtss2-staticdev \
    libtctidevice \
    libtctidevice-dev \
    libtctidevice-staticdev \
    libtctisocket \
    libtctisocket-dev \
    libtctisocket-staticdev \
    resourcemgr \
"

FILES_libtss2 = " \
    ${libdir}/libtss2.so.0.0.0 \
    ${libdir}/libtss2-sys.so.0.0.0 \
    ${libdir}/libtss2-esys.so.0.0.0 \
    ${libdir}/libtss2-mu.so.0.0.0 \
"
FILES_libtss2-dev = " \
    ${includedir}/tss2 \
    #${libdir}/libmarshal.so* \
    ${libdir}/libtss2.so* \
    ${libdir}/libtss2-sys.so* \
    ${libdir}/libtss2-esys.so* \
    ${libdir}/libtss2-mu.so* \
    ${libdir}/pkgconfig/tss2.pc \
    ${libdir}/pkgconfig/tss2-sys.pc \
    ${libdir}/pkgconfig/tss2-esys.pc \
    ${libdir}/pkgconfig/tss2-mu.pc \
"
FILES_libtss2-staticdev = " \
    #${libdir}/libmarshal.a \
    #${libdir}/libmarshal.la \
    ${libdir}/libtss2.a \
    ${libdir}/libtss2.la \
    ${libdir}/libtss2-sys.a \
    ${libdir}/libtss2-esys.a \
    ${libdir}/libtss2-mu.a \
"
FILES_libtctidevice = "${libdir}/libtss2-tcti-device.so.0.0.0"
FILES_libtctidevice-dev = " \
    ${includedir}/tss2/tss2_tcti_device.h \
    ${libdir}/libtss2-tcti-device.so* \
    ${libdir}/pkgconfig/tss2-tcti-device.pc \
"
FILES_libtctidevice-staticdev = "${libdir}/libtss2-tcti-device.*a"
FILES_libtctisocket = "${libdir}/libtss2-tcti-mssim.so.0.0.0"
FILES_libtctisocket-dev = " \
    ${includedir}/tss2/tss2_tcti_mssim.h \
    ${libdir}/libtss2-tcti-mssim.so* \
    ${libdir}/pkgconfig/tss2-tcti-mssim.pc \
"
FILES_libtctisocket-staticdev = "${libdir}/libtss2-tcti-mssim.*a"
FILES_resourcemgr = "${sbindir}/resourcemgr"

inherit autotools pkgconfig

do_configure_prepend () {
	# execute the bootstrap script
	currentdir=$(pwd)
	cd ${S}
	ACLOCAL="aclocal --system-acdir=${STAGING_DATADIR}/aclocal" ./bootstrap
	cd ${currentdir}
}
