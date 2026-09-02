# SupremeMC (Minecraft 26.2 MultiLoader Mod)

SupremeMC is a multi-platform Minecraft mod template compiled for **Fabric** and **NeoForge** using a shared `common` module.

## Getting Started in VS Code

### Prerequisites
- **JDK 25** installed and configured as your default Java Development Kit.
- **VS Code** with the **Extension Pack for Java** and **Gradle for Java** extensions.

### Running & Building in VS Code
1. Open the repository root folder in VS Code.
2. Use **Terminal > Run Task...** to execute tasks directly, or run Gradle wrapper commands in terminal:
   - **Fabric Client:** `.\gradlew :fabric:runClient`
   - **NeoForge Client:** `.\gradlew :neoforge:runClient`
   - **NeoForge Data Generator:** `.\gradlew :neoforge:runData`
   - **Build All Jars:** `.\gradlew build`
3. Refer to [COMMANDS.md](COMMANDS.md) for a complete list of commands.

## Development Guide
When using this template the majority of your mod should be developed in the `common` project. The `common` project is compiled against the vanilla game and is used to hold code that is shared between the different loader-specific versions of your mod. The `common` project has no knowledge or access to ModLoader specific code, apis, or concepts. Code that requires something from a specific loader must be done through the project that is specific to that loader, such as the `fabric` or `neoforge` projects.

Loader specific projects such as the `fabric` and `neoforge` project are used to load the `common` project into the game. These projects also define code that is specific to that loader. Loader specific projects can access all the code in the `common` project. It is important to remember that the `common` project can not access code from loader specific projects.

## Removing Platforms and Loaders
While this template has support for many modloaders, new loaders may appear in the future, and existing loaders may become less relevant.

Removing loader specific projects is as easy as deleting the folder, and removing the `include("projectname")` line from the `settings.gradle` file.
For example if you wanted to remove support for `forge` you would follow the following steps:

1. Delete the subproject folder. For example, delete `MultiLoader-Template/forge`.
2. Remove the project from `settings.gradle`. For example, remove `include("forge")`. 
