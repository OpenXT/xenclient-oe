#
# Convenience tasks for corner cases.
#

__makeclean() {
    oe_runmake clean
}
python do_makeclean() {
    workdir = d.getVar('B', True)
    makefiles = [ '/Makefile', '/GNUmakefile', '/makefile' ]
    if reduce(lambda x, y: x or os.path.exists(workdir + y), makefiles, False):
        bb.build.exec_func('__makeclean', d)
        bb.build.write_taint('do_compile' , d)
        sstate_clean_cachefiles(d)
}
addtask do_makeclean
do_makeclean[nostamp] = "1"
do_makeclean[doc] = "Run `make clean', if applicable and mark do_compile as tainted."

python do_force_rebuild() {
    bb.build.write_taint('do_compile' , d)
    sstate_clean_cachefiles(d)
}
addtask do_force_rebuild
do_force_rebuild[nostamp] = "1"
do_force_rebuild[doc] = "Mark do_compile as tainted, forcing it to be run again."

