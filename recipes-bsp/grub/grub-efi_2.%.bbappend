FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://grub-efi-installer.cfg \
"

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

# grub-efi_2.%.bb defines do_deploy, but overrides do_deploy_class-native.
# Under these circumstances, _append will not work as it will append both
# class-target and class-native.
# Appending only class-target will override the target step altogether.
do_deploy() {
    install -m 0644 "${B}/${GRUB_IMAGE_PREFIX}${GRUB_IMAGE}" "${DEPLOYDIR}"
    install -m 0755 -d "${DEPLOYDIR}/iso"
    install -m 0644 "${WORKDIR}/grub-efi-installer.cfg" "${DEPLOYDIR}/iso/grub.cfg"
}
