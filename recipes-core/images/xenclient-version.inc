# Writes the XenClient build number, build date, version and release
# to /etc/xenclient.conf. Also updates /etc/issue and /etc/issue.net.

XENCLIENT_BUILD ?= "local"
XENCLIENT_BUILD_DATE ?= "unknown"
XENCLIENT_VERSION ?= "local"
XENCLIENT_RELEASE ?= "unknown"
XENCLIENT_TOOLS ?= "unknown"

post_rootfs_version_commands() {
	# Add /etc/xenclient.conf
	{
		echo 'product = OpenXT';
		echo 'build = ${XENCLIENT_BUILD}';
		echo 'build_date = ${XENCLIENT_BUILD_DATE}';
		echo 'build_branch = ${XENCLIENT_BUILD_BRANCH}';
		echo 'version = ${XENCLIENT_VERSION}';
		echo 'release = ${XENCLIENT_RELEASE}';
		echo 'tools = ${XENCLIENT_TOOLS}';
		echo 'DefaultNICs = 0';
	} > ${IMAGE_ROOTFS}/etc/xenclient.conf;

	# Update /etc/issue and /etc/issue.net
	{
		echo 'OpenXT';
		echo;
		cat ${IMAGE_ROOTFS}/etc/xenclient.conf;
		echo;
	} > ${IMAGE_ROOTFS}/etc/issue;
	cp ${IMAGE_ROOTFS}/etc/issue ${IMAGE_ROOTFS}/etc/issue.net;
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_version_commands; "
