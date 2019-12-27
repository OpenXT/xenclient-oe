FILESEXTRAPATHS_prepend := "${THISDIR}/files-openxt:"

GRUB_BUILDIN = " \
    all_video boot btrfs cat chain configfile echo \
    efinet ext2 fat font gfxmenu gfxterm gzio halt \
    hfsplus iso9660 jpeg loadenv loopback lvm mdraid09 mdraid1x \
    minicmd normal part_apple part_msdos part_gpt \
    password_pbkdf2 png \
    reboot search search_fs_uuid search_fs_file search_label \
    serial sleep test tftp video xfs \
    linux backtrace usb usbserial_common \
    usbserial_pl2303 usbserial_ftdi \
    multiboot multiboot2 \
"

EXTRA_OECONF += " \
    --enable-efiemu=no \
"
