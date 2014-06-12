PRINC = "1"
DEPENDS += "qemu-native"
inherit rootfs-gdk-pixbuf

postinst_pixbufloader () {
if [ "x$D" != "x" ]; then
    ${ROOTFS_GDK_PIXBUF}
    exit 0
fi

GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders gdk-pixbuf-query-loaders --update-cache
test -x ${bindir}/gtk-update-icon-cache && gtk-update-icon-cache  -q ${datadir}/icons/hicolor
}
