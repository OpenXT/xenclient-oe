FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
            file://do-not-include-kernel-hedaers.patch;patch=1 \
            "
