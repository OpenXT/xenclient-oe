PR .= ".1"

DEPENDS_class-native += "attr-native"

# ensure coreutils-native's cp supports xattr for surfman package config
EXTRA_OECONF_class-native += "--enable-xattr"
