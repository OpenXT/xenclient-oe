# Use OpenXT defeconfig.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/${BPN}:"

SRC_URI += "file://busybox-CVE-2017-16544.patch"

# We should use sh wrappers instead of links so the commands could get correct
# security labels
python create_sh_wrapper_reset_alternative_vars_openxt () {
    # We need to load the full set of busybox provides from the /etc/busybox.links
    # Use this to see the update-alternatives with the right information

    dvar = d.getVar('D')
    pn = d.getVar('PN')

    def create_sh_alternative_vars(links, target, mode):
        import shutil
        # Create sh wrapper template
        fwp = open("busybox_wrapper", 'w')
        fwp.write("#!%s" % (target))
        os.fchmod(fwp.fileno(), mode)
        fwp.close()
        # Install the sh wrappers and alternatives reset to link to them
        wpdir = os.path.join(d.getVar('libdir'), pn)
        wpdir_dest = '%s%s' % (dvar, wpdir)
        if not os.path.exists(wpdir_dest):
            os.makedirs(wpdir_dest)
        f = open('%s%s' % (dvar, links), 'r')
        for alt_link_name in f:
            alt_link_name = alt_link_name.strip()
            alt_name = os.path.basename(alt_link_name)
            # Copy script wrapper to wp_path
            alt_wppath = '%s%s' % (wpdir, alt_link_name)
            alt_wppath_dest = '%s%s' % (wpdir_dest, alt_link_name) 
            alt_wpdir_dest = os.path.dirname(alt_wppath_dest)
            if not os.path.exists(alt_wpdir_dest):
                os.makedirs(alt_wpdir_dest)
            shutil.copy2("busybox_wrapper", alt_wppath_dest)
            # Re-set alternatives
            if os.path.exists(alt_wppath_dest):
                d.setVarFlag('ALTERNATIVE_TARGET', alt_name, alt_wppath)
        f.close()

        os.remove("busybox_wrapper")
        return

    if os.path.exists('%s/etc/busybox.links' % (dvar)):
        create_sh_alternative_vars("/etc/busybox.links", "/bin/busybox", 0o0755)
    else:
        create_sh_alternative_vars("/etc/busybox.links.nosuid", "/bin/busybox.nosuid", 0o0755)
        create_sh_alternative_vars("/etc/busybox.links.suid", "/bin/busybox.suid", 0o4755)
}

# Add to PACKAGEBUILDPKGD so it could override the alternatives, which are set in
# do_package_prepend() section of busybox_*.bb.
PACKAGEBUILDPKGD_remove = "create_sh_wrapper_reset_alternative_vars "
PACKAGEBUILDPKGD_prepend = "create_sh_wrapper_reset_alternative_vars_openxt "
