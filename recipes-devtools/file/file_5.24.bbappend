TAR_PN="${@"${BPN}".upper()}"
TAR_PV="${@"_".join("${PV}".split('.'))}"

SRC_URI = "https://github.com/${BPN}/${BPN}/archive/${TAR_PN}${TAR_PV}.tar.gz \
        file://debian-742262.patch \
        file://0001-Add-P-prompt-into-Usage-info.patch \
        file://host-file.patch \
        "

SRC_URI[md5sum] = "42df3827fd1b936c6fc8c38cf86a9680"
SRC_URI[sha256sum] = "52e160662c45d8b204c583552d80e4ab389a3a641f9745a458da2f6761c9b206"

S = "${WORKDIR}/${BPN}-${TAR_PN}${TAR_PV}"
