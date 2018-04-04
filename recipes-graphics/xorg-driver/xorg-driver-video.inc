include xorg-driver-common.inc

DEPENDS =+ "renderproto videoproto xextproto fontsproto"

# xorg-driver-abi was introduced in later versions of the recipe.
# This should be removed when xserver & relevant gets upgraded.
INSANE_SKIP_${PN} = "xorg-driver-abi"
