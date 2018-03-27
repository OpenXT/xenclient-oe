FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
    file://dnsmasq_dnsout_interface.patch;patch=1 \
"

# we don't want all the "goodies" in dnsmasq package,
# instead we split the package into dnsmasq-full containing everything
# and very minimal dnsmasq containing just a single binary
PACKAGES =+ "${PN}-full"
INITSCRIPT_PACKAGES = "${PN}-full"
RDEPENDS_${PN}-full = "${PN}"
FILES_${PN}-full = "${sysconfdir}/*"
CONFFILES_${PN}-full = "${sysconfdir}/dnsmasq.conf"
CONFFILES_${PN} = ""
