PR .= ".1"

pkg_postinst_${PN}() {
    if [ -n "$D" ]; then
        exit 0 
    fi
}
