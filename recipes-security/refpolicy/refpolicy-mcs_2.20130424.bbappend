FILESEXTRA := "${THISDIR}/${PN}"
FILESEXTRAPATHS_prepend := "${FILESEXTRA}:"

PR = "r1"

SRC_URI += " \
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
    file://openxt_policy_modules_admin_apptool.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_apptool.if.patch;patch=1 \
    file://openxt_policy_modules_admin_apptool.te.patch;patch=1 \
    file://openxt_policy_modules_admin_getedid.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_getedid.if.patch;patch=1 \
    file://openxt_policy_modules_admin_getedid.te.patch;patch=1 \
    file://openxt_policy_modules_admin_statusreport.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_statusreport.if.patch;patch=1 \
    file://openxt_policy_modules_admin_statusreport.te.patch;patch=1 \
    file://openxt_policy_modules_admin_sysutils.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_sysutils.if.patch;patch=1 \
    file://openxt_policy_modules_admin_sysutils.te.patch;patch=1 \
    file://openxt_policy_modules_admin_tpmsetup.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_tpmsetup.if.patch;patch=1 \
    file://openxt_policy_modules_admin_tpmsetup.te.patch;patch=1 \
    file://openxt_policy_modules_admin_txtstat.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_txtstat.if.patch;patch=1 \
    file://openxt_policy_modules_admin_txtstat.te.patch;patch=1 \
    file://openxt_policy_modules_admin_vhdutils.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_vhdutils.if.patch;patch=1 \
    file://openxt_policy_modules_admin_vhdutils.te.patch;patch=1 \
    file://openxt_policy_modules_admin_xsmutils.fc.patch;patch=1 \
    file://openxt_policy_modules_admin_xsmutils.if.patch;patch=1 \
    file://openxt_policy_modules_admin_xsmutils.te.patch;patch=1 \
    file://openxt_policy_modules_apps_db-cmd.fc.patch;patch=1 \
    file://openxt_policy_modules_apps_db-cmd.if.patch;patch=1 \
    file://openxt_policy_modules_apps_db-cmd.te.patch;patch=1 \
    file://openxt_policy_modules_apps_xec.fc.patch;patch=1 \
    file://openxt_policy_modules_apps_xec.if.patch;patch=1 \
    file://openxt_policy_modules_apps_xec.te.patch;patch=1 \
    file://openxt_policy_modules_contrib_alsa.te.patch;patch=1 \
    file://openxt_policy_modules_contrib_alsa.fc.patch;patch=1 \
    file://openxt_policy_modules_contrib_tpmutil.fc.patch;patch=1 \
    file://openxt_policy_modules_contrib_tpmutil.if.patch;patch=1 \
    file://openxt_policy_modules_contrib_tpmutil.te.patch;patch=1 \
    file://openxt_policy_modules_services_blktap.fc.patch;patch=1 \
    file://openxt_policy_modules_services_blktap.if.patch;patch=1 \
    file://openxt_policy_modules_services_blktap.te.patch;patch=1 \
    file://openxt_policy_modules_services_ctxusb.fc.patch;patch=1 \
    file://openxt_policy_modules_services_ctxusb.if.patch;patch=1 \
    file://openxt_policy_modules_services_ctxusb.te.patch;patch=1 \
    file://openxt_policy_modules_services_dbd.fc.patch;patch=1 \
    file://openxt_policy_modules_services_dbd.if.patch;patch=1 \
    file://openxt_policy_modules_services_dbd.te.patch;patch=1 \
    file://openxt_policy_modules_services_dbusbouncer.fc.patch;patch=1 \
    file://openxt_policy_modules_services_dbusbouncer.if.patch;patch=1 \
    file://openxt_policy_modules_services_dbusbouncer.te.patch;patch=1 \
    file://openxt_policy_modules_services_dm-agent.fc.patch;patch=1 \
    file://openxt_policy_modules_services_dm-agent.if.patch;patch=1 \
    file://openxt_policy_modules_services_dm-agent.te.patch;patch=1 \
    file://openxt_policy_modules_services_icbinn.fc.patch;patch=1 \
    file://openxt_policy_modules_services_icbinn.if.patch;patch=1 \
    file://openxt_policy_modules_services_icbinn.te.patch;patch=1 \
    file://openxt_policy_modules_services_input-server.fc.patch;patch=1 \
    file://openxt_policy_modules_services_input-server.if.patch;patch=1 \
    file://openxt_policy_modules_services_input-server.te.patch;patch=1 \
    file://openxt_policy_modules_services_language-sync.fc.patch;patch=1 \
    file://openxt_policy_modules_services_language-sync.if.patch;patch=1 \
    file://openxt_policy_modules_services_language-sync.te.patch;patch=1 \
    file://openxt_policy_modules_services_network-daemon.fc.patch;patch=1 \
    file://openxt_policy_modules_services_network-daemon.if.patch;patch=1 \
    file://openxt_policy_modules_services_network-daemon.te.patch;patch=1 \
    file://openxt_policy_modules_services_surfman.fc.patch;patch=1 \
    file://openxt_policy_modules_services_surfman.if.patch;patch=1 \
    file://openxt_policy_modules_services_surfman.te.patch;patch=1 \
    file://openxt_policy_modules_services_uid.fc.patch;patch=1 \
    file://openxt_policy_modules_services_uid.if.patch;patch=1 \
    file://openxt_policy_modules_services_uid.te.patch;patch=1 \
    file://openxt_policy_modules_services_updatemgr.fc.patch;patch=1 \
    file://openxt_policy_modules_services_updatemgr.if.patch;patch=1 \
    file://openxt_policy_modules_services_updatemgr.te.patch;patch=1 \
    file://openxt_policy_modules_services_xenpmd.fc.patch;patch=1 \
    file://openxt_policy_modules_services_xenpmd.if.patch;patch=1 \
    file://openxt_policy_modules_services_xenpmd.te.patch;patch=1 \
    file://openxt_policy_modules_services_xmlstore.fc.patch;patch=1 \
    file://openxt_policy_modules_services_xmlstore.if.patch;patch=1 \
    file://openxt_policy_modules_services_xmlstore.te.patch;patch=1 \
    file://openxt_policy_modules_system_pcm-config.fc.patch;patch=1 \
    file://openxt_policy_modules_system_pcm-config.if.patch;patch=1 \
    file://openxt_policy_modules_system_pcm-config.te.patch;patch=1 \
    file://openxt_policy_modules_system_stubdom-helpers.fc.patch;patch=1 \
    file://openxt_policy_modules_system_stubdom-helpers.if.patch;patch=1 \
    file://openxt_policy_modules_system_stubdom-helpers.te.patch;patch=1 \
    file://openxt_policy_modules_system_vgmch.fc.patch;patch=1 \
    file://openxt_policy_modules_system_vgmch.if.patch;patch=1 \
    file://openxt_policy_modules_system_vgmch.te.patch;patch=1 \
    file://openxt_policy_modules_system_xc-files.fc.patch;patch=1 \
    file://openxt_policy_modules_system_xc-files.if.patch;patch=1 \
    file://openxt_policy_modules_system_xc-files.te.patch;patch=1 \
    file://openxt_policy_modules_system_xc-installer.fc.patch;patch=1 \
    file://openxt_policy_modules_system_xc-installer.if.patch;patch=1 \
    file://openxt_policy_modules_system_xc-installer.te.patch;patch=1 \
    file://qemu1.4_wrapper_file_context.patch;striplevel=1 \
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

FILES_${PN} += "/selinux ${sysconfdir}/selinux ${datadir}/selinux/*/*.bz2"

EXTRA_OEMAKE += ' -j 1 BINDIR="${STAGING_BINDIR_NATIVE}" SETFILES=true '

conf_file = "${FILESEXTRA}/config"
POL_TYPE = "${@get_poltype(conf_file)}"

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
