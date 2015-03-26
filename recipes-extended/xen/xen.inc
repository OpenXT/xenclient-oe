inherit xenclient

# For deb-xenstore-client-tools and deb-libxenstore to pickup patches
FILESEXTRAPATHS := "${THISDIR}/files"

PV = "${XEN_VERSION}"

SRC_URI = "${XEN_SRC_URI};name=source \
    file://bp-ept-update-aa9114edd97b292cd89b3616e3f2089471fd220.patch;patch=1 \
    file://fix-xenctrl-enum-defn.patch;patch=1 \
    file://xen-cpuidle-hang.patch;patch=1 \
    file://sb_reboot_turbo_workaround.patch;patch=1 \
    file://tapdisk_shutdown_recursion.patch;patch=1 \
    file://xen-allow-hvm.patch;patch=1 \
    file://blktap-resume-fix.patch;patch=1 \
    file://blktap-retry-fix.patch;patch=1 \
    file://rmrr-validate-range.patch;patch=1 \
    file://xentrace-format-handle-zero-tsc.patch;patch=1 \
    file://iobitmap-on-all-vcpus.patch;patch=1 \
    file://blktap-quieten-udevd.patch;patch=1 \
    file://fix-write-clash.patch;patch=1 \
    file://fix-32bit-xsm-interface.patch;patch=1 \
    file://fix-pci-serial-hang.patch;patch=1 \
    file://xsm-add-corespersocket.patch;patch=1 \
    file://incremental-dmesg-processing.patch;patch=1 \
    file://add-percentages-to-xenpm.patch;patch=1 \
    file://stack_on_triple_fault.patch;patch=1 \
    file://hvmloader_print_e820.patch;patch=1 \
    file://tapdisk_debug.patch;patch=1 \
    file://microcode-info.patch;patch=1 \
    file://add_system_time_timestamps.patch;patch=1 \
    file://local-ipxe.patch;patch=1 \
    file://10ms_timeslice.patch;patch=1 \
    file://publish_xentrace_formats.patch;patch=1 \
    file://add-command-line-option-to-disable-arat.patch;patch=1 \
    file://hotplug-scripts-iptables.patch;patch=1 \
    file://hvm-pm-s-states.patch;patch=1 \
    file://hvm-pm-hibernate-s-state.patch;patch=1 \
    file://acpi-pm.patch;patch=1 \
    file://prune-acpi-devices.patch;patch=1 \
    file://prune-vga-acpi-dev.patch;patch=1 \
    file://battery-units.patch;patch=1 \
    file://two-battery-support.patch;patch=1 \
    file://xen-libhvm.patch;patch=1 \
    file://smbios.patch;patch=1 \
    file://qemu-acpi-tables.patch;patch=1 \
    file://acpi-no-hotplug.patch;patch=1 \
    file://evtchn-do-not-set-pending-if-s3.patch;patch=1 \
    file://acpi-applesmc.patch;patch=1 \
    file://acpi-hvmloader-tm.patch;patch=1 \
    file://acpi-hvmloader-pm-xt.patch;patch=1 \
    file://hvmloader-vga-command-io.patch;patch=1 \
    file://vbe-lfb-addr-matches-hvmloader.patch;patch=1 \
    file://blktap2-vci.patch;patch=1 \
    file://blktap2-multi-coalesce.patch;patch=1 \
    file://libvhd-disable-static-batmaps.patch;patch=1 \
    file://vhd-validate-size.patch;patch=1 \
    file://vhd-util-allow-resize.patch;patch=1 \
    file://tapdisk-increase-vhd-req-pool.patch;patch=1 \
    file://bios-uuid.patch;patch=1 \
    file://bios-signature.patch;patch=1 \
    file://crash_flag_hypercall.patch;patch=1 \
    file://hvm-rtc.patch;patch=1 \
    file://large-remap.patch;patch=1 \
    file://dom0_auto_mem.patch;patch=1 \
    file://rombios-faster.patch;patch=1 \
    file://include-seabios.patch;patch=1 \
    file://XSA-59_chipset_updates.patch;patch=1 \
    file://XSA-104_fix_race_condition_with_dirty_vram_state.patch;patch=1 \
    file://XSA-105_priv_check_in_x86_HLT_LGDT_LIDT_LMSW.patch;patch=1 \
    file://XSA-106_privilege_check_in_x86_sw_int.patch;patch=1 \
    file://XSA-108_properly_bound_x2APIC_MSR_range.patch;patch=1 \
    file://xsa109.patch;patch=1 \
    file://xsa110-4.3-and-4.2.patch;patch=1 \
    file://xc-xt-hvm-get-time.patch;patch=1 \
    file://hvm-rtc-refresh-time.patch;patch=1 \
    file://fix-race-between-udev-and-tapctl.patch;patch=1 \
    file://reboot-quirk-pci.patch;patch=1 \
    file://xenstored-instrumentation.patch;patch=1 \
    file://xc-xt-hvm-info.patch;patch=1 \
    file://xc-xt-xenconsoled-syslog.patch;patch=1 \
    file://xc-xt-ept-respect-cacheattr-pin.patch;patch=1 \
    file://xc-xt-interrupt-debug-info.patch;patch=1 \
    file://xc-xt-shadow-op-blow-tables.patch;patch=1 \
    file://xc-xt-vcpu-get-time.patch;patch=1 \
    file://xc-xt-tboot-shutdown-disable-irqs.patch;patch=1 \
    file://xc-xt-txt-shutdown-acpi-access-width.patch;patch=1 \
    file://xc-xt-parse-video-from-mbi.patch;patch=1 \
    file://xc-xt-cpuid.patch;patch=1 \
    file://xc-xt-isa-irq-guest-binding.patch;patch=1 \
    file://xc-xt-set-servicevm.patch;patch=1 \
    file://xc-xt-aperture-map.patch;patch=1 \
    file://xc-xt-xen-translate.patch;patch=1 \
    file://xc-xt-local-pxe-rom.patch;patch=1 \
    file://xc-xt-v4v.patch;patch=1 \
    file://xc-xt-v4v-viptables.patch;patch=1 \
    file://xc-xt-unmap-shared-info.patch;patch=1 \
    file://xc-xt-foreign-batch-cacheattr.patch;patch=1 \
    file://xc-xt-allow-pat-cacheattrs-for-all-domains.patch;patch=1 \
    file://xc-xt-opt-disable-vmcs-shadowing.patch;patch=1 \
    file://xc-xt-xenstore-use-pthread-always.patch;patch=1 \
    file://xc-xt-hvmloader-legacy-seabios-optionroms.patch;patch=1 \
    file://xc-xt-hvmloader-move-reservation-area.patch;patch=1 \
    file://xc-xt-tools-v3-mapspace-workaround.patch;patch=1 \
    file://gpt-iommu-mapping.patch;patch=1 \
    file://gpt-superblanker.patch;patch=1 \
    file://gpt-nvidia.patch;patch=1 \
    file://gpt-quiet-log.patch;patch=1 \
    file://gpt-igd-hp-resume-workaround.patch;patch=1 \
    file://gpt-s3-resume-reason.patch;patch=1 \
    file://flask-bool-utility-fix.patch;patch=1 \
    file://flask-persist-booleans.patch;patch=1 \
    file://cpufreq_gov_stop_avoid_panic.patch;patch=1 \
    file://fix-memcpy-in-x86-emulate.patch;patch=1 \
    file://blktap-instrumentation.patch;patch=1 \
    file://blktap-timeout.patch;patch=1 \
    file://stubdomain-msi-irq-access.patch;striplevel=1 \
"

SRC_URI[source.md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[source.sha256sum] := "${XEN_SRC_SHA256SUM}"

S = "${WORKDIR}/xen-${XEN_VERSION}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e0f0f3ac55608719a82394cc353928df"

