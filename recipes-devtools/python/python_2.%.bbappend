FILESEXTRAPATHS_prepend := "${THISDIR}/python:"

SRC_URI += "file://bpo-35907-cve-2019-9948.patch \
            file://bpo-35907-cve-2019-9948-fix.patch \
            file://bpo-36216-cve-2019-9636.patch \
            file://bpo-36216-cve-2019-9636-fix.patch \
            file://0001-closes-bpo-34540-Convert-shutil._call_external_zip-t.patch \
"

