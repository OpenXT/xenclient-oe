FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI += " \
    file://openxt-menus.patch \
    file://disable-available-to-all-users-checkbox.patch \
    file://always-use-psk-hash.patch \
    file://disable-show-password.patch \
    file://disable-auto-ethernet.patch \
    file://org.openxt.nmapplet.xml \
"

do_configure_prepend() {
    gdbus-codegen --generate-c-code ${S}/src/popup-menu --c-namespace OpenXT --interface-prefix org.openxt. ${WORKDIR}/org.openxt.nmapplet.xml
}
