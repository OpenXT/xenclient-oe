require recipes/gtk-webcore/midori.inc

PR="r3"

# workaround for WAF hanging during do_configure:
export JOBS = "1"

#DEPENDS += "python-native python-docutils-native"

SRC_URI = "http://goodies.xfce.org/releases/midori/midori-${PV}.tar.bz2 \
           file://waf \
	   file://midori-kiosk.patch;patch=1 \
	   file://borderless.patch;patch=1 \
           file://midori-grab-option.patch;patch=1 \
           file://midori-prune-find-and-zoom-features.patch;patch=1 \
	   file://midori-dont-stop-loading-on-escape.patch;patch=1 \
	   file://midori-view_use_WebKitDOM.patch;patch=1 \
	   file://midori-define-have-jscore.patch;patch=1 \
	   file://midori-full-screen.patch;patch=1 \
"
SRC_URI[md5sum] = "06935203b20e9794121a2c354fc9dea5"
SRC_URI[sha256sum] = "3b0e4a2c5c3e4457ce8f44e6cbb9f14ddfb66a515017b430f4f4484893e8ce9d"

EXTRA_OECONF += " --disable-nls --disable-unique --disable-libidn --disable-sqlite --disable-addons --disable-docs --disable-userdocs --disable-apidocs"

do_configure() {
	cp -f ${WORKDIR}/waf ${S}/
	sed -i -e 's:, shell=False::g' wscript 
	./configure \
            --prefix=${prefix} \
            --bindir=${bindir} \
            --sbindir=${sbindir} \
            --libexecdir=${libexecdir} \
            --datadir=${datadir} \
            --sysconfdir=${sysconfdir} \
            --sharedstatedir=${sharedstatedir} \
            --localstatedir=${localstatedir} \
            --libdir=${libdir} \
            --includedir=${includedir} \
            --infodir=${infodir} \
            --mandir=${mandir} \
            ${EXTRA_OECONF} 
 
	sed -i /LINK_CC/d ./_build_/c4che/default.cache.py 
	echo "LINK_CC = '${CXX}'" >>  ./_build_/c4che/default.cache.py
}



