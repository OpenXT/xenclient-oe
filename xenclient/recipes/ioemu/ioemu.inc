
inherit xenclient
inherit xenclient-pq
inherit pkgconfig

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"

DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v libdmbus openssl seabios ipxe"

SRC_URI = "${OPENXT_GIT_MIRROR}/ioemu.git;protocol=git;tag=${OPENXT_TAG} \
	   ${OPENXT_GIT_MIRROR}/ioemu-pq.git;protocol=git;tag=${OPENXT_TAG} \
	   file://qemu-ifup-stubdom"

S = "${WORKDIR}/git"

FILES_${PN}-dbg += "/usr/lib/xen/bin/.debug"
FILES_${PN} += "/usr/lib/xen/bin /etc/xen /usr/share/xen/qemu"

# hack, sneak --sysroot in CFLAGS and LDFLAGS as qemu build scripts do not deal to good with crosscompilation
CFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} "
LDFLAGS_prepend = "--sysroot=${STAGING_DIR_TARGET} "

# export some variable for IOEMU compilation
export STAGING_INCDIR
export STAGING_LIBDIR
export IDLDIR = "${STAGING_IDLDIR}"
export RPCGEN_TEMPLATES = "${STAGING_DATADIR}/xc-rpcgen"

do_configure() {
	# fixme these extras should be a patch in configure
        export CFLAGS="${CFLAGS} -msse4.1"
        ./xen-setup --cross-prefix=${HOST_PREFIX} --extra-ldflags="${TARGET_LDFLAGS}"
}

do_compile(){
    # BitBake run make with -j8 but it doesn't work with ioemu
    make
}

#do_stage(){
#        mkdir -p ${STAGING_DIR_TARGET}/usr/src
#        rsync --exclude-from '${S}/.gitignore' --exclude '.git' -qr ${S}/ ${STAGING_DIR_TARGET}/usr/src/ioemu
#}

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/i386-dm/qemu-dm ${D}/usr/lib/xen/bin/qemu-dm
        install -d ${D}/etc/xen
        install -d ${D}/etc/xen/scripts
        install -m 755 ${S}/i386-dm/qemu-ifup-Linux ${D}/etc/xen/scripts/qemu-ifup

	install -d ${D}/usr/share/xen/qemu
	BLOBS="bios.bin vgabios.bin vgabios-cirrus.bin \
		video.x \
		pxe-ne2k_pci.bin pxe-rtl8139.bin pxe-pcnet.bin pxe-e1000.bin \
		bamboo.dtb"
	for f in $BLOBS ; do
		install -m 0644 ${S}/pc-bios/$f ${D}/usr/share/xen/qemu
	done

        # install the keymaps (for vnc)
        install -d ${D}/usr/share/xen/qemu/keymaps
        for i in ${S}/keymaps/* ; do
                install -m 0644 ${i} ${D}/usr/share/xen/qemu/keymaps
        done
}

do_install_append_xenclient-stubdomain() {
        rm -f ${D}/etc/xen/scripts/qemu-ifup
        install -d ${D}/etc/xen
        install -m 755 ${WORKDIR}/qemu-ifup-stubdom ${D}/etc/xen/scripts/qemu-ifup
}
