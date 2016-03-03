FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

PR .= ".1"

SRC_URI += " \
    file://remove-xml-doc-gen.patch \
    file://Makefile.diff \
    file://build.conf.diff \
    file://virtual_domain_context.diff \
    file://policy.modules.conf.patch \
    file://policy.modules.conf_xt.diff \
    file://policy.modules.contrib.alsa.diff \
    file://policy.modules.contrib.brctl.diff \
    file://policy.modules.contrib.dmidecode.diff \
    file://policy.modules.contrib.dpkg.diff \
    file://policy.modules.contrib.firstboot.diff \
    file://policy.modules.contrib.logrotate.diff \
    file://policy.modules.contrib.qemu.diff \
    file://policy.modules.kernel.corecommands.diff \
    file://policy.modules.kernel.corenetwork.diff \
    file://policy.modules.kernel.devices.diff \
    file://policy.modules.kernel.domain.diff \
    file://policy.modules.kernel.filesystem.diff \
    file://policy.modules.kernel.kernel.diff \
    file://policy.modules.kernel.files.diff \
    file://policy.modules.kernel.storage.diff \
    file://policy.modules.kernel.terminal.diff \
    file://policy.modules.roles.staff.diff \
    file://policy.modules.roles.sysadm.diff \
    file://policy.modules.contrib.cron.diff \
    file://policy.modules.contrib.dbus.diff \
    file://policy.modules.contrib.dnsmasq.diff \
    file://policy.modules.contrib.hal.diff \
    file://policy.modules.contrib.networkmanager.diff \
    file://policy.modules.services.ssh.diff \
    file://policy.modules.contrib.virt.diff \
    file://policy.modules.system.authlogin.diff \
    file://policy.modules.system.fstools.diff \
    file://policy.modules.system.getty.diff \
    file://policy.modules.system.init.diff \
    file://policy.modules.system.libraries.diff \
    file://policy.modules.system.logging.diff \
    file://policy.modules.system.lvm.diff \
    file://policy.modules.system.miscfiles.diff \
    file://policy.modules.system.modutils.diff \
    file://policy.modules.system.mount.diff \
    file://policy.modules.system.selinuxutil.diff \
    file://policy.modules.system.sysnetwork.patch \
    file://policy.modules.system.udev.diff \
    file://policy.modules.system.unconfined.diff \
    file://policy.modules.system.userdomain.diff \
    file://policy.support.misc_patterns.spt.diff \
    file://policy.modules.contrib.xen.diff \
    file://policy.modules.contrib.tcsd.patch \
    file://policy.modules.contrib.networkmanager_xt.diff \
    file://policy.modules.system.init_xt.diff \
    file://apptool-interfaces.diff \
    file://blktap-interfaces.diff \
    file://db-cmd-interfaces.diff \
    file://dbd-interfaces.diff \
    file://getedid-interfaces.diff \
    file://input-server-interfaces.diff \
    file://network-daemon-interfaces.diff \
    file://statusreport-interfaces.diff \
    file://stubdom-helpers-interfaces.diff \
    file://surfman-interfaces.diff \
    file://sysutils-interfaces.diff \
    file://tcs-interfaces.diff \
    file://tpmsetup-interfaces.diff \
    file://txtstat-interfaces.diff \
    file://uid-interfaces.diff \
    file://updatemgr-interfaces.diff \
    file://vgmch-interfaces.diff \
    file://vhdutils-interfaces.diff \
    file://xc-files-interfaces.patch \
    file://xc-installer-interfaces.diff \
    file://xec-interfaces.diff \
    file://xsmutils-interfaces.diff \
    file://ctxusb-interfaces.diff \
    file://dbusbouncer-interfaces.diff \
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
    file://openxt_system_unconfined_readonly_neverallow_fixes.patch;patch=1 \
    "
    
def get_poltype(f):
    import re
    config = open (f, "r")
    regex = re.compile('^[\s]*SELINUXTYPE=[\s]*(\w+)[\s]*$')
    for line in config:
        match = regex.match(line)
        if match is not None:
            return match.group(1)
    return None

RDEPENDS_${PN} = ""
inherit xenclient

POLICY_NAME = "${POL_TYPE}"
POLICY_DISTRO = "debian"
POLICY_UBAC = "y"
POLICY_DIRECT_INITRC = "y"
POLICY_QUIET = "n"
POLICY_MLS_CATS = "256"

S = "${WORKDIR}/refpolicy"

FILES_${PN} += "${sysconfdir}/selinux"

# Just explicitly define the policy
POL_TYPE = "xc_policy"
#conf_file = "${THISDIR}/${PN}-${PV}/config"
#POL_TYPE = "${@get_poltype(conf_file)}"

do_install_append() {
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
