inherit xenclient
inherit xenclient-pq

PV = "${XEN_VERSION}"

SRC_URI = "${XEN_SRC_URI} \
	   ${OPENXT_GIT_MIRROR}/xen-common-pq.git;protocol=git;tag=${OPENXT_TAG} \
"

SRC_URI[md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[sha256sum] := "${XEN_SRC_SHA256SUM}"

S = "${WORKDIR}/xen-${PV}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e0f0f3ac55608719a82394cc353928df"

do_apply_patchqueue_prepend() {
	cat >${WORKDIR}/patchqueue/master/guards <<EOF
xt-only
EOF
}
