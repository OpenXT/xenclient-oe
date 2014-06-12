DESCRIPTION = "Packages build during extra step"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit task

RDEPENDS_${PN} = "\
    autoconf \
    automake \
    binutils \
    essential-target-builddepends \
    ethtool \
    evtest \
    gcc \
    gdb \
    git \
    icbinn-fuse \
    ltrace \
    make \
    nfs-utils \
    powertop \
    quilt \
    screen \
    strace \
    tcpdump \
    valgrind \
    vim \
    xenclient-devscripts \
    xenclient-rpcgen \
"
