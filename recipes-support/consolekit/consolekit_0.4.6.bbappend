DEPENDS += " \
    polkit \
    libpam \
"
PACKAGECONFIG:append = " polkit pam" 
