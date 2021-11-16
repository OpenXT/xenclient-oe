FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# XSA patches take precedence and belong at the head of the queue.
# This ensures that any collisions with other patches get addressed
# by modifying the OpenXT-specific patches, not the XSA patches.

SRC_URI_append = " \
    file://defconfig \
    file://0001-python-pygrub-pass-DISTUTILS-xen.4.12.patch \
    file://0001-tools-firmware-Build-firmware-as-ffreestanding.patch \
    file://kconfig-grant-table-v2-interface.patch \
    file://kconfig-grant-table-exotic.patch \
    file://hvm-pm-hibernate-s-state.patch;patch=1 \
    file://prune-vga-acpi-dev.patch;patch=1 \
    file://smbios.patch;patch=1 \
    file://evtchn-do-not-set-pending-if-s3.patch;patch=1 \
    file://hvm-rtc.patch;patch=1 \
    file://hvm-rtc-refresh-time.patch;patch=1 \
    file://acpi-pm-feature.patch \
    file://xenconsoled-syslog.patch;patch=1 \
    file://Dell-980-txt-shutdown-acpi-access-width.patch;patch=1 \
    file://parse-video-from-mbi.patch;patch=1 \
    file://allow-mwait-cstate.patch;patch=1 \
    file://opt-disable-vmcs-shadowing.patch;patch=1 \
    file://hvmloader-legacy-seabios-optionroms.patch;patch=1 \
    file://gpt-s3-resume-reason.patch;patch=1 \
    file://increase-ap-startup-time.patch;patch=1 \
    file://acpi-slic-support.patch \
    file://tboot-xen-evtlog-support.patch \
    file://add-xc-hypercall-page.patch \
    file://libxl-syslog.patch \
    file://libxl-vif-cleanup.patch \
    file://libxl-vif-make-ioemu-and-stubdom-mac-addresses-configurable.patch \
    file://libxl-openxt-helpers.patch \
    file://libxl-openxt-tweaks.patch \
    file://libxl-domain-state.patch \
    file://libxl-fixup-cmdline-ops.patch \
    file://libxl-fix-reboot.patch \
    file://libxl-display-manager-support.patch \
    file://libxl-xenmgr-support.patch \
    file://libxl-no-PV-qemu.patch \
    file://libxl-hard-shutdown.patch \
    file://libxl-argo-xswait.patch \
    file://libxl-move-extra-qemu-args-to-the-end.patch \
    file://libxl-stubdom-options.patch \
    file://libxl-support-hvm-readonly-disks.patch \
    file://libxl-vwif-support.patch \
    file://libxl-atapi-pt.patch \
    file://libxl-avoid-creating-unusable-cdrom-vbd-xs-nodes.patch \
    file://libxl-disable-json-updates.patch \
    file://libxl-allow-non-qdisk-cdrom.patch \
    file://libxl-fix-flr.patch \
    file://libxl-disable-vnc-query-when-disabled.patch \
    file://libxl-allow-save-vnuma.patch \
    file://libxl-block-scripts-log-to-syslog.patch \
    file://0001-convert-block-tap-for-shared.patch \
    file://domain-reboot.patch \
    file://enable-blktap3-within-tree.patch \
    file://blktap3-build-integration.patch \
    file://efi-hardcode-openxt-cfg.patch \
    file://shim-support-for-shim-lock-measure.patch \
    file://efi-require-shim.patch \
    file://tboot-measure-and-launch-from-xen-efi.patch \
    file://efi-load-options-no-default-image-name.patch \
    file://argo-add-viptables.patch \
    file://argo-quiet-xsm-check-during-init.patch \
    file://libxl-seabios-ipxe.patch \
    file://memory-scrub-on-domain-shutdown.patch \
    file://ocamlfind-static.patch \
    file://ocaml-makefiles-sysroot.patch \
    file://patch-xen-disable-efi-gettime.patch \
    file://libxl-add-ovmf-varstored.patch \
    file://libxl-const-stubdom-is-linux.patch \
    file://libxl-disable-dom0-qemu.patch \
    file://libxl-xen-disable-dom0-qemu.patch \
"

# Unset variables inherited from meta-virt dunfell to avoid multilib
# dependency.  0001-tools-firmware-Build-firmware-as-ffreestanding.patch makes
# it unnecessary (from Xen 4.15).  These can be removed in OE hardknott or
# later.
GLIBC32_x86-64 = ""
ADD_SYSROOT32_CFLAGS_x86-64 = ""

PACKAGECONFIG =+ "xsm"
PACKAGECONFIG =+ "hvm"

#--
# Add ovmf binary path
PACKAGECONFIG[hvm] = "--with-system-seabios="/usr/share/firmware/bios.bin" --with-system-ovmf="/usr/share/firmware/ovmf.bin",--disable-seabios,seabios ipxe vgabios ovmf,"

# The deploy task is detected as MACHINE-specific due to the variables it uses.
# The task is not required here so drop it.
deltask deploy
