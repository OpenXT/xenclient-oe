IS_DKMS = "1"
DEB_SECTION ?= "kernel"
DEB_EXTA_PKG_TOOLS += "dkms"

UBUNTU_DISTROS="lucid maverik natty oneiric precise"

inherit xenclient-deb


do_configure() {
	:
}

do_compile() {
	KERNVERSION=\$(
		find /lib/modules -type d  -maxdepth 1 -mindepth 1  2>/dev/null |\
			 while read fpath; do [[ -e "\${fpath}/build" ]] && echo "\${fpath}"; done | \
			 head -n 1 | xargs basename 2>/dev/null)
	if ! [[ -z "\${KERNVERSION}" ]]; then
		pushd "${S}"
			[[ -d "./oe-test-dkms" ]] || mkdir "oe-test-dkms"
			rsync -a --exclude oe-test-dkms . oe-test-dkms
			pushd "oe-test-dkms"
				sed -i -re "s/^\s*KVERSION\s*:=\s*.+\\\$/KVERSION:=\${KERNVERSION}/g" Makefile
				echo "Trying to compile the dkms module for \${KERNVERSION}"
				KVERSION="\${KERNVERSION}" make
			popd
			rm -rf "oe-test-dkms"
		popd
	fi
}

do_install() {
	:
}

debinst_prep() {
	[[ -z "${1}" ]] && die "deb_install: no path"
	cat <<EOFD >> "${1}"
	
	DD="${D}/usr/share/${DEB_NAME}-${PR}"
	[[ -d "\${DD}" ]] || die "debinst_prep(): the install directory cannot be found"

	mod_name=\$(echo "${DEB_NAME}" | sed -n -re 's/^(.+)\-dkms\$/\1/p')
	[[ -z "\${mod_name}" ]] && die "debinst_prep(): the DEB_NAME whould be of the form NAME-dkms"
	src_name="\${mod_name}-\${version}"
	#src_name="\${mod_name}"
	
	DEBIAN_DIR="${D}/debian/"
	mkdir -p "\${DEBIAN_DIR}/usr/src/\${src_name}/"
	rsync -a "\${DD}/" "\${DEBIAN_DIR}/usr/src/\${src_name}/"

	## dkms.conf
	dkms_conf="\${DEBIAN_DIR}/usr/src/\${src_name}/dkms.conf"
	if [[ -e "\${dkms_conf}" ]]; then
		sed -i -re "s/^\s*PACKAGE_NAME\s*=.*\\\$/PACKAGE_NAME=\"\${mod_name}\"/g" "\${dkms_conf}"
		sed -i -re "s/^\s*PACKAGE_VERSION\s*=.*\\\$/PACKAGE_VERSION=\"\${version}\"/g" "\${dkms_conf}"
	else
	cat <<EOFDD > "\${dkms_conf}"
PACKAGE_NAME="\${mod_name}"
PACKAGE_VERSION="\${version}"
CLEAN="make clean"
MAKE[0]="make all KVERSION=\\\$kernelver"
BUILT_MODULE_NAME[0]="\${mod_name}"
DEST_MODULE_LOCATION[0]="/kernel/driver/xen"
AUTOINSTALL="yes"
EOFDD
	fi
	
	## debian files
	mkdir -p "\${DEBIAN_DIR}/DEBIAN/"

	## (needs to be removed)
	## in_list "${UBUNTU_DISTROS}" "\${deb_suite}" && extra_deps=", linux-virtual, linux-headers-virtual"

	extra_deps=", linux-headers-generic | linux-headers, make, sed (>> 3.0), linux-libc-dev"
	cat <<EOFDD > "\${DEBIAN_DIR}/DEBIAN/control"
Package: ${DEB_NAME}
Source: \${src_name}
Version: \${version}
Architecture: all
Maintainer:  ${DEB_PKG_MAINTAINER}
Depends: dkms (>= 1.95) \${extra_deps}
Section: ${DEB_SECTION}
Priority: extra
Description: ${DEB_DESC}
 This is part of XenClient Tools for linux guests. ${DEB_DESC_EXT}
EOFDD


	## debian scripts
	cat <<\EOFDD | sed -re "s|\{mod_name\}|\${mod_name}|g" | sed -re "s|\{version\}|\${version}|g" > "\${DEBIAN_DIR}/DEBIAN/postinst" && chmod 755 "\${DEBIAN_DIR}/DEBIAN/postinst"
#!/bin/sh
set -e

DKMS_NAME="{mod_name}"
DKMS_PACKAGE_NAME="\${DKMS_NAME}-dkms"
DKMS_VERSION={version}

postinst_found=0

case "\$1" in
        configure)
                for DKMS_POSTINST in /usr/lib/dkms/common.postinst /usr/share/\$DKMS_PACKAGE_NAME/postinst; do
                        if [ -f \$DKMS_POSTINST ]; then
                                \$DKMS_POSTINST \$DKMS_NAME \$DKMS_VERSION /usr/share/\$DKMS_PACKAGE_NAME "" \$2
                                postinst_found=1
                                break
                        fi
                done
                if [ "\$postinst_found" -eq 0 ]; then
                        echo "ERROR: DKMS version is too old and \$DKMS_PACKAGE_NAME was not"
                        echo "built with legacy DKMS support."
                        echo "You must either rebuild \$DKMS_PACKAGE_NAME with legacy postinst"
                        echo "support or upgrade DKMS to a more current version."
                        exit 1
                fi
        ;;
esac
# End automatically added section

EOFDD


	cat <<\EOFDD | sed -re "s|\{mod_name\}|\${mod_name}|g" | sed -re "s|\{version\}|\${version}|g" > "\${DEBIAN_DIR}/DEBIAN/prerm" && chmod 755  "\${DEBIAN_DIR}/DEBIAN/prerm"
#!/bin/sh

NAME="{mod_name}"
VERSION="{version}"

set -e

case "\$1" in
    remove|upgrade|deconfigure)
      if [  "\`dkms status -m \$NAME\`" ]; then
         dkms remove -m \$NAME -v \$VERSION --all
      fi
    ;;

    failed-upgrade)
    ;;

    *)
        echo "prerm called with unknown argument '\$1'" >&2
        exit 1
    ;;
esac


exit 0	
EOFDD

	   mkdir -p "\${DEBIAN_DIR}/usr/share/doc/${DEB_NAME}"
	   crt_year=$(date '+%Y')
       cat - <<EOFDD > "\${DEBIAN_DIR}/usr/share/doc/${DEB_NAME}/copyright"
Format-Specification: http://svn.debian.org/wsvn/dep/web/deps/dep5.mdwn?op=file&rev=135
Name: ${DEB_NAME}
Maintainer: ${DEB_PKG_MAINTAINER}
        
Files: *
Copyright: \${crt_year} Citrix Systems.

License: GPL-2+
 This file is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 .
 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.
 .
 On Debian systems, the complete text of the GNU General Public License
 (GPL) version 2 can be found an /usr/share/common-licenses/GPL-2".
 .
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.


EOFDD

		## some lintian overrides
		mkdir -p "\${DEBIAN_DIR}/usr/share/lintian/overrides/"
    	cat <<EOFDD > "\${DEBIAN_DIR}/usr/share/lintian/overrides/${DEB_NAME}"
${DEB_NAME}: extended-description-is-empty
${DEB_NAME}: changelog-file-missing-in-native-package
${DEB_NAME}: debian-changelog-file-missing
EOFDD
	
EOFD
	## ok
}
