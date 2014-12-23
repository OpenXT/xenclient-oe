TARGET_BUILDDEPENDS_PACKAGE_SUFFIX = "-target-builddepends"
PACKAGES += "${PN}${TARGET_BUILDDEPENDS_PACKAGE_SUFFIX}"
ALLOW_EMPTY_${PN}${TARGET_BUILDDEPENDS_PACKAGE_SUFFIX} = "1"
#PACKAGE_ARCH_${PN}${TARGET_BUILDDEPENDS_PACKAGE_SUFFIX} = "all"

python create_builddepends_package() {
	import re
	def get_var_as_list(varname):
		result = bb.data.getVar(varname, d, 1)
		if result:
			return result.split()
		else:
			return []
	def append_var_from_list(varname, list_):
		value = get_var_as_list(varname)
		value.extend(list_)
		bb.data.setVar(varname, " ".join(value), d)
	def filter_list(list_, regex_list):
		filterre = re.compile("|".join(regex_list))
		return [ i for i in list_ if not filterre.search(i) ]
	def get_pkgdata(pkgname): # hack: get info for package that may have different architecture
		archs = bb.data.expand("${PACKAGE_ARCHS}", d).split(" ")
		archs.reverse()
		for arch in archs:
			localdata = bb.data.createCopy(d)
			#bb.data.setVar('BASE_PACKAGE_ARCH', arch, localdata)
			bb.data.setVar('MULTIMACH_ARCH', arch, localdata)
			result = read_pkgdata(pkgname, localdata)
			if result:
				return result
		return None

	my_package_suffix = bb.data.getVar("TARGET_BUILDDEPENDS_PACKAGE_SUFFIX", d, 1)
	if not my_package_suffix:
		raise bb.build.FuncFailed("No suffix for build dependency package defined")
	autoremoved_builddepends = [ "-dbg$", "-locale$", "-doc$" ]
#	if bb.data.getVar("GENERATE_STATIC_TARGET_BUILDDEPENDS", d, 1) != "1": # automaticly remove -static dependencies if not disabled
#		autoremoved_builddepends.append("-static$");
#	if bb.data.getVar("GENERATE_BIN_TARGET_BUILDDEPENDS", d, 1) != "1": # automaticly remove -bin dependencies if not disabled
#		autoremoved_builddepends.append("-bin$");

	unwanted_builddepends = get_var_as_list('UNWANTED_TARGET_BUILDDEPENDS')
	additional_builddepends = get_var_as_list('ADDITIONAL_TARGET_BUILDDEPENDS') 

	builddepends = []
	depends = get_var_as_list('DEPENDS')
	for depend in depends:
		print "Dependency", depend
		if depend.find('-native') != -1 or depend.find('-cross') != -1 or depend.startswith('virtual/'):
			bb.note("Skipping depend %s when calculating build dependencies" % depend)
			continue
		pkgdata = get_pkgdata(depend)
		if not pkgdata:
			bb.note("No pkgdata for %s" % depend)
			continue
		subpackages = pkgdata['PACKAGES']
		if not subpackages:
			bb.note("No suppackages for %s" % depend)
			continue
		subpackages = subpackages.split()
		subpackages = [ s for s in subpackages if packaged(s, d) ] # depend only on existing packages
		subpackages = [ s for s in subpackages if not s.endswith(my_package_suffix) ] # do not depend on other builddepends packages 
		#bb.note("Suppackages for %s: %s" % (depend, subpackages))
		builddepends.extend(subpackages)
	builddepends = filter_list(builddepends, autoremoved_builddepends)
	builddepends = [ i for i in builddepends if i not in unwanted_builddepends ]
	builddepends.extend(additional_builddepends)
	
	pn = bb.data.getVar("PN", d, 1)
	bb.note("Target build depends for %s: %s" % (pn, " ".join(builddepends)))
	my_package_name = "%s%s" % (pn, my_package_suffix)
	#append_var_from_list("PACKAGES", [ my_package_name ])
	append_var_from_list("RDEPENDS_%s" % my_package_name, builddepends)
}

python populate_packages_prepend () {
	bb.build.exec_func('create_builddepends_package', d)
}
