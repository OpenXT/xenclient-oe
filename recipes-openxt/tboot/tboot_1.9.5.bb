require recipes-openxt/tboot/tboot.inc

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=7730ab1e15a162ca347bcc1722486d89"

PR = "r1"

S = "${WORKDIR}/${PN}-${PV}"

SRC_URI = "http://downloads.sourceforge.net/tboot/tboot-${PV}.tar.gz \
           file://build-system-integration.patch \
           file://linux-kernel-cmdline-buffer-overflow.patch \
           file://increase-cmdline-in-linux-real-mode-boot-header-to-1024.patch \
           file://tboot-adjust-grub2-modules.patch \
           file://warn-on-failure-policy.patch \
           file://tpm-reserve-mmio-region.patch \
           file://add-a-PCR-calculator-to-predict-measurements.patch \
           "

SRC_URI[md5sum] = "7946ed861628fab1e6d5e35fbcc5d614"
SRC_URI[sha256sum] = "c7032e367ac0129493c9bb1fcd1437f400ff5533c970119ddce281ff4d58a13f"

