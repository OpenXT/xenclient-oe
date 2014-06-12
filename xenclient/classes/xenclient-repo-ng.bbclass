# classs to handle XC Git repositories (and replace old repo class)

def is_xc_repo(url, d):
    xc_repo_prefix = bb.data.getVar("OPENXT_GIT_MIRROR", d, True)
    return url.startswith(xc_repo_prefix)

def filter_out_xc_repos(d):
    """Removes all occurences of XC Git repositories from SRC_URI variable"""
    uris = (bb.data.getVar("SRC_URI", d, True) or "").split()
    filtered_uris = [ uri for uri in uris if not is_xc_repo(uri, d) ]
    bb.data.setVar("SRC_URI", " ".join(filtered_uris), d)

def get_xc_repos(d):
    uris = (bb.data.getVar("SRC_URI", d, True) or "").split()
    return [ uri for uri in uris if is_xc_repo(uri, d) ]

# filter out XC Git repos in prefuncs, so that XC Git repos are not handled by OE
python do_fetch_prefunc() {
    filter_out_xc_repos(d)
}


python do_unpack_prefunc() {
    filter_out_xc_repos(d)
}

# our own version of unpack which is compatible with old repo class 
python do_unpack_xc_repos() {
    from bb.fetch import runfetchcmd
    xc_repos = get_xc_repos(d)
    for repo in xc_repos:
        workdir = bb.data.getVar("WORKDIR", d, True)
        (type, host, path, user, pswd, parm) = bb.decodeurl(bb.data.expand(repo, d))
        if path.endswith("-pq") or path.endswith("-pq.git"):
            destdir = 'patchqueue'
        else:
            destdir = 'git'
        if parm.has_key("tag"):
            tag = parm['tag']
        else:
            tag = 'HEAD'
        os.chdir(workdir)
        if os.path.exists(destdir):
            bb.warn("Git destination directory '%s' already exist - not cloning '%s'" % (destdir, repo))
            continue
        bareurl = "%s://%s%s" % (type, host, path)
        runfetchcmd("git clone -n '%s' '%s'" % (bareurl, destdir), d)
        os.chdir(destdir)
        runfetchcmd("git checkout '%s'" % tag, d)
        runfetchcmd("git symbolic-ref HEAD >/dev/null 2>/dev/null || git branch '%s'" % tag, d) # not on a branch? create new one and make everyone happy!
}

addtask unpack_xc_repos after do_fetch before do_unpack

do_fetch[prefuncs] += "do_fetch_prefunc"
do_unpack[prefuncs] += "do_unpack_prefunc"

# workaround creating ${S} by bitbake
do_unpack_prefunc[dirs] = "${WORKDIR}"
do_fetch_prefunc[dirs] = "${WORKDIR}"
do_unpack_xc_repos[dirs] = "${WORKDIR}"
