
require recipes-openxt/tboot/tboot.inc

# This is not a very good way to do this. Some update to this file like a
# change to the copyright date broke the checksum.
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://tboot/include/types.h;beginline=4;endline=32;md5=5a2b57a442b97cfc3d81ba639fda3ac1"

PR = "r5"

S="${WORKDIR}/tboot-1.7.0"

SRC_URI = "http://downloads.sourceforge.net/tboot/tboot-1.7.0.tar.gz \
           file://tboot-config-cross-compile.patch;patch=1 \
           file://tboot-broken-lcptools-build.patch;patch=1 \
           file://prot-mem.patch;patch=1 \
           file://tboot-serial-card.patch;patch=1 \
           file://tboot-add-ehci-handoff.patch;patch=1 \
           file://tboot-xenclient-policy.patch;patch=1 \
           file://tboot-min-ram.patch;patch=1 \
           file://tboot-bypass-ffffffff-error.patch;patch=1 \
           file://tboot-adjust-grub2-modules.patch;patch=1 \
           file://set-tboot-private-region-as-reserved.patch;patch=1 \
           file://configure_tboot \
           file://lcp_data.bin \
           file://tboot-1.7.0-CVE-2014-5118.patch \
           "
SRC_URI[md5sum] = "1913ec6170a10f16fdcc2220b6f45d4a"
SRC_URI[sha256sum] = "01e8329c59ef0d8e06e12c1bbee007348272269ff11765aedafc5961f79567b6"
