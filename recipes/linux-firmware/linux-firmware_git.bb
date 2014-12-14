DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"

# Notes:
# Based on the OE one but with the new git repository

LICENSE = "Intel"

LIC_FILES_CHKSUM = "file://LICENCE.iwlwifi_firmware;md5=8b938534f77ffd453690eb34ed84ae8b"

SRCREV = "dec41bce44e0dff6a2c3358a958fadf22bf58858"
PV = "0.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git;protocol=git"

S = "${WORKDIR}/git"

inherit allarch

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/

	# Intel wifi firmware
	cp iwlwifi*.ucode ${D}/lib/firmware/
	cp LICENCE.iwlwifi_firmware ${D}/lib/firmware

	# Broadcom NetXtreme II firmware
	install -d ${D}/lib/firmware/bnx2/
	cp bnx2/bnx2-mips-09-6.2.1b.fw ${D}/lib/firmware/bnx2
}

FILES_${PN} = "/lib/firmware/"
