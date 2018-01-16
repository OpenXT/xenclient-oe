# sysroot{,-native} will need access to xc-rpcgen (xenclient-rpcgen-native), to
# run against the XML IDL and templates (xenclient-idl).
DEPENDS_append += " \
    xenclient-idl \
    xenclient-rpcgen-native \
"
