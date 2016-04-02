DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"

# Notes:
# Based on the OE one but with the new git repository

LICENSE = "Intel & Broadcom"

LIC_FILES_CHKSUM = "file://LICENCE.iwlwifi_firmware;md5=3fd842911ea93c29cd32679aa23e1c88 \
                    file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc \
                    file://${FILE_DIRNAME}/LICENCE.broadcom_bnx2;md5=46e2a5689ed924549feb511065c6efa9"

SRCREV = "75cc3ef8ba6712fd72c073b17a790282136cc743"
PV = "0.1+git${SRCPV}"
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
    # Ref: OXT-524
    # It is unclear from the upstream source whether this is the intended
    # software licence for the Broadcom bnx2 driver -- given the filename,
    # it seems unlikely -- but we take the conservative approach and include it:
	install -m 644 LICENCE.broadcom_bcm43xx ${D}/lib/firmware
    # This second Broadcom licence is extracted from the WHENCE file in the
    # upstream linux-firmware repository and is derived from comments in the
    # kernel source:
	install -m 644 ${FILE_DIRNAME}/LICENCE.broadcom_bnx2 ${D}/lib/firmware
}

FILES_${PN} = "/lib/firmware/"
