FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://openxt-branding.patch \
    file://disable-right-click-menu.patch \
    file://filter-input-methods.patch \
    file://hide-toolbar-from-env.patch \
"

EXTRA_OEMAKE_append += "CFLAGS+=-DOPENXT_BRANDING"

# This should not be necessary, yet autoconf will not set PKG_CONFIG
# automatically (it does for other projects...), which in turn will fail all
# PKG_CHECK_MODULES.
export PKG_CONFIG="pkg-config"
