#include <linux/kobject.h>
#include <linux/module.h>
#include <linux/device.h>
#include <linux/platform_device.h>
#include <linux/io.h>

#define TXT_PUB_CR_BASE	    0xfed30000
#define TXT_PUB_CR_SIZE	    0x10000
static const struct resource txt_resources[] = {
	{
		.start = TXT_PUB_CR_BASE,
		.end = TXT_PUB_CR_BASE + TXT_PUB_CR_SIZE - 1,
		.flags = IORESOURCE_MEM,
	},
};
#define TXT_PUB_CR_INDEX    0

struct platform_device *pdev;
struct txt_info {
	void __iomem *cr_pub;
	void __iomem *cr_priv;
};
static struct txt_info txt_info;

static void __iomem *txt_info_map_regs(struct platform_device *pdev,
	size_t index)
{
	struct resource *res;
	void __iomem *base;

	res = platform_get_resource(pdev, IORESOURCE_MEM, index);
	if (IS_ERR(res)) {
		dev_dbg(&pdev->dev,
			"Failed to access IOMEM resource %zu.\n", index);
		return res;
	}

	base = devm_ioremap(&pdev->dev, res->start, resource_size(res));
	if (IS_ERR(base))
		dev_dbg(&pdev->dev,
			"Failed to ioremap configuration registers.\n");

	return base;
}

/* Registers offset from TXT_PUB_CR_BASE */
#define TXT_STS_OFFSET		0x000
#define TXT_ESTS_OFFSET		0x008
#define TXT_ERRORCODE_OFFSET	0x030
#define TXT_VER_FSBIF_OFFSET	0x100
#define TXT_DIDVID_OFFSET	0x110
#define TXT_VER_QPIIF_OFFSET	0x200

#define DECLARE_PUB_SHOW_U8(name, offset)				\
static ssize_t name##_show(struct kobject *kobj,			\
	struct kobj_attribute *attr, char *buf)				\
{									\
	uint8_t v = ioread8(txt_info.cr_pub + (offset));		\
	return sprintf(buf, "%#04x\n", v);				\
}									\
static struct kobj_attribute txt_attr_##name = __ATTR_RO(name);

#define DECLARE_PUB_SHOW_U32(name, offset)				\
static ssize_t name##_show(struct kobject *kobj,			\
	struct kobj_attribute *attr, char *buf)				\
{									\
	uint32_t v = ioread32(txt_info.cr_pub + (offset));		\
	return sprintf(buf, "%#010x\n", v);				\
}									\
static struct kobj_attribute txt_attr_##name = __ATTR_RO(name);

#define DECLARE_PUB_SHOW_U64(name, offset)				\
static ssize_t name##_show(struct kobject *kobj,			\
	struct kobj_attribute *attr, char *buf)				\
{									\
	uint64_t v = ioread32(txt_info.cr_pub + (offset) + 0x4);	\
	v <<= 32;							\
	v |= ioread32(txt_info.cr_pub + (offset));			\
	return sprintf(buf, "%#018llx\n", v);				\
}									\
static struct kobj_attribute txt_attr_##name = __ATTR_RO(name);

DECLARE_PUB_SHOW_U64(sts, TXT_STS_OFFSET);
DECLARE_PUB_SHOW_U8(ests, TXT_ESTS_OFFSET);
DECLARE_PUB_SHOW_U32(errorcode, TXT_ERRORCODE_OFFSET);
DECLARE_PUB_SHOW_U32(ver_fsbif, TXT_VER_FSBIF_OFFSET);
DECLARE_PUB_SHOW_U64(didvid, TXT_DIDVID_OFFSET);
DECLARE_PUB_SHOW_U32(ver_qpiif, TXT_VER_QPIIF_OFFSET);

static struct attribute *txt_subsys_attrs[] = {
	&txt_attr_sts.attr,
	&txt_attr_ests.attr,
	&txt_attr_errorcode.attr,
	&txt_attr_ver_fsbif.attr,
	&txt_attr_didvid.attr,
	&txt_attr_ver_qpiif.attr,
	NULL,
};

static umode_t txt_attr_is_visible(struct kobject *kobj,
	struct attribute *attr, int n)
{
	return attr->mode;
}

static const struct attribute_group txt_subsys_attr_group = {
	.attrs = txt_subsys_attrs,
	.is_visible = txt_attr_is_visible,
};

struct kobject *txt_kobj;

static int __init init_txt_info(void)
{
	int rc;
	void __iomem *base;

	pr_info("%s\n", __func__);

	pdev = platform_device_register_simple(
		"txt", -1, txt_resources, ARRAY_SIZE(txt_resources));
	if (IS_ERR(pdev)) {
		rc = PTR_ERR(pdev);
		pr_err("Failed to register txt platform device driver (%d).\n", rc);
		goto fail_register;
	}

	base = txt_info_map_regs(pdev, TXT_PUB_CR_INDEX);
	if (IS_ERR(base)) {
		rc = PTR_ERR(base);
		dev_err(&pdev->dev,
			"Failed to map TXT public resources (%d).\n", rc);
		goto fail_map_pub;
	}
	txt_info.cr_pub = base;

	rc = sysfs_create_group(&pdev->dev.kobj, &txt_subsys_attr_group);
	if (rc) {
		dev_err(&pdev->dev, "Failed to create sysfs group (%d).\n", rc);
		goto fail_sysfs;
	}

	return 0;

fail_sysfs:
	devm_iounmap(&pdev->dev, txt_info.cr_pub);
fail_map_pub:
	platform_device_unregister(pdev);
fail_register:
	return rc;
}

static void __exit cleanup_txt_info(void)
{
	pr_info("%s\n", __func__);

	if (pdev)
		platform_device_unregister(pdev);
}

module_init(init_txt_info);
module_exit(cleanup_txt_info);

MODULE_AUTHOR("Assured Information Security, Inc");
MODULE_DESCRIPTION("TXT driver.");
MODULE_VERSION("1.0");
MODULE_LICENSE("GPL");
