# Ported out from the netbase_4.21 recipe.  Still need to check how much of this is not done by upstream.

PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
  file://options \
  file://init \
  file://interfaces \
  file://if-pre-up.d \
  file://if-up.d \
  file://if-down.d \
  file://if-post-down.d \
  "
INITSCRIPT_PARAMS = "start 40 S . stop 40 0 6 1 ."

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${mandir}/man8
    install -d ${D}${sysconfdir}/network/if-pre-up.d
    install -d ${D}${sysconfdir}/network/if-up.d
    install -d ${D}${sysconfdir}/network/if-down.d
    install -d ${D}${sysconfdir}/network/if-post-down.d

    for dir in if-pre-up.d if-up.d if-down.d if-post-down.d
    do
        for script in `ls -1 "${WORKDIR}/${dir}"`
        do
            install -m 0755 "${WORKDIR}/${dir}/${script}" "${D}${sysconfdir}/network/${dir}"
        done
    done

    install -m 0644 ${WORKDIR}/options ${D}${sysconfdir}/network/options
    install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/networking
    install -m 0644 ${WORKDIR}/interfaces ${D}${sysconfdir}/network/interfaces
}


