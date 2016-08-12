require recipes-kernel/intel/intel-microcode.inc

LIC_FILES_CHKSUM = "file://microcode.dat;md5=d8c57b35388b4b9f970f5377fcdfc486"

SRC_URI = "${OPENXT_MIRROR}/microcode-${PV}.tgz " 
SRC_URI[md5sum] = "84e4c0530dc38fd7b804daf894b1bdf9"
SRC_URI[sha256sum] = "f3a9c6fc93275bf1febc26f7c397ac93ed5f109e47fb52932f6dbd5cfdbc840e"
