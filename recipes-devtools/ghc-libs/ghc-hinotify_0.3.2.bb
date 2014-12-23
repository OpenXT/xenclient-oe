BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

SRC_URI[md5sum] = "1105bc75012bd2a299181c47b21cac39"
SRC_URI[sha256sum] = "88c86540528f9d36bedc5779c975fbd0fd1277a4e143b9a078871ba7c2ce293f"
SRC_URI += "file://hinotify-fix-watching-symlinks.patch;patch=1"

DEPENDS += ""
DESCRIPTION = "This library provides a wrapper to the Linux Kernel's inotify feature, allowing applications to subscribe to notifications when a file is accessed or modified."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=842154b7fc56299acd68b1fe3fcd79b2"
GHC_PN = "hinotify"

