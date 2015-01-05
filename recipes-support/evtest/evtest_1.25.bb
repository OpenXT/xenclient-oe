DESCRIPTION = "Simple tool for input event debugging."
HOMEPAGE = "http://cgit.freedesktop.org/evtest/"
AUTHOR = "Vojtech Pavlik <vojtech@suse.cz>"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "libxml2"

SRC_URI = "http://cgit.freedesktop.org/evtest/snapshot/evtest-${PV}.tar.bz2;name=archive"
SRC_URI[archive.md5sum] = "0ef3fe5e20fa2dee8994827d48482902"
SRC_URI[archive.sha256sum] = "6e93ef54f0aa7d263f5486ce4a14cac53cf50036bfd20cf045fef2b27ee6664b"

inherit autotools
