PV = "${XEN_VERSION}"

SRC_URI := "${XEN_SRC_URI}"
SRC_URI[md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[sha256sum] := "${XEN_SRC_SHA256SUM}"

SRC_URI_append = " \
    file://config.patch \
    file://do-not-overwrite-cc-and-ld.patch \
    file://disable-xen-root-check.patch \
    file://blktap-resume-fix.patch;patch=1 \
    file://xsm-add-corespersocket.patch \
    file://hvm-pm-hibernate-s-state.patch;patch=1 \
    file://prune-vga-acpi-dev.patch;patch=1 \
    file://smbios.patch;patch=1 \
    file://evtchn-do-not-set-pending-if-s3.patch;patch=1 \
    file://hvm-rtc.patch;patch=1 \
    file://hvm-rtc-refresh-time.patch;patch=1 \
    file://acpi-pm-feature.patch \
    file://xenconsoled-syslog.patch;patch=1 \
    file://tboot-shutdown-disable-irqs.patch;patch=1 \
    file://Dell-980-txt-shutdown-acpi-access-width.patch;patch=1 \
    file://parse-video-from-mbi.patch;patch=1 \
    file://hvm-cpuid-multicore.patch;patch=1 \
    file://xen-translate.patch;patch=1 \
    file://local-pxe-rom.patch;patch=1 \
    file://v4v.patch;patch=1 \
    file://v4v-viptables.patch;patch=1 \
    file://unmap-shared-info.patch;patch=1 \
    file://foreign-batch-cacheattr.patch;patch=1 \
    file://allow-pat-cacheattrs-for-all-domains.patch;patch=1 \
    file://opt-disable-vmcs-shadowing.patch;patch=1 \
    file://hvmloader-legacy-seabios-optionroms.patch;patch=1 \
    file://blktap2-miscellaneous-oxt-fixes.patch \
    file://blktap2-libvhd-atomic-write-helpers.patch \
    file://blktap2-vhd-marker-flag.patch \
    file://blktap2-vhd-print-allocated-virtblock-list.patch \
    file://blktap2-tap-ctl-optional-pid-ro-mount.patch \
    file://blktap2-vhd-keyhash-support.patch \
    file://blktap2-vhd-encryption-support.patch \
    file://blktap2-vhd-icbinn-support.patch \
    file://gpt-s3-resume-reason.patch;patch=1 \
    file://cpufreq_gov_stop_avoid_panic.patch;patch=1 \
    file://fix-memcpy-in-x86-emulate.patch;patch=1 \
    file://stubdomain-msi-irq-access.patch;striplevel=1 \
    file://workaround-nehalem-igd-vtd.patch;patch=1 \
    file://xen-x86-Fix-up-rules-when-forcing-mno-sse.patch;patch=1 \
    file://increase-ap-startup-time.patch;patch=1 \
    file://openxt-xen-xsmv4vuse.patch \
    file://xenstat-disable-tmem-use.patch;patch=1 \
    file://libxl-do-not-destroy-in-use-tapdevs.patch \
    file://libxl-syslog.patch \
    file://libxl-RFC-4of7-Add-stubdomain-version-tools-domain-build-info.patch \
    file://libxl-RFC-5of7-Handle-Linux-stubdomain-specific-QEMUoptions.patch \
    file://libxl-RFC-6of7-Build-the-domain-with-a-Linux-based-stubdomain.patch \
    file://libxl-RFC-7of7-Wait-for-QEMU-startup-in-stubdomain.patch \
    file://libxl-RFC-fixes.patch \
    file://libxl-misc-fixes.patch \
    file://libxl-vif-make-ioemu-and-stubdom-mac-addresses-configurable.patch \
    file://libxl-linux-stubdom-replace-disk-with-initramfs.patch \
    file://libxl-openxt-helpers.patch \
    file://libxl-openxt-qemu-args.patch \
    file://libxl-openxt-xci-cpuid-signature.patch \
    file://libxl-pass-qemu-options-through-linux-stubdom-cmdline.patch \
    file://libxl-openxt-tweaks.patch \
    file://libxl-domain-state.patch \
    file://libxl-fixup-cmdline-ops.patch \
    file://libxl-crypto-key-dir.patch \
    file://libxl-fix-reboot.patch \
    file://libxl-display-manager-support.patch \
    file://libxl-xenmgr-support.patch \
    file://libxl-move-extra-qemu-args-to-the-end.patch \
    file://libxl-stubdom-options.patch \
    file://libxl-add-cores-per-socket-support.patch \
    file://libxl-support-hvm-readonly-disks.patch \
    file://libxl-pci-passthrough-fixes.patch \
    file://xsa-191-x86-null-segments-not-always-treated-as-unusable.patch \
    file://xsa-192-x86-task-switch-to-vm86-mode-mis-handled.patch \
    file://xsa-193-x86-segment-base-write-emulation-lacking-canonical-address-checks.patch \
    file://xsa-195-x86-64-bit-bit-test-instruction-emulation-broken.patch \
    file://xsa-196-x86-software-interrupt-injection-mis-handled.patch \
    file://xsa-200-x86-cmpxchg8b-emulation-fails-to-ignore-operand-size-override.patch \
    file://xsa-202-x86-pv-guests-may-be-able-to-mask-interrupts.patch \
    file://xsa-203-x86-missing-null-pointer-check-in-vmfunc-emulation.patch \
    file://xsa-204-x86-mishandling-of-syscall-singlestep-during-emulation.patch \
"

COMPATIBLE_HOST = 'i686-oe-linux|(x86_64.*).*-linux|aarch64.*-linux'

PACKAGECONFIG =+ "xsm"
PACKAGECONFIG =+ "hvm"

S = "${WORKDIR}/xen-${PV}"
