DESCRIPTION = "All packages required for a very minimal installation of XFCE"
SECTION = "x11/wm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
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
