inherit syslinux

#
# Configuring for a multiboot image
#
# ${SYSLINUX_MULTIBOOT} - a space separated list of multiboot modules
# ${SYSLINUX_MULTIBOOT_CMDLINE} - a "\n" separated list of command lines in same order as
#   ${SYSLINUX_MULTIBOOT} with empty lines for the sparse case
#

syslinux_iso_populate_append() {
    if [ -n "${SYSLINUX_MULTIBOOT}" ]; then
        install -m 0444 ${STAGING_DATADIR}/syslinux/libcom32.c32 $iso_dir${ISOLINUXDIR}
        install -m 0444 ${STAGING_DATADIR}/syslinux/mboot.c32 $iso_dir${ISOLINUXDIR}
    fi
}

syslinux_hddimg_populate_append() {
    if [ -n "${SYSLINUX_MULTIBOOT}" ]; then
        install -m 0444 ${STAGING_DATADIR}/syslinux/libcom32.c32 $hdd_dir${SYSLINUXDIR}
        install -m 0444 ${STAGING_DATADIR}/syslinux/mboot.c32 $hdd_dir${SYSLINUXDIR}
    fi
}

build_syslinux_cfg () {
    if [ -z "${SYSLINUX_CFG}" ]; then
        bbfatal "Unable to read SYSLINUX_CFG"
    fi

    cat /dev/null > ${SYSLINUX_CFG}

    echo "${SYSLINUX_OPTS}"| tr ';' '\n' | while read opt; do
        echo "${opt}" >> ${SYSLINUX_CFG}
    done

    if [ -n "${SYSLINUX_ALLOWOPTIONS}" ]; then
        echo "ALLOWOPTIONS ${SYSLINUX_ALLOWOPTIONS}" >> ${SYSLINUX_CFG}
    else
        echo "ALLOWOPTIONS 1" >> ${SYSLINUX_CFG}
    fi

    # Do not support multi-label
    echo "DEFAULT boot" >> ${SYSLINUX_CFG}


    if [ -n "${SYSLINUX_TIMEOUT}" ]; then
        echo "TIMEOUT ${SYSLINUX_TIMEOUT}" >> ${SYSLINUX_CFG}
    else
        echo "TIMEOUT 10" >> ${SYSLINUX_CFG}
    fi
    if [ -n "${SYSLINUX_PROMPT}" ]; then
        echo "PROMPT ${SYSLINUX_PROMPT}" >> ${SYSLINUX_CFG}
    else
        echo "PROMPT 1" >> ${SYSLINUX_CFG}
    fi
    echo "LABEL boot" >> ${SYSLINUX_CFG}

    if [ -n "${SYSLINUX_MULTIBOOT}" ]; then
        echo "  KERNEL mboot.c32" >> ${SYSLINUX_CFG}

        echo -n "  APPEND" >> ${SYSLINUX_CFG}
        count=1
        for m in ${SYSLINUX_MULTIBOOT}; do
            if [ ${count} -ne 1 ]; then
                echo -n " ---" >> ${SYSLINUX_CFG}
            fi
            cmdline=$(echo "$x" | sed -n ${count}p)
            if [ -n "${cmdline}" ]; then
                echo -ne " /$m $cmdline" >> ${SYSLINUX_CFG}
            else
                echo -ne " /$m" >> ${SYSLINUX_CFG}
            fi
        done
    else
        echo "KERNEL /${KERNEL_IMAGETYPE}" >> ${SYSLINUX_CFG}
        echo "APPEND LABEL=boot ${APPEND}" >> ${SYSLINUX_CFG}
        if [ -n "${INITRD}" ]; then
            echo "INITRD /initrd" >> ${SYSLINUX_CFG}
        fi
    fi
}
