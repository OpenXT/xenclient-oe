FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Because po-gram-gen.y has been modified by fix-CVE-2018-18751.patch,
# it requires yacc which provided by bison-native
# Please remove bison-native from DEPENDS* when next upgrade
DEPENDS = "bison-native gettext-native virtual/libiconv"
DEPENDS_class-native = "bison-native gettext-minimal-native"

SRC_URI += "file://fix-CVE-2018-18751.patch"
