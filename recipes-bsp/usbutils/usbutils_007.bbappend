PR .= ".1"

do_install_append () {
    gunzip -c ${D}${datadir}/usb.ids.gz > ${D}${datadir}/usb.ids
}
