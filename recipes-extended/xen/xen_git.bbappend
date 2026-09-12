# Same revision the upstream master buildbot used (RELEASE-4.21.2).
# https://openxt.ainfosec.com/buildbot — xen-4.21+gitAUTOINC+78c2a59f9e
SRCREV = "78c2a59f9edba7ee4da31c7c95ac52a003d56b91"
XEN_REL = "4.21"
XEN_BRANCH = "stable-4.21"
LIC_FILES_CHKSUM = "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

# OpenXT's Xen recipes share a common patchqueue so reset SRC_URI
SRC_URI = "git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH};protocol=git"

require xen-common.inc
require xen-openxt.inc

DEFAULT_PREFERENCE = "1"
