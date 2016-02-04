PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

EXTRA_OECONF += "--enable-static_link --enable-udev_rules --enable-udev_sync --with-udevdir=/etc/udev/rules.d"
DEPENDS += "udev"
SRC_URI += " \
    file://lvm2-fix-static-compilation.patch \
    file://add-selinux-link-flags.patch\
    file://lvm-conf-cache-dir \
    file://0001-Replace-CPPFunction-with-rl_completion_func_t.patch \
"

PACKAGES =+ "${PN}-static"
FILES_${PN}-static = "/sbin/lvm"
FILES_${PN} += " \
    /config${sysconfdir}/lvm \
    ${sysconfdir}/default/volatiles/97_lvm \
"

do_install_append() {
    install -d ${D}${base_sbindir}
    mv ${D}${sbindir}/lvm.static ${D}${base_sbindir}/lvm
    mv ${D}${sbindir}/dmsetup.static ${D}${base_sbindir}/dmsetup
    (cd ${D} && patch -p1 < ${WORKDIR}/lvm-conf-cache-dir)

    mkdir -p ${D}${sysconfdir}/default/volatiles
    echo "d root root 0755 ${localstatedir}/cache/lvm none" > ${D}${sysconfdir}/default/volatiles/97_lvm
    ln -s ../..${localstatedir}/cache/lvm ${D}${sysconfdir}/lvm/cache

    mkdir -p ${D}/config${sysconfdir}/lvm
    mkdir -p ${D}/config${sysconfdir}/lvm/archive
    mkdir -p ${D}/config${sysconfdir}/lvm/backup
    ln -s ../../config${sysconfdir}/lvm/archive ${D}${sysconfdir}/lvm/archive
    ln -s ../../config${sysconfdir}/lvm/backup  ${D}${sysconfdir}/lvm/backup
}

