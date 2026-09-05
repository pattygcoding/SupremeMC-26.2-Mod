package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AmberDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesRequiredAmberProgressionResources() {
        listOf(
            "assets/suprememc/models/item/amber.json",
            "assets/suprememc/models/block/amber_ore.json",
            "assets/suprememc/blockstates/amber_ore.json",
            "assets/suprememc/blockstates/amber_stairs.json",
            "assets/suprememc/blockstates/amber_slab.json",
            "assets/suprememc/models/block/amber_stairs.json",
            "assets/suprememc/models/block/amber_stairs_inner.json",
            "assets/suprememc/models/block/amber_stairs_outer.json",
            "assets/suprememc/models/block/amber_slab.json",
            "assets/suprememc/models/block/amber_slab_top.json",
            "assets/suprememc/models/block/amber_slab_double.json",
            "assets/suprememc/equipment/amber.json",
            "assets/suprememc/items/amber.json",
            "assets/suprememc/items/amber_ore.json",
            "data/suprememc/loot_table/blocks/amber_ore.json",
            "data/suprememc/recipe/amber_block.json",
            "data/suprememc/recipe/amber_from_amber_block.json",
            "data/suprememc/worldgen/configured_feature/amber_ore.json",
            "data/suprememc/worldgen/placed_feature/amber_ore.json",
            "data/suprememc/worldgen/placed_feature/amber_ore_pale_garden.json",
            "data/suprememc/neoforge/biome_modifier/add_amber_ore.json",
            "data/suprememc/neoforge/biome_modifier/add_amber_ore_pale_garden.json"
        ).forEach(::assertResourceExists)
    }

    @Test
    fun generatesRequestedAmberOreBiomesAndPaleGardenRate() {
        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_amber_ore.json")
        assertEquals(listOf("minecraft:forest", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:old_growth_birch_forest", "minecraft:dark_forest", "minecraft:grove"), modifier.getAsJsonArray("biomes").map { it.asString })
        assertEquals(7, oreCount("amber_ore"))
        val paleModifier = readJson("data/suprememc/neoforge/biome_modifier/add_amber_ore_pale_garden.json")
        assertEquals(listOf("minecraft:pale_garden"), paleModifier.getAsJsonArray("biomes").map { it.asString })
        assertEquals("suprememc:amber_ore_pale_garden", paleModifier.get("features").asString)
        assertEquals(14, oreCount("amber_ore_pale_garden"))
        val layers = readJson("assets/suprememc/equipment/amber.json").getAsJsonObject("layers")
        assertTrue(layers.has("humanoid") && layers.has("humanoid_baby") && layers.has("humanoid_leggings"))
        assertEquals("suprememc:amber", layers.getAsJsonArray("humanoid").get(0).asJsonObject.get("texture").asString)
    }

    private fun oreCount(id: String) = readJson("data/suprememc/worldgen/placed_feature/$id.json")
        .getAsJsonArray("placement").first { it.asJsonObject.get("type").asString == "minecraft:count" }
        .asJsonObject.get("count").asInt

    @Test
    fun generatesAmberRecipesLootAndTags() {
        val compacting = readJson("data/suprememc/recipe/amber_block.json")
        assertEquals("suprememc:amber", compacting.getAsJsonObject("key").get("A").asString)
        assertEquals("suprememc:amber_block", compacting.getAsJsonObject("result").get("id").asString)
        assertEquals(9, readJson("data/suprememc/recipe/amber_from_amber_block.json").getAsJsonObject("result").get("count").asInt)
        assertTagContains("data/minecraft/tags/block/needs_iron_tool.json", "suprememc:amber_ore", "suprememc:deepslate_amber_ore", "suprememc:amber_block")
        assertTagContains("data/minecraft/tags/item/beacon_payment_items.json", "suprememc:amber")
        listOf("amber_ore", "deepslate_amber_ore").forEach { ore ->
            val loot = readJson("data/suprememc/loot_table/blocks/$ore.json").toString()
            assertTrue("minecraft:silk_touch" in loot && "minecraft:fortune" in loot && "minecraft:ore_drops" in loot)
        }
    }
}