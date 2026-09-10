DESCRIPTION = "XenClient EULA"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://EULA-en-us"

FILES:${PN} = "${datadir}/xenclient"

# Check the en-us version of the EULA doesn't contain non-ASCII characters -
# the installer can't display them.
do_compile() {
    if LC_ALL=C grep -n "[^[:print:]]" ${UNPACKDIR}/EULA-en-us > /dev/null ; then
        echo "ERROR: EULA-en-us contains non-ASCII characters:" >&2
        LC_ALL=C grep -n "[^[:print:]]" ${UNPACKDIR}/EULA-en-us >&2
        false
    fi
}

do_install() {
    install -d ${D}${datadir}/xenclient
    for i in en-us ; do
        install -m 0644 ${UNPACKDIR}/EULA-$i ${D}${datadir}/xenclient/
    done
}
