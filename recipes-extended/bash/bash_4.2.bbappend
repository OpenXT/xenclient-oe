FILESEXTRA := "${THISDIR}/${PN}-${PV}"
FILESEXTRAPATHS_prepend := "${FILESEXTRA}:"

SRC_URI += " \
           file://mkbuiltins_have_stringize.patch \
           file://cve-2014-6271.patch;striplevel=0 \
           file://cve-2014-7169.patch \
           file://Fix-for-bash-exported-function-namespace-change.patch;striplevel=0 \
           file://cve-2014-7186_cve-2014-7187.patch;striplevel=0 \
           file://cve-2014-6277.patch \
           file://cve-2014-6278.patch;striplevel=0 \
           "

PRINC := "${@int(PRINC) + 1}"
