#! /bin/sh
#
# Copyright (c) 2013 Citrix Systems, Inc.
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

usage()
{
    echo "usage: $0 -n node -r" >&2
    echo "       $0 -n node -w file" >&2
    echo " -r    Read dom0's db and creates appropriated files for nm."
    echo " -w    Read .ini file for 'connection/settings'."
    echo " -n    Database node to access."    
    exit $1
}

nm_scripts=/usr/share/xenclient/nm_scripts
syscon=/etc/NetworkManager/system-connections
nmstate=/var/lib/NetworkManager
DBNODE=""
CONNFILE=""

# usage: write_db <connection's name>
# read .ini file and update dom0's db.
write_db()
{
    DBNAME=`echo "$1" | sed 's/ /%20/g'`
        
    if [ "${DBNODE}" = "nm-connections" ]; then
        db-rm-dom0 "/$DBNODE/$DBNAME"
        if [ -f "$syscon/$1" ]; then
            cd $syscon
            awk -v node="${DBNODE}" -v file="$1" -f $nm_scripts/nm_to_db.awk < "$1"
        fi
    elif [ "${DBNODE}" = "nm-state" ]; then
        db-rm-dom0 "/$DBNODE/NetworkManager.state"
        if [ -f "$nmstate/$1" ]; then
            cd "$nmstate"
            awk -v node="${DBNODE}" -v file="$1" -f $nm_scripts/nm_to_db.awk < "NetworkManager.state"
        fi
    elif [ "${DBNODE}" = "seen-bssids" ]; then
        db-rm-dom0 "nm-state/seen-bssids"
        if [ -f "$nmstate/seen-bssids" ]; then
            cd "$nmstate"
            awk -v node="nm-state" -v file="$1" -f $nm_scripts/nm_to_db.awk < "seen-bssids"
        fi

    fi
}

# usage: read_db
# read dom0's db and creates appropriated files for nm.
read_db()
{
    if [ "${DBNODE}" = "nm-connections" ]; then
        rm -rf $syscon/*
        if [ `db-exists-dom0 nm-connections/Wired%20Ethernet%20Connection` = false ]; then
	    cp $nm_scripts/WiredEthernetConnection $syscon/Wired\ Ethernet\ Connection
            write_db Wired\ Ethernet\ Connection
            exit 0
        fi

        for i in `db-nodes-dom0 nm-connections`; do
            db-ls-dom0 "/nm-connections/$i" | awk -f $nm_scripts/db_to_nm.awk > "$syscon/`echo "$i" | sed -e 's/%20/ /g'`"
        done
    elif [ "${DBNODE}" = "nm-state" ]; then
        if [ `db-exists-dom0 nm-state/NetworkManager.state` = "true" ]; then
            db-ls-dom0 "/nm-state/NetworkManager.state" | awk -f $nm_scripts/db_to_nm.awk > "$nmstate/NetworkManager.state"
        fi
    elif [ "${DBNODE}" = "seen-bssids" ]; then
        if [ `db-exists-dom0 nm-state/seen-bssids` = "true" ] ; then
            db-ls-dom0 "/nm-state/seen-bssids" | awk -f $nm_scripts/db_to_nm.awk > "$nmstate/seen-bssids"
        fi
    fi
}

r=0
w=0
while getopts w:hrn: opt
do
    case "$opt" in
         n) DBNODE="$OPTARG";;
         h) usage 1;;
         w) w=1
            CONNFILE="$OPTARG"
            ;; 
         r) r=1;;
         \?) usage 1;;
    esac
done

# Entry
if [ $r = 1 ]; then
        read_db
elif [ $w = 1 ]; then
    if [ "x${DBNODE}" = "x" ]; then
        usage 1
    else
        write_db "`basename "$CONNFILE"`"
    fi
else
    usage 1
fi

