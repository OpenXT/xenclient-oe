inherit xenclient

# For deb-xenstore-client-tools and deb-libxenstore to pickup patches
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "${XEN_VERSION}"

DEPENDS = "glib-2.0"

SRC_URI = "${XEN_SRC_URI};name=source \
    file://config.patch \
    file://blktap-resume-fix.patch \
    file://xsm-add-corespersocket.patch \
    file://hvm-pm-hibernate-s-state.patch \
    file://prune-vga-acpi-dev.patch \
    file://xen-libhvm.patch \
    file://smbios.patch \
    file://evtchn-do-not-set-pending-if-s3.patch \
    file://hvm-rtc.patch \
    file://hvm-rtc-refresh-time.patch \
    file://acpi-pm-feature.patch \
    file://xenconsoled-syslog.patch \
    file://tboot-shutdown-disable-irqs.patch \
    file://Dell-980-txt-shutdown-acpi-access-width.patch \
    file://parse-video-from-mbi.patch \
    file://hvm-cpuid-multicore.patch \
    file://xen-translate.patch \
    file://local-pxe-rom.patch \
    file://v4v.patch \
    file://v4v-viptables.patch \
    file://unmap-shared-info.patch \
    file://foreign-batch-cacheattr.patch \
    file://allow-pat-cacheattrs-for-all-domains.patch \
    file://opt-disable-vmcs-shadowing.patch \
    file://hvmloader-legacy-seabios-optionroms.patch \
    file://blktap2-miscellaneous-oxt-fixes.patch \
    file://blktap2-libvhd-atomic-write-helpers.patch \
    file://blktap2-vhd-marker-flag.patch \
    file://blktap2-vhd-print-allocated-virtblock-list.patch \
    file://blktap2-tap-ctl-optional-pid-ro-mount.patch \
    file://blktap2-vhd-keyhash-support.patch \
    file://blktap2-vhd-encryption-support.patch \
    file://blktap2-vhd-icbinn-support.patch \
    file://gpt-s3-resume-reason.patch \
    file://cpufreq_gov_stop_avoid_panic.patch \
    file://fix-memcpy-in-x86-emulate.patch \
    file://stubdomain-msi-irq-access.patch \
    file://workaround-nehalem-igd-vtd.patch \
    file://xen-x86-Fix-up-rules-when-forcing-mno-sse.patch \
    file://increase-ap-startup-time.patch \
    file://openxt-xen-xsmv4vuse.patch \
    file://xenstat-disable-tmem-use.patch \
    file://acpi-slic-support.patch \
    file://xsm-for-vtd.patch \
    file://hvm-info-tool.patch \
    file://tboot-xen-evtlog-support.patch \
    file://xsa226-4.6/0001-gnttab-dont-use-possibly-unbounded-tail-calls.patch \
    file://xsa226-4.6/0002-gnttab-fix-transitive-grant-handling.patch \
    file://xsa227-4.6.patch \
    file://xsa228-4.8.patch \
    file://xsa230.patch \
    file://xsa231-4.7.patch \
    file://xsa232.patch \
    file://xsa233.patch \
    file://xsa234-4.6.patch \
    file://xsa235-4.6.patch \
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
