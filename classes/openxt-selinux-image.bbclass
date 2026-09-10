inherit openxt-image

DEPENDS += "policycoreutils-native attr-native"
IMAGE_INSTALL:append = " \
    libselinux-bin \
    policycoreutils-loadpolicy \
    policycoreutils-newrole \
    policycoreutils-runinit \
    policycoreutils-semodule \
    policycoreutils-sestatus \
    policycoreutils-setfiles \
    refpolicy-mcs \
"

inherit selinux-image
