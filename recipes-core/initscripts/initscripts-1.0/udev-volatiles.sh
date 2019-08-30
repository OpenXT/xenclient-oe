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

for dir in \
    /run/lock \
    /var/volatile/tmp
do
    mkdir -p "${dir}"
    if ! restore -R "${dir}" ; then
        failure "${RESTORECON} -R ${dir} failed."
        exit 1
    fi
done

success "Udev volatiles ready."
