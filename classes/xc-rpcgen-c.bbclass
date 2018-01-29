# sysroot{,-native} will need access to xc-rpcgen (xenclient-rpcgen-native), to
# run against the XML IDL and templates (xenclient-idl).
# xc-rpcgen, for c rpcs, needs dbus-binding-tool (dbus-glib-native), and will
# link against dbus-glib.
DEPENDS_append += " \
    xenclient-idl \
    xenclient-rpcgen-native \
    dbus-glib-native \
    dbus-glib \
"

EXTRA_OECONF_append = " \
    --with-idldir=${STAGING_IDLDATADIR} \
    --with-rpcgen-templates=${STAGING_RPCGENDATADIR_NATIVE} \
"
