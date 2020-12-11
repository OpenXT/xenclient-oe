# Since we have multilib enabled for xen, regular allarch doesn't take affect
# and the packages use the default tune.  Setting PACKAGE_ARCH forces allarch
# like we want.
PACKAGE_ARCH = "all"

inherit allarch
