# surf version
VERSION = 0.7

# paths
PREFIX = /usr
MANPREFIX = ${PREFIX}/share/man

INCS = `pkg-config --cflags xorg-server gtk+-2.0 webkit-1.0 x11`
LIBS = `pkg-config --libs xorg-server gtk+-2.0 webkit-1.0 x11`

# flags
CPPFLAGS = -DVERSION=\"${VERSION}\" -D_DEFAULT_SOURCE
CFLAGS += ${INCS} ${CPPFLAGS}
LDFLAGS = -g -lc ${LIBS}
