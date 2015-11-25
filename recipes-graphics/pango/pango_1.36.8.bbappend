PR .= ".1"
inherit qemu
DEPENDS += "qemu-native"

# hack: overwirte prologue and place target installation here
postinst_prologue_forcevariable() {
    mkdir -p $D${sysconfdir}/pango
    if [ -n "$D" ];then
       PSEUDO_RELOADED=YES ${@qemu_target_binary(d)} -E LD_LIBRARY_PATH=$D/lib:$D/usr/lib -E LD_PRELOAD= -L $D $D${bindir}/pango-querymodules > $D/etc/pango/pango.modules
       exit 0
    fi
}

