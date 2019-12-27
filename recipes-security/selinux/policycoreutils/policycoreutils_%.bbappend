FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://0001-policycoreutils-semodule-Enable-CIL-logging.patch;striplevel=2 \
"

AUDITH="`test -f ${STAGING_INCDIR}/libaudit.h >/dev/null 2>&1 && echo y `"
PAMH="`test -f ${STAGING_INCDIR}/security/pam_appl.h >/dev/null 2>&1 && echo y `"
