python() {
    src_uris = (d.getVar("SRC_URI", True) or "").split()
    xc_git = d.getVar("OPENXT_GIT_MIRROR", True)
    recipe = os.path.basename(d.getVar("FILE", True) or "")
    for src_uri in src_uris:
        if src_uri.startswith(xc_git):
    	    bb.note("Recipe %s uses XC Git - disabling pstaging" % recipe)
            d.setVar('PSTAGING_DISABLED','1')
}
