$BaseBranch = "1.20-1.20.1"
$CompareBranch = "1.20.1-forge-arch-loom"
$OutFile = "changed_files.txt"

git fetch --all --prune

git diff --name-status --find-renames "$BaseBranch...$CompareBranch" |
    Set-Content -Encoding UTF8 $OutFile

Write-Host "Wrote changed files to $OutFile"