PR .= ".1"

# We want to enable tui so filter out the disable from upstream
EXTRA_OECONF := "${@oe_filter_out('--disable-tui', '${EXTRA_OECONF}', d)}"
EXTRA_OECONF += "--enable-tui"


