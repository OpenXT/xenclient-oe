FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

# Configuration.
SRC_URI += " \
    file://policy/modules-upstream.conf \
    file://policy/modules-openxt.conf \
"
# Openxt additional modules.
SRC_URI += " \
    file://policy/modules/admin/statusreport.fc \
    file://policy/modules/admin/statusreport.if \
    file://policy/modules/admin/statusreport.te \
    file://policy/modules/admin/sysutils.fc \
    file://policy/modules/admin/sysutils.if \
    file://policy/modules/admin/sysutils.te \
    file://policy/modules/admin/tpmsetup.fc \
    file://policy/modules/admin/tpmsetup.if \
    file://policy/modules/admin/tpmsetup.te \
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
    file://policy/modules/apps/tpmutil.fc \
    file://policy/modules/apps/tpmutil.if \
    file://policy/modules/apps/tpmutil.te \
    file://policy/modules/services/blktap.fc \
    file://policy/modules/services/blktap.if \
    file://policy/modules/services/blktap.te \
    file://policy/modules/services/dbd.fc \
    file://policy/modules/services/dbd.if \
    file://policy/modules/services/dbd.te \
    file://policy/modules/services/dbusbouncer.fc \
    file://policy/modules/services/dbusbouncer.if \
    file://policy/modules/services/dbusbouncer.te \
    file://policy/modules/services/disman.fc \
    file://policy/modules/services/disman.if \
    file://policy/modules/services/disman.te \
    file://policy/modules/services/glass.fc \
    file://policy/modules/services/glass.if \
    file://policy/modules/services/glass.te \
    file://policy/modules/services/icbinn.fc \
    file://policy/modules/services/icbinn.if \
    file://policy/modules/services/icbinn.te \
    file://policy/modules/services/ivcd.fc \
    file://policy/modules/services/ivcd.if \
    file://policy/modules/services/ivcd.te \
    file://policy/modules/services/language-sync.fc \
    file://policy/modules/services/language-sync.if \
    file://policy/modules/services/language-sync.te \
    file://policy/modules/services/network-daemon.fc \
    file://policy/modules/services/network-daemon.if \
    file://policy/modules/services/network-daemon.te \
    file://policy/modules/services/rpcproxy.fc \
    file://policy/modules/services/rpcproxy.if \
    file://policy/modules/services/rpcproxy.te \
    file://policy/modules/services/uid.fc \
    file://policy/modules/services/uid.if \
    file://policy/modules/services/uid.te \
    file://policy/modules/services/updatemgr.fc \
    file://policy/modules/services/updatemgr.if \
    file://policy/modules/services/updatemgr.te \
    file://policy/modules/services/vusb.fc \
    file://policy/modules/services/vusb.if \
    file://policy/modules/services/vusb.te \
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
    file://policy/modules/system/xc-files.fc \
    file://policy/modules/system/xc-files.if \
    file://policy/modules/system/xc-files.te \
    file://policy/modules/system/xc-installer.fc \
    file://policy/modules/system/xc-installer.if \
    file://policy/modules/system/xc-installer.te \
