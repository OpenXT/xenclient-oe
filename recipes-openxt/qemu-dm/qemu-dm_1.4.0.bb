require qemu-dm.inc

EXTRA_OECONF += "--enable-debug --disable-strip --audio-drv-list=alsa --enable-openxt-iso"

PR = "${INC_PR}.5"
