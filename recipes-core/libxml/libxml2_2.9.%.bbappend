FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://libxml2-CVE-2016-4658.patch \
    file://libxml2-fix_NULL_pointer_derefs.patch \
    file://libxml2-CVE-2016-5131.patch \
    file://libxml2-fix_node_comparison.patch \
    file://CVE-2016-9318.patch \
"
