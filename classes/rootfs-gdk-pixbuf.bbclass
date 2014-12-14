# keep rootfs pixbuff generation code in one place as it's used by more than one file
inherit qemu
ROOTFS_GDK_PIXBUF = "PSEUDO_RELOADED=YES ${@qemu_target_binary(d)} -E LD_LIBRARY_PATH=$D/lib:$D/usr/lib -E GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders -E LD_PRELOAD= -L $D $D${bindir}/gdk-pixbuf-query-loaders > $D${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders.cache"
