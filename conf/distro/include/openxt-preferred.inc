
PREFERRED_PROVIDER_virtual/kernel ?= "linux-openxt"
PREFERRED_VERSION_linux-openxt ?= "4.9%"

PREFERRED_VERSION_linux-libc-headers ?= "${@"${PREFERRED_VERSION_linux-openxt}".rstrip('%')}"
PREFERRED_VERSION_linux-libc-headers-nativesdk ?= "${@"${PREFERRED_VERSION_linux-openxt}".rstrip('%')}"

PREFERRED_PROVIDER_openssl = "openssl"
PREFERRED_PROVIDER_openssl-native = "openssl-native"
PREFERRED_PROVIDER_libcrypto = "libcrypto"
PREFERRED_PROVIDER_libssl = "libssl"
PREFERRED_PROVIDER_openssl-conf = "openssl-conf"
PREFERRED_PROVIDER_nativesdk-openssl = "nativesdk-openssl"
PREFERRED_PROVIDER_nativesdk-openssl-conf = "nativesdk-openssl-conf"

PREFERRED_PROVIDER_console-tools = "console-tools"
PREFERRED_PROVIDER_virtual/libx11 = "libx11"

PREFERRED_VERSION_dojosdk-native ?= "1.7.2"
# ${MACHINE}.conf is overriden by ${DISTRO}.conf
# xenmgr_data and sync-wui (uivm and syncui machines) apparently depends on
# different versions, so preserve that.
PREFERRED_VERSION_dojosdk-native_xenclient-syncui ?= "1.8.1"

# Force specific package versions
PREFERRED_VERSION_grub = "2.00+AUTOINC%"
PREFERRED_VERSION_networkmanager = "0.9.2.0"
PREFERRED_VERSION_network-manager-applet = "0.9.2.0"
PREFERRED_VERSION_wpa-supplicant = "2.6"

PREFERRED_VERSION_midori = "0.1.8"
PREFERRED_VERSION_webkit-gtk = "1.8.3"

PREFERRED_VERSION_fontsproto = "2.1.3"
PREFERRED_VERSION_gnome-keyring = "2.32.1"

PREFERRED_VERSION_refpolicy-mcs = "2.20141203"

# Select our preferred providers
PREFERRED_PROVIDER_sysvinit-inittab = "sysvinit"
PREFERRED_PROVIDER_virtual/java-initial = "cacao-initial"
PREFERRED_PROVIDER_virtual/java-initial-native = "cacao-initial-native"
PREFERRED_PROVIDER_virtual/java-native = "jamvm-native"
PREFERRED_PROVIDER_virtual/javac-native = "ecj-bootstrap-native"
PREFERRED_PROVIDER_jpeg-native = "jpeg-native"

# Allow build of multiple packages containing xenstore daemons
MULTI_PROVIDER_WHITELIST += "xen-xenstored"
# Select a preferred XenStored daemon implementation.
PREFERRED_PROVIDER_xen-xenstored = "xen"
