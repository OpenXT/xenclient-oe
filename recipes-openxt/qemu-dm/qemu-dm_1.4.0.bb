require qemu-dm.inc

#Non-stubdom specific patches here, common patches belong in qemu-dm.inc
SRC_URI += " \
    file://0025-Enable-changing-of-ISO-media-in-non-stubdom-device-m.patch;striplevel=1 \
    "

EXTRA_OECONF += "--enable-debug --disable-strip --audio-drv-list=alsa "

PR = "${INC_PR}.4"
