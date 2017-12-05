# XML description files for the IDL.
idldatadir = "${STAGING_DATADIR}/idl"

# Header templates for targeted languages.
rpcgendatadir = "${STAGING_DATADIR_NATIVE}/xc-rpcgen-${RPCGEN_VERSION}/templates"

# sysroot{,-native} will need access to xc-rpcgen (xenclient-rpcgen-native),
# which in turn, needs dbus-binding-tool (dbus-glib-native) to run against the
# XML IDL and templates (xenclient-idl), and the compiled binary will link
# against dbus-glib.
DEPENDS_append = " \
    dbus-glib-native \
    dbus-glib \
    xenclient-idl \
    xenclient-rpcgen-native \
"

EXTRA_OECONF_append = " \
    --with-idldir=${idldatadir} \
    --with-rpcgen-templates=${rpcgendatadir} \
"
