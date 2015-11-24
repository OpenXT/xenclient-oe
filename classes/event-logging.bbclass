EVENT_LOG_FILE = "${TMPDIR}/event.log"

def __log_entry(entry, logfile):
    lf = bb.utils.lockfile(logfile + '.lock')
    fh = open(logfile, "a")
    fh.write("%s\n" % entry)
    fh.close
    bb.utils.unlockfile(lf)

def __event_to_string(obj):
    ignored_attrs = ('data', 'cfg', 'type', 'stampPrefix')
    ret = {}
    for attrname in dir(obj):
        attr = getattr(obj, attrname)
        if not attrname.startswith("_") and not callable(attr) and not attrname in ignored_attrs:
            ret[attrname] = attr
    return str(ret)

python __log_event_eh () {
    import time
    from bb.msg import MsgBase
    data = e.data
    classname = e.__class__.__name__
    if isinstance(e, MsgBase):
        return
    if hasattr(e, "task"):
        task = e.task
    else:
        task = "-"
    recipe = d.getVar('FILE', True).split("/")[-1]
    logfile = bb.data.expand('${EVENT_LOG_FILE}', data)
    stamp = time.time()
    timeformat = "%H:%M:%S"
    timestr = time.strftime(timeformat, time.localtime(stamp)) 
    logentry = "%s [%f] %s %s %s %s" % (timestr, stamp, recipe, task, classname, __event_to_string(e))
    __log_entry(logentry, logfile)
}
addhandler __log_event_eh
