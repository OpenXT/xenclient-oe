#!/bin/bash

#Double fork so udev can continue on its merry way
(
    (
        # Creation of attributes is really racy, so wait a bit before trying to
        # write, and check that the write actually went through correctly.
        TIME=0
        while [ $TIME -lt 10 ]; do
            sleep 2
            let "TIME += 2"

            if [ -e "/sys${DEVPATH}/power/control" ]; then
                echo "on" > "/sys${DEVPATH}/power/control"
                if [ $(cat "/sys${DEVPATH}/power/control") = "on" ]; then
                    exit 0
                fi
            fi

        done
    ) &
)
