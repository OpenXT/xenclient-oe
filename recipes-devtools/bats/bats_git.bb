SUMMARY = "The Bash Automated Testing System"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0bceab9b5f17c5e35ab69da161b48c1"

SRC_URI = "git://github.com/sstephenson/bats.git;protocol=https"
SRCREV = "03608115df2071fff4eaaff1605768c275e5f81f"

S = "${WORKDIR}/git"

inherit allarch

do_install () {
    ${S}/install.sh ${D}/${exec_prefix}
}

RDEPENDS_${PN} += " \
    bash \
"
