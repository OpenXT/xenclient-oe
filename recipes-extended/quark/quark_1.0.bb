SUMMARY = "An extremely small and simple HTTP GET/HEAD-only web server for static content"
HOMEPAGE = "https://tools.suckless.org/quark/"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a15ae487f2479a5b23430b7e876244e3"

SECTION = "net"

SRCREV = "e299e186edba03192fc12f6709df48d02aa83849"

S = "${WORKDIR}/git/"

SRC_URI = "git://git.suckless.org/quark;protocol=ssh;branch=master \
           file://add-POST-and-argo-support.patch \
           file://quark.initscript \
        "

inherit update-rc.d

INITSCRIPT_NAME = "quark"
INITSCRIPT_PARAMS = "defaults 81"

FILES_${PN} = " \
    ${bindir}/quark \
    ${sysconfdir}/init.d/quark \
"

do_install() {
    oe_runmake DESTDIR=${D} install

    # initscript
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/quark.initscript ${D}${sysconfdir}/init.d/quark
}
