DESCRIPTION = "Tool to pass stdio over argo"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit update-rc.d

SRC_URI += "file://argo-exec.c \
            file://argo-input-sender \
            file://argo-input-sender-kick \
            file://argo-input-sender.rules \
            file://argo-input-receiver \
            file://argo-input-receiver.init \
"

DEPENDS = "xen-tools libargo"

S = "${WORKDIR}"

CFLAGS += "-Wall -Werror"
export LDLIBS="-largo"

PACKAGES =+ "argo-input-receiver argo-input-sender"

RDEPENDS_argo-input-receiver = "argo-exec qubes-input-proxy-receiver"
RDEPENDS_argo-input-sender = "argo-exec qubes-input-proxy-sender"

INITSCRIPT_PACKAGES="argo-input-receiver argo-input-sender"
INITSCRIPT_NAME_argo-input-receiver = "argo-input-receiver"
INITSCRIPT_PARAMS_argo-input-receiver = "defaults 50"
INITSCRIPT_NAME_argo-input-sender = "argo-input-sender-kick"
INITSCRIPT_PARAMS_argo-input-sender = "start 99 S ."

FILES_argo-input-sender = " \
    ${bindir}/argo-input-sender \
    ${sysconfdir}/udev/rules.d/argo-input-sender.rules \
    ${sysconfdir}/init.d/argo-input-sender-kick \
"
FILES_argo-input-receiver = " \
    ${bindir}/argo-input-receiver \
    ${sysconfdir}/init.d/argo-input-receiver \
"

do_compile() {
    oe_runmake argo-exec
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 argo-exec ${D}${bindir}
    install -m 0755 argo-input-sender ${D}${bindir}
    install -m 0755 argo-input-receiver ${D}${bindir}/argo-input-receiver

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 argo-input-receiver.init \
        ${D}${sysconfdir}/init.d/argo-input-receiver
    install -m 0755 argo-input-sender-kick ${D}${sysconfdir}/init.d

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0755 argo-input-sender.rules ${D}${sysconfdir}/udev/rules.d
}
