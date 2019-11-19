DEPENDS += " \
    polkit \
    libpam \
"
PACKAGECONFIG_append += "polkit pam" 
