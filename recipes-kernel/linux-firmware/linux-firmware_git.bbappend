PR .= ".2"

# Recent kernels require an updated version of the firmwares (e.g,
# skl_guc_ver4), and in turn licenses md5 changed. So this bbappend has to
# redefine LIC_FILES_CHKSUM to keep using the initial recipe.
SRCREV = "6f5257c6299414b89d84145eedd37a6ead47b25b"

LIC_FILES_CHKSUM = "\
    file://LICENCE.Abilis;md5=b5ee3f410780e56711ad48eadc22b8bc \
    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
    file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc \
    file://LICENCE.ca0132;md5=209b33e66ee5be0461f13d31da392198 \
    file://LICENCE.chelsio_firmware;md5=819aa8c3fa453f1b258ed8d168a9d903 \
    file://LICENCE.cw1200;md5=f0f770864e7a8444a5c5aa9d12a3a7ed \
    file://LICENCE.ene_firmware;md5=ed67f0f62f8f798130c296720b7d3921 \
    file://LICENCE.fw_sst_0f28;md5=6353931c988ad52818ae733ac61cd293 \
    file://LICENCE.go7007;md5=c0bb9f6aaaba55b0529ee9b30aa66beb \
    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
    file://LICENCE.ibt_firmware;md5=fdbee1ddfe0fb7ab0b2fcd6b454a366b \
    file://LICENCE.it913x;md5=1fbf727bfb6a949810c4dbfa7e6ce4f8 \
    file://LICENCE.iwlwifi_firmware;md5=3fd842911ea93c29cd32679aa23e1c88 \
    file://LICENCE.IntcSST2;md5=9e7d8bea77612d7cc7d9e9b54b623062 \
    file://LICENCE.Marvell;md5=9ddea1734a4baf3c78d845151f42a37a \
    file://LICENCE.myri10ge_firmware;md5=42e32fb89f6b959ca222e25ac8df8fed \
    file://LICENCE.OLPC;md5=5b917f9d8c061991be4f6f5f108719cd \
    file://LICENCE.phanfw;md5=954dcec0e051f9409812b561ea743bfa \
    file://LICENCE.qat_firmware;md5=9e7d8bea77612d7cc7d9e9b54b623062 \
    file://LICENCE.qla2xxx;md5=f5ce8529ec5c17cb7f911d2721d90e91 \
    file://LICENCE.r8a779x_usb3;md5=4c1671656153025d7076105a5da7e498 \
    file://LICENCE.ralink_a_mediatek_company_firmware;md5=728f1a85fd53fd67fa8d7afb080bc435 \
    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
    file://LICENCE.tda7706-firmware.txt;md5=835997cf5e3c131d0dddd695c7d9103e \
    file://LICENCE.ti-connectivity;md5=c5e02be633f1499c109d1652514d85ec \
    file://LICENCE.ueagle-atm4-firmware;md5=4ed7ea6b507ccc583b9d594417714118 \
    file://LICENCE.via_vt6656;md5=e4159694cba42d4377a912e78a6e850f \
    file://LICENCE.wl1251;md5=ad3f81922bb9e197014bb187289d3b5b \
    file://LICENCE.xc4000;md5=0ff51d2dc49fce04814c9155081092f0 \
    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
    file://LICENCE.xc5000c;md5=12b02efa3049db65d524aeb418dd87ca \
    file://LICENSE.amd-ucode;md5=3a0de451253cc1edbf30a3c621effee3 \
    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
    file://LICENSE.radeon;md5=69612f4f7b141a97659cb1d609a1bde2 \
    file://LICENCE.siano;md5=4556c1bf830067f12ca151ad953ec2a5\
"

LICENSE_append := "& WHENCE \
                   & Firmware-i915_firmware \
"

LIC_FILES_CHKSUM += "file://WHENCE;beginline=1316;endline=1325;md5=6b6994826e3a4a9c194af28d3b06ed87 \
                     file://LICENSE.i915;md5=2b0b2e0d20984affd4490ba2cba02570 \
                     "

NO_GENERIC_LICENSE[WHENCE] = "WHENCE"
NO_GENERIC_LICENSE[Firmware-i915_firmware] = "LICENCE.i915"

PACKAGES =+ "${PN}-whence-license ${PN}-bnx2 \
             ${PN}-iwlwifi \
             ${PN}-iwlwifi-7260-12 ${PN}-iwlwifi-7260-13 \
             ${PN}-iwlwifi-8000c \
             ${PN}-i915 ${PN}-i915-license \
             ${PN}-check-whence \
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
FILES_${PN}-i915 = "/lib/firmware/i915/*.bin"
FILES_${PN}-i915-license = "/lib/firmware/LICENSE.i915"
FILES_${PN}-check-whence = "/lib/firmware/check_whence.py"

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
RDEPENDS_${PN}-check-whence = "python"

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
