require xc-rpcgen-common.inc

# xc-rpcgen, for c rpcs, needs dbus-binding-tool (dbus-glib-native), and will link against dbus-glib.
DEPENDS_append += " \
    dbus-glib-native \
    dbus-glib \
"

EXTRA_OECONF_append = " \
    --with-idldir=${STAGING_IDLDATADIR} \
    --with-rpcgen-templates=${STAGING_RPCGENDATADIR_NATIVE} \
"
