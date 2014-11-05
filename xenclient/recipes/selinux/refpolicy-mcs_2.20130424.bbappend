FILESEXTRA := "${THISDIR}/${PN}"
FILESEXTRAPATHS_prepend := "${FILESEXTRA}:"

SRCREV_FORMAT = "modules_patchqueue"

SRCREV_modules = "${AUTOREV}"
SRCREV_patchqueue = "${AUTOREV}"

# FIXME: We really should be setting this, but doing so breaks the base include which expects PV=2.20130424
# PV = "2.20130424+git${SRCPV}"

SRC_URI += "git://${OPENXT_GIT_MIRROR}/selinux-policy.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};destsuffix=modules;name=modules \
        git://${OPENXT_GIT_MIRROR}/refpolicy-xt-pq.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};destsuffix=patchqueue;name=patchqueue \
        file://config \
"

RDEPENDS_${PN} = ""
inherit xenclient
inherit xenclient-pq
require selinux-policy-common.inc

POLICY_NAME = "${POL_TYPE}"
POLICY_DISTRO = "debian"
POLICY_UBAC = "y"
POLICY_DIRECT_INITRC = "y"
POLICY_QUIET = "n"
POLICY_MLS_CATS = "256"

S = "${WORKDIR}/refpolicy"
MODS_DIR = "${WORKDIR}/modules"

FILES_${PN} += "/selinux ${sysconfdir}/selinux ${datadir}/selinux/*/*.bz2"

EXTRA_OEMAKE += ' -j 1 BINDIR="${STAGING_BINDIR_NATIVE}" SETFILES=true '

conf_file = "${FILESEXTRA}/config"
POL_TYPE = "${@get_poltype(conf_file)}"

do_apply_patchqueue_prepend() {
        find ${MODS_DIR} -name '*.fc' -o -name '*.if' -o -name '*.te' | while read MOD_FILE; do
                DIR_PART=$(echo ${MOD_FILE} | grep -o 'policy/modules/[0-9a-zA-Z_\-]\+/')
                cp ${MOD_FILE} ${S}/${DIR_PART}
        done
}

do_install_append() {
        install -d ${D}/selinux
        install -d ${D}/etc/selinux
        install -m 644 ${WORKDIR}/config ${D}/etc/selinux/config
}

sysroot_stage_all_append () {
	sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}

pkg_postinst_${PN} () {
	/sbin/setfiles /etc/selinux/${POL_TYPE}/contexts/files/file_contexts /
}

pkg_postinst_${PN}_xenclient-ndvm () {
    if [ -z "$D" ];then
        /sbin/setfiles /etc/selinux/${POL_TYPE}/contexts/files/file_contexts /
    fi
}

pkg_postinst_${PN}_append_xenclient-dom0 () {
	/sbin/setfiles /etc/selinux/${POL_TYPE}/contexts/files/file_contexts /config /storage
}
