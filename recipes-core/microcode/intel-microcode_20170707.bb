DESCRIPTION = "Intel microcode"

LICENSE = "Intel-Microcode-License"
LIC_FILES_CHKSUM = "file://microcode.dat;md5=e5b1dc41901d2de706d4bccee94bbadc"

SRC_URI = "https://downloadmirror.intel.com/26925/eng/microcode-${PV}.tgz"
SRC_URI[md5sum] = "fe4bcb12e4600629a81fb65208c34248"
SRC_URI[sha256sum] = "4fd44769bf52a7ac11e90651a307aa6e56ca6e1a814e50d750ba8207973bee93"

do_install() {
	install -d ${D}${base_libdir}/firmware/intel-ucode/
	install ${WORKDIR}/microcode_${PV}.bin ${D}${base_libdir}/firmware/intel-ucode/
	cd ${D}${base_libdir}/firmware/intel-ucode/
	ln -sf microcode_${PV}.bin microcode.bin
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install ${S}/microcode_${PV}.cpio ${DEPLOYDIR}/
	cd ${DEPLOYDIR}
	rm -f microcode.cpio
	ln -sf microcode_${PV}.cpio microcode.cpio
}
