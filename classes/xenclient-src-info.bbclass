# TODO: Use stamps and sstate to avoid rerunning task every time.

SRC_INFO_DIR ?= "${DEPLOY_DIR}/src-info"
SRC_INFO_FILE = "${SRC_INFO_DIR}/${PACKAGE_ARCH}/${PN}"

addtask src_info
do_src_info[nostamp] = "1"

python do_src_info() {
    import json

    info = {"source": {},
            "license": d.getVar("LICENSE", True) or "unknown"}

    fetcher = bb.fetch2.Fetch([], d)

    for url in fetcher.urls:
        url_data = fetcher.ud[url]

        url_no_params = bb.fetch2.encodeurl((url_data.type,
                                             url_data.host,
                                             url_data.path,
                                             url_data.user,
                                             url_data.pswd,
                                             None))

        if url_data.type in ["http", "https", "ftp", "ftps"]:
            md5 = url_data.md5_expected
            sha256 = url_data.sha256_expected
        else:
            md5 = None
            sha256 = None

        base = None
        path = fetcher.localpath(url)

        for x in ["DL_DIR", "TOPDIR"]:
            x_dir = d.getVar(x, True) + '/'
            if path.startswith(x_dir):
                base = x
                path = path[len(x_dir):]
                break

        if base is None:
            pn = d.getVar('PN', True)
            bb.warn("xenclient-src-info: '%s' has unexpected localpath '%s'" %
                    (pn, path))

        info["source"][url_no_params] = {"base": base,
                                         "path": path,
                                         "md5": md5,
                                         "sha256": sha256}

    output_file = d.getVar("SRC_INFO_FILE", True)
    bb.utils.mkdirhier(os.path.dirname(output_file))

    with open(output_file, "w") as f:
        json.dump(info, f, indent=4, sort_keys=True)
}

addtask src_info_all after do_src_info
do_src_info_all[recrdeptask] = "do_src_info"
do_src_info_all[nostamp] = "1"

python do_src_info_all() {
    pass
}
