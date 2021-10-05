# In dom0, monit controls vglass, so we only want monit to run in
# runlevel 5, which matches vglass, disman & ivcdaemon.
INITSCRIPT_PARAMS_${PN}_xenclient-dom0 = "start 99 5 . stop 01 0 1 2 3 4 6 ."
