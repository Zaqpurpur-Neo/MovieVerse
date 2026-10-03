#!/bin/bash
# Pemakaian: ./ctx.sh berkas1 berkas2 ... | xclip -selection clipboard
# (di Wayland ganti xclip dengan: wl-copy)
for f in "$@"; do
  echo "=== $f ==="
  cat "$f"
  echo
done
