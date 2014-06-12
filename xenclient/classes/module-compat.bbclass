inherit module

# Overwrite do_compile and do install tasks as new OE does not set KERNELDIR
do_compile() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
                   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
                   KERNELDIR=${STAGING_KERNEL_DIR}    \
                   KERNEL_VERSION=${KERNEL_VERSION}    \
                   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
                   AR="${KERNEL_AR}" \
                   ${MAKE_TARGETS}
}

do_install() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
                   KERNEL_SRC=${STAGING_KERNEL_DIR} \
                   KERNELDIR=${STAGING_KERNEL_DIR}    \
                   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
                   modules_install
}
