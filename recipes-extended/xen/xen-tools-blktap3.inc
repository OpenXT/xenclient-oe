FILESEXTRAPATHS_prepend := "${THISDIR}/blktap3:"

SRC_URI_append = " \
    git://github.com/xapi-project/blktap.git;protocol=https;destsuffix=git/tools/blktap3;name=blktap3 \
    file://tapback.initscript \
    file://fix-format-specifier-errors.patch \
    file://compiler-errors-fix.patch \
    file://remove-inline-function-declarations.patch \
    file://OXT-specific-errors-fix.patch \
    file://fix-run-time-errors-and-memory-leaks.patch \
    file://fix-segfault-if-startup-fails.patch \
    file://remove-creation-of-unused-log-files.patch \
    file://add-device-string-support-to-tap-destroy.patch \
    file://fix-error-checks.patch \
    file://add-missing-files-to-gitignore.patch \
    file://blktap3-vhd-icbinn-support.patch \
    file://Revert-CP-9798-Update-cgroups-path.patch \
    file://fix-encryption.patch \
    file://gcc9-compilation.patch \
    file://openssl-1.1.x.patch \
    file://blktap3-handle-EINTR-in-tap-ctl-IPC.patch \
"

SRCREV_blktap3 = "a7832564b4d7e540d2d5a85e2556f571b7f9d89b"
SRCREV_xen = "${XEN_SRCREV}"
SRCREV_FORMAT = "xen_blktap3"
LIC_FILES_CHKSUM[blktap3]="file://LICENSE;md5=a9e8593dad23434929bc6218a17b5634"

# blktap3 is built using the --enable-blktap2 (yes, blktap2) compile option
PACKAGECONFIG[blktap3] = "--enable-blktap2,--disable-blktap2,libicbinn,"
PACKAGECONFIG_append = " blktap3"

PACKAGES =+ " \
    ${PN}-blktap3 \
    ${PN}-blktap3-dev \
    ${PN}-blktap3-cpumond \
    ${PN}-tapback \
"

INITSCRIPT_PACKAGES =+ "${PN}-tapback "
INITSCRIPT_NAME_${PN}-tapback = "tapback"
INITSCRIPT_PARAMS_${PN}-tapback = "defaults 61 39"

TARGET_CPPFLAGS += "-DTAP_CTL_NO_DEFAULT_CGROUP_SLICE -DOPEN_XT"

