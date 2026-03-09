#!/usr/bin/env python3
"""Re-patch all lectures: nav bar centered at bottom."""
import os, re, subprocess

BASE   = r"C:\dev\teaching\teaching\course-java\semester2\lessons"
BUCKET = "java-oop-course-semester2"

LECTURES = [
    (0,  "lecture-00-intro",                   "Введение в курс"),
    (1,  "lecture-01-oop-paradigms",            "Парадигмы"),
    (2,  "lecture-02-classes-oop",              "Классы и объекты"),
    (3,  "lecture-03-interfaces-polymorphism",  "Интерфейсы"),
    (4,  "lecture-04-enum-singleton",           "Enum / Singleton"),
    (5,  "lecture-05-exceptions",               "Исключения"),
    (6,  "lecture-06-generics",                 "Дженерики"),
    (7,  "lecture-07-collections",              "Коллекции"),
    (8,  "lecture-08-maps",                     "Мапы"),
    (9,  "lecture-09-inner-classes",            "Внутренние классы"),
    (10, "lecture-10-lambda-fp",                "Лямбды и ФП"),
    (11, "lecture-11-streams",                  "Stream API"),
    (12, "lecture-12-modules-packages",         "Модули и пакеты"),
    (13, "lecture-13-layered-mvc",              "Слоистая архитектура"),
    (14, "lecture-14-io-serializable",          "IO / Serializable"),
    (15, "lecture-15-localization-datetime",    "Локализация / DateTime"),
    (16, "lecture-16-xml-json",                 "XML и JSON"),
    (17, "lecture-17-reflection-annotations",   "Рефлексия"),
    (18, "lecture-18-gc-memory",                "GC и память"),
    (19, "lecture-19-qa",                       "Q&amp;A"),
]

NAV_CSS = """\
<style>
#lecNav{
  position:fixed;bottom:.7rem;left:50%;transform:translateX(-50%);
  z-index:99999;
  display:inline-flex;align-items:center;gap:.3rem;
  font-family:Inter,sans-serif;
  background:rgba(15,15,26,.82);
  border:1px solid rgba(255,255,255,.12);
  border-radius:10px;
  padding:.28em .5em;
  backdrop-filter:blur(10px);
  opacity:.4;transition:opacity .2s;
  pointer-events:auto;
}
#lecNav:hover{opacity:1;}
#lecNav a{
  display:inline-flex;align-items:center;
  padding:.2em .55em;border-radius:6px;font-size:.72rem;font-weight:600;
  text-decoration:none;border:1px solid transparent;
  color:#cbd5e0;transition:all .15s;white-space:nowrap;
}
#lecNav a:hover{border-color:#58a6ff;color:#58a6ff;background:rgba(88,166,255,.08);}
#lecNav .nav-next{
  background:rgba(233,69,96,.75);border-color:rgba(233,69,96,.5);color:#fff;
}
#lecNav .nav-next:hover{background:#e94560;border-color:#e94560;}
#lecNav .nav-sep{width:1px;height:14px;background:rgba(255,255,255,.13);flex-shrink:0;margin:0 .1em;}
#lecNav .nav-lbl{font-size:.62rem;color:#6e7681;padding:0 .25em;user-select:none;}
</style>"""

def build_nav(i, num):
    prev_html = ""
    next_html = ""
    if i > 0:
        pn, pf, pt = LECTURES[i-1]
        prev_html = (f'<a href="../{pf}/lecture-{pn:02d}-reveal.html" title="{pt}">&#8592;</a>'
                     f'<div class="nav-sep"></div>')
    if i < len(LECTURES) - 1:
        nn, nf, nt = LECTURES[i+1]
        next_html = (f'<div class="nav-sep"></div>'
                     f'<a href="../{nf}/lecture-{nn:02d}-reveal.html"'
                     f' class="nav-next" title="{nt}">&#8594;</a>')

    return (
        f"{NAV_CSS}\n"
        f'<div id="lecNav">\n'
        f'  {prev_html}'
        f'<a href="../index.html" title="К списку лекций">&#9776;</a>\n'
        f'  <span class="nav-lbl">{num}\u202f/\u202f19</span>\n'
        f'  {next_html}\n'
        f'</div>'
    )

def remove_lecnav_div(content):
    """Find <div id="lecNav"> and remove it including all nested tags."""
    start = content.find('<div id="lecNav"')
    if start == -1:
        return content
    depth = 0
    i = start
    while i < len(content):
        if content[i:i+4] == '<div':
            depth += 1
            i += 4
        elif content[i:i+6] == '</div>':
            depth -= 1
            if depth == 0:
                end = i + 6
                content = content[:start] + content[end:]
                return remove_lecnav_div(content)  # remove more if present
            i += 6
        else:
            i += 1
    return content

def strip_old_nav(content):
    # 1. Remove any <style> block that mentions #lecNav or .lec-nav
    content = re.sub(r'<style>[^<]*(?:#lecNav|\.lec-nav)\{.*?</style>', '', content, flags=re.DOTALL)
    # 2. Remove div#lecNav with proper nested-tag awareness
    content = remove_lecnav_div(content)
    # 3. Remove leftover lecNav script comment block
    content = re.sub(r'<script>\s*/\*\s*lecNav.*?\*/\s*</script>', '', content, flags=re.DOTALL)
    # 4. Collapse everything between last Reveal script and </body> —
    #    removes any orphan nav fragments that accumulated from bad patches
    content = re.sub(
        r'(</script>)\s*(?:(?!</script>).)*?</body>',
        r'\1\n</body>',
        content,
        flags=re.DOTALL
    )
    return content

def local_path(folder, num):
    for name in [f"lecture-{num:02d}-reveal.html", f"lecture-{num}-reveal.html"]:
        p = os.path.join(BASE, folder, name)
        if os.path.exists(p):
            return p, name
    return None, None

ok = 0; failed = 0

for i, (num, folder, title) in enumerate(LECTURES):
    filepath, fname = local_path(folder, num)
    if not filepath:
        print(f"[{num:02d}] MISSING: {folder}")
        failed += 1
        continue

    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    content = strip_old_nav(content)
    nav     = build_nav(i, num)
    content = content.replace("</body>", nav + "\n</body>", 1)

    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)

    r = subprocess.run(
        ["aws", "s3", "cp", filepath,
         f"s3://{BUCKET}/{folder}/{fname}",
         "--content-type", "text/html; charset=utf-8"],
        capture_output=True, text=True
    )
    status = "OK" if r.returncode == 0 else f"S3-ERR:{r.stderr[:50]}"
    print(f"[{num:02d}] {status}  {fname}  ({os.path.getsize(filepath)}b)")
    ok += 1

print(f"\nDone. OK:{ok} Failed:{failed}")

