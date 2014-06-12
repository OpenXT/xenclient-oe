
CURL_FEATURES = "zlib,openssl,cookies,crypto-auth,dict,file,ftp,http,telnet,tftp"

require recipes/curl/curl-common.inc
require recipes/curl/curl-target.inc

SRC_URI += "file://off_t_abi_fix.patch;patch=1;pnum=0"
SRC_URI += "file://libcurl-7.19.7-certdata_url.patch;patch=1"
SRC_URI += "file://libcurl-7.19.7-nthash.patch;patch=1"
SRC_URI += "file://libcurl-7.19.7-reuse-nobody.patch;patch=1"
SRC_URI += "file://libcurl-7.19.7-ntlmv2.patch;patch=1"
SRC_URI += "file://libcurl-7.19.7-utf8.patch;patch=1"

PR = "${INC_PR}xc2"

SRC_URI[tarball.md5sum] = "79a8fbb2eed5464b97bdf94bee109380"
SRC_URI[tarball.sha256sum] = "1a15f94ae3401e3bd6208ce64155c2577815019824bceae7fd3221a12bc54a70"
