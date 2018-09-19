LIC_FILES_CHKSUM := "file://COPYING;md5=bbb4b1bdc2c3b6743da3c39d03249095"

require xen-version.inc

PV = "${XEN_PV}"

SRCREV = "${XEN_SRCREV}"
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH}"

# XSA patches take precedence and belong at the head of the queue.
# This ensures that any collisions with other patches get addressed
# by modifying the OpenXT-specific patches, not the XSA patches.

require ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap2.inc', 'xen-blktap3.inc', d)}

SRC_URI_append = " \
    file://defconfig \
    file://config.patch \
    file://disable-xen-root-check.patch \
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
    file://hide-cores-from-cpuid.patch;patch=1 \
    file://openxt-xci-cpuid-signature.patch;patch=1 \
    file://allow-mwait-cstate.patch;patch=1 \
    file://xen-translate.patch;patch=1 \
    file://local-pxe-rom.patch;patch=1 \
    file://v4v.patch;patch=1 \
    file://v4v-viptables.patch;patch=1 \
    file://unmap-shared-info.patch;patch=1 \
    file://foreign-batch-cacheattr.patch;patch=1 \
    file://allow-pat-cacheattrs-for-all-domains.patch;patch=1 \
    file://opt-disable-vmcs-shadowing.patch;patch=1 \
    file://hvmloader-legacy-seabios-optionroms.patch;patch=1 \
    file://gpt-s3-resume-reason.patch;patch=1 \
    file://fix-memcpy-in-x86-emulate.patch;patch=1 \
    file://stubdomain-msi-irq-access.patch;patch=1 \
    file://workaround-nehalem-igd-vtd.patch;patch=1 \
    file://xen-x86-Fix-up-rules-when-forcing-mno-sse.patch;patch=1 \
    file://increase-ap-startup-time.patch;patch=1 \
    file://openxt-xen-xsmv4vuse.patch \
    file://xenstat-disable-tmem-use.patch;patch=1 \
    file://acpi-slic-support.patch \
    file://tboot-xen-evtlog-support.patch \
    file://disable-cpuid-hle-rtm.patch \
    file://allow-stubdoms-cacheattr-control.patch \
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
    file://domain-reboot.patch \
    ${BLKTAP_PATCHQUEUE} \
    file://efi-hardcode-openxt-cfg.patch \
    file://0001-EFI-add-EFI_LOAD_OPTION-support.patch \
    file://shim-support-for-shim-lock-measure.patch \
    file://efi-require-shim.patch \
    file://tboot-measure-and-launch-from-xen-efi.patch \
    file://efi-load-options-no-default-image-name.patch \
"

COMPATIBLE_HOST = 'i686-oe-linux|(x86_64.*).*-linux|aarch64.*-linux'

PACKAGECONFIG =+ "xsm"
PACKAGECONFIG =+ "hvm"

S = "${WORKDIR}/git"

#--
# Override meta-virtualization's path to seabios and add ovmf:
PACKAGECONFIG[hvm] = "--with-system-seabios="/usr/share/firmware/bios.bin" --with-system-ovmf="/usr/share/firmware/ovmf.bin",--disable-seabios,seabios ipxe vgabios ovmf,"

#--
# Modifications to the meta-virtualiation configuration (ref: OXT-1016)

# iproute2 on OpenXT is currently a MACHINE-specific recipe
# which causes all recipes that DEPEND upon it to be rebuilt for each MACHINE.
# Drop that apparently-unnecessary dependency here to avoid that.

DEPENDS_remove = "iproute2"

# The stubs and deploy tasks are detected as MACHINE-specific due to the
# variables that they use. Those tasks are not required here so drop them.

deltask stubs
deltask deploy
#--
