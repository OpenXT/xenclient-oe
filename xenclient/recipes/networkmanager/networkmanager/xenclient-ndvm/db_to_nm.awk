BEGIN {
}

/^ [a-zA-Z0-9-]+ =$/ {
    print ""
    group=substr($0, 1, length($0) - 2);
    gsub(/(^ *| *$)/, "", group);
    print "[" group "]";
}

/^  [a-zA-Z0-9-]+ = .+$/ {
    equals=index($0, "=");
    key=substr($0, 1, equals - 1)
    value=substr($0, equals + 1)
    gsub(/(^ *| *$)/, "", key);
    gsub(/^ *"/, "", value);
    gsub(/"$/, "", value);
    print key "=" value
}

END {
}
