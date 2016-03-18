PR .= ".1"

# TODO: Upstream glib disables SELinux for class-native.  Not sure if this affects OpenXT
# or not so removing it for now.  Needs more investigation.
EXTRA_OECONF_class-native := "${@oe_filter_out('--disable-selinux', '${EXTRA_OECONF_class-native}', d)}"

EXTRA_OECONF += " --enable-static"

