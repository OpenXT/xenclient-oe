LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac  \
                    file://COPYING.LIB;md5=79ffa0ec772fa86740948cb7327a0cc7"
DEPENDS = "xen-tools alsa-lib pciutils libpng xen-blktap libv4v openssl zlib libcap-ng libdmbus"

# Leave this in it's own definition so that it can be replaced with a local
# tarball (the QEMU download speed is terrible). If this is combined with the
# other patches, it doesn't seem to work
SRC_URI = "http://wiki.qemu-project.org/download/qemu-${PV}.tar.bz2"

# QEMU Patch Queue
SRC_URI += "file://xsa-126-unmediated-pci-command-register-access-in-qemu.patch;patch=1 \
            file://xsa-128-potential-unintended-writes-to-host-msi-message-data-field-via-qemu.patch;patch=1 \
            file://xsa-129-pci-msi-mask-bits-inadvertently-exposed-to-guests.patch;patch=1 \
            file://xsa-130-guest-triggerable-qemu-msi-x-pass-through-error-messages.patch;patch=1 \
            file://xsa-131-unmediated-pci-register-access-in-qemu.patch;patch=1 \
            file://xsa-133-privilege-escalation-via-emulated-floppy-disk-drive.patch;patch=1 \
            file://xsa-135-heap-overflow-in-qemu-pcnet-controller.patch;patch=1 \
            file://xsa-138-qemu-heap-overflow-flaw-while-processing-certain-atapi-commands.patch;patch=1 \
            file://xsa-139-use-after-free-in-qemu-xen-block-unplug-protocol.patch;patch=1 \
            file://xsa-140-qemu-leak-of-uninitialized-heap-memory-in-rtl8139-device-model.patch;patch=1 \
            file://xsa-155-paravirtualized-drivers-incautious-about-shared-memory-contents.patch;patch=1 \
            file://xsa-162-heap-buffer-overflow-vulnerability-in-pcnet-emulator.patch;patch=1 \
            file://vbe-make-bochs-dispi-interface-return-the-correct-me.patch;patch=1 \
            file://cve-2014-3615-vbe-rework-sanity-checks.patch;patch=1 \
            file://xsa-179-banked-access-to-vga-memory-uses-inconsistent-bounds-checks.patch;patch=1 \
            file://vga-add-sr_vbe-register-set.patch;patch=1 \
            file://0001-compile-time-stubdom-flag.patch \
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
            file://0024-Xenstore-based-ISO-media-change-detection-for-both-s.patch;striplevel=1 \
            file://0026-openxtaudio.patch;striplevel=1 \
            file://0027-nic-link-state-propagation.patch;striplevel=1 \
            file://0029-stubdom-read-gsi-from-device-config-space.patch;striplevel=1 \
            file://0030-xenfv-i440fx-max-ram-below-4g.patch;striplevel=1 \
            file://0031-bp-rt8139-net-queue-flush-fix-00b7ade807b5ce6779ddd86ce29c5521ec5c529a.patch;striplevel=1 \
            file://0032-bp-hub-net-queue-flush-fix-199ee608f0d08510b5c6c37f31a7fbff211d63c4.patch;striplevel=1 \
            file://0033-acpi-pm-feature.patch;striplevel=1 \
            file://0034-maintain-time-offset.patch;striplevel=1 \
            file://0035-add-acpi-wakeup.patch;striplevel=1 \
            "

SRC_URI[md5sum] = "78f13b774814b6b7ebcaf4f9b9204318"
SRC_URI[sha256sum] = "066297ed77408fb7588889c271a85cf3c259ad55c939315988e6062d7708eda8"

S = "${WORKDIR}/qemu-${PV}"

inherit xenclient autotools-brokensep

# Include a minimal amount of blobs by default.
FILES_${PN} += "/usr/share/qemu/keymaps/en-us       \
                /usr/share/qemu/keymaps/commons     \
                /usr/share/qemu/keymaps/modifiers   \
                "
FILES_${PN}-dbg += "/usr/lib/.debug/* /usr/libexec/.debug/*"
FILES_${PN}-extra-keymaps = "/usr/share/qemu/keymaps/*"
FILES_${PN}-extra-roms = "/usr/share/qemu/*"
INSANE_SKIP_${PN}-extra-roms = "arch"
FILES_${PN}-utils = "/usr/libexec/*"

PACKAGES += "${PN}-extra-keymaps ${PN}-extra-roms ${PN}-utils"

EXTRA_OECONF += " \
    --enable-xen \
    --target-list=i386-softmmu \
    --without-system-pixman \
    --disable-sdl \
    --disable-vnc \
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
    --enable-pie \
    --enable-werror \
    --enable-surfman \
    --enable-atapi-pt \
    --enable-atapi-pt-v4v \
    --enable-readonly-ide \
    --enable-debug \
"

do_configure(){
    ${S}/configure --prefix=${prefix} --sysconfdir=${sysconfdir} ${EXTRA_OECONF}
}

EXTRA_OEMAKE += "AUTOCONF_HOST='--host=`arch`' DESTDIR=${D} STRIP=''"

CFLAGS_append = " -Wno-unused-parameter -Wno-unused-local-typedefs --param ssp-buffer-size=4 -ftrapv -D_FORTIFY_SOURCE=2"

INC_PR = "r16"
