inherit xenclient
inherit xenclient-pq

SRCREV_FORMAT = "source_patchqueue"

PV = "4.3.2+git${SRCPV}"

SRCREV_source = "RELEASE-4.3.2"
SRCREV_patchqueue = "4400dd3b3505ffa69ecd5b69ff4be582bc4164f3"

SRC_URI = "git://xenbits.xen.org/xen.git;protocol=git;name=source \
	  git://github.com/openxt/xen-common-pq.git;protocol=https;destsuffix=patchqueue;name=patchqueue \
"

S = "${WORKDIR}/git"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e0f0f3ac55608719a82394cc353928df"

do_apply_patchqueue_prepend() {
	cat >${WORKDIR}/patchqueue/master/guards <<EOF
xt-only
EOF
}
