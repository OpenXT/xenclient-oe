SUMMARY = "Firmware files for use with Linux kernel"
SECTION = "kernel"

LICENSE = "Firmware-i915 & Firmware-iwlwifi_firmware & WHENCE"
LICENSE_${PN}-i915 = "Firmware-i915"
LICENSE_${PN}-iwlwifi = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-bnx2 = "WHENCE"

LIC_FILES_CHKSUM = "\
    file://LICENSE.i915;md5=2b0b2e0d20984affd4490ba2cba02570 \
    file://LICENCE.iwlwifi_firmware;md5=3fd842911ea93c29cd32679aa23e1c88 \
    file://WHENCE;beginline=1316;endline=1325;md5=6b6994826e3a4a9c194af28d3b06ed87 \
"

NO_GENERIC_LICENSE[Firmware-i915] = "LICENSE.i915"
NO_GENERIC_LICENSE[Firmware-iwlwifi_firmware] = "LICENCE.iwlwifi_firmware"
NO_GENERIC_LICENSE[WHENCE] = "WHENCE"

SRCREV = "6f5257c6299414b89d84145eedd37a6ead47b25b"
PE = "1"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git"

S = "${WORKDIR}/git"

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	# i915 firmware
	cp -r i915 ${D}/lib/firmware/
	cp LICENSE.i915 ${D}/lib/firmware

	# Intel wifi firmware
	cp iwlwifi*.ucode ${D}/lib/firmware/
	cp LICENCE.iwlwifi_firmware ${D}/lib/firmware

	# Broadcom NetXtreme II firmware
	cp -r bnx2 ${D}/lib/firmware/bnx2
	cp WHENCE ${D}/lib/firmware
}

FILES_${PN}-i915 = "/lib/firmware/i915 /lib/firmware/LICENSE.i915"
FILES_${PN}-iwlwifi = "/lib/firmware/iwlwifi*.ucode /lib/firmware/LICENCE.iwlwifi_firmware"
FILES_${PN}-bnx2 = "/lib/firmware/bnx2 /lib/firmware/WHENCE"

PACKAGES = "${PN}-i915 ${PN}-iwlwifi ${PN}-bnx2"
