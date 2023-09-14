#!/bin/sh

if [ -d /sys/firmware/efi ] ; then
    mount -t efivarfs efivarfs /sys/firmware/efi/efivars
fi
