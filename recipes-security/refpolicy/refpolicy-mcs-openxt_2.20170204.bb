SUMMARY = "MCS variant of the SElinux reference policy, extended for OpenXT."
DESCRIPTION = "Derivative of the SE Linux reference policy with MCS support \
(Multi Category Security) including changes and extensions for OpenXT \
operation."

POLICY_TYPE = "mcs"

require recipes-security/refpolicy/refpolicy_${PV}.inc
require refpolicy-mcs-openxt-${PV}.inc
