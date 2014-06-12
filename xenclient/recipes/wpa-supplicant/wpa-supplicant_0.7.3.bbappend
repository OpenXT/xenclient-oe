FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "openssl"
 
PRINC = "1"

SRC_URI += " \
            file://dbus-scan-results.patch;patch=1 \
"
 
do_configure () {
    install -m 0755 ${S}/defconfig .config
    echo "CONFIG_CTRL_IFACE_DBUS=y" >> .config
    echo "CONFIG_CTRL_IFACE_DBUS_NEW=y" >> .config 
    echo "CONFIG_TLS=openssl" >> .config
    echo "CONFIG_DRIVER_NL80211=y" >> .config
    echo "CONFIG_LIBNL20=y" >> .config
    echo "CONFIG_BGSCAN=y" >> .config
    echo "CONFIG_BGSCAN_SIMPLE=y" >> .config
}
