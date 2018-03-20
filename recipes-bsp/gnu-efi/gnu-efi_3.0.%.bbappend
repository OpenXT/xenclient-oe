# Force 64-bit version.
# Cheat and use the host environment. This would normaly use glibc headers from
# the target sysroot to figure out basic informations. It becomes a problem as
# gnu-efi is forced in 64bit and will redefine critical macros.
# Since this only produce static libraries, c-runtime and ld scripts, use and
# rely on the host capabilities.
# Requires libc64 headers on the host (debian libc6-dev-amd64).
# This will no longer be necessary with a 64bit target environement.
EXTRA_OEMAKE = " \
    'ARCH=x86_64' \
    'RANLIB=${RANLIB}' \
    'OBJCOPY=${OBJCOPY}' \
    'PREFIX=${prefix}' \
    'LIBDIR=${libdir}' \
"

INSANE_SKIP_${PN}-dev = "arch"
