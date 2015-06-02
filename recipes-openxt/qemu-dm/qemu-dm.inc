LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = "xen-tools alsa-lib pciutils libpng blktap libxenacpi libv4v openssl zlib libcap-ng libdmbus"

# These patches were removed until further stabilization occurs. ioreq will likely not
# return until a move to Xen 4.5 occurs. Pulling ioreq requires an additional patch
# "0002-hvm..." and a removal of a Xen patch (xc-xt-multiple-ioreq)
#
#            file://0003-ioreq-server-upstream.patch;striplevel=1 \
#            file://0004-ioreq-server.patch;striplevel=1 \
#            file://0010-xc-emulated-nic-link-state-propagation.patch;striplevel=1 \
#            file://0012-audio-alsa.patch;striplevel=1 \
#            file://0014-audio-alsa-stub.patch;striplevel=1 \
#            file://0026-openxtaudio.patch;striplevel=1 \
#            file://0007-pci-reserve-igd-slot.patch;striplevel=1 \

# Leave this in it's own definition so that it can be replaced with a local
# tarball (the QEMU download speed is terrible). If this is combined with the
# other patches, it doesn't seem to work
SRC_URI = "http://wiki.qemu-project.org/download/qemu-${PV}.tar.bz2"

# QEMU Patch Queue
SRC_URI += "file://0001-compile-time-stubdom-flag.patch \
            file://0001-generic-xenstore-extensions.patch \
            file://0001-readonly-ide.patch;striplevel=1 \
            file://0003-hvm-param-dm-domain.patch;striplevel=1 \
            file://0005-logging-syslog.patch;striplevel=1 \
            file://0006-dmbus.patch;striplevel=1 \
            file://0008-switcher.patch;striplevel=1 \
            file://0009-acpi.patch;striplevel=1 \
            file://0013-xenmou.patch;striplevel=1 \
            file://0015-atapi-pass-through.patch;striplevel=1 \
            file://0016-vbe-xt-extensions.patch;striplevel=1 \
            file://0017-vga-spinlock.patch;striplevel=1 \
            file://0018-vga-shadow-bda.patch;striplevel=1 \
            file://0019-surfman-dcl.patch;striplevel=1 \
            file://0020-audio-policy.patch;striplevel=1 \
            file://0023-msix-cap-disable.patch;striplevel=1 \
            file://0026-openxtaudio.patch;striplevel=1 \
            file://0027-nic-link-state-propagation.patch;striplevel=1 \
            file://0029-stubdom-read-gsi-from-device-config-space.patch;striplevel=1 \
            file://0030-xenfv-i440fx-max-ram-below-4g.patch;striplevel=1 \
            file://0031-bp-rt8139-net-queue-flush-fix-00b7ade807b5ce6779ddd86ce29c5521ec5c529a.patch;striplevel=1 \
            file://0032-bp-hub-net-queue-flush-fix-199ee608f0d08510b5c6c37f31a7fbff211d63c4.patch;striplevel=1 \
            file://0033-acpi-pm-feature.patch;striplevel=1 \
            "

SRC_URI[md5sum] = "78f13b774814b6b7ebcaf4f9b9204318"
SRC_URI[sha256sum] = "066297ed77408fb7588889c271a85cf3c259ad55c939315988e6062d7708eda8"

S = "${WORKDIR}/qemu-${PV}"

inherit xenclient
inherit autotools

# Include a minimal amount of blobs by default.
FILES_${PN} += "/usr/share/qemu/keymaps/en-us       \
                /usr/share/qemu/keymaps/commons     \
                /usr/share/qemu/keymaps/modifiers   \
                "
FILES_${PN}-extra-keymaps = "/usr/share/qemu/keymaps/*"
FILES_${PN}-extra-roms = "/usr/share/qemu/*"
INSANE_SKIP_${PN}-extra-roms = "arch"

PACKAGES += "${PN}-extra-keymaps ${PN}-extra-roms"

do_configure(){
    ${S}/configure ${EXTRA_OECONF} --enable-xen \
                --target-list=i386-softmmu \
                --prefix=${prefix} \
                --sysconfdir=${sysconfdir} \
                --extra-cflags="-I${STAGING_INCDIR}" \
                --extra-ldflags="-L${STAGING_LIBDIR}" \
                --without-system-pixman \
                --disable-sdl \
                --disable-vnc \
                --disable-pie \
                --disable-docs \
                --disable-strip \
                --disable-virtfs \
                --disable-bluez \
                --disable-guest-agent \
                --disable-curses \
                --disable-slirp \
                --disable-curl \
                --disable-kvm \
                --disable-tools \
                --disable-guest-base \
                --enable-werror \
                --enable-surfman \
                --enable-atapi-pt \
                --enable-atapi-pt-v4v \
                --enable-readonly-ide \
                --enable-debug
}

do_compile(){
    DESTDIR=${D} oe_runmake AUTOCONF_HOST='--host=`arch`'
}

do_install(){
    # QEMU's build system conflicts with STRIP defined in OE.
    DESTDIR=${D} oe_runmake STRIP='' install
}

INC_PR = "r13"
