FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

PR .= ".1"

SRC_URI += " \
    file://config \
    file://policy/modules/admin/apptool.fc \
    file://policy/modules/admin/apptool.if \
    file://policy/modules/admin/apptool.te \
    file://policy/modules/admin/getedid.fc \
    file://policy/modules/admin/getedid.if \
    file://policy/modules/admin/getedid.te \
    file://policy/modules/admin/statusreport.fc \
    file://policy/modules/admin/statusreport.if \
    file://policy/modules/admin/statusreport.te \
    file://policy/modules/admin/sysutils.fc \
    file://policy/modules/admin/sysutils.if \
    file://policy/modules/admin/sysutils.te \
    file://policy/modules/admin/tpmsetup.fc \
    file://policy/modules/admin/tpmsetup.if \
    file://policy/modules/admin/tpmsetup.te \
    file://policy/modules/admin/txtstat.fc \
    file://policy/modules/admin/txtstat.if \
    file://policy/modules/admin/txtstat.te \
    file://policy/modules/admin/vhdutils.fc \
    file://policy/modules/admin/vhdutils.if \
    file://policy/modules/admin/vhdutils.te \
    file://policy/modules/admin/xsmutils.fc \
    file://policy/modules/admin/xsmutils.if \
    file://policy/modules/admin/xsmutils.te \
    file://policy/modules/apps/db-cmd.fc \
    file://policy/modules/apps/db-cmd.if \
    file://policy/modules/apps/db-cmd.te \
    file://policy/modules/apps/xec.fc \
    file://policy/modules/apps/xec.if \
    file://policy/modules/apps/xec.te \
    file://policy/modules/contrib/tpmutil.fc \
    file://policy/modules/contrib/tpmutil.if \
    file://policy/modules/contrib/tpmutil.te \
    file://policy/modules/services/blktap.fc \
    file://policy/modules/services/blktap.if \
    file://policy/modules/services/blktap.te \
    file://policy/modules/services/ctxusb.fc \
    file://policy/modules/services/ctxusb.if \
    file://policy/modules/services/ctxusb.te \
    file://policy/modules/services/dbd.fc \
    file://policy/modules/services/dbd.if \
    file://policy/modules/services/dbd.te \
    file://policy/modules/services/dbusbouncer.fc \
    file://policy/modules/services/dbusbouncer.if \
    file://policy/modules/services/dbusbouncer.te \
    file://policy/modules/services/dm-agent.fc \
    file://policy/modules/services/dm-agent.if \
    file://policy/modules/services/dm-agent.te \
    file://policy/modules/services/icbinn.fc \
    file://policy/modules/services/icbinn.if \
    file://policy/modules/services/icbinn.te \
    file://policy/modules/services/input-server.fc \
    file://policy/modules/services/input-server.if \
    file://policy/modules/services/input-server.te \
    file://policy/modules/services/language-sync.fc \
    file://policy/modules/services/language-sync.if \
    file://policy/modules/services/language-sync.te \
    file://policy/modules/services/network-daemon.fc \
    file://policy/modules/services/network-daemon.if \
    file://policy/modules/services/network-daemon.te \
    file://policy/modules/services/surfman.fc \
    file://policy/modules/services/surfman.if \
    file://policy/modules/services/surfman.te \
    file://policy/modules/services/uid.fc \
    file://policy/modules/services/uid.if \
    file://policy/modules/services/uid.te \
    file://policy/modules/services/updatemgr.fc \
    file://policy/modules/services/updatemgr.if \
    file://policy/modules/services/updatemgr.te \
    file://policy/modules/services/xenpmd.fc \
    file://policy/modules/services/xenpmd.if \
    file://policy/modules/services/xenpmd.te \
    file://policy/modules/services/xmlstore.fc \
    file://policy/modules/services/xmlstore.if \
    file://policy/modules/services/xmlstore.te \
    file://policy/modules/system/pcm-config.fc \
    file://policy/modules/system/pcm-config.if \
    file://policy/modules/system/pcm-config.te \
    file://policy/modules/system/stubdom-helpers.fc \
    file://policy/modules/system/stubdom-helpers.if \
    file://policy/modules/system/stubdom-helpers.te \
    file://policy/modules/system/vgmch.fc \
    file://policy/modules/system/vgmch.if \
    file://policy/modules/system/vgmch.te \
    file://policy/modules/system/xc-files.fc \
    file://policy/modules/system/xc-files.if \
    file://policy/modules/system/xc-files.te \
    file://policy/modules/system/xc-installer.fc \
    file://policy/modules/system/xc-installer.if \
    file://policy/modules/system/xc-installer.te \
    file://patches/remove-xml-doc-gen.patch \
    file://patches/Makefile.diff \
    file://patches/build.conf.diff \
    file://patches/virtual_domain_context.diff \
    file://patches/policy.modules.conf.patch \
    file://patches/policy.modules.conf_xt.diff \
    file://patches/policy.modules.contrib.alsa.diff \
    file://patches/policy.modules.contrib.brctl.diff \
    file://patches/policy.modules.contrib.dmidecode.diff \
    file://patches/policy.modules.contrib.dpkg.diff \
    file://patches/policy.modules.contrib.firstboot.diff \
    file://patches/policy.modules.contrib.logrotate.diff \
    file://patches/policy.modules.contrib.qemu.diff \
    file://patches/policy.modules.kernel.corecommands.diff \
    file://patches/policy.modules.kernel.corenetwork.diff \
    file://patches/policy.modules.kernel.devices.diff \
    file://patches/policy.modules.kernel.domain.diff \
    file://patches/policy.modules.kernel.filesystem.diff \
    file://patches/policy.modules.kernel.kernel.diff \
    file://patches/policy.modules.kernel.files.diff \
    file://patches/policy.modules.kernel.storage.diff \
    file://patches/policy.modules.kernel.terminal.diff \
    file://patches/policy.modules.roles.staff.diff \
    file://patches/policy.modules.roles.sysadm.diff \
    file://patches/policy.modules.contrib.cron.diff \
    file://patches/policy.modules.contrib.dbus.diff \
    file://patches/policy.modules.contrib.dnsmasq.diff \
    file://patches/policy.modules.contrib.hal.diff \
    file://patches/policy.modules.contrib.networkmanager.diff \
    file://patches/policy.modules.services.ssh.diff \
    file://patches/policy.modules.contrib.virt.diff \
    file://patches/policy.modules.system.authlogin.diff \
    file://patches/policy.modules.system.fstools.diff \
    file://patches/policy.modules.system.getty.diff \
    file://patches/policy.modules.system.init.diff \
    file://patches/policy.modules.system.libraries.diff \
    file://patches/policy.modules.system.logging.diff \
    file://patches/policy.modules.system.lvm.diff \
    file://patches/policy.modules.system.miscfiles.diff \
    file://patches/policy.modules.system.modutils.diff \
    file://patches/policy.modules.system.mount.diff \
    file://patches/policy.modules.system.selinuxutil.diff \
    file://patches/policy.modules.system.sysnetwork.patch \
    file://patches/policy.modules.system.udev.diff \
    file://patches/policy.modules.system.unconfined.diff \
    file://patches/policy.modules.system.userdomain.diff \
    file://patches/policy.support.misc_patterns.spt.diff \
    file://patches/policy.modules.contrib.xen.diff \
    file://patches/policy.modules.contrib.tcsd.patch \
    file://patches/policy.modules.contrib.networkmanager_xt.diff \
    file://patches/policy.modules.system.init_xt.diff \
    file://patches/apptool-interfaces.diff \
    file://patches/blktap-interfaces.diff \
    file://patches/db-cmd-interfaces.diff \
    file://patches/dbd-interfaces.diff \
    file://patches/getedid-interfaces.diff \
    file://patches/input-server-interfaces.diff \
    file://patches/network-daemon-interfaces.diff \
    file://patches/statusreport-interfaces.diff \
    file://patches/stubdom-helpers-interfaces.diff \
    file://patches/surfman-interfaces.diff \
    file://patches/sysutils-interfaces.diff \
    file://patches/tcs-interfaces.diff \
    file://patches/tpmsetup-interfaces.diff \
    file://patches/txtstat-interfaces.diff \
    file://patches/uid-interfaces.diff \
    file://patches/updatemgr-interfaces.diff \
    file://patches/vgmch-interfaces.diff \
    file://patches/vhdutils-interfaces.diff \
    file://patches/xc-files-interfaces.patch \
    file://patches/xc-installer-interfaces.diff \
    file://patches/xec-interfaces.diff \
    file://patches/xsmutils-interfaces.diff \
    file://patches/ctxusb-interfaces.diff \
    file://patches/dbusbouncer-interfaces.diff \
    file://patches/qemu1.4_wrapper_file_context.patch;striplevel=1 \
    file://patches/openxt_system_unconfined_readonly_neverallow_fixes.patch;patch=1 \
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

do_compile_prepend() {
        cp -r ${WORKDIR}/policy ${S}/
}

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
