PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-git:"

#SRC_URI += "file://grub-add-sector-offset.patch \
#    file://grub-2.00-branding.patch \
#    file://remove-editing-and-shell.patch \
#    file://accept-video-always.patch \
#    file://explicitly-specify-root-dev.patch \
#    "

EXTRA_OECONF += " --disable-manpages \
		--enable-graphics \
		--disable-auto-linux-mem-opt \
		--disable-werror"

PACKAGECONFIG = "device-mapper"