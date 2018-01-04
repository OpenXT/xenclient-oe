# XML description files for the IDL.
idldatadir = "${STAGING_DATADIR}/idl"

# XML description files for Network-manager.
nmidldatadir = "${STAGING_DATADIR}/nm-idl"

# Header templates for targeted languages.
rpcgendatadir = "${STAGING_DATADIR_NATIVE}/xc-rpcgen-${RPCGEN_VERSION}/templates"

# sysroot{,-native} will need access to xc-rpcgen (xenclient-rpcgen-native),
# to run against the
# XML IDL and templates (xenclient-idl).
DEPENDS_append = " \
    xenclient-idl \
    xenclient-rpcgen-native \
"
