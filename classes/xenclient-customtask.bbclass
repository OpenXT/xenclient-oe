# This class contains convenience tasks for working with XenClient

# makeclean:
# Run "make clean" and remove the do_compile stamp if it exists

__do_xcmakeclean() {
    oe_runmake clean
}

FORCE_REBUILD_TASK_WHITELIST = "do_fetch do_unpack do_unpack_xc_repos do_patch do_apply_patchqueue do_populate_lic do_configure"

python do_makeclean() {
    from bb import note
    note("Running make clean")
    try:
        bb.build.exec_func("__do_xcmakeclean", d)
    except bb.build.FuncFailed:
        pass
}
python do_force_rebuild() {
    from bb import note,build
    from os import unlink, listdir
    from glob import glob

    # clean stamps...
    task_whitelist = d.getVar("FORCE_REBUILD_TASK_WHITELIST", True).split()
    stampglob = bb.data.expand('${STAMP}.*', d)
    for stamp in glob(stampglob):
        if stamp.find(".sigdata.") != -1:
            continue
        task = stamp.rsplit(".", 1)[-1]
        if task in task_whitelist:
            continue
        note("Removing compilation stamp %s" % stamp)
        try:
            unlink(stamp)
        except OSError:
            pass
    # ...and sstate
    sstate_clean_cachefiles(d)
}

addtask do_force_rebuild
do_force_rebuild[depends] = ""
do_force_rebuild[nostamp] = "1"

addtask makeclean
do_makeclean[depends] = "${PN}:do_force_rebuild"
do_makeclean[nostamp] = "1"

do_protos() {
    oe_runmake protos
}

addtask protos
do_protos[depends] = ""
do_protos[nostamp] = "1"
