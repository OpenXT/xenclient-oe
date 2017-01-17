DESCRIPTION = "Trusted Boot"
HOMEPAGE = "http://tboot.sourceforge.net/"
SECTION = "bootloaders"
PRIORITY = "optional"
DEPENDS = "trousers"

do_compile() {
	oe_runmake SUBDIRS="tboot" CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}" LDFLAGS="" CFLAGS=""
	oe_runmake SUBDIRS="lcptools tb_polgen utils" CFLAGS+="-std=c99" TARGET_ARCH="x86_32"
}

do_install() {
	oe_runmake SUBDIRS="tboot lcptools tb_polgen utils" DISTDIR="${D}${base_prefix}" install
}

PACKAGES += " \
    ${PN}-grub \
    ${PN}-lcptools \
    ${PN}-tbpolgen \
    ${PN}-utils \
"

FILES_${PN} = " \
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
    ${sbindir}/lcp_crtpconf \
    ${sbindir}/lcp_crtpol \
    ${sbindir}/lcp_crtpol2 \
    ${sbindir}/lcp_crtpolelt \
    ${sbindir}/lcp_crtpollist \
    ${sbindir}/lcp_mlehash \
    ${sbindir}/lcp_readpol \
    ${sbindir}/lcp_writepol \
    ${sbindir}/tb_polgen \
    ${sbindir}/tpmnv_defindex \
    ${sbindir}/tpmnv_getcap \
    ${sbindir}/tpmnv_lock \
    ${sbindir}/tpmnv_relindex \
"
FILES_${PN}-utils = " \
    ${sbindir}/txt-stat \
    ${sbindir}/parse_err \
    ${sbindir}/acminfo \
"
