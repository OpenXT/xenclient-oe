PR .= ".5"

SRC_URI = "git://github.com/xranby/jamvm;protocol=https \
           file://jamvm-jni_h-noinst.patch \
           file://libffi.patch \
           file://jamvm-minmax-heap.patch \
           file://java \
          "
