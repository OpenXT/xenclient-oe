PRINC = "1"
pkg_postinst() {
    if [ -n "$D" ]; then
        exit 0 
    fi
}
