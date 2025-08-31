# Just copy-paste into terminal to get all changed files out of the commits
# --- CONFIG ---
# Put your commit SHAs/refs here (hashes, tags, or branch@{n} syntax all work)
$commits = @(
  "d66262277230f347d051b1dd2e520682cf424cf9",
  "a0f744bcaa76f000457b64140cda4256ce8e3870",
  "7abddf1ded344c11c966d871a1a47ea2654bebc6",
  "b6d5b23019d5513c5bb95f27a2d4d39d288d07f0",
  "b938b360b96d5b722af18f8982c25d54c9b1d0eb",
  "2468ce60c236bab59abba08746236d43d748b30c",
  "8d4785ec8586f9e778e2d017e830bd51c26171b1",
  "4612292fd1f9035d7c84b1cd27a5c2cfc57d76b1",
  "046029c0aa7e6b2ae75f4b48f61158fb334f92b6",
  "4564f8b23c6736a58da03d63fc288b1e0574decf",
  "3e2efff16bcdbc6ac8b0363779a9547a5f5fa142",
  "2972ec25fe42e3f4318b03ffa5849d6d513eb60c",
  "8985068b0e7bb5f410035f4a7074920d8789d6d7",
  "f94ab2eb783362be5f1e0eed922edb9853860358",
  "ce37b8a9812a26e050525fa13408a65cae777e80",
  "5fa7592f5612d7614c9efb925ff656ac6f013eb3",
  "8806551f2e8bc7d986c80a6569c8d20200e97d0b",
  "5eea85926b4f70ad4dd87811e0985c31086ff1b2",
  "52608e98911cf62c671019248bf7e45de7ca3a58",
  "0c77d22ebdd5d0a28e38f52449989f2a905774eb",
  "c539bb74f0101c8db3740aa7d2a04a56496d06ab",
  "6940cf0a60a2f8932081c075f3db38e17fd2767e",
  "f52659c93837d819181d7a5f052ca6e91d4b6492",
  "2ae143b521d1617b300eff16de5120e5728b6d85",
  "284ef1163b352d40d7657317dcd4e748ddff429a",
  "694ccfd680ccf2a4d0bdf5bb63874fb2018f26c4",
  "4f42f3c7194fb9a7d1197b04c798ce3a1097348e",
  "620455985e492b85444577c5f1a68d57d27ba9d1",
  "04d2348f9fb6b4cd6bbe7613c76236c86a2a64d9",
  "edde34e50cb52b90cade9e82df63f717a52a4f52",
  "4b12a0c0bb0e52cf35317711519145b1b658d9be",
  "5fdfda058093da76926379645e9ceab86b8870fe",
  "fe8653f949bf87b457b3403b88bc6a05c8eebe0b",
  "c3452cfe7598cd88c4ee15037a6683061b32aeb4",
  "eb9746e280adad7c330a68b310195de69336c268",
  "a29ab91bb277cb3d7000f569bf52b7e5b39ee6ba",
  "62a97965db8b9a6e27acaf658b49384fb0d4f345",
  "9ee44a50c53fc3d756b1b289560fc2787c10c64e",
  "c26a3b784ba845d0434779a5c9d3e1d28eca12b4",
  "3fdd2e06e5a48671106583a829a817fade5e9ab4",
  "f5002f6963bb4aff9edcf2024c32ad2d7e170bd9",
  "160bb0e598e1018e928cf8e217be11def0dc26e4",
  "02f5d0c96c51312c7df70ad6c0d0ff8754c4dfd8",
  "22ab813e3e1087a7132eeeab500987074c8c0228",
  "dd5789048dab957c49ee35894b094048bef85599",
  "2339bfe115506bcc4a236c58e79ccb6b3c5153db"
)

# (Optional) Restrict to certain paths. Leave empty for whole repo.
$onlyPaths = @()  # e.g. @("src/core", "shared")

# Include deletions as "changed"?
$includeDeleted = $false
# Include copies as "changed"?
$includeCopies  = $false
# ---------------

$files = [System.Collections.Generic.HashSet[string]]::new()
$filters = "AMR" + ($(if ($includeDeleted) { "D" } else { "" })) + ($(if ($includeCopies) { "C" } else { "" }))

foreach ($c in $commits) {
  # Skip if not a commit-ish
  & git cat-file -e "$c^{commit}" 2>$null
  if ($LASTEXITCODE -ne 0) { Write-Warning "Skipping $c (not a commit)"; continue }

  $args = @("show","--name-status","-M","-C","--diff-filter=$filters","$c","--") + $onlyPaths
  $lines = & git @args

  foreach ($line in $lines) {
    # Parse: A|M|D\tpath   or   Rxxx|Cxxx\told\tnew
    if ($line -match '^(A|M|D)\t(.+)$') {
      $null = $files.Add($Matches[2])
    } elseif ($line -match '^(R\d+|C\d+)\t(.+?)\t(.+)$') {
      # For renames/copies, count the *new* path as changed
      $null = $files.Add($Matches[3])
      # If you also care about the old path, uncomment:
      # $null = $files.Add($Matches[2])
    }
  }
}

$unique = $files | Sort-Object
$unique | Set-Content changed_files.txt
"`nFound $($unique.Count) unique changed files. Saved to changed_files.txt`n"
$unique
