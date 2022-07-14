DESCRIPTION = "XenClient Network Slave"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxchutils \
    hkg-hsyslog \
    libxch-rpc \
    libxchxenstore \
    networkmanager \
    hkg-regex-posix \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-network \
"
RDEPENDS_${PN} += " \
    bridge-utils \
    carrier-detect \
    glibc-gconv-utf-32 \
    iproute2 \
    iptables \
    networkmanager \
"

require network.inc

S = "${WORKDIR}/git/nws"

inherit haskell update-rc.d xc-rpcgen

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "network-slave"
INITSCRIPT_PARAMS_${PN} = "defaults 29 15"

FILES_${PN} += "/usr/bin/network-slave"

do_configure_append() {
    # generate rpc stubs
    mkdir -p ${S}/Rpc/Autogen

    # Server objects
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/network_slave.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/network.xml
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --server -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/network_nm.xml

    # NetworkManager objects (stored in the network-manager specific directory)
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.xml -n NmManager
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.Device.xml -n NmDevice
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.Device.Wired.xml -n NmDeviceEthernet
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.Device.Wireless.xml -n NmDeviceWifi
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.Device.Modem.xml -n NmDeviceModem
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.AccessPoint.xml -n NmAccessPoint
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} --client -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_NMIDLDATADIR}/org.freedesktop.NetworkManager.Connection.Active.xml -n NmActiveConnection

    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/dbus.xml
}

do_install_append() {
    install -m 0755 -d ${D}${sysconfdir}/network-daemon
    install -m 0755 ${S}/dnsmasq-template ${D}${sysconfdir}/network-daemon/dnsmasq-template
    install -m 0755 ${S}/dnsmasq-script-template ${D}${sysconfdir}/network-daemon/dnsmasq-script-template
    install -m 0755 ${S}/bridge-connection ${D}${sysconfdir}/network-daemon/bridge-connection
    install -m 0755 ${S}/slave-connection ${D}${sysconfdir}/network-daemon/slave-connection

    install -m 0755 -d ${D}${sysconfdir}/network-daemon/scripts
    install -m 0755 ${S}/nw_notify ${D}${sysconfdir}/network-daemon/scripts/nw_notify

    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/nws.initscript ${D}${sysconfdir}/init.d/network-slave
    
    install -d ${D}/${sysconfdir}/udev/rules.d
    install -D -m 0644 ${S}/nws-notify.rules ${D}/${sysconfdir}/udev/rules.d/81-nws-notify.rules

    install -d ${D}/${sysconfdir}/dnsmasq-config
}
