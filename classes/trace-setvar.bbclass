# bb class helping to trace where particular variable is set or changed
# hackish, for debug purposes only 
python () {
    import bb
    def interposeSetVar(name, value, d):
        path = d.getVar("FILE", True)
        if not path:
            path = "Unknown"
        file = path.split("/")[-1]
        runqueue = d.getVar("__RUNQUEUE_DO_NOT_USE_EXTERNALLY", False)
        if not runqueue:
            task = "Unknown"
        else:
            task = runqueue.cooker.configuration.cmd
        bb.note("SETVAR %s %s: setVar('%s', '%s', d)" % (file, task, name, str(value)[:50]))
        orig_bb_data_setVar(name, value, d)

    if d.setVar != interposeSetVar:
        orig_bb_data_setVar = d.setVar
        d.setVar = interposeSetVar
}
