SRC_URI[gm45.md5sum] = "330c774e71fe390d7ab649d5e2b1d504"
SRC_URI[gm45.sha256sum] = "2b7c9f76c68b48ea537e8b120a8ecd477f8f7a53eaa656a60435111200be4e6a"
SRC_URI[q45.md5sum] = "4af698f82ff70f5f25c99968b47e679e"
SRC_URI[q45.sha256sum] = "98e93222ec02452ac93360cc8a55a422aabfd19098c60bfeb28e7764d8231c29"
SRC_URI[q35.md5sum] = "f6a72be69ee9884f158bfea932f0f18b"
SRC_URI[q35.sha256sum] = "698274e267ed7baa5953251dc48b04fc61df636e7720e60e4c140eeee347c569"
SRC_URI[i5.md5sum] = "81d44790235fc6188ca15776fd42948f"
SRC_URI[i5.sha256sum] = "1607adf4e2f63c806f01264a5c665279a1964ce05cd28bdbd8ec3d1ef0d9ff06"
SRC_URI[i7.md5sum] = "5ffbd75cdf76dedd448603e7af8602a0"
SRC_URI[i7.sha256sum] = "8d2176bf9a0dd3ddd6427164ca3a18941ea86ebb5b8d2cb11bc86677f97c1fe4"
SRC_URI[xeon_5600.md5sum] = "2deba9adea0881fcd4a9f656389e68fc"
SRC_URI[xeon_5600.sha256sum] = "36174627a817732cca80635ac27d4ee9d1dd4668b6bf9f0f6146bd1da4f0d9d7"
SRC_URI[xeon_e7.md5sum] = "069c296cee118f73483eec2e93e23e2b"
SRC_URI[xeon_e7.sha256sum] = "d28b53cf17adf001db4cb7a7e1cee2342f1f4e9526c9947120107c2f0aa043c1"
SRC_URI[ivb_snb.md5sum] = "b5d19bc8ac2185e3fa88d78d80a3e254"
SRC_URI[ivb_snb.sha256sum] = "8618d72c00a824d693836d1334330e1cf831c952bb541a93a5e70e54c9e479a2"
SRC_URI[hsw.md5sum] = "e1fbdd2b8c255d3ded6f33d14e30d498"
SRC_URI[hsw.sha256sum] = "63ad2d0b8fdb4422e0f751c23472a0e9bfbc3c643959e21249c66336943b910b"
SRC_URI[bdw.md5sum] = "f40771addcb12c82b44c2ad53dbbe994"
SRC_URI[bdw.sha256sum] = "3057efadd6bcf9ddf192c6aa027cc28e07ae6997a5c0037ef1fa09e8938893f0"

PR="r3"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://GM45_GS45_PM45-SINIT_51/license.txt;md5=60d123634e0b94f8c425003389e64bda \
                    file://Q45_Q43-SINIT_51/license.txt;md5=60d123634e0b94f8c425003389e64bda \
                    file://Q35-SINIT_51/license.txt;md5=60d123634e0b94f8c425003389e64bda \
                    file://i5_i7_DUAL-SINIT_51/license.txt;md5=60d123634e0b94f8c425003389e64bda \
                    file://i7_QUAD-SINIT_51/license.txt;md5=60d123634e0b94f8c425003389e64bda \
                    file://license.txt;md5=203b6b806e49ca139abdf6706024c871 \
                    file://3rd_gen_i5_i7-SINIT_67/license.txt;md5=a879c484244808a2202d65166a2f3f72 \
                    file://4th_gen_i5_i7-SINIT_75/license.txt;md5=a879c484244808a2202d65166a2f3f72 \
                    file://5th_gen_i5_i7-SINIT_79/license.txt;md5=68248a22232ba4fd23010e9c65209406 \
"

SRC_URI = "${OPENXT_MIRROR}/GM45_GS45_PM45-SINIT_51.zip;name=gm45 \
           ${OPENXT_MIRROR}/Q45_Q43-SINIT_51.zip;name=q45 \
           ${OPENXT_MIRROR}/Q35-SINIT_51.zip;name=q35 \
           ${OPENXT_MIRROR}/i5_i7_DUAL-SINIT_51.zip;name=i5 \
           ${OPENXT_MIRROR}/i7_QUAD-SINIT_51.zip;name=i7 \
           ${OPENXT_MIRROR}/Xeon-5600-3500-SINIT-v1.1.zip;name=xeon_5600 \
           ${OPENXT_MIRROR}/Xeon-E7-8800-4800-2800-SINIT-v1.1.zip;name=xeon_e7 \
           ${OPENXT_MIRROR}/3rd-gen-i5-i7-sinit-67.zip;name=ivb_snb \
           ${OPENXT_MIRROR}/4th-gen-i5-i7-sinit-75.zip;name=hsw \
           ${OPENXT_MIRROR}/5th_gen_i5_i7-SINIT_79.zip;name=bdw \
"

FILES_${PN} = "/boot"

S = "${WORKDIR}"

do_install() {
        install -d ${D}/boot
        for i in `find "${WORKDIR}" -iname '*DEBUG*' -prune -o -iname '*NPW*' -prune -o -iname "*.bin" -print`
        do
                install -m 644 "$i" ${D}/boot
        done
}
