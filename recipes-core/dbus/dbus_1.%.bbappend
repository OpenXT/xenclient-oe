# LibSELinux does not extend nativesdk.
DEPENDS_append_class-native += " \
    libselinux \
"
# DBus will not link with libv4v in the native case.
# The v4v kernel headers cannot be expected in the native environment since
# libv4v depends on Xen, and the hypervisor headers are not separated from the
# main recipe.
DEPENDS_append_class-target += " \
    libselinux \
    libv4v \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://0001-Make-the-default-DBus-reply-timeout-configurable.patch \
    file://add-domid-authentication.patch \
    file://v4v.patch \
    file://fix-segfault-bus_connection_disconnected.patch \
"

do_install_append() {
    # Disable EXTERNAL authentification scheme on the system-wide bus.
    # This is "controversial" (to say the least).
    # It is currently required for scripts and programs in service-vms that
    # poke at dom0 based services listening on DBus (XenMgr & Co).
    # This relies entirely on:
    # - A trusted dom0 (with DBus not listening wildly)
    # - A sane XSM policy
    # - A sane set of viptables (V4V)
    # - A trusted rpc-proxy implementation (daemon listening on v4v to proxy
    #   DBus requests)
    # - A sane white-list defined for rpc-proxy.
    # (Since DBus 1.9?) /etc/dbus-1/system.conf is deprecated, use
    # /usr/share/dbus-1/system.conf instead.
    sed -i -e 's|<auth>EXTERNAL</auth>|<!--<auth>EXTERNAL</auth>-->|' ${D}${datadir}/dbus-1/system.conf

    # Switch the system-bus policy to "default-allow" for sending method calls
    # and owning bus names.
    # Not great. The policy should be restricted to known existing services
    # (which requires an auth mecanism in place anyway).
    sed -i -e 's|<deny own="\*"/>|<allow own="*"/>|' ${D}${datadir}/dbus-1/system.conf
    sed -i -e 's|<deny send_type="method_call"/>|<allow send_type="method_call"/>|' ${D}${datadir}/dbus-1/system.conf

    # Inscrease the amount of pending replies per connections. The UI is
    # dispatching messages in parallel.
    sed -i -e '/<\/busconfig>/ i\  <limit name="max_replies_per_connection">2000</limit>' ${D}${datadir}/dbus-1/system.conf
    # Double number of allowed dbus connections per user (default is 256).
    # The UI and Toolstack are demanding.
    sed -i -e '/<\/busconfig>/ i\  <limit name="max_connections_per_user">512</limit>' ${D}${datadir}/dbus-1/system.conf
}
