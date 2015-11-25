PR .= ".1"

# meta-selinux does not cover vim-tiny so do this ourselves
DEPENDS += "libselinux"
EXTRA_OECONF += "--enable-selinux --disable-acl"
