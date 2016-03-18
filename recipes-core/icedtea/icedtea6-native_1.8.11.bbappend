PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://support_linux_3.txt"

do_replace_patches() {
    #If you need to update an icedtea patch applied during do configure, just overwrite it here
    cp -f ${WORKDIR}/support_linux_3.txt ${S}/patches/support_linux_3.patch
}

addtask replace_patches after do_patch before do_configure