"
# Patches.
SRC_URI += " \
    file://patches/fc-subs-config-aliases.patch \
    file://patches/policy.modules.admin.alsa.diff \
    file://patches/policy.modules.admin.brctl.diff \
    file://patches/policy.modules.admin.dmidecode.diff \
    file://patches/policy.modules.admin.dpkg.diff \
    file://patches/policy.modules.admin.firstboot.diff \
    file://patches/policy.modules.admin.logrotate.diff \
    file://patches/policy.modules.apps.qemu.diff \
    file://patches/policy.modules.kernel.devices.diff \
    file://patches/policy.modules.kernel.domain.diff \
    file://patches/policy.modules.kernel.filesystem.diff \
    file://patches/policy.modules.kernel.kernel.diff \
    file://patches/policy.modules.kernel.files.diff \
    file://patches/policy.modules.kernel.storage.diff \
    file://patches/policy.modules.roles.staff.diff \
    file://patches/policy.modules.roles.sysadm.diff \
    file://patches/policy.modules.services.acpi.diff \
    file://patches/policy.modules.services.cron.diff \
    file://patches/policy.modules.services.dbus.diff \
    file://patches/policy.modules.services.dnsmasq.diff \
    file://patches/policy.modules.services.hal.diff \
    file://patches/policy.modules.services.networkmanager.diff \
    file://patches/policy.modules.services.ssh.diff \
    file://patches/policy.modules.services.virt.diff \
    file://patches/policy.modules.system.authlogin.diff \
    file://patches/policy.modules.system.fstools.diff \
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
    file://patches/policy.modules.system.xen.diff \
    file://patches/policy.modules.services.tcsd.patch \
    file://patches/policy.modules.services.networkmanager_xt.diff \
    file://patches/policy.modules.system.init_xt.diff \
    file://patches/blktap-interfaces.diff \
    file://patches/db-cmd-interfaces.diff \
    file://patches/dbd-interfaces.diff \
    file://patches/network-daemon-interfaces.diff \
    file://patches/statusreport-interfaces.diff \
    file://patches/stubdom-helpers-interfaces.diff \
    file://patches/sysutils-interfaces.diff \
    file://patches/tcs-interfaces.diff \
    file://patches/tpmsetup-interfaces.diff \
    file://patches/uid-interfaces.diff \
    file://patches/updatemgr-interfaces.diff \
    file://patches/vhdutils-interfaces.diff \
    file://patches/xc-files-interfaces.patch \
    file://patches/xc-installer-interfaces.diff \
    file://patches/xec-interfaces.diff \
    file://patches/xsmutils-interfaces.diff \
    file://patches/vusb-interfaces.diff \
    file://patches/openxt_system_unconfined_readonly_neverallow_fixes.patch \
    file://patches/openxt-sysadm-lsusb.patch \
    file://patches/openxt-init-spec-domtrans.patch \
    file://patches/busybox-mmap-read-execute-checks-from-new-domain.patch \
    file://patches/policy.modules.admin.su.patch \
    file://patches/signed-kernel-modprobe.patch \
    file://patches/add-missing-dbusd-permissions.patch \
    file://patches/xl-sysadm-interfaces.patch \
    file://patches/policy.modules.admin.bootloader.diff \
"

DEPENDS_append += " \
    strace-native \
"

S = "${WORKDIR}/refpolicy"

FILES_${PN} += " \
    ${sysconfdir}/selinux \
"

POLICY_NAME = "xc_policy"
POLICY_DISTRO = "debian"
POLICY_UBAC = "y"
POLICY_DIRECT_INITRC = "y"
POLICY_QUIET = "n"
POLICY_MLS_CATS = "256"

# Custom name to reflect modifications.
EXTRA_OEMAKE += "PKGNAME=${POLICY_NAME}-${PV}"

do_srctree_copy() {
    cp -r "${WORKDIR}/policy/modules" "${S}/policy"
}
addtask do_srctree_copy after do_unpack before do_patch
do_srctree_copy[doc] = "Copy layer provided policy files in work directory."
do_srctree_copy[dirs] = "${B}"

do_policy_conf() {
    for conf in modules booleans; do
        rm -f "${S}/policy/${conf}.conf"
        for e in ${WORKDIR}/policy/${conf}*.conf; do
            if [ -e "${e}" ]; then
                cat "${e}" >> "${S}/policy/${conf}.conf"
            fi
        done
    done
}
addtask do_policy_conf after do_srctree_copy before do_configure
do_policy_conf[doc] = "Generate configuration files (booleans.conf, \
modules.conf) from the configuration elements passed by the layer. \
See 'make conf' of the refpolicy for more information."
do_policy_conf[dirs] = "${B}"

do_install_append() {
    install -d ${D}/etc/selinux
    install -m 644 ${WORKDIR}/config ${D}/etc/selinux/config
}

sysroot_stage_all_append () {
    sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}

pkg_postinst_${PN} () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /
    fi
}

pkg_postinst_${PN}_append_xenclient-dom0 () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /config /storage
    fi
}
