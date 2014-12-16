#! /bin/sh
#
# Copyright (c) 2012 Citrix Systems, Inc.
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

export DISPLAY=:0

p=0
p1=0
while true;
do
	uimpid=`cat /tmp/uim-toolbar-gtk.pid || echo NO`
	v=`xenstore-read "login/state"`
        if [ "$?" -eq 0 ]; then
            case $v in
                    0)
                            [ "$p" -ne 0 ] && kill "$p"
                            [ "$p1" -ne 0 ] && kill "$p1"
			    p=0
			    p1=0
                            if [ "$uimpid" != "NO" ] ; then
                              rm -f /tmp/uim-toolbar-gtk.pid
                              kill $uimpid
                            fi
                            xenstore-write "login/state" 3
                            ;;
                    1)
                            # Wait for the other midori window to be there
                            while true ; do
                                xprop -name "Midori" > /dev/null 2>&1
                                [ "$?" -eq 0 ] && break;
                                sleep 1
                            done

                            # Close NM applet menu.
                            DBUS_SYSTEM_BUS_ADDRESS=tcp:host=1.0.0.0,port=5555 \
                                LD_PRELOAD=/usr/lib/libv4v-1.0.so.0 \
                                INET_IS_V4V=1 \
                                dbus-send --system --type=method_call \
                                          --dest=com.citrix.xenclient.networkdaemon \
                                          /nm \
                                          com.citrix.xenclient.network.nm.close_network_menu

                            if [ "$uimpid" != "NO" ] ; then
                              rm -f /tmp/uim-toolbar-gtk.pid
                              kill $uimpid
                            fi
                            LD_PRELOAD=/usr/lib/libv4v-1.0.so.0.0.0 INET_IS_V4V=1 \
                            midori -g --class=midori-auth -a "`xenstore-read "login/url"`" < /dev/null &
		            p="$!"
                            xenstore-write "login/state" 2
			    sh -c "while true; do win=\`xdotool search --onlyvisible --class midori-auth\`; xdotool windowactivate \$win; sleep 1; done" < /dev/null &
			    p1="$!"
                            ;;
                    3)
                            ;;

            esac
        fi
	sleep 1
done
