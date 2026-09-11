#!/usr/bin/env python3
import os, re, subprocess, sys
from pathlib import Path

s, b, tdir = Path(sys.argv[1]), Path(sys.argv[2]), Path(sys.argv[3])
compat = s / "policy/modules/system/openxtcompat.if"
compat_b = b / "policy/modules/system/openxtcompat.if"
extra = os.environ.get("EXTRA_OEMAKE", "")
cmd = "make %s policy" % extra
token_re = re.compile(r"at token '([a-z][a-z0-9_]*)'")
skip_tokens = {
    "require", "gen_require", "optional_policy", "tunable_policy", "ifdef",
    "ifndef", "else", "endif", "if", "define", "divert", "undivert",
    "interface", "template", "type", "class", "role", "attribute", "bool",
    "true", "false", "module", "allow", "dontaudit", "auditallow",
}
dup_re = re.compile(r"duplicate definition of ([a-z][a-z0-9_]*)\(\)")
oq, cq = chr(96), "'"


def wipe_tmp():
    for d in (b / "tmp", s / "tmp"):
        if not d.is_dir():
            continue
        for stale in d.glob("*"):
            if stale.suffix in (".conf", ".tmp", ".mod") or stale.name.startswith("all_interfaces"):
                try:
                    stale.unlink()
                except FileNotFoundError:
                    pass


for i in range(40):
    sys.stderr.write("policy compile attempt %d\n" % (i + 1))
    r = subprocess.run(cmd, cwd=b, capture_output=True, text=True, shell=True)
    sys.stdout.write(r.stdout)
    sys.stderr.write(r.stderr)
    (tdir / "policy.make").write_text(r.stdout + r.stderr)
    if r.returncode == 0:
        sys.exit(0)
    dups = sorted(set(dup_re.findall(r.stdout + r.stderr)))
    if dups:
        text = compat.read_text()
        for name in dups:
            sys.stderr.write("removing duplicate stub %s\n" % name)
            text = re.sub(
                r"\ninterface\(" + re.escape(oq + name + cq) + r",[\s\S]*?\n" + re.escape(cq + ")") + r"\n",
                "\n",
                text,
                count=1,
            )
        compat.write_text(text)
        if compat_b != compat:
            compat_b.write_text(text)
        wipe_tmp()
        continue
    tokens = sorted(set(token_re.findall(r.stdout + r.stderr)) - skip_tokens)
    if not tokens:
        sys.exit(r.returncode or 1)
    text = compat.read_text()
    added = 0
    for token in tokens:
        marker = "interface(%s%s%s" % (oq, token, cq)
        if marker in text:
            sys.stderr.write("token %s already stubbed\n" % token)
            continue
        sys.stderr.write("stubbing missing interface %s\n" % token)
        text += "\ninterface(%s%s%s,%s\n\tgen_require(%s\n\t\ttype device_t;\n\t%s)\n%s)\n" % (
            oq, token, cq, oq, oq, cq, cq)
        added += 1
    if added == 0:
        sys.exit(r.returncode or 1)
    compat.write_text(text)
    if compat_b != compat:
        compat_b.parent.mkdir(parents=True, exist_ok=True)
        compat_b.write_text(text)
    wipe_tmp()
sys.exit(1)
