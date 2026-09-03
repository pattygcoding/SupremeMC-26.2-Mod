package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AquamarineDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesRequiredAquamarineProgressionResources() {
        listOf("assets/suprememc/models/item/aquamarine.json", "assets/suprememc/models/block/aquamarine_ore.json", "assets/suprememc/blockstates/aquamarine_ore.json", "assets/suprememc/equipment/aquamarine.json", "assets/suprememc/items/aquamarine.json", "assets/suprememc/items/aquamarine_ore.json", "data/suprememc/loot_table/blocks/aquamarine_ore.json", "data/suprememc/recipe/aquamarine_block.json", "data/suprememc/recipe/aquamarine_from_aquamarine_block.json", "data/suprememc/worldgen/configured_feature/aquamarine_ore.json", "data/suprememc/worldgen/placed_feature/aquamarine_ore.json", "data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json", "data/minecraft/tags/block/needs_iron_tool.json", "data/minecraft/tags/item/beacon_payment_items.json").forEach(::assertResourceExists)
    }

    @Test
    fun generatesMoistFarmlandAndAquamarineEquipmentModels() {
        assertEquals("minecraft:block/farmland_moist", readJson("assets/suprememc/models/block/wet_farmland.json").get("parent").asString)
        val layers = readJson("assets/suprememc/equipment/aquamarine.json").getAsJsonObject("layers")
        assertTrue(layers.has("humanoid") && layers.has("humanoid_baby") && layers.has("humanoid_leggings"))
        assertEquals("suprememc:aquamarine", layers.getAsJsonArray("humanoid").get(0).asJsonObject.get("texture").asString)
    }

    @Test
    fun generatesOceanOnlyAquamarineOrePlacement() {
        val config = readJson("data/suprememc/worldgen/configured_feature/aquamarine_ore.json").getAsJsonObject("config")
        assertEquals(4, config.get("size").asInt)
        assertEquals(3, config.getAsJsonArray("targets").size())
        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json")
        assertEquals("#minecraft:is_ocean", modifier.get("biomes").asString)
        assertEquals("suprememc:aquamarine_ore", modifier.get("features").asString)
    }

    @Test
    fun generatesCorrectAquamarineRecipesLootAndTags() {
        val compacting = readJson("data/suprememc/recipe/aquamarine_block.json")
        assertEquals("suprememc:aquamarine", compacting.getAsJsonObject("key").get("A").asString)
        assertEquals("suprememc:aquamarine_block", compacting.getAsJsonObject("result").get("id").asString)
        assertEquals(9, readJson("data/suprememc/recipe/aquamarine_from_aquamarine_block.json").getAsJsonObject("result").get("count").asInt)
        listOf("aquamarine_ore", "deepslate_aquamarine_ore").forEach { ore ->
            val loot = readJson("data/suprememc/loot_table/blocks/$ore.json").toString()
            assertTrue("minecraft:silk_touch" in loot && "minecraft:fortune" in loot && "minecraft:ore_drops" in loot)
            assertTagContains("data/minecraft/tags/block/needs_iron_tool.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block")
        }
    }
}