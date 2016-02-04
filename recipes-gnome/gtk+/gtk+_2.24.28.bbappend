PR .= ".1"

PACKAGES_DYNAMIC += "^gtk+-locale-*"

BBCLASSEXTEND = "native"
RRECOMMENDS_${PN}_class-native = ""
DEPENDS_class-native = "glib-2.0-native atk-native pango-native cairo-native gdk-pixbuf-native"
