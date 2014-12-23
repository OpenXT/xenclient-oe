# Conditionally inherits xenclient-image-src-info-real.
#
# Set XENCLIENT_COLLECT_SRC_INFO to 1 to enable.

inherit ${@"xenclient-image-src-info-real"if(bb.data.getVar("XENCLIENT_COLLECT_SRC_INFO",d,1)=="1")else("null")}
