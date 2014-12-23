BEGIN {
    group="";
    gsub(/ /, "%20", file);
    gsub(/'/, "'\\''", file);
    gsub(/'/, "'\\''", node);
}

/^\[.+\]$/ {
    group=substr($0, 2, length($0) - 2);
    gsub(/'/, "'\\''", group);
}


/[a-zA-Z0-9-]+=.+$/ { 
    equals=index($0, "=");
    key=substr($0, 1, equals - 1)
    value=substr($0, equals + 1)
    gsub(/(^ *| *$)/, "", key);
    gsub(/^ */, "", value);
    gsub(/'/, "'\\''", key);
    gsub(/'/, "'\\''", value);

    system("db-write-dom0 '/" node "/" file "/" group "/" key "' '" value "'");
}

END {
}
