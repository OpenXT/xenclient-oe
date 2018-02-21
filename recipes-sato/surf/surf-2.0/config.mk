# surf version
VERSION = 2.0

# Customize below to fit your system

# paths
PREFIX = /usr
MANPREFIX = ${PREFIX}/share/man
LIBPREFIX = ${PREFIX}/lib/surf

GTKINC = `pkg-config --cflags gtk+-3.0 webkit2gtk-4.0 x11`
GTKLIB = `pkg-config --libs gtk+-3.0 webkit2gtk-4.0 x11`

# includes and libs
INCS = -I. -I${X11INC} ${GTKINC}
LIBS = -lc -L${X11LIB} -lX11 ${GTKLIB} -lgthread-2.0

# flags
CPPFLAGS += -DVERSION=\"${VERSION}\" -DWEBEXTDIR=\"${LIBPREFIX}\" -D_DEFAULT_SOURCE
CFLAGS += -std=c99 -pedantic -Wall -Os ${INCS} ${CPPFLAGS}
LDFLAGS += -s ${LIBS}
