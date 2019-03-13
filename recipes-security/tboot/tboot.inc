DESCRIPTION = "Trusted Boot"
HOMEPAGE = "http://tboot.sourceforge.net/"
SECTION = "bootloaders"
PRIORITY = "optional"
DEPENDS = "trousers"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_x86 = ".*"
COMPATIBLE_MACHINE_x86-64 = ".*"

require tboot-arch.inc

INSANE_SKIP_${PN} = "arch"

export TBOOT_TARGET_ARCH = "${@map_tboot_arch(d.getVar('TARGET_ARCH'), d)}"

python () {
    if d.getVar('TBOOT_TARGET_ARCH') == 'INVALID':
        raise bb.parse.SkipPackage('Cannot map `%s` to a xen architecture' % d.getVar('TARGET_ARCH'))
}

do_compile() {
	oe_runmake SUBDIRS="tboot" CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}"
	oe_runmake SUBDIRS="lcptools lcptools-v2 tb_polgen utils pcr-calc" TARGET_ARCH="${TBOOT_TARGET_ARCH}"
}

do_install() {
	oe_runmake SUBDIRS="tboot lcptools lcptools-v2 tb_polgen utils pcr-calc" DISTDIR="${D}${base_prefix}" install
	gunzip -k "${D}${base_prefix}/boot/tboot.gz"
}

PACKAGES += " \
    ${PN}-grub \
    ${PN}-lcptools \
    ${PN}-lcptools-v2 \
    ${PN}-tbpolgen \
    ${PN}-utils \
    ${PN}-pcr-calc \
"

FILES_${PN} = " \
    ${base_prefix}/boot/tboot \
    ${base_prefix}/boot/tboot.gz \
"
FILES_${PN}-dbg += " \
    ${base_prefix}/boot/tboot-syms \
"
FILES_${PN}-grub = " \
    ${sysconfdir}/grub.d/20_linux_tboot \
    ${sysconfdir}/grub.d/20_linux_xen_tboot \
"
FILES_${PN}-lcptools = " \
    ${sbindir}/lcp_readpol \
    ${sbindir}/lcp_writepol \
    ${sbindir}/tb_polgen \
    ${sbindir}/tpmnv_defindex \
    ${sbindir}/tpmnv_getcap \
    ${sbindir}/tpmnv_lock \
    ${sbindir}/tpmnv_relindex \
"

FILES_${PN}-lcptools-v2 = " \
    ${sbindir}/lcp2_mlehash \
    ${sbindir}/lcp2_crtpol \
    ${sbindir}/lcp2_crtpollist \
    ${sbindir}/lcp2_crtpolelt \
"

FILES_${PN}-utils = " \
    ${sbindir}/txt-stat \
    ${sbindir}/parse_err \
    ${sbindir}/acminfo \
"
FILES_${PN}-pcr-calc = " \
    ${sbindir}/module_hash \
    ${sbindir}/pcr-calc \
    ${sbindir}/acmmatch \
"
