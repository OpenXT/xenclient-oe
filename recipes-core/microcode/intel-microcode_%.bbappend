# intel-microcode is MACHINE specific for some reason.

# meta-intel writes an early-initrd cpio. Xen/tboot load a concatenated
# Intel microcode binary as a boot module named microcode_intel.bin.
do_compile:append() {
    ${STAGING_DIR_NATIVE}${sbindir_native}/iucode_tool \
        ${UCODE_FILTER_PARAMETERS} \
        --overwrite \
        --write-to=${WORKDIR}/microcode_${PV}.bin \
        ${S}/intel-ucode/* ${S}/intel-ucode-with-caveats/*
}

# Since dom0 is shipped as a full image, the installer copies relevant files
# from /boot to the boot partition.
do_install:append() {
    install -d "${D}/boot"
    install "${WORKDIR}/microcode_${PV}.bin" "${D}/boot/microcode_${PV}.bin"
    ln -sfr "${D}/boot/microcode_${PV}.bin" "${D}/boot/microcode_intel.bin"
}

# Override do_deploy to suit OpenXT existing bootstrap.
do_deploy() {
    install -d ${DEPLOYDIR}
    install "${WORKDIR}/microcode_${PV}.bin" "${DEPLOYDIR}/microcode_intel.bin"
}

FILES:${PN} += "/boot"
