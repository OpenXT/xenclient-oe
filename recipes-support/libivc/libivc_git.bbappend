# CMake 4 dropped compatibility with cmake_minimum_required() < 3.5.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"

LICENSE = "GPL-2.0-only"
INSANE_SKIP:${PN} += "buildpaths"
INSANE_SKIP:${PN}-dbg += "buildpaths"
