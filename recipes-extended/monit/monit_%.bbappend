FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# In dom0, monit controls vglass, so we only want monit to run in
# runlevel 5, which matches vglass, disman & ivcdaemon.
INITSCRIPT_PARAMS_${PN}_xenclient-dom0 = "start 99 5 . stop 01 0 1 2 3 4 6 ."

SRC_URI += " \
    file://volatiles \
"

do_install_append() {
    install -d -m 700 ${D}${sysconfdir}/default/volatiles
    install -m 600 ${WORKDIR}/volatiles \
        ${D}${sysconfdir}/default/volatiles/50_monit
}
