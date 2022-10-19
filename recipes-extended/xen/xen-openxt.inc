# Undo some of the upstream xen.inc configuration to retain
# the classic OpenXT Xen build configuration:
EXTRA_OECONF_remove = " \
    --exec-prefix=/usr \
    --prefix=/usr \
    --host=${HOST_SYS} \
    --with-systemd=${systemd_unitdir}/system \
    --with-systemd-modules-load=${systemd_unitdir}/modules-load.d \
    --disable-stubdom \
    --disable-ioemu-stubdom \
    --disable-pv-grub \
    --disable-xenstore-stubdom \
    --disable-ocamltools \
    --with-initddir=${INIT_D_DIR} \
    --with-sysconfig-leaf-dir=default \
    --with-system-qemu=${bindir}/qemu-system-i386 \
    --disable-qemu-traditional \
"

# Force the target architecture for the hypervisor build to be 64-bit
# irrespective of the current MACHINE settings.
# Also supply a custom vendor suffix.
EXTRA_OEMAKE += " \
    XEN_TARGET_ARCH=x86_64 \
    XEN_VENDORVERSION=-xc \
    "
# Since this recipe forces the hypervisor build to target 64-bit architecture
# even when MACHINE is 32-bit, disable this usual arch sanity check:
INSANE_SKIP_${PN}-dbg = "arch"

do_configure() {
    echo "debug := n" > .config
    echo "XSM_ENABLE := y" >> .config
    echo "FLASK_ENABLE := y" >> .config

    cp "${WORKDIR}/defconfig" "${B}/xen/.config"
    #Define CONFIG_TXT_OP in the hypervisor build to export tboot evtlog data
    #It's stubbed out for the pv-shim since it's not supported, but uses the
    #same hypercall headers
    echo "CONFIG_TXT_OP=y" >> "${B}/xen/.config"

    # Enable argo
    echo "CONFIG_EXPERT=y" >> "${B}/xen/.config"
    echo "CONFIG_ARGO=y" >> "${B}/xen/.config"

    unset CFLAGS

    # do configure
    oe_runconf EXTRA_CFLAGS_XEN_CORE="${EXTRA_CFLAGS_XEN_CORE}" \
               EXTRA_CFLAGS_XEN_TOOLS="${EXTRA_CFLAGS_XEN_TOOLS}" \
               PYTHON="${PYTHON}"
}

do_install_append() {
    install -d ${D}/boot
    ln -sf "`basename ${D}/boot/xen-*xc.gz`" ${D}/boot/xen-debug.gz

    # Install the efi binary into /boot rather than /usr/lib64/efi
    install -m 600 ${B}/xen/xen.efi ${D}/boot/
    rm -rf ${D}/usr/lib64
}
