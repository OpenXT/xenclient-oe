DESCRIPTION = "Authenticated Code Modules for SINIT"
HOMEPAGE = "https://software.intel.com/en-us/articles/intel-trusted-execution-technology"
BUGTRACKER = "https://software.intel.com/en-us/forums/intel-trusted-execution-technology-intel-txt"
SECTION = "bootloaders"
LICENSE = "Intel-ACMs"
FILESEXTRAPATHS ?= "${THISDIR}/acms"
LIC_FILES_CHKSUM = " \
    file://license.txt;md5=24983614c000aac0d87e5aea9d13b8be \
"

SRC_URI = " \
    file://license.txt \
    ${OPENXT_MIRROR}/SNB_IVB_SINIT_20190708_PW.bin;downloadfilename=snb_ivb.bin;name=snb_ivb \
    ${OPENXT_MIRROR}/BDW_SINIT_20190708_1.3.2_PW.bin;downloadfilename=bdw_hsw.bin;name=bdw_hsw \
    ${OPENXT_MIRROR}/SKL_KBL_AML_SINIT_20211019_PRODUCTION_REL_NT_O1_1.10.0.bin;downloadfilename=skl_kbl_aml.bin;name=skl_kbl_aml \
    ${OPENXT_MIRROR}/CFL_SINIT_20221220_PRODUCTION_REL_NT_O1_1.10.1_signed.bin;downloadfilename=cfl_wkl_cml.bin;name=cfl_wkl_cml \
    ${OPENXT_MIRROR}/CML_S_SINIT_1_13_33_REL_NT_O1.PW_signed.bin;downloadfilename=cml_s.bin;name=cml_s \
    ${OPENXT_MIRROR}/CMLSTGP_SINIT_v1_14_46_20220819_REL_NT_O1.PW_signed.bin;downloadfilename=cml_s_tgp.bin;name=cml_s_tgp \
    ${OPENXT_MIRROR}/RKLS_SINIT_v1_14_46_20220819_REL_NT_O1.PW_signed.bin;downloadfilename=rkls.bin;name=rkls \
    ${OPENXT_MIRROR}/TGL_SINIT_v1_14_46_20220819_REL_NT_O1.PW_signed.bin;downloadfilename=tgl.bin;name=tgl \
    ${OPENXT_MIRROR}/ADL_SINIT_v1_18_16_20230427_REL_NT_O1.PW_signed.bin;downloadfilename=adl.bin;name=adl \
"

SRC_URI[snb_ivb.md5sum] = "e258fecf649b3aaa68af621247e98711"
SRC_URI[snb_ivb.sha256sum] = "1e888aebc78d637d119c489adffa95387b53429125dc3ad61f10a5cad0496834"
SRC_URI[bdw_hsw.md5sum] = "cfa4e19d2f8bdc0e231e5059a6f1bb42"
SRC_URI[bdw_hsw.sha256sum] = "2c64f6f8790049d6cf6024c75bebdb0bbacf4ef78502caeb803f90568f7c18eb"
SRC_URI[skl_kbl_aml.md5sum] = "03fc1c63b129a18e63023e5d375eedcb"
SRC_URI[skl_kbl_aml.sha256sum] = "f43470267af72eec5e7354cddbccbc8c6d0263802e48bcd85f95e1b2ae249327"
SRC_URI[cfl_wkl_cml.md5sum] = "d18ba02daa2c14a50ec7b400589a9f45"
SRC_URI[cfl_wkl_cml.sha256sum] = "ad6b7fc3ba3f4d4de2932f39cf00b206aa922bade57497e42ce97f2162f9361e"
SRC_URI[cml_s.md5sum] = "c7554bc5548e440eaf6e2ce289cebfbd"
SRC_URI[cml_s.sha256sum] = "69075f1153e884ac78fcf0a3e2095dd023d56e9e250d13e9c848ddd88e3764b3"
SRC_URI[cml_s_tgp.md5sum] = "d7ff2779f82d2752ae0e13c31146c989"
SRC_URI[cml_s_tgp.sah256sum] = "b24a6e5ead2910b3ccbb8edbe32e3430a1d694481dfdb17c9069de5e50200371"
SRC_URI[rkls.md5sum] = "bd529b3793cd767badaa8f92bcef5a24"
SRC_URI[rkls.sha256sum] = "eb4a6044f421fbab6ed018d2692f2afbeb0162e8b228945fcb04b663be4d5ac1"
SRC_URI[tgl.md5sum] = "1c90662cff90063583b82d42bb9cdc66"
SRC_URI[tgl.sha256sum] = "e87488bff9c38c5a48056925991131d48a2c9a30e459cf88b8559e4895ec1ba5"
SRC_URI[adl.md5sum] = "9877356c8b6be640d994c6bf921526e2"
SRC_URI[adl.sha256sum] = "c2ffa0441d8dd072e3f9287581ca0e982e1a08359bd1b4adca9976380c3e5e13"

PR = "r3"

S = "${WORKDIR}"

inherit deploy

do_install() {
    install -d ${D}/boot
    for i in `find "${WORKDIR}" -name "*.bin*"`
    do
        install -m 644 "$i" ${D}/boot
    done

    # After inspection of the licenses of the individual ACM files,
    # the most recent Coffee Lake license is sufficient to cover all:
    install -m 444 license.txt ${D}/boot/license-SINIT-ACMs.txt
}

do_deploy() {
    install -m 0644 "${D}/boot/snb_ivb.bin" "${DEPLOYDIR}/snb_ivb.bin"
    install -m 0644 "${D}/boot/bdw_hsw.bin" "${DEPLOYDIR}/bdw_hsw.bin"
    install -m 0644 "${D}/boot/skl_kbl_aml.bin" "${DEPLOYDIR}/skl_kbl_aml.bin"
    install -m 0644 "${D}/boot/cfl_wkl_cml.bin" "${DEPLOYDIR}/cfl_wkl_cml.bin"
    install -m 0644 "${D}/boot/cml_s.bin" "${DEPLOYDIR}/cml_s.bin"
    install -m 0644 "${D}/boot/cml_s_tgp.bin" "${DEPLOYDIR}/cml_s_tgp.bin"
    install -m 0644 "${D}/boot/rkls.bin" "${DEPLOYDIR}/rkls.bin"
    install -m 0644 "${D}/boot/tgl.bin" "${DEPLOYDIR}/tgl.bin"
    install -m 0644 "${D}/boot/adl.bin" "${DEPLOYDIR}/adl.bin"
    install -m 0644 "${D}/boot/license-SINIT-ACMs.txt" "${DEPLOYDIR}/license-SINIT-ACMs.txt"
}
addtask do_deploy after do_install before do_build

FILES_${PN} = "/boot"
