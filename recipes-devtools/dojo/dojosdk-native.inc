DEPENDS = "icedtea7-native"

inherit native

SRC_URI = "http://download.dojotoolkit.org/release-${PV}/dojo-release-${PV}-src.tar.gz"

S = "${WORKDIR}"

do_configure () {
    :
}

do_compile () {
    :
}

do_install () {

    mkdir -p ${D}/${libdir}/dojo-release-${PV}-src
    cp -rd dojo-release-${PV}-src/. ${D}/${libdir}/dojo-release-${PV}-src

    sed -i -e "s#^buildScriptsPath =.*#buildScriptsPath = \"@DOJOHOME@/util/buildscripts/\";#" "${D}/${libdir}/dojo-release-${PV}-src/util/buildscripts/build.js"

    mkdir -p ${D}/${bindir}

    cat << EOF >  ${D}/${bindir}/dojo-${PV}
#!/bin/sh
@DOJOHOME@/util/buildscripts/build.sh "\$@"
EOF

    chmod 0755  ${D}/${bindir}/dojo-${PV}


}

sysroot_fix_dojo_build_path() {

    sed -i -e "s#@DOJOHOME@#${libdir}/dojo-release-${PV}-src#g" "${SYSROOT_DESTDIR}/${libdir}/dojo-release-${PV}-src/util/buildscripts/build.js"
    sed -i -e "s#@DOJOHOME@#${libdir}/dojo-release-${PV}-src#g" "${SYSROOT_DESTDIR}/${bindir}/dojo-${PV}"

}

SYSROOT_PREPROCESS_FUNCS += "sysroot_fix_dojo_build_path"


