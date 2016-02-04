DEB_REPO_DIR="${TMPDIR}/deb-xctools-image/"

SKIP_SSTATE_PACKAGE_CREATION = "1"

def deb_bootstrap_deps(d):
    deb_suite = d.getVar('DEB_SUITE', 1)
    deb_arch  = d.getVar('DEB_ARCH', 1)

    result=""
    for suite in deb_suite.split():
        for arch in deb_arch.split():
            result += "debootstrap-%s-%s " % (suite, arch)
    return result

PSTAGING_DISABLED = "1"

DEB_DESC_EXT ?= "${DEB_DESC}"


DEB_PKG_MAINTAINER ?= "Citrix Systems <customerservice@citrix.com>"

CHROOT_PATH="${STAGING_DIR}/debootstrap"
STAGING_DIR_DEB="${CHROOT_PATH}"

APT_LOCK="flock -x /tmp/.lck-apt-deb"
CHOWN_LCK_FILE="/tmp/.lck-chown-deb"


## this is in the chroot

DEB_EXTA_PKG_TOOLS="rsync help2man dpkg lintian"

die() {
        echo "ERROR: $1"
        exit 1
}

my_chroot() {
        root="${1}"
        arch="${2}"
        shift 2

        if [[ ${arch} == 'i386' ]]; then
                sudo chroot "${root}" linux32 $*
        elif [[ ${arch} == 'amd64' ]]; then
                sudo chroot "${root}" linux64 $*
        else
            sudo chroot $*
        fi

## I don't think it is now necessary    

##      ( 
##              set +e; 
##              sudo flock -x "${root}/${CHOWN_LCK_FILE}" chown -R $(whoami) "${root}" 
##              exit 0 
##      )

}

prepare_chroot() {
        local chroot="$1"

        (
                set +e 
                flock -x 210
                sudo cp -f /etc/resolv.conf "${chroot}/etc"
                [[ -d "${chroot}/dev" ]] || sudo mkdir -p "${chroot}/dev" 
                [[ -c "${chroot}/dev/null" ]] || sudo rm -f "${chroot}/dev/null"
                [[ -e "${chroot}/dev/null" ]] || ( sudo mknod "${chroot}/dev/null" c 1 3  && sudo chmod 666 "${chroot}/dev/null" )

                exit 0
        )  210> /tmp/.lck-oe-prepare_chroot
        return 0
}

update_chroot() {
    local chroot="$1"
    local arch="$2"

    return 0 ## not needed now

        ( set +e;  my_chroot "${chroot}" "${arch}" ${APT_LOCK} apt-get update; exit 0 )
    return 0
}

do_clean_prepend() {
    dir = d.getVar('WORKDIR', 1)
    userid = d.getVar('BUILD_UID', 1)
    os.system("sudo chown -R %s '%s'" % (userid, dir))
}
do_clean_append() {
        deb_suite = d.getVar('DEB_SUITE', 1)
        deb_arch  = d.getVar('DEB_ARCH', 1)
        chroot_path = d.getVar('CHROOT_PATH', 1)
        if chroot_path == '' or chroot_path is None or len(chroot_path) < 2:
                return
        WORKDIR = d.getVar('WORKDIR', 1)
        D = d.getVar('D', 1)
        if WORKDIR is None or D is None: return
        for suite in deb_suite.split():
                if suite == '': continue
                for arch in deb_arch.split():
                        if arch == '' : continue
                        print "I: cleaning chroot build of %s_%s" % (suite, arch)
                        os.system("sudo rm -rf '%s/%s_%s/%s'" % (chroot_path, suite, arch, WORKDIR))
                        os.system("sudo rm -rf '%s/%s_%s/%s'" % (chroot_path, suite, arch, D))
}

