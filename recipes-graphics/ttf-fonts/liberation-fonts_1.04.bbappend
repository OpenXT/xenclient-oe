PR .= ".1"

pkg_postinst_${PN}() {
    if [ -z "$D" ]; then
        update-fonts 
    fi
}
