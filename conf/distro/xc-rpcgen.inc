#
# rpcgen path definitions.
#

RPCGEN_VERSION = "1.0"

# XML description files for the IDL.
idldatadir = "${datadir}/idl"
# XML description files for Network-manager.
nmidldatadir = "${datadir}/nm-idl"
# Header templates for targeted languages.
rpcgendatadir = "${datadir}/xc-rpcgen-${RPCGEN_VERSION}/templates"
# Staging locations.
STAGING_IDLDATADIR = "${STAGING_DATADIR}/idl"
STAGING_NMIDLDATADIR = "${STAGING_DATADIR}/nm-idl"
STAGING_RPCGENDATADIR_NATIVE = "${STAGING_DATADIR_NATIVE}/xc-rpcgen-${RPCGEN_VERSION}/templates"
