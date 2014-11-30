DESCRIPTION = "XenClient EULA"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://EULA-en-us"

FILES_${PN} = "${datadir}/xenclient"

inherit xenclient

# Check the en-us version of the EULA doesn't contain non-ASCII characters -
# the installer can't display them.
do_compile() {
    if LC_ALL=C grep -n "[^[:print:]]" ${WORKDIR}/EULA-en-us > /dev/null ; then
        echo "ERROR: EULA-en-us contains non-ASCII characters:" >&2
        LC_ALL=C grep -n "[^[:print:]]" ${WORKDIR}/EULA-en-us >&2
        false
    fi
}

do_install() {
    install -d ${D}${datadir}/xenclient
    for i in en-us ; do
        install -m 0644 ${WORKDIR}/EULA-$i ${D}${datadir}/xenclient/
    done
}
