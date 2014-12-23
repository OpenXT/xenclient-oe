PRINC = "1"

# Disable parallel make to work around the bug described in this commit:
#
# http://cgit.openembedded.org/openembedded-core/commit/?id=80e4833782edc5fbda2a7f5d003a854f127137ec
#
# which occasionally causes build errors like this:
#
# tic: error while loading shared libraries: /build/buildbot2/XT_installer/build
# /scratch/build/oe/tmp-eglibc/work/i686-linux/ncurses-native-5.9-r8.1/ncurses-5
# .9/narrowc/lib/libtinfo.so.5: file too short
# ? tic could not build /build/buildbot2/XT_installer/build/scratch/build/oe/tmp
# -eglibc/work/i686-linux/ncurses-native-5.9-r8.1/image/build/buildbot2/XT_insta# ller/build/scratch/build/oe/tmp-eglibc/sysroots/i686-linux/usr/share/terminfo
# make[1]: *** [install.data] Error 
#
# This workaround can be removed once we upgrade to an OpenEmbedded version
# which includes the above commit.
PARALLEL_MAKE = ""
