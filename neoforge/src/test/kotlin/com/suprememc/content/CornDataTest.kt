package com.suprememc.content

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class CornDataTest : GeneratedDataTestSupport() {
    @Test
    fun cornPatchesGenerateSporadicallyAcrossPlainsVariants() {
        val configured = readJson("data/suprememc/worldgen/configured_feature/corn_patches.json")
        assertEquals("suprememc:corn_patch", configured.get("type").asString)

        val placements = readJson("data/suprememc/worldgen/placed_feature/corn_patches.json")
            .getAsJsonArray("placement").map { it.asJsonObject }
        assertTrue(placements.any { it.get("type").asString == "minecraft:rarity_filter" && it.get("chance").asInt == 5 })
        assertTrue(placements.any { it.get("type").asString == "minecraft:count" && it.get("count").asInt == 24 })
        assertTrue(placements.any { it.get("type").asString == "minecraft:random_offset" })

        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_corn_patches.json")
        assertEquals(listOf("minecraft:plains", "minecraft:sunflower_plains"), modifier.getAsJsonArray("biomes").map { it.asString })
        assertEquals("suprememc:corn_patches", modifier.get("features").asString)
        assertEquals("vegetal_decoration", modifier.get("step").asString)
    }
}