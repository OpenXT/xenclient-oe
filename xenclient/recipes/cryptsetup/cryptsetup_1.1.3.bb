# Copyright Matthias Hentges <devel@hentges.net> (c) 2007
# License: MIT (see http://www.opensource.org/licenses/mit-license.php
#               for a copy of the license)
inherit gettext

DESCRIPTION = "cryptsetup with luks support creates and manages encrypted containers and partitions"
HOMEPAGE = "http://luks.endorphin.org/"
SECTION = "console"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "e2fsprogs lvm2 libgcrypt popt"
RRECOMMENDS = "kernel-module-aes \
               kernel-module-dm-crypt \
               kernel-module-md5 \
               kernel-module-cbc \
               kernel-module-sha256 \
              "
PR = "r2xc1"

SRC_URI = "http://cryptsetup.googlecode.com/files/cryptsetup-${PV}.tar.bz2 \
	   file://luks-check-key.diff;patch=1 \
	   file://cryptsetup-mapping-name.patch;patch=1 \
"
SRC_URI[md5sum] = "318a64470861ea5b92a52f2014f1e7c1"
SRC_URI[sha256sum] = "9c8e68a272f6d9cfb6cd65cc0743f4c44a2096c61f74e0602bf40208b5e69c0a"

inherit autotools

