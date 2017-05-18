SUMMARY = "Software stack for TPM2."
DESCRIPTION = "tpm2.0-tss like woah."
SECTION = "tpm"

# This is a lie. The source for this project is covered by several licenses.
# We're currently working on a way to make this clear for those consuming the
# project. Till then I'm using 'BSD' as a place holder since the Intel license
# is "BSD-like".
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

DEPENDS = "autoconf-archive autoconf pkgconfig"

SRC_URI = " \
    git://github.com/01org/TPM2.0-TSS.git;protocol=git;branch=master;name=TPM2.0-TSS;destsuffix=TPM2.0-TSS \
    "

# CAPS? SRSLY?
S = "${WORKDIR}/${@d.getVar('BPN',d).upper()}"

# https://lists.yoctoproject.org/pipermail/yocto/2013-November/017042.html
SRCREV = "56fec897d55873dbf8677fa0cfdc2144c14412b3"
PVBASE := "${PV}"
PV = "${PVBASE}.${SRCPV}"

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
    ${libdir}/libsapi.so.0.0.0 \
    ${libdir}/libsapi.so.0.0.0 \
"
FILES_libtss2-dev = " \
    ${includedir}/sapi \
    ${includedir}/tcti/common.h \
    #${libdir}/libmarshal.so* \
    ${libdir}/libsapi.so* \
    ${libdir}/pkgconfig/sapi.pc \
"
FILES_libtss2-staticdev = " \
    #${libdir}/libmarshal.a \
    #${libdir}/libmarshal.la \
    ${libdir}/libsapi.a \
    ${libdir}/libsapi.la \
"
FILES_libtctidevice = "${libdir}/libtcti-device.so.0.0.0"
FILES_libtctidevice-dev = " \
    ${includedir}/tcti/tcti_device.h \
    ${libdir}/libtcti-device.so* \
    ${libdir}/pkgconfig/tcti-device.pc \
"
FILES_libtctidevice-staticdev = "${libdir}/libtcti-device.*a"
FILES_libtctisocket = "${libdir}/libtcti-socket.so.0.0.0"
FILES_libtctisocket-dev = " \
    ${includedir}/tcti/tcti_socket.h \
    ${libdir}/libtcti-socket.so* \
    ${libdir}/pkgconfig/tcti-socket.pc \
"
FILES_libtctisocket-staticdev = "${libdir}/libtcti-socket.*a"
FILES_resourcemgr = "${sbindir}/resourcemgr"

inherit autotools pkgconfig

do_configure_prepend () {
	# execute the bootstrap script
	currentdir=$(pwd)
	cd ${S}
	ACLOCAL="aclocal --system-acdir=${STAGING_DATADIR}/aclocal" ./bootstrap
	cd ${currentdir}
}
