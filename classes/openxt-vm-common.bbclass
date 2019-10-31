# Based upon the OE meta class live-vm-common.bbclass with modifcations to
# align with OpenXT's conventions.

KERNEL_IMAGETYPE ??= "bzImage"
VM_DEFAULT_KERNEL ??= "${KERNEL_IMAGETYPE}"

INITRD_VM ?= ""

VM_BOOT_DIR ?= "/boot"
INSTALL_VM_KERNEL ?= "0"
INSTALL_VM_INITRD ?= "0"

python() {
    initrds = d.getVar('INITRD_VM')
    deploy = d.getVar('DEPLOY_DIR_IMAGE')
    machine = d.getVar('MACHINE')

    if initrds:
        for i in initrds.split():
            d.appendVar('INITRD', '%s/%s-%s.cpio.gz' % (deploy, i, machine))
            d.appendVarFlag('do_vm_common', 'depends', ' %s:do_image_complete' % i)
}

populate_kernel() {
    dest=$1
    install -d $dest

    # Install kernel in DEST for all loaders to use.
    bbnote "Trying to install ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} as $dest/${VM_DEFAULT_KERNEL}"
    if [ -e ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ]; then
        install -m 0644 "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}" "$dest/${VM_DEFAULT_KERNEL}"
    else
        bbwarn "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} doesn't exist"
    fi
}

populate_initrd() {
    dest=$1
    install -d $dest

    # initrd is made of concatenation of multiple filesystem images
    bbnote "Trying to install ${INITRD} as $dest/initrd"
    if [ -n "${INITRD}" ]; then
        rm -f "$dest/initrd"
        for fs in ${INITRD}
        do
            if [ -s "$fs" ]; then
                cat "$fs" >> "$dest/initrd"
            else
                bbfatal "$fs is invalid. initrd image creation failed."
            fi
        done
        chmod 0644 "$dest/initrd"
    fi
}

do_vm_common() {
    if [ "${INSTALL_VM_KERNEL}" -eq 1 ]; then
        populate_kernel "${IMAGE_ROOTFS}${VM_BOOT_DIR}"
    fi

    if [ "${INSTALL_VM_INITRD}" -eq 1 ]; then
        populate_initrd "${IMAGE_ROOTFS}${VM_BOOT_DIR}"
    fi
}

addtask vm_common after do_rootfs before do_image
