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
mkdir -p /run/lock
if ! restore -R /run/lock ; then
    failure "${RESTORECON} -R /run/lock failed."
    exit 1
fi

success "Udev volatiles ready."
