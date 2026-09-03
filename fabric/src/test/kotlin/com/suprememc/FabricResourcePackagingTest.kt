package com.suprememc

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

class FabricResourcePackagingTest {
    @Test
    fun packagesGeneratedAndSharedAquamarineResources() {
        listOf(
            "data/suprememc/recipe/aquamarine_block.json",
            "data/suprememc/worldgen/configured_feature/aquamarine_ore.json",
            "assets/suprememc/equipment/aquamarine.json",
            "assets/suprememc/textures/item/aquamarine.png",
            "assets/suprememc/textures/entity/equipment/humanoid/aquamarine.png"
        ).forEach(::assertResourceExists)
    }

    private fun assertResourceExists(relativePath: String) {
        assertTrue("Fabric did not package: $relativePath") { Files.isRegularFile(processedResources.resolve(relativePath)) }
    }

    private companion object {
        val processedResources: Path = Path.of("build", "resources", "main")
    }
}