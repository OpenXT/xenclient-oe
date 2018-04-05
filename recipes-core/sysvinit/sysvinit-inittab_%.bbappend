
do_install_append() {
    sed -i '/getty 38400 tty/s/12345:respawn/3:respawn/' \
        ${D}${sysconfdir}/inittab
}
