my_step() {
    local step="$1"
    local pn=`echo ${PN} | sed "s/sbuild-//"`

    for suite in ${SBUILD_SUITE}
    do
        for arch in ${SBUILD_ARCH}
        do
            case "$step" in
                install)
                        mkdir -p "${S}/debian"
                        cat <<EOF > "${S}/debian/changelog"
$pn (1.0) unstable; urgency=low

  * first version

 -- Jed <jed@citrix.com>  Sat, 15 May 2013 10:15:03 +0200
EOF

                        echo 7 > ${S}/debian/compat

                        cat <<EOF > "${S}/debian/control"
Source: $pn
Section: ${SBUILD_SECTION}
Priority: optional
Maintainer: Citrix Systems <customerservice@citrix.com>
Build-Depends: ${SBUILD_DEPENDS}

Package: $pn
Architecture: any
Depends: ${SBUILD_RDEPENDS}
Description: $pn
 ${SBUILD_DESCRIPTION}
EOF

                        touch "${S}/copyright"

                        cat <<'EOF' > "${S}/debian/rules"
#!/usr/bin/make -f
# -*- makefile -*-

comma := ,
LDFLAGS := $(filter-out -Wl$(comma)-Bsymbolic-functions,$(LDFLAGS))
LDFLAGS := $(filter-out -Wl$(comma)-z$(comma)relro,$(LDFLAGS))

binary:
	dh binary --parallel

binary-arch:
	dh binary-arch --parallel

clean:
	echo Not cleaning

configure:
EOF
                        cat "${S}/oe_do_configure.sh" >> "${S}/debian/rules"
                        cat <<'EOF' >> "${S}/debian/rules"

compile: configure
EOF
                        cat "${S}/oe_do_compile.sh" >> "${S}/debian/rules"
                        cat <<'EOF' >> "${S}/debian/rules"

install: build
EOF
                        cat "${S}/oe_do_install.sh" >> "${S}/debian/rules"
                        cat <<'EOF' >> "${S}/debian/rules"

build-stamp: configure compile
	touch build-stamp

build: build-stamp

override_dh_auto_build: build
build-arch: build
build-indep: build

override_dh_auto_install: install

override_dh_auto_test:
	echo auto test
EOF

                        chmod +x "${S}/debian/rules"

                        cd "${S}"
                        sbuild -n --arch=${SBUILD_ARCH} --dist=${SBUILD_SUITE} --purge=never --purge-deps=never

                        repo_dir=${STAGING_DIR}/debian_repos/${SBUILD_SUITE}/${SBUILD_ARCH}
                        cd "${repo_dir}/debian/"
                        for deb_file in `ls ${WORKDIR}/*.deb`; do
                            reprepro includedeb ${SBUILD_SUITE} $deb_file
                        done
                        ;;
            esac
        done
    done
}





do_compile_append() {
EOF
}

do_configure_prepend() {
cat <<EOF > "${S}/oe_do_configure.sh"
}

do_configure_append() {
EOF
}

do_compile_prepend() {
cat <<EOF > "${S}/oe_do_compile.sh"
}

do_install_prepend() {
cat <<EOF > "${S}/oe_do_install.sh"
}

do_install_append() {
EOF
        my_step "install"
}

do_build() {
	   :
}