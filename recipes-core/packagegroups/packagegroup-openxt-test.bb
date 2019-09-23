SUMMARY = "Provides testing packages"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

RDEPENDS_${PN} = " \
    bats-suite-initscript \
"
