# intel-microcode is MACHINE specific for some reason.

# Since dom0 is shipped as a full image, the installer copies relevant files
# from /boot to the boot partition.
do_install_append() {
    install -d "${D}/boot"
    # intel-microcode recipe installs in ${WORKDIR}.
    install "${WORKDIR}/microcode_${PV}.bin" "${D}/boot/microcode_${PV}.bin"
    ln -sfr "${D}/boot/microcode_${PV}.bin" "${D}/boot/microcode_intel.bin"
}

# Override do_deploy to suit OpenXT existing bootstrap.
do_deploy() {
    # intel-microcode recipe installs in ${WORKDIR}.
    install "${WORKDIR}/microcode_${PV}.bin" "${DEPLOYDIR}/microcode_intel.bin"
}

FILES_${PN} += "/boot"
