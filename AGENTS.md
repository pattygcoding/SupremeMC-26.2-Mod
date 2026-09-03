# AGENTS.md

This repository uses a Minecraft 26.2 multi-loader setup. When implementing new content, always check the local reference files under `.references/mc-26.2/` before importing or copying examples from older mappings.

## Source of truth for API lookup
- Use the merged Minecraft jar class-list and decompiled references in `.references/mc-26.2/`.
- Treat these files as the canonical lookup for MC 26.2 names, constructors, methods, and signatures.
- Do not assume compatibility with older Minecraft examples or stale stackoverflow/YouTube mapping patterns.

## Workflow
1. Check the relevant reference file for the specific class or package you need.
2. Confirm the current item/block/tool/armor API names in the 26.2 jar.
3. Implement against those current signatures.
4. Validate with the relevant Gradle build task.
5. Write new tests in Kotlin by default; use Java only when Kotlin cannot access a required API or toolchain feature.

## Reference folder
- `.references/mc-26.2/README.md`
- `.references/mc-26.2/<class-specific reference files>`
- `.tmp/mcjar.txt` for a generated class list snapshot of the merged MC jar

This is the preferred source for Java API reference while working in this repo.

## Autonomy on reference lookups
- Never ask the user for permission before running `jar tf`/`javap` commands against the merged Minecraft jar, or before searching/reading files under `.references/mc-26.2/`. Just run them.
- Prefer `grep_search`/`read_file` on the already-cached `.references/mc-26.2/class-list.txt` and `core-api.txt` first, since these avoid a terminal call entirely.
- When a needed class/signature isn't cached yet, run a single batched terminal command (one call covering all the classes you need, e.g. a loop over multiple `javap` targets) and save the output into `.references/mc-26.2/` for reuse, instead of asking the user to run it or issuing many small one-off commands.
- When writing new reference files with `javap`, write the output as UTF-8 text (e.g. `[System.IO.File]::WriteAllText($out, $text, [System.Text.Encoding]::UTF8)`), not PowerShell's default UTF-16, so the files can be read back as plain text.

## Content and data generation
- Add registered items and blocks to the loader-compatible creative inventory hook; do not leave new registered content hidden from players.
- Generate JSON resources from Java `DataProvider` code. Do not hand-author generated models, blockstates, recipes, loot tables, tags, or language JSON under generated-resource output folders.
- After changing registered content or its provider, run `./gradlew :neoforge:runData --console=plain` and inspect the generated output before reporting the feature complete.
- For every implemented player-facing feature, update `FEATURES.md` with its current behavior and progression details.
- When an implementation adds, removes, or changes a required texture, update `TEXTURE_FILES.md` with the exact shared resource path and filename.
