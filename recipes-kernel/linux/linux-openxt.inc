inherit xenclient

DEPENDS += "bc-native"

KERNEL_IMAGETYPE = "bzImage"

S = "${WORKDIR}/linux-${PV}"

