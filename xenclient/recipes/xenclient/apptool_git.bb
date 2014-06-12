require recipes/ghc/ghc-xcprog.inc

DESCRIPTION = "XenClient appliance tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libxenmgr-core libxchutils libxchdb libxch-rpc xenclient-rpcgen-native xenclient-idl ghc-json ghc-hsyslog ghc-regex-posix ghc-hxt udbus-intro"
RDEPENDS_${PN} += "glibc-gconv-utf-32 cdrtools xenclient-idl-dev xsd-validate"

SRC_URI = "${OPENXT_GIT_MIRROR}/manager.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/apptool"

INSANE_SKIP_${PN} = "dev-deps"

FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/2001/XMLSchema.dtd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/2001/datatypes.dtd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/TR/2002/REC-xmlenc-core-20021210/xenc-schema.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/TR/2012/WD-xmlenc-core1-20120105/xenc-schema-11.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/www.w3.org/2001/xml.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/common.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_BootSourceSetting.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_StorageAllocationSettingData.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_VirtualSystemSettingData.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_BootConfigSetting.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_EthernetPortAllocationSettingData.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/dsp8023_2.0.0c.xsd"
FILES_${PN} += "/usr/share/apptool-1.0/schema/xciovf.xsd"

inherit xenclient

do_configure_append() {
    mkdir -p Rpc/Autogen
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_vm.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_host.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/vm_nic.xml
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/vm_disk.xml
}

do_install() {
    runhaskell Setup.hs copy --destdir=${D}
}

