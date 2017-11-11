# Force 64-bit version
EXTRA_OEMAKE = "'ARCH=x86_64' 'CC=${CC}' 'AS=${AS}' 'LD=${LD}' 'AR=${AR}' \
                'RANLIB=${RANLIB}' 'OBJCOPY=${OBJCOPY}' 'PREFIX=${prefix}' 'LIBDIR=${libdir}' \
                "

# gnu-efi's Makefile treats prefix as toolchain prefix, so don't
# export it.
prefix[unexport] = "1"
