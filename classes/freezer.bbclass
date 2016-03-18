FREEZER_PATH = "${WORKDIR}/freezer"
FREEZER_URI_FILE = "${FREEZER_PATH}/frozen-uris.ini"
FREEZER_LOCALS_FILE = "${FREEZER_PATH}/frozen-locals.ini"
#BBPATH =. "${FREEZER_PATH}:"
FREEZER_LOCK = "${TMPDIR}/freezer.lock"

# how much time (in s) wait after remote repository interaction
REPO_ACTION_DELAY ?= "1.0"


def run_ls_remote(url, tag, d):
    from bb.fetch import runfetchcmd
    # using --heads --tags does not display remote branches
    cmd = "git ls-remote --heads --tags '%s' '%s'" % (url, tag)
    output = runfetchcmd(cmd, d)
    lines = output.split("\n")
    if len(lines) != 2 or lines[1] != '':
        raise Exception("command %s returned unexpected result: '%s', terminating" % (cmd, output))
    commit, refstr = lines[0].split(None, 1)
    return (commit, refstr)

def get_remote_repo_hash(url, tag, d):
    commit, refstr = run_ls_remote(url, tag, d)
    if refstr.startswith("refs/tags"): # get commit hash
        tag = tag + "^{}"
        commit, refstr = run_ls_remote(url, tag, d)
    return commit

def rewrite_uri(uri, d):
    import re
    xc_repo_prefix = d.getVar("OPENXT_GIT_MIRROR", True)
    if not uri.startswith(xc_repo_prefix):
        return uri
    (type_, host, path, user, pswd, parm) = bb.decodeurl(uri)
    if not parm.has_key("tag"):
        return uri
    tag = parm["tag"]
    if re.match(r'^[0-9a-f]{40}$', tag): # tag is already a hash:
        return uri 
    repourl = "%s://%s%s" % (type_, host, path)
    newtag = get_remote_repo_hash(repourl, tag, d)
    parm["tag"] = newtag
    return bb.encodeurl((type_, host, path, user, pswd, parm))

def save_dict(filename, mdict):
    import ConfigParser
    parser = ConfigParser.SafeConfigParser()
    for key in mdict:
        parser.set(None , key, mdict[key])
    with open(filename + ".tmp", "w") as fh:
        parser.write(fh)
    os.rename(filename + ".tmp", filename)  

def read_dict(filename):
    import ConfigParser
    ret = {}
    parser = ConfigParser.SafeConfigParser()
    if not parser.read(filename):
        return None
    for k,v in parser.items(ConfigParser.DEFAULTSECT):
        ret[k] = v
    return ret
        

def rewrite_uris(d):
    repo_action_delay = d.getVar("REPO_ACTION_DELAY", True)
    rewriten_uris = []
    src_uri = (d.getVar("SRC_URI", True) or "").split()
    changed = False
    for uri in src_uri:
        rewriten_uri = rewrite_uri(uri, d)
        if rewriten_uri != uri:
            changed = True
            time.sleep(float(repo_action_delay))
        rewriten_uris.append(rewriten_uri)
    freezer_file = d.getVar("FREEZER_URI_FILE", True)
    rewriten_uris_str = "\n".join(rewriten_uris)
    if changed:
        save_dict(freezer_file, {"src_uri": rewriten_uris_str})

def hash_local_url(url, d):
    import glob 
    import hashlib
    import os.path
    result = []
    fetcher = bb.fetch2.Fetch([url], d)
    localpath = fetcher.localpath(url)
    files = glob.glob(localpath)
    for f in files:
        if os.path.isdir(f):
            bb.warn("%s is a directory, not hashing" % f)
            h = "DIRECTORY"
        else:
            with open(f) as fh:
                h = hashlib.md5(fh.read()).hexdigest()
                bb.debug(1, "hash of %s is %s" % (f, h))
        result.append("%s:%s" % (f, h))
    return result

def hash_locals(d):
    freezer_locals_file = d.getVar("FREEZER_LOCALS_FILE", True)
    src_uri = (d.getVar("SRC_URI", True) or "").split()
    hashes = []
    for url in src_uri:
        if url.startswith("file://"):
            bb.debug(1, "hasing local url %s" % url)
            hashes.extend(hash_local_url(url, d))
    if hashes:
        save_dict(freezer_locals_file, {"local_hashes": "\n".join(hashes)})


python do_freeze_uris() {
    freezer_path = d.getVar("FREEZER_PATH", True)
    bb.utils.mkdirhier(freezer_path)
    rewrite_uris(d)
    hash_locals(d)
}

do_freeze_uris[nostamp] = "1"
do_freeze_uris[dirs] = "${WORKDIR}"
addtask freeze_uris

include frozen-uris.inc

do_freeze_uris[lockfiles] += "${FREEZER_LOCK}"

LOCAL_HASHES = ""

python() {
    freezer_file = d.getVar("FREEZER_URI_FILE", True)
    mydict = read_dict(freezer_file)
    if mydict:
        bb.debug(1, "freezer: dict found overwriting SRC_URI with: %s" % mydict["src_uri"])
        d.setVar("SRC_URI", mydict["src_uri"])
    freezer_locals_file = d.getVar("FREEZER_LOCALS_FILE", True)
    mydict = read_dict(freezer_locals_file)
    if mydict:
        bb.debug(1, "freezer: dict found overwriting LOCAL_HASHES with: %s" % mydict["local_hashes"])
        d.setVar("LOCAL_HASHES", mydict["local_hashes"])
}

do_fetch[vardeps] += "LOCAL_HASHES"


do_freezeall() {
    :
}
do_freezeall[recrdeptask] = "do_freeze_uris"
do_freezall[dirs] = "${WORKDIR}"
do_freezall[nostamp] = "1"
addtask do_freezeall after do_freeze_uris 

