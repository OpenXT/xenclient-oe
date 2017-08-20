# The tasks that depend on the contents of staging need to be sure
# that nobody is changing it while they are using it to prevent
# various build errors.

# This class uses a shared lock for read access during those tasks
# (from before configure to before install)

def release_tasklock (d, task):
    fd = d.getVar("_task_lock_held_%s" % task, False)
    if fd is not None:
        bb.note("Releasing lock for %s" % task);
        bb.utils.unlockfile(fd)

def acquire_sharedlock (d, task):
    bb.note("Acquiring shared lock for %s" % task);
    lockfile = bb.data.expand("${SYSROOT_LOCK}", d)
    fd = bb.utils.lockfile_shared(lockfile)
    d.setVar("_task_lock_held_%s" % task, fd)

def acquire_exclock (d, task):
    bb.note("Acquiring exclusive lock for %s" % task);
    lockfile = bb.data.expand("${SYSROOT_LOCK}", d)
    fd = bb.utils.lockfile(lockfile)
    d.setVar("_task_lock_held_%s" % task, fd)

addhandler locking_eventhandler
python locking_eventhandler () {
    from bb.build import TaskStarted, TaskSucceeded, TaskFailed
    name = getName(e)
    if e.data is None or name == "MsgNote":
        return NotHandled
    if isinstance(e, TaskStarted):
        if e.task == "do_configure" or \
           e.task == "do_compile" or \
           e.task == "do_runlibtool":
             excl_var = bb.data.expand("${EXCLUSIVE_CONFIGURE_LOCK}", e.data)
             if excl_var == "1" and e.task == "do_configure":    
               acquire_exclock(e.data, e.task)
             else:
               acquire_sharedlock(e.data, e.task)
    elif isinstance(e, TaskSucceeded) or isinstance(e, TaskFailed):
        release_tasklock(e.data, e.task)

    return NotHandled
}
