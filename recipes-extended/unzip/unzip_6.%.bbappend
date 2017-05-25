FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://18-cve-2014-9913-unzip-buffer-overflow.patch \
    file://19-cve-2016-9844-zipinfo-buffer-overflow.patch \
"
