require recipes/ghc/ghc-pkg.inc

DESCRIPTION = "Chat with N-Fuse servers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "ghc-native ghc-hxt ghc-curl ghc-text ghc-hsyslog libxchdb libxch-rpc"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

# Ocaml stuff is built with the native compiler with "-m32".

SRC_URI = "${OPENXT_GIT_MIRROR}/xctools.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/fusechat"

inherit xenclient

do_configure_append() {
    # generate rpc stubs
    mkdir -p Rpc/Autogen
    # Server objects
    xc-rpcgen --haskell -s -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/fusechat.xml
}

do_install() {
	install -m 0755 -d ${D}/usr
	install -m 0755 -d ${D}/usr/bin
	runhaskell Setup.hs copy --destdir=${D}
}

