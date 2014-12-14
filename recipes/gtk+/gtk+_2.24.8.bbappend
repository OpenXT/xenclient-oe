PRINC = "1"
EXTRA_OECONF += "--enable-xkb"
PACKAGES_DYNAMIC += "gtk+-locale-*"

BBCLASSEXTEND = "native"
RRECOMMENDS_${PN}_virtclass-native = ""
DEPENDS_virtclass-native = "glib-2.0-native atk-native pango-native cairo-native gdk-pixbuf-native"
