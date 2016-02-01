# WORKAROUND: Avoid glibc abort() from presumed invalid locale binaries:
# cross-localdef seems to generate a LC_LOCATE that fails to be parsed by the
# glibc deployed on the target.

# set "1" to use cross-localedef for locale generation
# set "0" for qemu emulation of native localedef for locale generation
LOCALE_GENERATION_WITH_CROSS-LOCALEDEF = "0"
