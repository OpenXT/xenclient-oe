FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
OPENXT_REFPOLICY_STUB := "${THISDIR}/files/stub-missing-interfaces.py"

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
    file://policy/modules/services/argo-input.fc \
    file://policy/modules/services/argo-input.if \
    file://policy/modules/services/argo-input.te \
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
    file://patches/policy.modules.kernel.domain.diff \
    file://patches/policy.modules.kernel.filesystem.diff \
    file://patches/policy.modules.kernel.kernel.diff \
    file://patches/policy.modules.kernel.files.diff \
    file://patches/policy.modules.kernel.storage.diff \
    file://patches/policy.modules.roles.staff.diff \
    file://patches/policy.modules.roles.sysadm.diff \
    file://patches/policy.modules.services.acpi.diff \
    file://patches/policy.modules.services.cron.diff \
    file://patches/policy.modules.services.dnsmasq.diff \
    file://patches/policy.modules.services.virt.diff \
    file://patches/policy.modules.system.authlogin.diff \
    file://patches/policy.modules.system.fstools.diff \
    file://patches/policy.modules.system.init.diff \
    file://patches/policy.modules.system.libraries.diff \
    file://patches/policy.modules.system.logging.diff \
    file://patches/policy.modules.system.lvm.diff \
    file://patches/policy.modules.system.miscfiles.diff \
    file://patches/policy.modules.system.modutils.diff \
    file://patches/policy.modules.system.selinuxutil.diff \
    file://patches/policy.modules.system.udev.diff \
    file://patches/policy.modules.system.unconfined.diff \
    file://patches/policy.modules.system.userdomain.diff \
    file://patches/policy.support.misc_patterns.spt.diff \
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
    file://patches/xl-sysadm-interfaces.patch \
    file://patches/policy.modules.admin.bootloader.diff \
    file://patches/monit-volatiles.patch \
    file://patches/xenstore-labeling.patch \
"

DEPENDS:append = " \
    strace-native \
"

S = "${UNPACKDIR}/refpolicy"

FILES:${PN} += " \
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
    cp -r "${UNPACKDIR}/policy/modules" "${S}/policy"
}
addtask do_srctree_copy after do_unpack before do_patch
do_srctree_copy[doc] = "Copy layer provided policy files in work directory."
do_srctree_copy[dirs] = "${B}"

