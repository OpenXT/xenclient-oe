PR .= ".1"

# We only want libkmod2 really...
#EXTRA_OECONF := "${@oe_filter_out('--enable-tools', '${EXTRA_OECONF}', d)}"
EXTRA_OECONF := "${@oe_filter_out('--enable-logging', '${EXTRA_OECONF}', d)}"
EXTRA_OECONF += " --disable-logging --without-bashcompletiondir"
