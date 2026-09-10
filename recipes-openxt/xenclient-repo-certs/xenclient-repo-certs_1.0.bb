DESCRIPTION = "OpenXT repository certificates"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

# Optional; set in local.conf when signing feeds. Unset values must not
# appear in SRC_URI or parse-time checksums fail.
REPO_PROD_CACERT ?= ""
REPO_DEV_CACERT ?= ""

SRC_URI = "file://verify-repo-metadata"
SRC_URI += "${@'file://${REPO_PROD_CACERT}' if d.getVar('REPO_PROD_CACERT') else ''}"
SRC_URI += "${@'file://${REPO_DEV_CACERT}' if d.getVar('REPO_DEV_CACERT') else ''}"

FILES:${PN} = "${datadir}/xenclient/repo-certs \
               ${bindir}/verify-repo-metadata"

inherit allarch

do_install() {
    CERTDIR_PROD=${D}${datadir}/xenclient/repo-certs/prod
    CERTDIR_DEV=${D}${datadir}/xenclient/repo-certs/dev
    install -d ${CERTDIR_PROD}
    install -d ${CERTDIR_DEV}

    if [ -n "${REPO_PROD_CACERT}" ]; then
        install -m 0644 ${UNPACKDIR}/${REPO_PROD_CACERT} ${CERTDIR_PROD}/cert.pem
    fi
    if [ -n "${REPO_DEV_CACERT}" ]; then
        install -m 0644 ${UNPACKDIR}/${REPO_DEV_CACERT} ${CERTDIR_DEV}/cert.pem
    fi

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/verify-repo-metadata ${D}${bindir}/
}

RDEPENDS:${PN} += " \
    openssl-bin \
"
