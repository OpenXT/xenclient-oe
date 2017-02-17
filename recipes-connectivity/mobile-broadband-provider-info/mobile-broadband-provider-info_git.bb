DESCRIPTION = "Mobile Broadband Service Provider Database"
SECTION = "network"
LICENSE = "CCPD"
LIC_FILES_CHKSUM = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"
NO_GENERIC_LICENSE[CCPD] = "COPYING"

# The OE recipe sets PV to "gitr${SRCREV}", which has the unfortunate property
# of decreasing when SRCREV is updated to a newer revision with a lower hash.
# The PV here is carefully chosen to be higher than any the OE recipe could set.
PV = "xcgit"
SRCREV = "3903ab8662f9a225744ef268fce70886d10ce80c"
SRC_URI = "git://git.gnome.org/mobile-broadband-provider-info;protocol=git"
S = "${WORKDIR}/git"

inherit autotools_stage
