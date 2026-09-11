package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GlendstoneDataTest : GeneratedDataTestSupport() {
    @Test
    fun glendstoneGeneratesAsGlowstoneStyleClustersInEndBiomes() {
        val configured = readJson("data/suprememc/worldgen/configured_feature/glendstone_cluster.json")
        assertEquals("suprememc:glendstone_cluster", configured.get("type").asString)

        val regularPlacement = readJson("data/suprememc/worldgen/placed_feature/glendstone.json")
        assertEquals("suprememc:glendstone_cluster", regularPlacement.get("feature").asString)
        val regularModifiers = regularPlacement.getAsJsonArray("placement").map { it.asJsonObject }
        assertEquals("minecraft:count", regularModifiers[0].get("type").asString)
        assertEquals(10, regularModifiers[0].get("count").asInt)
        assertEquals("minecraft:height_range", regularModifiers[2].get("type").asString)
        assertEquals("minecraft:biome", regularModifiers.last().get("type").asString)

        val extraPlacement = readJson("data/suprememc/worldgen/placed_feature/glendstone_extra.json")
        assertEquals("suprememc:glendstone_cluster", extraPlacement.get("feature").asString)
        val count = extraPlacement.getAsJsonArray("placement")[0].asJsonObject.getAsJsonObject("count")
        assertEquals("minecraft:biased_to_bottom", count.get("type").asString)
        assertEquals(9, count.get("max_inclusive").asInt)

        listOf("add_glendstone.json", "add_glendstone_extra.json").forEach { fileName ->
            val modifier = readJson("data/suprememc/neoforge/biome_modifier/$fileName")
            assertEquals("neoforge:add_features", modifier.get("type").asString)
            assertEquals("underground_decoration", modifier.get("step").asString)
            val biomes = modifier.getAsJsonArray("biomes").map { it.asString }.toSet()
            assertTrue("#minecraft:is_end" in biomes)
            assertTrue("suprememc:lavender_barrens" in biomes)
            assertTrue("suprememc:lavender_midlands" in biomes)
            assertTrue("suprememc:lavender_highlands" in biomes)
        }

        val recipe = readJson("data/suprememc/recipe/glendstone.json")
        assertEquals("minecraft:crafting_shaped", recipe.get("type").asString)
        assertEquals(listOf("##", "##"), recipe.getAsJsonArray("pattern").map { it.asString })
        assertEquals("suprememc:glendstone_dust", recipe.getAsJsonObject("key").get("#").asString)
        assertEquals("suprememc:glendstone", recipe.getAsJsonObject("result").get("id").asString)

        val loot = readJson("data/suprememc/loot_table/blocks/glendstone.json")
        val alternatives = loot.getAsJsonArray("pools")[0].asJsonObject
            .getAsJsonArray("entries")[0].asJsonObject
        assertEquals("minecraft:alternatives", alternatives.get("type").asString)
        val children = alternatives.getAsJsonArray("children").map { it.asJsonObject }
        assertEquals("suprememc:glendstone", children[0].get("name").asString)
        assertEquals("suprememc:glendstone_dust", children[1].get("name").asString)
        val dustFunctions = children[1].getAsJsonArray("functions").map { it.asJsonObject }
        assertEquals("minecraft:set_count", dustFunctions[0].get("function").asString)
        assertEquals(2.0, dustFunctions[0].getAsJsonObject("count").get("min").asDouble)
        assertEquals(4.0, dustFunctions[0].getAsJsonObject("count").get("max").asDouble)
        assertEquals("minecraft:apply_bonus", dustFunctions[1].get("function").asString)
        assertEquals("minecraft:limit_count", dustFunctions[2].get("function").asString)
        assertEquals("minecraft:explosion_decay", dustFunctions[3].get("function").asString)
    }
}