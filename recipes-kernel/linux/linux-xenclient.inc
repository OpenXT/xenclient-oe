inherit xenclient

DEPENDS += "bc-native"

KERNEL_IMAGETYPE = "bzImage"

SRC_URI = "file://defconfigs/${MACHINE} \
           "
# rename_defconfig task: Rename a machine specific defconfig to "defconfig"
# for linux.inc do_configure_prepend.
addtask rename_defconfig before do_configure after do_unpack
do_rename_defconfig() {
    cp ${WORKDIR}/defconfigs/${MACHINE} ${WORKDIR}/defconfig
}

S = "${WORKDIR}/linux-${PV}"

