FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://remove-ipx-depends.patch \
    file://swig-immutable.patch \
"
