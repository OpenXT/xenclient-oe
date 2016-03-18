INSTALLED_PACKAGES_VERSIONS_FILE = "${WORKDIR}/installed_packages_versions.log"
DO_NOT_VALIDATE_PACKAGE_VERSION_FOR += "ghc-native-runtime"

def create_package_version_dict(d):
    import os, os.path
    versions = {}
    # arch are sorted from less specific to more specific
    archs = bb.data.expand("${PACKAGE_ARCHS}", d).split(" ")
    pkgdata = bb.data.expand('${TMPDIR}/pkgdata/', d)
    # as in packagedata.bbclass
    targetdir = bb.data.expand('${TARGET_VENDOR}-${TARGET_OS}/runtime', d)
    for arch in archs:
        datadir = pkgdata + arch + targetdir
        if not os.path.isdir(datadir):
            continue
        for subpackage in os.listdir(datadir):
            datafile = "%s/%s" % (datadir, subpackage)
            if datafile.endswith(".packaged") or not os.path.isfile(datafile):
                continue
            subpackagedata = read_pkgdatafile(datafile)
            if not subpackagedata:
                bb.note("No subpackage data in %s" % dataile)
                continue
            version = "%(PV)s-%(PKGR)s" % subpackagedata
            # actual package name may differ from the name specified in PACKAGES
            package_name = subpackagedata["PKG_%s" % subpackage]
            versions[package_name] = version
    return versions

python do_validate_package_versions() {
    error = False
    error_descriptions = []
    pkg_versions = create_package_version_dict(d)
    installed_packages_versions_file = d.getVar("INSTALLED_PACKAGES_VERSIONS_FILE", True)
    do_not_validate_package_version_for = d.getVar("DO_NOT_VALIDATE_PACKAGE_VERSION_FOR", True).split()
    with open(installed_packages_versions_file) as f:
        for line in f.xreadlines():
            (pkgname, installed_version) = line.split()
            if pkgname in do_not_validate_package_version_for:
                continue
            if not pkg_versions.has_key(pkgname):
                error = True
                error_descriptions.append("no data for %s" % pkgname)
                continue
            installed_version = installed_version.split(":")[-1] # get rid of package epoch
            if pkg_versions[pkgname] != installed_version:
                error = True
                error_descriptions.append("installed %s version of %s, but compiled/staged version is %s" % (installed_version, pkgname, pkg_versions[pkgname]))
    if error:
        bb.error("Fatal problems encountered during package versions verification:");
        bb.error("\n".join(error_descriptions))

}


# A little bit hackish, but packages metadata may be removed when running ROOTFS_POSTPROCESS_COMMAND
# and I don't want to overwriterootfs_ipk.bbclass
rootfs_ipk_log_check_append() {
    echo "$lf_txt" | grep "Installing .* to root\.\.\." \
    | sed -e "s/Installing \(.[^ ]*\) (\([^)]*\)) to root\.\.\./\1 \2/"\
    | sort | uniq >  "${INSTALLED_PACKAGES_VERSIONS_FILE}"
}


addtask validate_package_versions after do_rootfs before do_build
