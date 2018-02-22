DEPENDS_${PN} += " \
    libselinux \
    libv4v \
    xen \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://0001-Make-the-default-DBus-reply-timeout-configurable.patch \
    file://add-domid-authentication.patch \
    file://v4v.patch \
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
}
