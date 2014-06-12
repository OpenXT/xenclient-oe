# To be used in an image recipe. Creates an empty package which depends on the
# source package of every recipe which needs to be built in order to build the
# image recipe.
#
# For example, for the xenclient-dom0-image recipe, creates a package
# xenclient-dom0-image-sources which depends on busybox-src, openssh-src and so
# on.
#
# The dependencies are deduced from the task run queue. This relies on bitbake
# reading all the available recipes, not just the image recipe, so for example,
# the xenclient-dom0-image recipe must be built using:
#
#     ./bb xenclient-dom0-image
#
# rather than:
#
#     ./bb -b xenclient/recipes/images/xenclient-dom0-image.bb

IMAGE_SRC_PACKAGE_ARCH ?= "all"
IMAGE_SRC_PACKAGE_DIR ?= "package-sources"

IMAGE_SRC_PACKAGE_ROOT = "${WORKDIR}/${IMAGE_SRC_PACKAGE_DIR}"

PACKAGES_DYNAMIC += "${PN}-sources"

INSTALLED_PACKAGES_LIST = "${WORKDIR}/installed_packages_list.txt"

save_installed_packages_list() {
    list_installed_packages > "${INSTALLED_PACKAGES_LIST}"
}

ROOTFS_POSTPROCESS_COMMAND =+ "save_installed_packages_list ; "

# Create proper mapping from runtime package names to recipes as OE functions does not handle pkg rename hooks
def prepare_proper_package_map(d):
    import oe.packagedata
    proper_map = {}
    oe_map = oe.packagedata.pkgmap(d)
    for pkg in oe_map:
        pkgdict = oe.packagedata.read_subpkgdata_dict(pkg, d)
        realname = pkgdict.get("PKG", pkg)
        proper_map[realname] = oe_map[pkg]
    return proper_map



python do_image_sources_package_write() {
    import glob, re

    pn = bb.data.getVar('PN', d, 1)

    depends = set()
    package_list_file = bb.data.getVar("INSTALLED_PACKAGES_LIST", d, True)
    with open(package_list_file) as fh:
        lines = fh.readlines()
    pkgmap = prepare_proper_package_map(d)
    recipes = set()
    for line in lines:
        pkg = line.strip()
        try:
            recipe = pkgmap[pkg]
            depends.add(recipe + '-src')
        except KeyError:
            bb.warn("xenclient-image-src-package-real: No mapping for package %s source package for this pkg may not be shipped" % pkg)

    # Create package which depends on source package for each of these recipes.

    root = bb.data.getVar('IMAGE_SRC_PACKAGE_ROOT', d, 1)
    bb.mkdirhier(root)

    outdir = bb.data.getVar('DEPLOY_DIR_IPK', d, 1)
    if not outdir:
        raise bb.build.FuncFailed("xenclient-image-src-package-real: " \
                                  "DEPLOY_DIR_IPK not defined")

    lf = bb.utils.lockfile(root + ".lock")

    basedir = os.path.join(os.path.dirname(root))
    arch = bb.data.getVar('IMAGE_SRC_PACKAGE_ARCH', d, 1)
    pkgoutdir = os.path.join(outdir, arch)

    bb.mkdirhier(pkgoutdir)
    os.chdir(root)

    controldir = os.path.join(root, "CONTROL")
    bb.mkdirhier(controldir)
    try:
        ctrlfile = file(os.path.join(controldir, "control"), 'w')
    except OSError:
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-image-src-package-real: Unable " \
                                  "to open control file for writing")

    ctrlfile.write("Package: %s-sources\n" % pn)

    fields = []
    pe = bb.data.getVar('PE', d, 1)
    if pe and int(pe) > 0:
        fields.append(["Version: %s:%s-%s\n", ['PE', 'PKGV', 'PKGR']])
    else:
        fields.append(["Version: %s-%s\n", ['PKGV', 'PKGR']])
    fields.append(["Description: %s\n", ['DESCRIPTION']])
    fields.append(["Section: %s\n", ['SECTION']])
    fields.append(["Priority: %s\n", ['PRIORITY']])
    fields.append(["Maintainer: %s\n", ['MAINTAINER']])
    fields.append(["License: %s\n", ['LICENSE']])
    fields.append(["Architecture: %s\n", ['IMAGE_SRC_PACKAGE_ARCH']])
    fields.append(["OE: %s\n", ['PN']])
    fields.append(["Homepage: %s\n", ['HOMEPAGE']])

    def pullData(l, d):
        l2 = []
        for i in l:
            l2.append(bb.data.getVar(i, d, 1))
        return l2

    try:
        for (c, fs) in fields:
            for f in fs:
                if bb.data.getVar(f, d) is None:
                    raise KeyError(f)
            ctrlfile.write(c % tuple(pullData(fs, d)))
    except KeyError:
        import sys
        (type, value, traceback) = sys.exc_info()
        ctrlfile.close()
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-image-src-package-real: " \
                                  "Missing field for ipk generation: %s" %
                                  value)

    if depends:
        ctrlfile.write("Depends: %s\n" % ", ".join(sorted(depends)))

    src_uri = bb.data.getVar('SRC_URI', d, 1) or d.getVar('FILE', True)
    src_uri = re.sub("\s+", " ", src_uri)
    ctrlfile.write("Source: %s\n" % " ".join(src_uri.split()))
    ctrlfile.close()

    os.chdir(basedir)
    ret = os.system("PATH=\"%s\" %s %s %s" %
                    (bb.data.getVar('PATH', d, 1),
                     bb.data.getVar('OPKGBUILDCMD', d, 1),
                     bb.data.getVar('IMAGE_SRC_PACKAGE_DIR', d, 1),
                     pkgoutdir))
    if ret != 0:
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-image-src-package-real: " \
                                  "ipkg-build execution failed")

    bb.utils.prunedir(controldir)
    bb.utils.unlockfile(lf)
}

addtask image_sources_package_write after do_rootfs before do_build
