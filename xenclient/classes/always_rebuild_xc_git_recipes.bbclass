python() {
    src_uris = (bb.data.getVar("SRC_URI", d, True) or "").split()
    xc_git = bb.data.getVar("OPENXT_GIT_MIRROR", d, True)
    recipe = os.path.basename(bb.data.getVar("FILE", d, True) or "")
    for src_uri in src_uris:
        if src_uri.startswith(xc_git):
    	    bb.note("Recipe %s uses XC Git - disabling pstaging" % recipe)
            bb.data.setVar('PSTAGING_DISABLED','1', d)
}
