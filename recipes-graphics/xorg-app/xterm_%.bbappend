
do_configure_prepend() {
	echo >> ${S}/XTerm.ad
	echo '*metaSendsEscape: true' >> ${S}/XTerm.ad
	echo '*eightBitInput: false' >> ${S}/XTerm.ad

	echo >> ${S}/XTerm-col.ad
	echo '*metaSendsEscape: true' >> ${S}/XTerm-col.ad
	echo '*eightBitInput: false' >> ${S}/XTerm-col.ad
}
