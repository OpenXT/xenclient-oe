FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# XSA patches take precedence and belong at the head of the queue.
# This ensures that any collisions with other patches get addressed
# by modifying the OpenXT-specific patches, not the XSA patches.

SRC_URI_append = " \
    file://defconfig \
    file://0001-python-pygrub-pass-DISTUTILS-xen.4.12.patch \
    file://0001-tools-firmware-Build-firmware-as-ffreestanding.patch \
    file://kconfig-grant-table.patch \
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
    file://openxt-xci-cpuid-signature.patch;patch=1 \
    file://allow-mwait-cstate.patch;patch=1 \
    file://opt-disable-vmcs-shadowing.patch;patch=1 \
    file://hvmloader-legacy-seabios-optionroms.patch;patch=1 \
    file://gpt-s3-resume-reason.patch;patch=1 \
    file://stubdomain-msi-irq-access.patch;patch=1 \
    file://xen-x86-Fix-up-rules-when-forcing-mno-sse.patch;patch=1 \
    file://increase-ap-startup-time.patch;patch=1 \
    file://xenstat-disable-tmem-use.patch;patch=1 \
    file://acpi-slic-support.patch \
    file://tboot-xen-evtlog-support.patch \
    file://disable-cpuid-hle-rtm.patch \
    file://add-xc-hypercall-page.patch \
    file://honor-multiple-per-device-pci.patch \
    file://establish-per-device-reserved-memory.patch \
    file://dont-report-highmem-in-use.patch \
    file://libxl-do-not-destroy-in-use-tapdevs.patch \
    file://libxl-syslog.patch \
    file://libxl-RFC-4of7-Add-stubdomain-version-tools-domain-build-info.patch \
    file://libxl-RFC-5of7-Handle-Linux-stubdomain-specific-QEMUoptions.patch \
    file://libxl-RFC-6of7-Build-the-domain-with-a-Linux-based-stubdomain.patch \
    file://libxl-RFC-7of7-Wait-for-QEMU-startup-in-stubdomain.patch \
    file://libxl-RFC-fixes.patch \
    file://libxl-rulesmk-opt-ldlibs.patch \
    file://libxl-tapdisk-cdrom.patch \
    file://libxl-vif-cleanup.patch \
    file://libxl-RFC-fix-multiple-disks.patch \
    file://libxl-vif-make-ioemu-and-stubdom-mac-addresses-configurable.patch \
    file://libxl-linux-stubdom-replace-disk-with-initramfs.patch \
    file://libxl-openxt-helpers.patch \
    file://libxl-openxt-qemu-args.patch \
    file://libxl-openxt-xci-cpuid-signature.patch \
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
    file://libxl-pci-passthrough-fixes.patch \
    file://libxl-vwif-support.patch \
    file://libxl-atapi-pt.patch \
    file://libxl-iso-hotswap.patch \
    file://libxl-avoid-creating-unusable-cdrom-vbd-xs-nodes.patch \
    file://libxl-disable-json-updates.patch \
    file://libxl-allow-non-qdisk-cdrom.patch \
    file://libxl-fix-flr.patch \
    file://libxl-disable-vnc-query-when-disabled.patch \
    file://xl-shutdown-wait-for-domain-death.patch \
    file://libxl-allow-save-vnuma.patch \
    file://libxl-block-scripts-log-to-syslog.patch \
    file://domain-reboot.patch \
    file://libxl-retry-qmp-pci-add.patch \
    file://libxl-blktap3-support.patch \
    file://libxl-crypto-key-dir.patch \
    file://libxl-blktap3-do-not-destroy-in-use-tapdevs.patch \
    file://libxl-blktap3-iso-hotswap.patch \
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
    file://0001-tools-helpers-Introduce-cmp-fd-file-inode-utility.patch \
    file://0002-Linux-locking.sh-Use-cmp-fd-file-inode-for-lock-chec.patch \
    file://ocamlfind-static.patch \
    file://ocaml-makefiles-sysroot.patch \
    file://0001-efi-remove-old-SetVirtualAddressMap-arrangement.patch \
    file://0002-efi-optionally-call-SetVirtualAddressMap.patch \
    file://0003-efi-use-directmap-to-access-runtime-services-table.patch \
    file://0004-efi-do-not-use-runtime-services-table-with-efi-no-rs.patch \
    file://patch-xen-disable-efi-gettime.patch \
    file://libxl-add-ovmf-varstored.patch \
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
