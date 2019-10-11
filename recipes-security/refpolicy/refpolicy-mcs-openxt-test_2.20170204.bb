SUMMARY = "MCS variant of the SElinux reference policy, extended for OpenXT \
test environment."
DESCRIPTION = "Derivative of the SE Linux reference policy with MCS support \
(Multi Category Security) including changes and extensions for OpenXT \
operation and test suite."

POLICY_TYPE = "mcs"

# repolicy_%.inc from meta-selinux assigns SRC_URI.
require recipes-security/refpolicy/refpolicy_${PV}.inc

SRC_URI += " \
    file://policy/modules-openxt-test.conf \
    file://policy/modules/contrib/bats.fc \
    file://policy/modules/contrib/bats.if \
    file://policy/modules/contrib/bats.te \
    file://patches/bats-interfaces.patch \
"

require refpolicy-mcs-openxt-${PV}.inc
