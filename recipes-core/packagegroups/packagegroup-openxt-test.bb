SUMMARY = "Provides testing packages"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

RDEPENDS_${PN} = " \
            ${@bb.utils.contains("DISTRO_FEATURES", "bats", "bats-suite", "", d)} \
            "
