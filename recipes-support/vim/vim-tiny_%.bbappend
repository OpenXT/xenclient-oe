# Don't build graphical support to avoid X11/GTK3 depends
PACKAGECONFIG:remove = "gtkgui x11"
