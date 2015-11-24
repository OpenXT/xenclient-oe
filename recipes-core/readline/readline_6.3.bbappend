PR .= ".1"

# Code examples should not be installed with the binary package.
PACKAGES += "readline-extra"
FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-extra = "${datadir}/${BPN}/*"
