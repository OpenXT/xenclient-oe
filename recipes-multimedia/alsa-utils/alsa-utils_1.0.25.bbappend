FILESEXTRAPATHS := "${THISDIR}/${PN}-${PV}"
SRC_URI += " \
    file://change_amixer_scontents_output.patch \
    "