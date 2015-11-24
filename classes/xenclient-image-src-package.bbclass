# Conditionally inherits the package xenclient-image-src-package-real.
#
# Set XENCLIENT_BUILD_SRC_PACKAGES to 1 to enable.

inherit ${@"xenclient-image-src-package-real"if(d.getVar("XENCLIENT_BUILD_SRC_PACKAGES",1)=="1")else("null")}

