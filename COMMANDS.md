# SupremeMC Command Reference

This document lists all useful Gradle commands for developing, running, building, and generating data for the SupremeMC mod across Fabric and NeoForge.

> **Note:** Run all commands from the project directory (`SupremeMC-26.2-Mod`). On Windows PowerShell, use `.\gradlew`. On Linux/macOS, use `./gradlew`.

---

## 🚀 Running the Game (Development Environment)

### Fabric
- **Run Fabric Client:**
  ```powershell
  .\gradlew :fabric:runClient
  ```
- **Run Fabric Dedicated Server:**
  ```powershell
  .\gradlew :fabric:runServer
  ```

### NeoForge
- **Run NeoForge Client:**
  ```powershell
  .\gradlew :neoforge:runClient
  ```
- **Run NeoForge Dedicated Server:**
  ```powershell
  .\gradlew :neoforge:runServer
  ```

---

## 🛠️ Data Generation & JSON Migrations

Data generators allow you to automatically generate recipes, loot tables, blockstates, models, tags, and language JSON files.

- **Run NeoForge Data Generator (JSON / Data Generation):**
  ```powershell
  .\gradlew :neoforge:runData
  ```

---

## 📦 Building & Packaging (Release Jars)

- **Build Everything (Common, Fabric, & NeoForge Jars):**
  ```powershell
  .\gradlew build
  ```
  *Built `.jar` files will be output to `fabric/build/libs/` and `neoforge/build/libs/`.*

- **Build Fabric Mod Only:**
  ```powershell
  .\gradlew :fabric:build
  ```

- **Build NeoForge Mod Only:**
  ```powershell
  .\gradlew :neoforge:build
  ```

- **Clean Build Directories (Reset cached builds):**
  ```powershell
  .\gradlew clean
  ```

---

## ⚡ Decompiling & VS Code Setup

- **Generate Decompiled Minecraft Sources (Fabric Loom):**
  ```powershell
  .\gradlew genSources
  ```

- **Generate VS Code Configurations:**
  ```powershell
  .\gradlew vscode
  ```

---

## 🧪 Testing & Local Publishing

- **Run All Verification Checks & Tests:**
  ```powershell
  .\gradlew check
  ```

- **Publish to Local Maven Cache (`~/.m2/`):**
  ```powershell
  .\gradlew publishToMavenLocal
  ```
