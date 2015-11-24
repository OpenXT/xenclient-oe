do_rm_work() {
        :
}

python do_checkout() {
    from bb import note,build
    import re

    # The following like will list the whole bb environment, very useful!
    # all = bb.data.keys(d)
    # for x in all:
    #     note("%s = %s" % (x, bb.data.getVar(x, d, True)))

    src = bb.data.getVar("SRC_URI", d, True)
    branches = re.findall('branch=[^ \t\n\r\f\v,;]+', src)
    if len(branches) == 1 :
        branch = branches[0].split('=')[1]
        command = 'git checkout ' + branch
        wd = bb.data.getVar('WORKDIR', d, True)
        note('Switching the git clone to %s. You\'re welcome.' % branch)
        note('  Directory: %s/git' % wd)
        note('  Command: %s' % command)
        os.chdir(wd + "/git")
        bb.process.run(command, shell=True)
        #runfetchcmd(command, d)
}

addtask checkout after do_unpack before do_patch
