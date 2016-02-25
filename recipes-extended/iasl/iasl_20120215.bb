DESCRIPTION = "This is a cross development C compiler, assembler and linker environment for the production of 8086 executables (Optionally MSDOS COM)"
HOMEPAGE = "http://www.acpica.org/"
LICENSE = "Intel-ACPI"
LIC_FILES_CHKSUM = "file://asldefine.h;endline=115;md5=d4d7cf809b8b5e03131327b3f718e8f0"
SECTION = "console/tools"
PR="r1"

DEPENDS="flex bison"

SRC_URI="https://github.com/acpica/acpica/archive/R02_15_12.tar.gz"

SRC_URI[md5sum] = "5f705791ea39da4c06e5d81fdc375b89"
SRC_URI[sha256sum] = "13bbcf4371dc0946e4c179c9b2b4336d6e4cab2db81994deeb3bc06079d33fc2"

S="${WORKDIR}/acpica-R02_15_12/source/compiler"

NATIVE_INSTALL_WORKS = "1"
BBCLASSEXTEND = "native"

do_configure() {
	cp ../../generate/linux/Makefile.iasl Makefile
}

do_compile() {
	CFLAGS="-Wno-error=redundant-decls" $MAKE
}

do_install() {
	mkdir -p ${D}${prefix}/bin
	cp ${S}/iasl ${D}${prefix}/bin
}


