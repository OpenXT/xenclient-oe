FILESEXTRA := "${THISDIR}/${PN}"
FILESEXTRAPATHS_prepend := "${FILESEXTRA}:"

SRCREV_modules = "${AUTOREV}"

# FIXME: We really should be setting this, but doing so breaks the base include which expects PV=2.20130424
# PV = "2.20130424+git${SRCPV}"

SRC_URI += "git://${OPENXT_GIT_MIRROR}/selinux-policy.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};destsuffix=modules;name=modules \
    file://gitignore.diff;patch=1 \
    file://remove-xml-doc-gen.patch;patch=1 \
    file://Makefile.diff;patch=1 \
    file://build.conf.diff;patch=1 \
    file://virtual_domain_context.diff;patch=1 \
    file://policy.modules.conf.diff;patch=1 \
    file://policy.modules.conf_xt.diff;patch=1 \
    file://policy.modules.contrib.alsa.diff;patch=1 \
    file://policy.modules.contrib.brctl.diff;patch=1 \
    file://policy.modules.contrib.dmidecode.diff;patch=1 \
    file://policy.modules.contrib.dpkg.diff;patch=1 \
    file://policy.modules.contrib.firstboot.diff;patch=1 \
    file://policy.modules.contrib.logrotate.diff;patch=1 \
    file://policy.modules.contrib.loadkeys.diff;patch=1 \
    file://policy.modules.contrib.qemu.diff;patch=1 \
    file://policy.modules.kernel.corecommands.diff;patch=1 \
    file://policy.modules.kernel.corenetwork.diff;patch=1 \
    file://policy.modules.kernel.devices.diff;patch=1 \
    file://policy.modules.kernel.domain.diff;patch=1 \
    file://policy.modules.kernel.filesystem.diff;patch=1 \
    file://policy.modules.kernel.kernel.diff;patch=1 \
    file://policy.modules.kernel.files.diff;patch=1 \
    file://policy.modules.kernel.storage.diff;patch=1 \
    file://policy.modules.kernel.terminal.diff;patch=1 \
    file://policy.modules.roles.staff.diff;patch=1 \
    file://policy.modules.roles.sysadm.diff;patch=1 \
    file://policy.modules.contrib.cron.diff;patch=1 \
    file://policy.modules.contrib.dbus.diff;patch=1 \
    file://policy.modules.contrib.dnsmasq.diff;patch=1 \
    file://policy.modules.contrib.hal.diff;patch=1 \
    file://policy.modules.contrib.networkmanager.diff;patch=1 \
    file://policy.modules.services.ssh.diff;patch=1 \
    file://policy.modules.contrib.virt.diff;patch=1 \
    file://policy.modules.system.authlogin.diff;patch=1 \
    file://policy.modules.system.fstools.diff;patch=1 \
    file://policy.modules.system.getty.diff;patch=1 \
    file://policy.modules.system.init.diff;patch=1 \
    file://policy.modules.system.libraries.diff;patch=1 \
    file://policy.modules.system.logging.diff;patch=1 \
    file://policy.modules.system.lvm.diff;patch=1 \
    file://policy.modules.system.miscfiles.diff;patch=1 \
    file://policy.modules.system.modutils.diff;patch=1 \
    file://policy.modules.system.mount.diff;patch=1 \
    file://policy.modules.system.selinuxutil.diff;patch=1 \
    file://policy.modules.system.sysnetwork.diff;patch=1 \
    file://policy.modules.system.udev.diff;patch=1 \
    file://policy.modules.system.unconfined.diff;patch=1 \
    file://policy.modules.system.userdomain.diff;patch=1 \
    file://policy.support.misc_patterns.spt.diff;patch=1 \
    file://policy.modules.contrib.xen.diff;patch=1 \
    file://policy.modules.contrib.tcsd.diff;patch=1 \
    file://policy.modules.contrib.networkmanager_xt.diff;patch=1 \
    file://policy.modules.system.init_xt.diff;patch=1 \
    file://apptool-interfaces.diff;patch=1 \
    file://blktap-interfaces.diff;patch=1 \
    file://db-cmd-interfaces.diff;patch=1 \
    file://dbd-interfaces.diff;patch=1 \
    file://getedid-interfaces.diff;patch=1 \
    file://input-server-interfaces.diff;patch=1 \
    file://network-daemon-interfaces.diff;patch=1 \
    file://statusreport-interfaces.diff;patch=1 \
    file://stubdom-helpers-interfaces.diff;patch=1 \
    file://surfman-interfaces.diff;patch=1 \
    file://sysutils-interfaces.diff;patch=1 \
    file://tcs-interfaces.diff;patch=1 \
    file://tpmsetup-interfaces.diff;patch=1 \
    file://txtstat-interfaces.diff;patch=1 \
    file://uid-interfaces.diff;patch=1 \
    file://updatemgr-interfaces.diff;patch=1 \
    file://vgmch-interfaces.diff;patch=1 \
    file://vhdutils-interfaces.diff;patch=1 \
    file://xc-files-interfaces.diff;patch=1 \
    file://xc-installer-interfaces.diff;patch=1 \
    file://xec-interfaces.diff;patch=1 \
    file://xsmutils-interfaces.diff;patch=1 \
    file://ctxusb-interfaces.diff;patch=1 \
    file://dbusbouncer-interfaces.diff;patch=1 \
    file://config \
"

RDEPENDS_${PN} = ""
inherit xenclient
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

do_configure_prepend() {
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
    if [ -z "$D" ]; then
        /sbin/setfiles /etc/selinux/${POL_TYPE}/contexts/files/file_contexts /
    fi
}

pkg_postinst_${PN}_append_xenclient-dom0 () {
    if [ -z "$D" ]; then
        /sbin/setfiles /etc/selinux/${POL_TYPE}/contexts/files/file_contexts /config /storage
    fi
}
