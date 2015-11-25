PR .= ".1"

inherit qemu

DEPENDS += "qemu-native"

pkg_postinst_matchbox-keyboard-im () {
    if [ -n "$D" ]; then
        PSEUDO_RELOADED=YES ${@qemu_target_binary(d)} -E LD_LIBRARY_PATH=$D/lib:$D/usr/lib -E GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders -E LD_PRELOAD= -L $D $D${bindir}/gtk-query-immodules-2.0 > $D/etc/gtk-2.0/gtk.immodules
    else
        gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules
    fi
}

