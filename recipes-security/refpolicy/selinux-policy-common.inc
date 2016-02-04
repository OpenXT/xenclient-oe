def get_poltype(f):
    import re
    config = open (f, "r")
    regex = re.compile('^[\s]*SELINUXTYPE=[\s]*(\w+)[\s]*$')
    for line in config:
        match = regex.match(line)
        if match is not None:
            return match.group(1)
    return None
