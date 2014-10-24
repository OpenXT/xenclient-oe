require recipes/ghc/ghc-xcprog.inc

DESCRIPTION = "XenClient RPC proxy"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS += "libxchutils libxchwebsocket libxchv4v libxchxenstore xenclient-rpcgen-native xenclient-idl ghc-dbus-core ghc-json ghc-hsyslog ghc-network-bytestring libxch-rpc ghc-transformers ghc-parsec ghc-deepseq ghc-text ghc-mtl ghc-network ghc-monad-loops ghc-lifted-base ghc-monad-control libxchdb ghc-errors"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

PV = "0+git${SRCPV}"

SRCREV = "71bfc70ec028c744e84914dc7ffcdccdd499c8c8"
SRC_URI = "git://github.com/openxt/manager.git;protocol=https"
SRC_URI += "file://rpc-proxy.rules \
            file://rpc-proxy.initscript \
"

S = "${WORKDIR}/git/rpc-proxy"

inherit xenclient update-rc.d

INITSCRIPT_NAME = "rpc-proxy"
INITSCRIPT_PARAMS = "defaults 30"

# ToDo: move xc-rpcgen into compile?

do_configure_append() {
	# generate rpc stubs
	mkdir -p Rpc/Autogen
	xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/rpc_proxy.xml
	xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/dbus.xml
}

do_install() {
	runhaskell Setup.hs copy --destdir=${D}
	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}/etc/init.d
	install -m 0644 ${WORKDIR}/rpc-proxy.rules ${D}/etc/rpc-proxy.rules
	install -m 0755 ${WORKDIR}/rpc-proxy.initscript ${D}${sysconfdir}/init.d/rpc-proxy
}
