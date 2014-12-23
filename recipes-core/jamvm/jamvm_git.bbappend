# Override broken git URL from upstream recipe.
SRC_URI = "git://git.code.sf.net/p/jamvm/code;protocol=git \
           file://jamvm-jni_h-noinst.patch \
           file://libffi.patch \
           file://jamvm-minmax-heap.patch \
          "
