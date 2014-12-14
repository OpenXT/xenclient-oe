LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v openssl seabios ipxe zlib"

PV = "0+git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/qemu-dm.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};name=source \
    file://static-fix.patch;patch=1 \
    file://ioreq-server-upstream.patch;patch=1 \
    file://ioreq-server.patch;patch=1 \
    file://logging-syslog.patch;patch=1 \
    file://dmbus.patch;patch=1 \
    file://surfman.patch;patch=1 \
    file://switcher.patch;patch=1 \
    file://acpi.patch;patch=1 \
    file://xc-emulated-nic-link-state-propagation.patch;patch=1 \
    file://battery.patch;patch=1 \
    file://audio-alsa.patch;patch=1 \
    file://xenmou.patch;patch=1 \
    file://audio-alsa-stub.patch;patch=1 \
    file://atapi-pass-through.patch;patch=1 \
"

S = "${WORKDIR}/git"

inherit xenclient

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
