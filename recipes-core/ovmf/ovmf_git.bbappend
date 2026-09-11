FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# xcp-ng patches taken from:
# https://github.com/xcp-ng-rpms/edk2/tree/master/SOURCES
SRC_URI += " \
    file://xcp-ng-set-default-resolution-1024-768.patch \
    file://xcp-ng-add-xen-variable.patch \
    file://xcp-ng-add-xen-platform-device-id.patch \
    file://xcp-ng-xenorder.patch \
    file://xcp-ng-keep-caching-enabled.patch \
"

do_compile:append:class-target() {
    bbnote "Building with Xen variable support."
    rm -rf ${S}/Build/Ovmf$OVMF_DIR_SUFFIX
    ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} -D XEN_VARIABLE_ENABLE=TRUE -D SECURE_BOOT_ENABLE=TRUE
    ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.xen.fd
    ln ${build_dir}/FV/OVMF_CODE.fd ${WORKDIR}/ovmf/ovmf.xen.code.fd
}

do_install:append:class-target() {
    install -d ${D}${datadir}/firmware
    install -m 0600 ${WORKDIR}/ovmf/ovmf.xen.fd ${D}${datadir}/firmware/ovmf.bin
}

PACKAGES += " \
    ${PN}-firmware \
"

FILES:${PN}-firmware += " \
    ${datadir}/firmware \
"
