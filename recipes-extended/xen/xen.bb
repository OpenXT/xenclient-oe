require xen.inc

PROVIDES = "xen"

PACKAGES += "${PN}-efi"

FILES_${PN}-efi = "/usr/lib64/efi/*"
FILES_${PN}-dbg += "/boot/xen*syms*"
FILES_${PN} += "/boot"

EXTRA_OEMAKE += "CFLAGS='' LDFLAGS='' XEN_TARGET_ARCH=x86_64 CC='${BUILD_CC}' LD='${BUILD_LD}' XEN_VENDORVERSION=-xc"

# skip insane arch test as xen is compiled for different arch than rest of stuff
INSANE_SKIP_${PN}-dbg = "arch"

do_configure() {
        echo "debug := n" > .config
	echo "XSM_ENABLE := y" >> .config
	echo "FLASK_ENABLE := y" >> .config
}

do_compile() {
        oe_runmake dist-xen
}

do_install() {
        install -d ${D}/boot
        oe_runmake DESTDIR=${D} install-xen
        ln -sf "`basename ${D}/boot/xen-*xc.gz`" ${D}/boot/xen-debug.gz
}

PR = "${INC_PR}.5"
