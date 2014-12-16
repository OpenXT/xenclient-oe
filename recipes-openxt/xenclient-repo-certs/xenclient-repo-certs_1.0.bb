DESCRIPTION = "OpenXT repository certificates"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://prod-cacert.pem;localpath=${REPO_PROD_CACERT} \
           file://dev-cacert.pem;localpath=${REPO_DEV_CACERT} \
           file://verify-repo-metadata"

FILES_${PN} = "${datadir}/xenclient/repo-certs \
               ${bindir}/verify-repo-metadata"

inherit xenclient

do_install() {
    install -d ${D}${datadir}/xenclient/repo-certs/prod

    for i in prod dev ; do
        CERTDIR=${D}${datadir}/xenclient/repo-certs/$i

        install -d ${CERTDIR}
        install -m 0644 ${WORKDIR}/$i-cacert.pem ${CERTDIR}/cert.pem
    done

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/verify-repo-metadata ${D}${bindir}/
}
