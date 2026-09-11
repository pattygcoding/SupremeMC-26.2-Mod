package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeachGrassDataTest : GeneratedDataTestSupport() {
    @Test
    fun configuredFeaturesPlaceTheCorrectBeachGrassStates() {
        val shortGrass = readJson("data/suprememc/worldgen/configured_feature/beach_grass.json")
        assertEquals("minecraft:simple_block", shortGrass.get("type").asString)
        assertEquals("suprememc:beach_grass", shortGrass.getAsJsonObject("config").getAsJsonObject("to_place").getAsJsonObject("state").get("Name").asString)

        val tallGrass = readJson("data/suprememc/worldgen/configured_feature/tall_beach_grass.json")
        val tallState = tallGrass.getAsJsonObject("config").getAsJsonObject("to_place").getAsJsonObject("state")
        assertEquals("suprememc:tall_beach_grass", tallState.get("Name").asString)
        assertEquals("lower", tallState.getAsJsonObject("Properties").get("half").asString)
    }

    @Test
    fun patchPlacementsMirrorVanillaGrassAndRequireSandBelowAir() {
        val shortPlacement = readJson("data/suprememc/worldgen/placed_feature/beach_grass.json").getAsJsonArray("placement").map { it.asJsonObject }
        assertEquals("minecraft:noise_threshold_count", shortPlacement.first().get("type").asString)
        assertEquals("WORLD_SURFACE_WG", shortPlacement.first { it.get("type").asString == "minecraft:heightmap" }.get("heightmap").asString)

        val tallPlacement = readJson("data/suprememc/worldgen/placed_feature/tall_beach_grass.json").getAsJsonArray("placement").map { it.asJsonObject }
        assertEquals(5, tallPlacement.first().get("chance").asInt)
        assertEquals("MOTION_BLOCKING", tallPlacement.first { it.get("type").asString == "minecraft:heightmap" }.get("heightmap").asString)

        listOf(shortPlacement, tallPlacement).forEach { placement ->
            val predicate = placement.last().getAsJsonObject("predicate")
            val predicates = predicate.getAsJsonArray("predicates").map { it.asJsonObject }
            val substrate = predicates.first { it.get("type").asString == "minecraft:matching_blocks" }
            assertEquals(listOf("minecraft:sand", "minecraft:red_sand", "minecraft:suspicious_sand"), substrate.getAsJsonArray("blocks").map { it.asString })
            assertEquals(listOf(0, -1, 0), substrate.getAsJsonArray("offset").map { it.asInt })
            assertTrue(predicates.any { it.get("type").asString == "minecraft:matching_block_tag" && it.get("tag").asString == "minecraft:air" })
        }
    }

    @Test
    fun biomeModifiersTargetBeachesAndCays() {
        listOf("add_beach_grass" to "beach_grass", "add_tall_beach_grass" to "tall_beach_grass").forEach { (file, feature) ->
            val modifier = readJson("data/suprememc/neoforge/biome_modifier/$file.json")
            assertEquals("neoforge:add_features", modifier.get("type").asString)
            assertEquals(listOf("#minecraft:is_beach"), modifier.getAsJsonArray("biomes").map { it.asString })
            assertEquals("suprememc:$feature", modifier.get("features").asString)
            assertEquals("vegetal_decoration", modifier.get("step").asString)
        }
    }
}