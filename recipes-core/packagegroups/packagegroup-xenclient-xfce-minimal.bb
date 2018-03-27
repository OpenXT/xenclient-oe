DESCRIPTION = "All packages required for a very minimal installation of XFCE"
SECTION = "x11/wm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"
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
