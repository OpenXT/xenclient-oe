# Class to get a version number based on the git-rev count
# Originally based on git_ver.bbclass

XC_AUTO_PR = "${@get_xc_auto_pr(d)}"

def get_xc_auto_pr(d):
    from subprocess import Popen, PIPE, STDOUT
    import os
    from bb import error, note, debug
    from bb.parse import mark_dependency

    oexcdir = os.path.normpath(d.getVar("OE_XENCLIENT_DIR", True))
    file = d.getVar("FILE", True)
    filedir = os.path.normpath(os.path.dirname(file))
    #note("FILE: %s FILEDIR: %s" % (file, filedir))

    isxcrecipe = False
    prefix = os.path.join(oexcdir, "xenclient")
    if prefix == os.path.commonprefix((prefix, filedir)):
        isxcrecipe = True
    prefix = os.path.join(oexcdir, "repos", "extra")
    if prefix == os.path.commonprefix((prefix, filedir)):
        isxcrecipe = True

    if not isxcrecipe:
        #note("NOT XC")
        return ""

    #note("IS XC RECIPE")

    def popen_count(cmd, **kwargs):
        #kwargs["stderr"] = STDOUT
        kwargs["bufsize"] = 1
        kwargs["stdout"] = PIPE
        try:
            #note("Popen: %s" % cmd)
            pipe = Popen(cmd, **kwargs)
        except OSError, e:
            error("Execution of %s failed: %s" % (cmd, e))
            return 0

        lines = 0
        for line in pipe.stdout.xreadlines():
            lines += 1
            #note("Read line: %s" % line)
        pipe.wait()
        if pipe.returncode != 0:
            error("Execution of %s failed: %d" % (cmd, pipe.returncode))
            return 0

        return lines

    save_cwd = os.getcwd()
    os.chdir(filedir)
    # See XC-6917 for explanation of --full-history.
    ver = popen_count(["git", "rev-list", "--full-history", "HEAD", "--", "."])
    os.chdir(save_cwd)
    #note("Setting XenClient PR to %s for %s" % (".xc"+str(ver), d.getVar("FILE", True)))
    return ".xc"+str(ver)
