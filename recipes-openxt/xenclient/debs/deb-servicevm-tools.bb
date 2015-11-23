LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
COMPATIBLE_MACHINE = "(xenclient-nilfvm)"

DEB_PACKAGES = "\
        linux-openxt \
        v4v-module \
        deb-libv4v \
        xenclient-toolstack \
        db-tools \
        xenclient-dbusbouncer \
        xenfb2 \
    "

DEPENDS = "${DEB_PACKAGES}"

do_configure() {
	:
}
do_compile() {
	:
}
