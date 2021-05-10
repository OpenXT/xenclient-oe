require classes/module-signing.bbclass
# override value from module-signing to use the in-tree utility
SIGN_FILE = "${B}/scripts/sign-file"

# Set KERNEL_MODULE_SIG_CERT in local.conf to the filepath of a public key
# to embed in the kernel to verify signed modules
export KERNEL_MODULE_SIG_CERT

do_configure_append() {
    if ! grep -q '^CONFIG_MODULE_SIG=y' ${B}/.config ; then
        return
    fi
    if [ -z "${KERNEL_MODULE_SIG_CERT}" ]; then
        bbfatal "Kernel module signing should only be used when setting \
KERNEL_MODULE_SIG_CERT in local.conf."
    fi

    sed -i -e '/CONFIG_MODULE_SIG_KEY[ =]/d' ${B}/.config
    echo "CONFIG_MODULE_SIG_KEY=\"${KERNEL_MODULE_SIG_CERT}\"" >> \
        ${B}/.config
    sed -i -e '/CONFIG_MODULE_SIG_ALL[ =]/d' ${B}/.config
    echo "# CONFIG_MODULE_SIG_ALL is not set" >> \
        ${B}/.config
}

do_configure[file-checksums] += "${@get_signing_cert(d)}"
