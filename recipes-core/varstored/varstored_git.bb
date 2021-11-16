SUMMARY = "Package for managing guest EFI variables"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8444b396c3cde7d8fe18ae36a3638a55"

inherit useradd xc-rpcgen-c

DEPENDS = " \
    dbus \
    dbus-glib \
    libseccomp \
    libxml2 \
    libxcdbus \
    openssl \
    xen-tools \
"

# lock to this SRCREV until we uprev Xen to >4.12
SRCREV = "f8c2656be1cfca35bb9297b97d9af962f51f19f9"
PV = "0+git${SRCPV}"

S = "${WORKDIR}/git"

SRC_URI = " \
    git://github.com/xapi-project/varstored.git;protocol=https;branch=master \
    file://0001-add-oxtdb-varstore.patch \
"

# download the public Microsoft certs for verifying MS-signed binaries
SRC_URI += " \
    https://www.microsoft.com/pkiops/certs/MicCorUEFCA2011_2011-06-27.crt;name=uefica \
    https://www.microsoft.com/pkiops/certs/MicWinProPCA2011_2011-10-19.crt;name=pca \
    https://www.microsoft.com/pkiops/certs/MicCorKEKCA2011_2011-06-24.crt;name=kekca \
"

SRC_URI[uefica.sha256sum] = "48e99b991f57fc52f76149599bff0a58c47154229b9f8d603ac40d3500248507"
SRC_URI[pca.sha256sum] = "e8e95f0733a55e8bad7be0a1413ee23c51fcea64b3c8fa6a786935fddcc71961"
SRC_URI[kekca.sha256sum] = "a1117f516a32cefcba3f2d1ace10a87972fd6bbe8fe0d0b996e09e65d802a503"

# MS is particular about the user-agent, clobber the wget call here
FETCHCMD_wget = "/usr/bin/env wget -t 2 -T 30 --passive-ftp --user-agent 'curl/7.76.1' --no-check-certificate"

# download the ever-growing community dbx.auth file, which contains
# a list of known malicious guids that we should never boot with.
SRC_URI += " \
    https://uefi.org/sites/default/files/resources/dbxupdate_x64.bin;downloadfilename=dbx.auth \
"

SRC_URI[sha256sum] = "46ba1f2a0a2ed7aabe20f9b7b2a8d717cb0b514cea83c7a1a24fe25f6b208784"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --no-create-home \
                       --shell /bin/false \
                       --groups varstored \
                       --gid 415 \
                       --uid 416 \
                       varstored"
GROUPADD_PARAM_${PN} = "--system --gid 415 varstored"

do_configure_append() {
    mkdir -p rpcgen
    xc-rpcgen --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o rpcgen ${STAGING_IDLDATADIR}/db.xml
}

# generate auth signing keys
do_compile_append() {
    openssl x509 -inform DER -in ${WORKDIR}/MicCorUEFCA2011_2011-06-27.crt -outform PEM -out ${S}/MicCorUEFCA2011_2011-06-27.pem -text
    openssl x509 -inform DER -in ${WORKDIR}/MicWinProPCA2011_2011-10-19.crt -outform PEM -out ${S}/MicWinProPCA2011_2011-10-19.pem -text
    openssl x509 -inform DER -in ${WORKDIR}/MicCorKEKCA2011_2011-06-24.crt -outform PEM -out ${S}/MicCorKEKCA2011_2011-06-24.pem

    echo ${S}/MicCorKEKCA2011_2011-06-24.pem > ${S}/KEK.list
    echo ${S}/MicWinProPCA2011_2011-10-19.pem > ${S}/db.list
    echo ${S}/MicCorUEFCA2011_2011-06-27.pem >> ${S}/db.list

    oe_runmake auth
}

do_install() {
    install -d ${D}/usr/sbin
    install -m 0755 ${S}/varstored ${D}/usr/sbin/varstored

    install -d ${D}/usr/bin
    install -m 0755 ${S}/tools/varstore-{get,set,ls,rm,sb-state} ${D}/usr/bin

    install -d ${D}/usr/share/varstored
    install -m 0755 ${S}/{PK.auth,KEK.auth,db.auth} ${D}/usr/share/varstored
    install -m 0755 ${WORKDIR}/dbx.auth ${D}/usr/share/varstored
}
