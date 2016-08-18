inherit xenclient

# For deb-xenstore-client-tools and deb-libxenstore to pickup patches
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "${XEN_VERSION}"

DEPENDS = "glib-2.0"

SRC_URI = "${XEN_SRC_URI};name=source \
    file://config.patch \
    file://do-not-overwrite-cc-and-ld.patch \
    file://disable-xen-root-check.patch \
    file://blktap-resume-fix.patch;patch=1 \
    file://xentrace-format-handle-zero-tsc.patch;patch=1 \
    file://xsm-add-corespersocket.patch \
    file://hvm-pm-hibernate-s-state.patch;patch=1 \
    file://prune-vga-acpi-dev.patch;patch=1 \
    file://xen-libhvm.patch;patch=1 \
    file://smbios.patch;patch=1 \
    file://evtchn-do-not-set-pending-if-s3.patch;patch=1 \
    file://hvm-rtc.patch;patch=1 \
    file://hvm-rtc-refresh-time.patch;patch=1 \
    file://acpi-pm-feature.patch \
    file://hvm-info.patch \
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
    file://xsa-155-paravirtualized-drivers-incautious-about-shared-memory-contents.patch;patch=1 \
    file://xsa-170-guest-user-mode-may-crash-guest-with-non-canonical-rip.patch;patch=1 \
    file://xsa-173-x86-shadow-pagetables-address-width-overflow.patch;patch=1 \
    file://openxt-xen-xsmv4vuse.patch \
    file://xsa-154-x86-inconsistent-cachability-flags-on-guest-mappings.patch \
    file://xsa-182-x86-privilege-escalation-in-pv-guests.patch \
"

SRC_URI[source.md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[source.sha256sum] := "${XEN_SRC_SHA256SUM}"

S = "${WORKDIR}/xen-${XEN_VERSION}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6260c12cd5de27e80d89ae18e48d20a"

INC_PR = "r4"

do_configure_prepend() {
    # fixup AS/CC/CCP/etc variable within StdGNU.mk
    for i in AS LD CC CPP AR RANLIB NM STRIP OBJCOPY OBJDUMP; do
        sed -i "s/^\($i\s\s*\).*=/\1?=/" ${S}/config/StdGNU.mk
    done
    # fixup environment passing in some makefiles
    sed -i 's#\(\w*\)=\(\$.\w*.\)#\1="\2"#' ${S}/tools/firmware/Makefile
}
