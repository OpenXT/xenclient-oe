FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += " \
    file://openxt-branding.patch \
    file://disable-right-click-menu.patch \
    file://filter-input-methods.patch \
    file://hide-toolbar-from-env.patch \
"
# OpenXT ships some homebrewed translations.
# TODO: This should be done differently, probably fetching existing translation
# pot files that are up to date.
SRC_URI += " \
    file://translations/de.po \
    file://translations/es.po \
    file://translations/fr.po \
    file://translations/ja.po \
    file://translations/zh_CN.po \
"

do_compile_prepend() {
    for i in de es fr ja zh_CN ; do
        cp ${WORKDIR}/translations/${i}.po ${S}/po/${i}.po
        if ! grep -q ${i} ${S}/po/LINGUAS; then
            echo ${i} >> ${S}/po/LINGUAS
        fi
    done
}
EXTRA_OEMAKE = "CFLAGS+=-DOPENXT_BRANDING"

# do_install fails intermitently:
# https://github.com/uim/uim/issues/44
PARALLEL_MAKE = ""

# We need a read-only rootfs...
# Disable:
# gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules
pkg_postinst_uim-gtk2.0() {
    :
}
