inherit xenclient

# For deb-xenstore-client-tools and deb-libxenstore to pickup patches
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "${XEN_VERSION}"

DEPENDS = "glib-2.0"

SRC_URI = "${XEN_SRC_URI};name=source \
    file://config.patch \
    file://do-not-overwrite-cc-and-ld.patch \
    file://disable-xen-root-check.patch \
    file://bp-ept-update-aa9114edd97b292cd89b3616e3f2089471fd220.patch;patch=1 \
    file://fix-xenctrl-enum-defn.patch;patch=1 \
    file://xen-cpuidle-hang.patch;patch=1 \
    file://sb_reboot_turbo_workaround.patch;patch=1 \
    file://tapdisk_shutdown_recursion.patch;patch=1 \
    file://xen-allow-hvm.patch;patch=1 \
    file://blktap-resume-fix.patch;patch=1 \
    file://rmrr-validate-range.patch;patch=1 \
    file://xentrace-format-handle-zero-tsc.patch;patch=1 \
    file://iobitmap-on-all-vcpus.patch;patch=1 \
    file://fix-32bit-xsm-interface.patch;patch=1 \
    file://fix-pci-serial-hang.patch;patch=1 \
    file://xsm-add-corespersocket.patch;patch=1 \
    file://incremental-dmesg-processing.patch;patch=1 \
    file://add-percentages-to-xenpm.patch;patch=1 \
    file://stack_on_triple_fault.patch;patch=1 \
    file://hvmloader_print_e820.patch;patch=1 \
    file://microcode-info.patch;patch=1 \
    file://add_system_time_timestamps.patch;patch=1 \
    file://local-ipxe.patch;patch=1 \
    file://10ms_timeslice.patch;patch=1 \
    file://publish_xentrace_formats.patch;patch=1 \
    file://add-command-line-option-to-disable-arat.patch;patch=1 \
    file://hotplug-scripts-iptables.patch;patch=1 \
    file://hvm-pm-s-states.patch;patch=1 \
    file://hvm-pm-hibernate-s-state.patch;patch=1 \
    file://prune-acpi-devices.patch;patch=1 \
    file://prune-vga-acpi-dev.patch;patch=1 \
    file://xen-libhvm.patch;patch=1 \
    file://smbios.patch;patch=1 \
    file://qemu-acpi-tables.patch;patch=1 \
    file://acpi-no-hotplug.patch;patch=1 \
    file://evtchn-do-not-set-pending-if-s3.patch;patch=1 \
    file://hvmloader-vga-command-io.patch;patch=1 \
    file://vbe-lfb-addr-matches-hvmloader.patch;patch=1 \
    file://bios-uuid.patch;patch=1 \
    file://bios-signature.patch;patch=1 \
    file://crash_flag_hypercall.patch;patch=1 \
    file://hvm-rtc.patch;patch=1 \
    file://large-remap.patch;patch=1 \
    file://dom0_auto_mem.patch;patch=1 \
    file://rombios-faster.patch;patch=1 \
    file://include-seabios.patch;patch=1 \
    file://xc-xt-hvm-get-time.patch;patch=1 \
    file://hvm-rtc-refresh-time.patch;patch=1 \
    file://reboot-quirk-pci.patch;patch=1 \
    file://xenstored-instrumentation.patch;patch=1 \
    file://acpi-pm-feature.patch \
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
    file://stubdomain-msi-irq-access.patch;striplevel=1 \
    file://workaround-nehalem-igd-vtd.patch;patch=1 \
    file://xen-x86-Fix-up-rules-when-forcing-mno-sse.patch;patch=1 \
    file://xsa-125-long-latency-MMIO-mapping-operations-are-not-preemptible.patch;patch=1 \
    file://xsa-127-certain-domctl-operations-may-be-abused-to-lock-up-the-host.patch;patch=1 \
    file://xsa-132-information-leak-through-XEN_DOMCTL_gettscinfo.patch;patch=1 \
    file://xsa-134-GNTTABOP_swap_grant_ref-operation-misbehavior.patch;patch=1 \
    file://xsa-136-vulnerability-in-the-iret-hypercall-handler.patch;patch=1 \
    file://xsa-137-xl-command-line-config-handling-stack-overflow.patch;patch=1 \
    file://xsa-142-libxl-fails-to-honour-readonly-flag-on-disks-with-qemu-xen.patch;patch=1 \
    file://xsa-148-uncontrolled-creation-of-large-page-mappings-by-PV-guests.patch;patch=1 \
    file://xsa-149-leak-of-main-per-domain-vcpu-pointer-array.patch;patch=1 \
    file://xsa-150-long-latency-populate-on-demand-operation-is-not-preemptible.patch;patch=1 \
    file://xsa-151-leak-of-per-domain-profiling-related-vcpu-pointer-array.patch;patch=1 \
    file://xsa-152-some-pmu-and-profiling-hypercalls-log-without-rate-limiting.patch;patch=1 \
    file://xsa-153-populate-on-demand-balloon-size-inaccuracy-can-crash-guests.patch;patch=1 \
    file://xsa-156-cpu-lockup-during-exception-delivery.patch;patch=1 \
    file://xsa-155-paravirtualized-drivers-incautious-about-shared-memory-contents.patch;patch=1 \
    file://xsa-159-XENMEM_exchange-error-handling-issues.patch;patch=1 \
    file://xsa-160-libxl-leak-of-pv-kernel-and-initrd-on-error.patch;patch=1 \
    file://xsa-165-information-leak-in-legacy-x86-FPU-XMM-initialization.patch;patch=1 \
    file://xsa-166-ioreq-handling-possibly-susceptible-to-multiple-read-issue.patch;patch=1 \
    file://xsa-167-pv-superpage-functionality-missing-sanity-checks.patch;patch=1 \
    file://xsa-168-intercept-issue-with-invlpg-on-non-canonical-address.patch;patch=1 \
    file://xsa-169-unintentional-logging-upon-guest-changing-callback-method.patch;patch=1 \
    file://xsa-170-guest-user-mode-may-crash-guest-with-non-canonical-rip.patch;patch=1 \
    file://xsa-173-x86-shadow-pagetables-address-width-overflow.patch;patch=1 \
    file://xen-fix-xsave.patch \
"

SRC_URI[source.md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[source.sha256sum] := "${XEN_SRC_SHA256SUM}"

S = "${WORKDIR}/xen-${XEN_VERSION}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e0f0f3ac55608719a82394cc353928df"

INC_PR = "r4"

do_configure_prepend() {
    # fixup AS/CC/CCP/etc variable within StdGNU.mk
    for i in AS LD CC CPP AR RANLIB NM STRIP OBJCOPY OBJDUMP; do
        sed -i "s/^\($i\s\s*\).*=/\1?=/" ${S}/config/StdGNU.mk
    done
    # fixup environment passing in some makefiles
    sed -i 's#\(\w*\)=\(\$.\w*.\)#\1="\2"#' ${S}/tools/firmware/Makefile
}
