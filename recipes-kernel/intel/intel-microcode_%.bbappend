
# Install the microcode on the boot partition for Xen to load.
do_install_append() {
	install -d ${D}/boot
	install ${WORKDIR}/microcode_${PV}.bin ${D}/boot/microcode_${PV}.bin
	cd ${D}/boot
	ln -sf microcode_${PV}.bin microcode.bin
}

FILES_${PN} += " /boot"
