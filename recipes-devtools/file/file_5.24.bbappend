SRC_URI = "ftp://ftp.astron.com/pub/file/file-5.24.tar.gz \
        file://debian-742262.patch \
        file://0001-Add-P-prompt-into-Usage-info.patch \
        file://host-file.patch \
        "

SRC_URI[md5sum] = "ec161b5a0d2aef147fb046e5630b1408"
SRC_URI[sha256sum] = "802cb3de2e49e88ef97cdcb52cd507a0f25458112752e398445cea102bc750ce"

S = "${WORKDIR}/${BPN}-${PV}"
