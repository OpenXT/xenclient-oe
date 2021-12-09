#!/bin/sh

exec 1>/dev/console 2>&1

add() {
	disk=${MDEV#xvd}
	disk_num=$( printf "%d" \'"$disk" )

	# 97 is decimal 0x61 for ascii 'a'
	fdset=$(( 8000 + disk_num - 97 ))

	qmp_hello=$(printf '{"execute":"qmp_capabilities","id":1}\r\n')
	qmp_addfd=$(printf '{"execute":"add-fd", "arguments": { "fdset-id": %d }, "id":42 }\r\n' "$fdset" )

	echo "add $MDEV to fdset $fdset"

	fd=/dev/fd/9
	if [ "$( cat /sys/class/block/"$MDEV"/ro )" -eq 1 ] ; then
		exec 9</dev/"$MDEV"
	else
		exec 9<>/dev/"$MDEV"
	fi

	add-fd /tmp/qemu-cdrom.qmp $fd "$qmp_hello$qmp_addfd" "42"
	echo add-fd returned $?

	exec 9<&-
}

case $ACTION in
	add)
		add
		;;
esac