do_policy_conf() {
    for conf in modules booleans; do
        rm -f "${S}/policy/${conf}.conf"
        for e in ${UNPACKDIR}/policy/${conf}*.conf; do
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

# OpenXT already wrote modules.conf. Skip make conf (broken XML) and drop
# entries whose .if files are not in this refpolicy tree.
do_compile() {
    rm -f "${S}/policy/modules.conf"
    for e in ${UNPACKDIR}/policy/modules*.conf; do
        if [ -e "${e}" ]; then
            cat "${e}" >> "${S}/policy/modules.conf"
        fi
    done
    python3 <<PY
from pathlib import Path
s = Path("${S}")
mods = s / "policy/modules"
# corenetwork.if is generated from corenetwork.if.in; treat both as present.
for p in mods.rglob("*"):
    if p.name.endswith(".if.in"):
        dest = p.with_name(p.name[:-3])
        if not dest.exists():
            dest.write_bytes(p.read_bytes())
names = set()
for p in mods.rglob("*"):
    if p.suffix in {".if", ".te", ".fc"} or p.name.endswith(".if.in") or p.name.endswith(".te.in"):
        names.add(p.name.split(".")[0])
conf = s / "policy/modules.conf"
out = []
for line in conf.read_text().splitlines():
    stripped = line.strip()
    if not stripped or stripped.startswith("#") or "=" not in stripped:
        out.append(line)
        continue
    name = stripped.split("=", 1)[0].strip()
    if name in names:
        out.append(line)
    else:
        out.append("# dropped missing module %s" % name)
conf.write_text("\n".join(out) + "\n")
PY
    if ! grep -q "interface(\`xen_dontaudit_rw_unix_stream_sockets'" "${S}/policy/modules/system/xen.if"; then
        cat >> "${S}/policy/modules/system/xen.if" <<'EOF'

interface(`xen_dontaudit_rw_unix_stream_sockets',`
	gen_require(`
		type xend_t;
	')
	dontaudit $1 xend_t:unix_stream_socket rw_stream_socket_perms;
')
interface(`xen_write_tmp',`
	gen_require(`
		type xend_tmp_t;
	')
	files_search_tmp($1)
	allow $1 xend_tmp_t:file write_file_perms;
')
interface(`xen_dbus_chat',`
	gen_require(`
		type xend_t;
		class dbus send_msg;
	')
	allow $1 xend_t:dbus send_msg;
	allow xend_t $1:dbus send_msg;
')
interface(`xen_dbus_send',`
	gen_require(`
		type xend_t;
		class dbus send_msg;
	')
	allow $1 xend_t:dbus send_msg;
')
interface(`xen_geattr_config_files',`
	gen_require(`
		type xend_config_t;
	')
	getattr_files_pattern($1, xend_config_t, xend_config_t)
')
interface(`xen_run_xm',`
	gen_require(`
		type xm_t;
	')
	xen_domtrans_xm($1)
	role $2 types xm_t;
')
interface(`xen_use_fds',`
	gen_require(`
		type xend_t;
	')
	allow $1 xend_t:fd use;
')
interface(`xen_domtrans',`
	gen_require(`
		type xm_t;
	')
	domtrans_pattern($1, $2, xm_t)
')
EOF
    fi
    if ! grep -q 'type xend_config_t' "${S}/policy/modules/system/xen.te"; then
        printf '\ntype xend_config_t;\nfiles_type(xend_config_t)\n' >> "${S}/policy/modules/system/xen.te"
    fi
    if ! grep -q 'type xend_t;' "${S}/policy/modules/system/xen.te"; then
        printf '\ntype xend_t;\ndomain_type(xend_t)\ntype xend_tmp_t;\nfiles_tmp_file(xend_tmp_t)\n' >> "${S}/policy/modules/system/xen.te"
    fi
    # Compatibility stubs for interfaces removed or renamed in refpolicy 2.2026.
    mkdir -p "${S}/policy/modules/system"
    cat > "${S}/policy/modules/system/openxtcompat.te" <<'EOF'
policy_module(openxtcompat)
EOF
    : > "${S}/policy/modules/system/openxtcompat.fc"
    cat > "${S}/policy/modules/system/openxtcompat.if" <<'EOF'
interface(`dev_search_xen',`
	gen_require(`
		type device_t;
	')
	allow $1 device_t:dir search_dir_perms;
')
interface(`dev_filetrans_blktap',`
	gen_require(`
		type device_t;
	')
	allow $1 device_t:dir add_entry_dir_perms;
')
interface(`dev_rw_xenpriv',`
	gen_require(`
		type device_t;
	')
	allow $1 device_t:chr_file rw_chr_file_perms;
')
interface(`hal_dbus_chat',`
	gen_require(`
		type system_dbusd_t;
		class dbus send_msg;
	')
	allow $1 system_dbusd_t:dbus send_msg;
')
interface(`init_daemon_pid_file',`
	gen_require(`
		attribute pidfile;
	')
	typeattribute $1 pidfile;
')
interface(`modutils_domtrans_depmod',`
	gen_require(`
		type device_t;
	')
')
interface(`modutils_domtrans_insmod',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_create_cert_files',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_manage_cert_dirs',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_manage_etc',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_read_cert_files',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_read_etc_rw',`
	gen_require(`
		type device_t;
	')
')
interface(`networkmanager_var_lib_filetrans',`
	gen_require(`
		type device_t;
	')
')
interface(`ssh_use_fds',`
	gen_require(`
		type device_t;
	')
')
interface(`sysnet_read_dhcpc_pid',`
	gen_require(`
		type device_t;
	')
')
interface(`udev_read_db',`
	gen_require(`
		type device_t;
	')
')
interface(`udev_read_pid_files',`
	gen_require(`
		type device_t;
	')
')
interface(`vt_ioctl',`
	gen_require(`
		type device_t;
	')
')
interface(`hal_setattr_cache_dirs',`
	gen_require(`
		type device_t;
	')
')
interface(`hal_setattr_pid_dirs',`
	gen_require(`
		type device_t;
	')
')
interface(`dev_create_generic_fifo_files',`
	gen_require(`
		type device_t;
	')
')
interface(`xen_sem_use',`
	gen_require(`
		type xend_t;
	')
	allow $1 xend_t:sem { associate };
')
interface(`dbus_manage_var_lib',`
	gen_require(`
		type device_t;
	')
')
interface(`iptables_dontaudit_read_pids',`
	gen_require(`
		type device_t;
	')
')
interface(`dev_list_xen',`
	gen_require(`
		type device_t;
	')
')
interface(`ssh_inherit_env',`
	gen_require(`
		type device_t;
	')
')
interface(`dev_unconfined_readonly',`
	gen_require(`
		type device_t;
	')
')
interface(`dev_create_sysfs_nodes',`
	gen_require(`
		type device_t;
	')
')

EOF
    echo 'openxtcompat = module' >> "${S}/policy/modules.conf"
    # Generate corenetwork.te/.if from m4 templates (normally make conf).
    rm -f "${S}/policy/modules/kernel/corenetwork.te" \
          "${S}/policy/modules/kernel/corenetwork.if"
    oe_runmake policy/modules/kernel/corenetwork.te policy/modules/kernel/corenetwork.if
    python3 - "${S}" <<'PY'
import sys
from pathlib import Path
p = Path(sys.argv[1]) / "policy/modules/kernel/corenetwork.te"
if not p.exists():
    sys.exit(0)
t = p.read_text()
start = t.find("container_mountpoint(tun_tap_device_t)")
if start != -1:
    pre = t.rfind("optional_policy", 0, start)
    post = t.find("'')", start)
    if pre != -1 and post != -1:
        t = t[:pre] + t[post + 3:]
        p.write_text(t)
PY
    disable_policy_modules
    export EXTRA_OEMAKE="${EXTRA_OEMAKE}"
    python3 ${OPENXT_REFPOLICY_STUB} "${S}" "${B}" "${T}"
}

do_install:append() {
    install -d ${D}/etc/selinux
    install -m 644 ${UNPACKDIR}/config ${D}/etc/selinux/config
}

sysroot_stage_all:append () {
    sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /
    fi
}

pkg_postinst:${PN}:append:xenclient-dom0 () {
    if [ -z "$D" ]; then
        ${base_sbindir}/setfiles "${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts" /config /storage
    fi
}
