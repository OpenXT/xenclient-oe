require recipes-openxt/tboot/tboot.inc

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=7730ab1e15a162ca347bcc1722486d89"

S = "${WORKDIR}/${PN}-${PV}"

SRC_URI = " \
    https://downloads.sourceforge.net/project/${BPN}/${BPN}/${BPN}-${PV}.tar.gz \
    file://0001-Fix-a-logical-error-in-function-bool-evtlog_append.patch \
    file://0002-Reset-debug-PCR16-to-zero.-PCR16-is-used-to-identify.patch \
    file://0003-lcptools-v2-utilities-fixes.patch \
    file://0004-Fix-openssl-1.0.2-double-frees.patch \
    file://0005-The-size-field-of-the-MB2-tag-is-the-size-of-the-tag.patch \
    file://0006-Fix-security-vulnerabilities-rooted-in-tpm_if-struct.patch \
    file://0007-Fix-a-null-pointer-dereference-bug-when-Intel-TXT-is.patch \
    file://0008-Fix-TPM-1.2-locality-selection-issue.patch \
    file://0009-Fix-memory-leak-and-invalid-reads-and-writes-issues.patch \
    file://0010-config-Allow-build-system-integration.patch \
    file://0011-grub2-Adjust-module-placement-locations-when-changin.patch \
    file://0012-tboot-Propagate-failure-to-map_tboot_pages.patch \
    file://0013-tboot-TB_POLTYPE_WARN_ON_FAILURE-with-pre-post.patch \
    file://0014-tboot-Mark-TPM-region-reserved-if-not-already.patch \
    file://0015-pcr-calc-Add-pcr-calculator-tool.patch \
    file://0016-tb-polgen-TPM2.0-support.patch \
    file://0017-tboot-Use-SHA256-by-default-with-TPM2.0.patch \
    file://0018-tpm2.0-Perform-orderly-shutdown.patch \
    file://0019-tboot-Export-TPM-event-log-to-VMM-Kernel.patch \
    file://0020-tboot-utils-Fix-tools-build-in-64bits-env.patch \
"

SRC_URI[md5sum] = "bf785aa8637846f4c741d436146227fa"
SRC_URI[sha256sum] = "1b55eed6ca8196b2a003936594248a242888ac34ff970eda651e7660c4772a39"
