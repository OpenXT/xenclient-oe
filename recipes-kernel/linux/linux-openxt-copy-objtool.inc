# Backport from upstream commit to attend 4.14 and later kernels and
# out-of-tree modules:
# https://patchwork.openembedded.org/patch/148047/
do_shared_workdir_append () {
    # With CONFIG_UNWINDER_ORC (the default in 4.14), objtool is required for
    # out-of-tree modules to be able to generate object files.
    if [ -x tools/objtool/objtool ]; then
        mkdir -p ${kerneldir}/tools/objtool
        cp tools/objtool/objtool ${kerneldir}/tools/objtool/
    fi
}
