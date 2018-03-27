valid_tboot_archs = " \
    x86_64 x86_32 \
"

def map_tboot_arch(a, d):
    import re
    valid_archs = d.getVar('valid_tboot_archs').split()

    if      re.match("i.86", a):    return "x86_32"
    elif    re.match("x86.64", a):  return "x86_64"
    else:
        return "INVALID"

