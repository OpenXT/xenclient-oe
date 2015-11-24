DESCRIPTION = "All packages required for a very minimal installation of XFCE"
SECTION = "x11/wm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"
PR = "r2"

inherit packagegroup

RDEPENDS_${PN} = " \
    xfwm4 \
    xfwm4-theme-default \
    xfce4-session \     
    xfconf \
    \
    gtk-xfce-engine \
    xfce4-appfinder \
    xfce4-settings \
    \
    xfce4-notifyd \
"