do_configure_append() {
    # blktap3 uses autotools but since we're splicing it into a subtree
    # of the Xen tree which has its own autotools material, we need to
    # perform a second round of autotools configuration here.
    # See autotools.bbclass for reference for this.
    bbnote "Creating configure script for blktap3"
    cd "${B}/tools/blktap3"
    # libtoolize:require_aux_dir looks for a file called install-sh
    touch "${B}/tools/blktap3/install-sh"
    touch "${B}/tools/blktap3/EXTRAVERSION"
    ACLOCAL="aclocal --system-acdir=${ACLOCALDIR}/"
    acpaths=
    for i in `find ${B}/tools/blktap3 -ignore_readdir_race -maxdepth 2 -name \*.m4|grep -v 'aclocal.m4'| \
        grep -v 'acinclude.m4' | sed -e 's,\(.*/\).*$,\1,'|sort -u`; do
        acpaths="$acpaths -I $i"
    done
    acpaths="$acpaths ${ACLOCALEXTRAPATH}"
    AUTOV=`automake --version | sed -e '1{s/.* //;s/\.[0-9]\+$//};q'`
    if [ -d ${STAGING_DATADIR_NATIVE}/aclocal-$AUTOV ]; then
        ACLOCAL="$ACLOCAL --automake-acdir=${STAGING_DATADIR_NATIVE}/aclocal-$AUTOV"
    fi
    bbnote Executing "ACLOCAL=\"$ACLOCAL\" autoreconf -Wcross --verbose --install --force ${EXTRA_AUTORECONF} $acpaths"
    ACLOCAL="$ACLOCAL" autoreconf -Wcross --verbose --install --force ${EXTRA_AUTORECONF} $acpaths \
        || die "autoreconf execution failed."

    # A note on libexecdir:
    # meta-virtualization's Xen recipe redefines ${libexecdir} to ${libdir}
    # to resolve some OE/Yocto vs. Xen build system handling of paths.
    # Override that here in order to retain the prior install path used
    # by the separate blktap3 recipe.

    bbnote "Configuring blktap3"
    if ! ./configure ${CONFIGUREOPTS} \
        --libexecdir=${libdir}exec \
        CC="${CC}" \
        LD="${LD}" \
        CFLAGS="${CFLAGS} -I${S}/tools/include \
                          -I${S}/tools/libs/toollog/include \
                          -I${S}/tools/libxc/include \
                          -I${S}/tools/xenstore/include \
                          " \
        LDFLAGS="${LDFLAGS} -L${B}/tools/libxc \
                            -L${B}/tools/libs/toollog \
                            -L${B}/tools/xenstore \
                            -Wl,-rpath-link=${B}/tools/libxc \
                            -Wl,-rpath-link=${B}/tools/libs/call \
                            -Wl,-rpath-link=${B}/tools/libs/evtchn \
                            -Wl,-rpath-link=${B}/tools/libs/foreignmemory \
                            -Wl,-rpath-link=${B}/tools/libs/devicemodel \
                            -Wl,-rpath-link=${B}/tools/libs/gnttab \
                            -Wl,-rpath-link=${B}/tools/libs/toolcore \
                            -Wl,-rpath-link=${B}/tools/libs/toollog \
                            -Wl,-rpath-link=${B}/tools/xenstore \
                            "
    then
        bbnote "The following config.log files may provide further information."
        bbnote `find ${B}/tools/blktap3 -ignore_readdir_race -type f -name config.log`
        bbfatal_log "configure failed"
    fi
}

do_compile_append() {
    oe_runmake CXX=/bin/false -C tools subdir-all-blktap3
}

do_install_append() {
    oe_runmake -C tools/blktap3 DESTDIR="${D}" install
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        rm -rf ${D}/usr/lib/systemd
    fi
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/tapback.initscript \
                    ${D}${INIT_D_DIR}/tapback
}

FILES_${PN}-libvhdio = "${libdir}/libvhd.so.*"
FILES_${PN}-libvhdio-dev = "${libdir}/libvhdio.so"

FILES_${PN}-blktap3 = " \
    ${sysconfdir}/logrotate.d/blktap \
    ${libdir}/libblockcrypto.so.* \
    ${libdir}/libblktapctl.so.* \
    ${libdir}/libvhdio-*.so \
    ${libdir}exec/tapdisk \
    ${sbindir}/cbt-util \
    ${sbindir}/vhdpartx \
    ${sbindir}/td-util \
    ${sbindir}/td-rated \
    ${sbindir}/tap-ctl \
    ${sbindir}/part-util \
    ${sbindir}/lvm-util \
    ${bindir}/vhd-util \
    ${bindir}/vhd-update \
    ${bindir}/vhd-index \
"

FILES_${PN}-blktap3-cpumond = " \
    ${bindir}/cpumond \
"

FILES_${PN}-doc += " \
  /etc/xensource/bugtool/tapdisk-logs.xml \
  /etc/xensource/bugtool/tapdisk-logs/description.xml \
"

FILES_${PN}-blktap3-dev += " \
    ${includedir}/blktap/[a-z]* \
    ${includedir}/vhd/[a-z]* \
    ${libdir}/libblktapctl.so \
    ${libdir}/libvhdio.so \
    ${libdir}/libblockcrypto.so \
"
FILES_${PN}-tapback += " \
    ${bindir}/tapback \
    ${INIT_D_DIR}/tapback \
"

RDEPENDS_${PN}-blktap3 += "glibc-gconv-utf-16"
