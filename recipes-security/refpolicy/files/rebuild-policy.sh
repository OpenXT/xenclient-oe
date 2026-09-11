#!/bin/sh
# Retry semodule, dropping CIL-unlinkable modules.
set -e
D="$1"
POLICY_NAME="$2"
POLICY_STORE_ROOT="$3"
SETFILES="$4"
SEFCONTEXT="$5"

mkdir -p "${D}/etc/selinux"
cat > "${D}/etc/selinux/semanage.conf" <<EOF
module-store = direct
[setfiles]
path = ${SETFILES}
args = -q -c \$@ \$<
[end]
[sefcontext_compile]
path = ${SEFCONTEXT}
args = \$@
[end]

policy-version = 35
store-root = "${POLICY_STORE_ROOT}"
EOF

POL_ACTIVE_MODS="${D}${POLICY_STORE_ROOT}/${POLICY_NAME}/active/modules/100"
i=0
while [ "$i" -lt 40 ]; do
    i=$((i + 1))
    if out=$(semodule -p "${D}" -s "${POLICY_NAME}" -n -B 2>&1); then
        printf '%s\n' "$out"
        break
    fi
    printf '%s\n' "$out"
    mod=$(printf '%s\n' "$out" | sed -n 's|.*/modules/[0-9]*/\([^/]*\)/cil:.*|\1|p' | tail -1)
    if [ -z "$mod" ]; then
        echo "semodule failed without a droppable module"
        exit 1
    fi
    echo "dropping policy module $mod due to CIL resolve failure"
    rm -rf "${POL_ACTIVE_MODS}/${mod}"
done
rm -f "${D}/etc/selinux/semanage.conf"
rm -rf "${D}${POLICY_STORE_ROOT}/final"
