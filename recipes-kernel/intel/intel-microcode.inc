DESCRIPTION = "Intel microcode"

# Actually it is covere under the Intel Software License Agreement here:
# http://www.intel.com/design/network/drivers/sla_ec.htm
# but the package does not contain the license so we set this to CLOSED to make bb happy. 
LICENSE = "CLOSED"

SRC_URI = " http://downloadmirror.intel.com/26156/eng/microcode-${PV}.tgz "

do_install() {
        install -d ${D}/etc
        install -m 0644 ${WORKDIR}/microcode.dat ${D}/etc/microcode.dat
}
