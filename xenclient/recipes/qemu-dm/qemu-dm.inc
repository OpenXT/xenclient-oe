LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v openssl seabios ipxe zlib"

SRC_URI = "${OPENXT_GIT_MIRROR}/qemu-dm.git;protocol=git;tag=${OPENXT_TAG} \
           ${OPENXT_GIT_MIRROR}/qemu-dm-pq.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

inherit xenclient
inherit xenclient-pq

do_configure(){
    ${S}/configure ${EXTRA_OECONF} --enable-xen \
                --target-list=i386-softmmu \
                --prefix=${prefix} --sysconfdir=${sysconfdir} \
                --extra-cflags="-I${STAGING_INCDIR}" \
                --extra-ldflags="-L${STAGING_LIBDIR}" \
                --disable-sdl --disable-vnc \
                --disable-pie --disable-strip    \
                --disable-virtfs --disable-bluez \
                --disable-guest-agent --disable-curses \
                --disable-slirp --disable-curl \
                --disable-kvm
}

do_install(){
    # Prevent qemu build system to strip the binary and let OE do it later
    export STRIP="true"
    install -d ${D}/usr/bin
    install -m 755 ${S}/i386-softmmu/qemu-system-i386 ${D}/usr/bin/qemu-system-i386
}
