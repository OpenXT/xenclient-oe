# Creates a source package containing the patched source code for a recipe.
#
# The source code is packaged under the directory (or directories) specified
# in the LICENSE variable for the recipe. For example, if the recipe has:
#
#     LICENSE = "GPL"
#     SRC_URI = "http://example.com/foo-1.0.tgz \
#                file://foo-fix.patch;patch=1"
#
# then a source package foo-src will be created containing:
#
#     GPL/foo/foo_1.0-r0.xc1.tar.bz2
#
# Packages with LICENSE set to 'Proprietary' or 'Unknown' are removed
# during image creation
#
# Based on the 'src_distribute' and 'package_ipk' classes from OpenEmbedded.

SRC_PACKAGE_ARCH ?= "all"
SRC_PACKAGE_DIR ?= "package-src"
SRC_PACKAGE_UNCLEAN ?= ".unclean"
SRC_PACKAGE_EXCLUDES ?= ".pc .pq patches"

SRC_PACKAGE_ROOT = "${WORKDIR}/${SRC_PACKAGE_DIR}"
SRC_PACKAGE_UNCLEAN_FILE = "${B}/${SRC_PACKAGE_UNCLEAN}"
PACKAGES_DYNAMIC += "${PN}-src"

SRCPKGWRITEDIRIPK = "${WORKDIR}/deploy-src-ipks"

# src_package_archive task: creates archive of source directory under
# 'package-src' directory.

addtask src_package_archive after do_patch before do_configure
do_configure[vardepsexclude] += "do_src_package_archive"
do_src_package_archive[cleandirs] = "${SRC_PACKAGE_ROOT}"

python() {
    # If do_apply_patchqueue exists, make sure it runs before
    # do_src_package_archive.
    if d.getVarFlag('do_apply_patchqueue', 'task', d):
        deps = d.getVarFlag('do_src_package_archive', 'deps', d)
        deps.append('do_apply_patchqueue')
        d.setVarFlag('do_src_package_archive', 'deps', deps, d)

    # run before do_populate_lic
    if d.getVarFlag('do_populate_lic', 'task', d):
        deps = d.getVarFlag('do_populate_lic', 'deps', d)
        deps.append('do_src_package_archive')
        d.setVarFlag('do_populate_lic', 'deps', deps, d)
}

python do_src_package_archive() {
    root = d.getVar('SRC_PACKAGE_ROOT', d, 1)
    unclean_file = d.getVar('SRC_PACKAGE_UNCLEAN_FILE', d, 1)
    excludes = d.getVar('SRC_PACKAGE_EXCLUDES', d, 1)
    licenses = d.getVar('LICENSE', d, 1) or "unknown"
    pn = d.getVar('PN', d, 1)
    pv = d.getVar('PV', d, 1)
    pr = d.getVar('PR', d, 1)
    s = d.getVar('S', d, 1)
    workdir = d.getVar('WORKDIR', d, 1)

    # This is to detect the case where this task is being rerun after another
    # task has written build output to the source directory. Raise an error
    # rather than including the build output in the source archive.
    if os.path.exists(unclean_file):
        raise bb.build.FuncFailed("xenclient-src-package-real: Cannot create "
                                  "source archive for package '%s' because "
                                  "source directory contains build output. "
                                  "To fix this problem, rebuild the package "
                                  "from clean. Note: this will wipe out the "
                                  "source directory, so take a copy of your "
                                  "changes first!" % (pn))

    bb.utils.mkdirhier(root)
    if 'work-shared' in s: 
        bb.note("xenclient-src-package-real: Skipping package '%s' as source "
                "directory '%s' is located in work-shared" % (pn, s))
        return

    if not os.path.exists(s):
        bb.note("xenclient-src-package-real: Skipping package '%s' as source "
                "directory '%s' does not exist" % (pn, s))
        return

    entries = set()

    for license in licenses.split():
        for entry in license.split("|"):
            entries.add(entry)

    for entry in entries:
        archivedir = os.path.abspath(os.path.join(root, entry, pn))
        bb.utils.mkdirhier(archivedir)

        # For consistency with ipkg-build, don't include PE in the archive name.
        name = "%s_%s-%s" % (pn, pv, pr)
        archive = os.path.join(archivedir, name + ".tar.bz2")

        (srcdirname, srcbasename) = os.path.split(os.path.abspath(s))

        command = "cd '%s' && tar cjf '%s' '%s' --owner 0 --group 0 " \
                  "--transform 's/^[^\/]*/%s/' --exclude-vcs" % \
                  (srcdirname, archive, srcbasename, name)

        excludelist = excludes.split()

        allow_tar_exit_with_one = False 

        # Workaround for packages which set S to WORKDIR.
        if os.path.abspath(s) == os.path.abspath(workdir):
            excludelist.append(d.getVar('SRC_PACKAGE_DIR', d, 1))
            excludelist.append('temp')
            # allow tar exit with 1 exit code (files changed) as stuff in temp directory could change
            # let's hope that any other thing hasn't
            allow_tar_exit_with_one = True

        for exclude in excludelist:
            command += " --exclude '%s'" % (exclude)

        result = os.system(command)
        if result != 0:
            if os.WEXITSTATUS(result) == 1 and allow_tar_exit_with_one:
                bb.warn("xenclient-src-package-real: some files changed during package creation, ignoring")
            else:
                raise bb.build.FuncFailed("xenclient-src-package-real: Failed to "
                                          "create source archive: %s" % (command))
}

