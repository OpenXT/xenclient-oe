PR .= ".1"

# Make sure we use libgcrypt as we do not link it against libcap
PACKAGECONFIG_append = "gcrypt"
