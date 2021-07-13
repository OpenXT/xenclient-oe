#
# udev-volatiles.sh
# Populate early tmpf that may be required by udev.
#

. /etc/init.d/functions-selinux

if [ -f /etc/default/udev-volatiles ]; then
    . /etc/default/udev-volatiles
fi

echo -n "Populate volatiles for udev..."

for dir in \
    /run/lock \
    /var/volatile/tmp
do
    mkdir -p "${dir}"
    if ! restore -R "${dir}" ; then
        echo "restore -R ${dir} failed."
        exit 1
    fi
done

echo "OK"
