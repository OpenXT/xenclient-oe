FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "    \
            file://cve-2015-7547.patch;patch=1 \
            "
