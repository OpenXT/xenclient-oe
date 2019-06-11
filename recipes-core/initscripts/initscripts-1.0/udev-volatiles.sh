#
# udev-volatiles.sh
# Populate early tmpf that may be required by udev.
#

. /etc/default/rcS
. /etc/init.d/functions

if [ -f /etc/default/udev-volatiles ]; then
    . /etc/default/udev-volatiles
fi
# Environment.

begin "Populate volatiles for udev..."
RESTORECON=${RESTORECON:-/sbin/restorecon}
if [ ! -x "${RESTORECON}" ]; then
    failure "SELinux \`${RESTORECON}' tool is missing."
    exit 1
fi

mkdir -p /run/lock
if ! ${RESTORECON} -R /run/lock ; then
    failure "${RESTORECON} failed."
    exit 1
fi

success "Udev volatiles ready."
