DESCRIPTION = "OpenXT repository certificates"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://${REPO_PROD_CACERT} \
           file://${REPO_DEV_CACERT} \
           file://verify-repo-metadata"

FILES_${PN} = "${datadir}/xenclient/repo-certs \
               ${bindir}/verify-repo-metadata"

inherit allarch xenclient

do_install() {
    CERTDIR_PROD=${D}${datadir}/xenclient/repo-certs/prod
    CERTDIR_DEV=${D}${datadir}/xenclient/repo-certs/dev
    install -d ${CERTDIR_PROD}
    install -d ${CERTDIR_DEV}

    install -m 0644 ${WORKDIR}/${REPO_PROD_CACERT} ${CERTDIR_PROD}/cert.pem
    install -m 0644 ${WORKDIR}/${REPO_DEV_CACERT} ${CERTDIR_DEV}/cert.pem

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/verify-repo-metadata ${D}${bindir}/
}
