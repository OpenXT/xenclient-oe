def get_tls_setting(bb, d):
    # until we have no prober TLS support in uclibc disable it
    if d.getVar('TARGET_OS', 1).find('uclibc') >= 0 :
        return ""
    return "--enable-glx-tls"

EXTRA_OECONF += "${@get_tls_setting(bb, d)}"
