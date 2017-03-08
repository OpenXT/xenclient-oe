require recipes-devtools/ghc/ghc-xcprog.inc

DESCRIPTION = "XenClient Update Manager"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libv4v xenclient-rpcgen-native xenclient-idl xen ghc-native ghc-hsyslog ghc-network ghc-monadprompt libxch-rpc ghc-http ghc-xenstore libxchxenstore libxchutils ghc-parsec ghc-deepseq ghc-text ghc-mtl ghc-json ghc-regex-posix ghc-hinotify ghc-lifted-base ghc-monad-control  ghc-transformers-base ghc-monad-loops"
RDEPENDS_${PN} += "glibc-gconv-utf-32 ghc-runtime"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
SRC_URI += "file://updatemgr.initscript"

S = "${WORKDIR}/git/updatemgr"

inherit xenclient update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "updatemgr"
INITSCRIPT_PARAMS_${PN} = "start 80 5 . stop 01 0 1 6 ."

do_configure_append() {
	# generate rpc stubs
	mkdir -p ${S}/Rpc/Autogen
	# Server objects
	xc-rpcgen --haskell -s -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/updatemgr.xml
	# Client objects
	xc-rpcgen --haskell -c -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/db.xml
	xc-rpcgen --haskell -c -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr.xml
	xc-rpcgen --haskell -c -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_vm.xml
	xc-rpcgen --haskell -c -o ${S}/Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/xenmgr_host.xml
}

do_install() {
	install -m 0755 -d ${D}/usr
	install -m 0755 -d ${D}/usr/bin
	runhaskell Setup.hs copy --destdir=${D}

	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/updatemgr.initscript ${D}${sysconfdir}/init.d/updatemgr
}

