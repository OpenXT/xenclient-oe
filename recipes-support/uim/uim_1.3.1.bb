inherit qemu
DEPENDS += "qemu-native"
SRC_URI[md5sum] = "2832e23d4778bbacbfa4b49bf642d667"
SRC_URI[sha256sum] = "ed2cfa15018a4fd2557e875f66fcb3f0b9dabe12fa0700aa2f11cca69c2cb256"
# prevent uim from dumb check for libedit that causes cross compilation sanity check fail
EXTRA_OECONF += "ac_cv_lib_edit_el_init=false"
require uim.inc
DEPENDS = "gtk+ uim-native anthy fontconfig libxft xt glib-2.0 ncurses"
SECTION_uim-gtk2.0 = "x11/inputmethods"
PR = "r4"

SRC_URI += "file://uim-module-manager.patch;patch=1 \
            file://fix-missing-includes.patch;patch=1 \
            file://xenclient-branding.patch;patch=1 \
            file://more-languages.patch;patch=1 \
            file://disable-right-click-menu.patch;patch=1 \
            file://fix-untranslatable-strings.patch;patch=1 \
            file://filter-input-methods.patch;patch=1 \
            file://show-or-hide.patch;patch=1 \
            file://translations/de.po \
            file://translations/es.po \
            file://translations/fr.po \
            file://translations/ja.po \
            file://translations/zh_CN.po"

inherit autotools-brokensep pkgconfig gettext

PACKAGES += "uim-xim uim-utils uim-skk uim-gtk2.0 uim-fep uim-common uim-anthy libuim0 libuim-dev"

LEAD_SONAME = "libuim.so.1"
RDEPENDS_uim = "libuim0"
RDEPENDS_uim-anthy = "virtual-japanese-font"

DESCRIPTION_libuim0 = "Simple and flexible input method collection and library"
SECTION_libuim0 = "libs/inputmethods"
FILES_libuim0 = "${libdir}/uim/plugin/libuim-custom-enabler.* \
                 ${libdir}/libuim-custom.so.* \
                 ${datadir}/locale/de/LC_MESSAGES/uim.mo \
                 ${datadir}/locale/es/LC_MESSAGES/uim.mo \
                 ${datadir}/locale/fr/LC_MESSAGES/uim.mo \
                 ${datadir}/locale/ja/LC_MESSAGES/uim.mo \
                 ${datadir}/locale/ko/LC_MESSAGES/uim.mo \
                 ${datadir}/locale/zh_CN/LC_MESSAGES/uim.mo \
                 ${libdir}/libuim.so.*"

DESCRIPTION_libuim-dev = "Development files for uim"
SECTION_libuim-dev = "devel/libs"
FILES_libuim-dev = "${libdir}/libuim*.a \
                    ${libdir}/libuim*.la \
                    ${libdir}/libuim*.so \
                    ${includedir}/uim \
                    ${libdir}/pkgconfig/uim.pc"

DESCRIPTION_uim-anthy = "Anthy plugin for uim"
FILES_uim-anthy = "${libdir}/uim/plugin/libuim-anthy.* \
                   ${datadir}/uim/anthy*.scm"

pkg_postinst_uim-anthy() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --register anthy --path /etc/uim
fi
}

pkg_postrm_uim-anthy() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --path /etc/uim --unregister anthy
fi
}

pkg_prerm_uim-anthy() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --register anthy --path /etc/uim
fi
}

DESCRIPTION_uim-fep = "uim Front End Processor"
FILES_uim-fep = "${bindir}/uim-fep*"

DESCRIPTION_uim-gtk2.0  = "GTK+2.x immodule for uim"
FILES_uim-gtk2.0 = "${libdir}/gtk-2.0 \
                    ${bindir}/uim-toolbar-gtk* \
                    ${bindir}/uim-*-gtk \
                    ${bindir}/uim-input-pad-ja \
                    ${datadir}/uim/helperdata/uim-dict-ui.xml"

pkg_postinst_uim-gtk2.0() {
#! /bin/sh
set -e
if [ -n "$D" ];then
    PSEUDO_RELOADED=YES ${@qemu_target_binary(d)} -E LD_LIBRARY_PATH=$D/lib:$D/usr/lib -E GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders -E LD_PRELOAD= -L $D $D${bindir}/gtk-query-immodules-2.0 > $D/etc/gtk-2.0/gtk.immodules
else
    gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules
fi
}

#pkg_postrm_uim-gtk2.0() {
##! /bin/sh
#set -e
#/usr/sbin/update-gtk-immodules
#}

DESCRIPTION_uim-skk = "SKK plugin for uim"
FILES_uim-skk = "${libdir}/uim/plugin/libuim-skk.* \
                 ${datadir}/uim/skk*.scm"

pkg_postinst_uim-skk() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --register skk --path /etc/uim
fi
}

pkg_postrm_uim-skk() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --path /etc/uim --unregister skk
fi
}

DESCRIPTION_uim-utils = "Utilities for uim"
FILES_uim-utils = "${bindir}/uim-sh \
                   ${bindir}/uim-module-manager \
		   ${libexecdir}/uim-helper-server"

DESCRIPTION_uim-xim = "A bridge between uim and XIM"
FILES_uim-xim = "${bindir}/uim-xim \
                 ${libexecdir}/uim-candwin-gtk \
                 ${datadir}/man/man1/uim-xim.1 \
                 ${sysconfdir}/X11/xinit/xinput.d/uim*"

# to .xinitrc, or .xsession
#pkg_postinst_uim-xim() {
#GTK_IM_MODULE=uim ; export GTK_IM_MODULE
#QT_IM_MODULE=uim ; export QT_IM_MODULE
#uim-xim &
#XMODIFIERS=@im=uim ; export XMODIFIERS
#}

DESCRIPTION_uim-common = "Common files for uim"
FILES_uim-common = "${datadir}/uim/pixmaps/*.png \
                    ${datadir}/uim"
pkg_postinst_uim-common() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --path /etc/uim --register \
		tutcode tcode hangul viqr ipa-x-sampa latin byeoru
fi
}

pkg_prerm_uim-common() {
#! /bin/sh
set -e
if [ -f /usr/bin/uim-module-manager ]; then
	/usr/bin/uim-module-manager --path /etc/uim --register \
		tutcode tcode hangul viqr ipa-x-sampa latin byeoru
fi
}

do_compile_prepend() {
    # Normally we'd bring the message files up to date with the source code and
    # then merge in the translated messages from the translation team. However
    # the code to bring the message files up to date is broken in this version
    # of uim, so we just copy the message files from the translation team.
    #
    # Run these commands in a subshell. Otherwise changing directory affects
    # the rest of do_compile.
    (
        cd po

        for i in de es fr ja zh_CN ; do
            mv $i.po $i.po.orig

            # tr -d '\r' < ${WORKDIR}/translations/$i.po |
            #     msgmerge - $i.po.orig -o $i.po

            tr -d '\r' < ${WORKDIR}/translations/$i.po >  $i.po
        done
    )
}

#do_stage() {
#	autotools_stage_all
#}
