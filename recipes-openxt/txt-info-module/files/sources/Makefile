ifneq ($(KERNELRELEASE),)
# kbuild part of makefile
include Kbuild

else
# normal makefile
KERNEL_VERSION ?= `uname -r`
KERNEL_SRC ?= /lib/modules/$(KERNEL_VERSION)/build
INSTALL_HDR_PATH ?= /usr

default:
	$(MAKE) -C $(KERNEL_SRC) M=$$PWD

clean:
	$(MAKE) -C $(KERNEL_SRC) M=$$PWD clean

modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$$PWD modules_install

endif
