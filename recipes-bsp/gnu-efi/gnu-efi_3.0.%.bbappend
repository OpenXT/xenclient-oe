# Force 64-bit version.
EXTRA_OEMAKE = " \
    'ARCH=x86_64' \
    'CC=${CC}' \
    'AS=${AS}' \
    'LD=${LD}' \
    'AR=${AR}' \
    'RANLIB=${RANLIB}' \
    'OBJCOPY=${OBJCOPY}' \
    'PREFIX=${prefix}' \
    'LIBDIR=${libdir}' \
"
