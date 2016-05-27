PR .= ".1"

LICENSE_append := "& WHENCE \
"

LIC_FILES_CHKSUM += "file://WHENCE;beginline=1224;endline=1264;md5=c31e99ad18d493aaa6bac6d78ea37155"

NO_GENERIC_LICENSE[WHENCE] = "WHENCE"

PACKAGES =+ "${PN}-whence-license ${PN}-bnx2 \
             ${PN}-iwlwifi-7260-12 ${PN}-iwlwifi-7260-13 \
            "
LICENSE_${PN}-bnx2 = "WHENCE"
LICENSE_${PN}-whence-license = "WHENCE"
LICENSE_${PN}-iwlwifi-7260-12 = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-7260-13 = "Firmware-iwlwifi_firmware"

# bug fix: these LICENSE lines are missing upstream:
LICENSE_${PN}-iwlwifi-6000g2b-5 = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-license = "Firmware-iwlwifi_firmware"

FILES_${PN}-bnx2 = "/lib/firmware/bnx2/*.fw"
FILES_${PN}-whence-license = "/lib/firmware/WHENCE"
FILES_${PN}-iwlwifi-7260-12 = "/lib/firmware/iwlwifi-7260-12.ucode"
FILES_${PN}-iwlwifi-7260-13 = "/lib/firmware/iwlwifi-7260-13.ucode"

RDEPENDS_${PN}-bnx2 += "${PN}-whence-license"
RDEPENDS_${PN}-iwlwifi-7260-12 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-7260-13 = "${PN}-iwlwifi-license"

LICENSE_${PN} += "& WHENCE \
"

LICENSE_${PN}-license += "/lib/firmware/WHENCE"

RDEPENDS_${PN} += "${PN}-bnx2 ${PN}-whence-license ${PN}-iwlwifi-7260-12 ${PN}-iwlwifi-7260-13"
