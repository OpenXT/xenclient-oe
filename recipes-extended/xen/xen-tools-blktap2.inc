DEPENDS += " \
    util-linux \
    openssl \
    libaio \
    libicbinn-resolved \
"

RDEPENDS_${PN}-blktap += "glibc-gconv-utf-16"
RCONFLICTS_${PN}-blktap = "blktap3"
RCONFLICTS_${PN}-blktapctl = "blktap3"
RCONFLICTS_${PN}-libvhd = "blktap3"
