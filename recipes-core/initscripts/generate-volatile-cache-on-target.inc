pkg_postinst_${PN}_append() {
    if [ -n "$D" ]; then
        $D/etc/init.d/populate-volatile.sh update
    fi
}
