PR .= ".2"

LICENSE_append := "& WHENCE \
                   & Firmware-i915_firmware \
"

LIC_FILES_CHKSUM += "file://WHENCE;beginline=1224;endline=1264;md5=c31e99ad18d493aaa6bac6d78ea37155 \
                     file://LICENSE.i915;md5=2b0b2e0d20984affd4490ba2cba02570 \
                     "

NO_GENERIC_LICENSE[WHENCE] = "WHENCE"
NO_GENERIC_LICENSE[Firmware-i915_firmware] = "LICENCE.i915"

PACKAGES =+ "${PN}-whence-license ${PN}-bnx2 \
             ${PN}-iwlwifi \
             ${PN}-iwlwifi-7260-12 ${PN}-iwlwifi-7260-13 \
             ${PN}-iwlwifi-8000c \
             ${PN}-i915 ${PN}-i915-license \
            "
# note that ${PN}-iwlwifi-misc is added to PACKAGES in do_package_prepend below.

LICENSE_${PN}-bnx2 = "WHENCE"
LICENSE_${PN}-whence-license = "WHENCE"
LICENSE_${PN}-iwlwifi = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-7260-12 = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-7260-13 = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-8000c = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-misc = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-i915 = "Firmware-i915_firmware"

# bug fix: these LICENSE lines are missing upstream:
LICENSE_${PN}-iwlwifi-6000g2b-5 = "Firmware-iwlwifi_firmware"
LICENSE_${PN}-iwlwifi-license = "Firmware-iwlwifi_firmware"

FILES_${PN}-bnx2 = "/lib/firmware/bnx2/*.fw"
FILES_${PN}-whence-license = "/lib/firmware/WHENCE"
FILES_${PN}-iwlwifi-7260-12 = "/lib/firmware/iwlwifi-7260-12.ucode"
FILES_${PN}-iwlwifi-7260-13 = "/lib/firmware/iwlwifi-7260-13.ucode"
FILES_${PN}-iwlwifi-8000c = "/lib/firmware/iwlwifi-8000C-*.ucode"
FILES_${PN}-iwlwifi-misc = "/lib/firmware/iwlwifi-*.ucode"
FILES_${PN}-i915 = " \
        /lib/firmware/i915/*.bin \
"
FILES_${PN}-i915-license = " \
        /lib/firmware/LICENSE.i915 \
"

# -iwlwifi-misc is a "catch all" package that includes all the iwlwifi
# firmwares that are not already included in other -iwlwifi- packages.
# -iwlwifi is a virtual package that depends upon all iwlwifi packages.
# These are distinct in order to allow the -misc firmwares to be installed
# without pulling in every other iwlwifi package.
ALLOW_EMPTY_${PN}-iwlwifi = "1"
ALLOW_EMPTY_${PN}-iwlwifi-misc = "1"

RDEPENDS_${PN}-bnx2 += "${PN}-whence-license"
RDEPENDS_${PN}-iwlwifi-7260-12 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-7260-13 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-8000c = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-misc = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-i915 = "${PN}-i915-license"

LICENSE_${PN} += "& WHENCE \
"

LICENSE_${PN}-license += "/lib/firmware/WHENCE"

# The iwlwifi-misc package needs to be carefully inserted into the PACKAGES
# variable at the correct position: prior to the final linux-firmware package.
python do_package_prepend() {
    packages = d.getVar("PACKAGES", d, 1)
    index = packages.rfind('linux-firmware')
    d.setVar("PACKAGES", packages[0:index] + 'linux-firmware-iwlwifi-misc ' + packages[index:])
}

# Make linux-firmware depend on all of the split-out packages.
# Make linux-firmware-iwlwifi depend on all of the split-out iwlwifi packages.
python populate_packages_prepend () {
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS_linux-firmware', ' ' + ' '.join(firmware_pkgs))

    iwlwifi_pkgs = filter(lambda x: x.find('-iwlwifi-') != -1, firmware_pkgs)
    d.appendVar('RDEPENDS_linux-firmware-iwlwifi', ' ' + ' '.join(iwlwifi_pkgs))
}
