PR .= ".1"

LICENSE_append := "& WHENCE \
"

LIC_FILES_CHKSUM += "file://WHENCE;beginline=1224;endline=1264;md5=c31e99ad18d493aaa6bac6d78ea37155"

NO_GENERIC_LICENSE[WHENCE] = "WHENCE"

PACKAGES =+ "${PN}-whence-license ${PN}-bnx2 \
            "
LICENSE_${PN}-bnx2 = "WHENCE"
LICENSE_${PN}-whence-license = "WHENCE"

FILES_${PN}-bnx2 = "/lib/firmware/bnx2/*.fw"
FILES_${PN}-whence-license = "/lib/firmware/WHENCE"

RDEPENDS_${PN}-bnx2 += "${PN}-whence-license"

LICENSE_${PN} += "& WHENCE \
"

LICENSE_${PN}-license += "/lib/firmware/WHENCE"

RDEPENDS_${PN} += "${PN}-bnx2 ${PN}-whence-license"
