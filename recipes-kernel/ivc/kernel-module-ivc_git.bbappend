FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

inherit module-signing

SRC_URI += "file://0001-linux-6.1-bind-interdomain-without-xenbus.patch"

LICENSE = "GPL-2.0-only"
