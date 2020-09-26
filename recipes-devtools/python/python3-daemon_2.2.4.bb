DESCRIPTION = "Python library to implement a well-behaved UNIX daemon process"
HOMEPAGE = "https://pagure.io/python-daemon/"
LICENSE = "Apache-2.0 & GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.ASF-2;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://LICENSE.GPL-3;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "python3-docutils-native"

SRC_URI[md5sum] = "922f2ce6ae9790994557c38faea75788"
SRC_URI[sha256sum] = "57c84f50a04d7825515e4dbf3a31c70cc44414394a71608dee6cfde469e81766"
SRC_URI += " \
        file://remove-twine.patch \
"

PYPI_PACKAGE = "python-daemon"
inherit pypi setuptools3

RDEPENDS_${PN} += " \
        python3-io \
        python3-resource \
"
