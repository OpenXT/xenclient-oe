PR .= ".1"

DEPENDS += " polkit libpam "

PACKAGECONFIG_append = " policykit pam" 
