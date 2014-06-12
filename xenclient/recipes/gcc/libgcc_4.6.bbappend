PRINC = "1"

do_install_append() {
    chmod a+r ${D}${base_libdir}/libgcc_s.so.*
}




