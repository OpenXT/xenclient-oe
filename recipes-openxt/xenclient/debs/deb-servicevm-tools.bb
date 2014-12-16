LICENSE = "GPL"
COMPATIBLE_MACHINE = "(xenclient-nilfvm)"

DEB_PACKAGES = "\
        linux-xenclient-nilfvm \
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