# src_package_unclean task: marks source directory as unclean; see comment
# in src_package_archive task.

addtask src_package_unclean after do_src_package_archive before do_configure
do_configure[vardepsexclude] += "do_src_package_unclean"

python do_src_package_unclean() {
    unclean_file = d.getVar('SRC_PACKAGE_UNCLEAN_FILE', d, 1)
    pn = d.getVar('PN', d, 1)
    s = d.getVar('S', d, 1)

    if not os.path.exists(s):
        bb.note("xenclient-src-package-real: Skipping package '%s' as source "
                "directory '%s' does not exist" % (pn, s))
        return

    open(unclean_file, 'a').close()
}

SSTATETASKS += "do_src_package_write"
do_src_package_write[sstate-name] = "deploy-source-ipk"
do_src_package_write[sstate-inputdirs] = "${SRCPKGWRITEDIRIPK}"
do_src_package_write[sstate-outputdirs] = "${DEPLOY_DIR_IPK}"

python do_src_package_write_setscene () {
    sstate_setscene(d)
}
addtask do_src_package_write_setscene

# src_package_write task: creates src package from 'package-src' directory.

python do_src_package_write() {
    import re

    root = d.getVar('SRC_PACKAGE_ROOT', d, 1)

    outdir = d.getVar('SRCPKGWRITEDIRIPK', d, 1)
    if not outdir:
        raise bb.build.FuncFailed("xenclient-src-package-real: "
                                  "DEPLOY_DIR_IPK not defined")

    lf = bb.utils.lockfile(root + ".lock")

    basedir = os.path.join(os.path.dirname(root))
    arch = d.getVar('SRC_PACKAGE_ARCH', d, 1)
    pkgoutdir = os.path.join(outdir, arch)

    bb.utils.mkdirhier(pkgoutdir)
    os.chdir(root)

    controldir = os.path.join(root, "CONTROL")
    bb.utils.mkdirhier(controldir)
    try:
        ctrlfile = file(os.path.join(controldir, "control"), 'w')
    except OSError:
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-src-package-real: Unable to "
                                  "open control file for writing")

    ctrlfile.write("Package: %s-src\n" % d.getVar('PN', d, 1))

    fields = []
    pe = d.getVar('PE', d, 1)
    if pe and int(pe) > 0:
        fields.append(["Version: %s:%s-%s\n", ['PE', 'PKGV', 'PKGR']])
    else:
        fields.append(["Version: %s-%s\n", ['PKGV', 'PKGR']])
    fields.append(["Description: %s\n", ['DESCRIPTION']])
    fields.append(["Section: %s\n", ['SECTION']])
    fields.append(["Priority: %s\n", ['PRIORITY']])
    fields.append(["Maintainer: %s\n", ['MAINTAINER']])
    fields.append(["License: %s\n", ['LICENSE']])
    fields.append(["Architecture: %s\n", ['SRC_PACKAGE_ARCH']])
    fields.append(["OE: %s\n", ['PN']])
    fields.append(["Homepage: %s\n", ['HOMEPAGE']])

    def pullData(l, d):
        l2 = []
        for i in l:
            l2.append(d.getVar(i, d, 1))
        return l2

    try:
        for (c, fs) in fields:
            for f in fs:
                if d.getVar(f, d) is None:
                    raise KeyError(f)
            ctrlfile.write(c % tuple(pullData(fs, d)))
    except KeyError:
        import sys
        (type, value, traceback) = sys.exc_info()
        ctrlfile.close()
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-src-package-real: Missing "
                                  "field for ipk generation: %s" % value)

    src_uri = d.getVar('SRC_URI', d, 1) or d.getVar('FILE', True)
    src_uri = re.sub("\s+", " ", src_uri)
    ctrlfile.write("Source: %s\n" % " ".join(src_uri.split()))
    ctrlfile.close()

    os.chdir(basedir)
    ret = os.system("PATH='%s' %s %s %s" %
                    (d.getVar('PATH', d, 1),
                     d.getVar('OPKGBUILDCMD', d, 1),
                     d.getVar('SRC_PACKAGE_DIR', d, 1),
                     pkgoutdir))
    if ret != 0:
        bb.utils.unlockfile(lf)
        raise bb.build.FuncFailed("xenclient-src-package-real: ipkg-build "
                                  "execution failed")

    bb.utils.prunedir(controldir)
    bb.utils.unlockfile(lf)
}

addtask src_package_write before do_package_write after do_src_package_archive
do_src_package_write[depends] += "opkg-utils-native:do_populate_sysroot"
do_src_package_write[depends] += "virtual/fakeroot-native:do_populate_sysroot"
