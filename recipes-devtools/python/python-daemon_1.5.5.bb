DESCRIPTION = "Python library to implement a well-behaved UNIX daemon process"
LICENSE = "PSFv2 & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.PSF-2;md5=df85bcaff3f7eee8d043d15be0f5f123      \
                    file://LICENSE.GPL-2;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS_${PN} += "python-io \
                   python-lang \
                   python-resource"

S = "${WORKDIR}/python-daemon-${PV}" 

SRC_URI = "https://pypi.python.org/packages/source/p/python-daemon/python-daemon-${PV}.tar.gz;name=tarball"
SRC_URI[tarball.md5sum] = "1f6cd41473c2e201021a0aeef395b2b1"
SRC_URI[tarball.sha256sum] = "1406962e48ce03642c6057f40f9ffd49493792a7b34357fe9e264708748c83c0"

inherit setuptools
