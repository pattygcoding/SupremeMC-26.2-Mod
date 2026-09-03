package com.suprememc.content

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModContentDataTest {
    @Test
    fun generatesRequiredAquamarineProgressionResources() {
        listOf(
            "assets/suprememc/models/item/aquamarine.json",
            "assets/suprememc/models/block/aquamarine_ore.json",
            "assets/suprememc/blockstates/aquamarine_ore.json",
            "assets/suprememc/equipment/aquamarine.json",
            "data/suprememc/loot_table/blocks/aquamarine_ore.json",
            "data/suprememc/recipe/aquamarine_block.json",
            "data/suprememc/recipe/aquamarine_from_aquamarine_block.json",
            "data/suprememc/worldgen/configured_feature/aquamarine_ore.json",
            "data/suprememc/worldgen/placed_feature/aquamarine_ore.json",
            "data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json",
            "data/minecraft/tags/block/needs_iron_tool.json",
            "data/minecraft/tags/item/beacon_payment_items.json"
        ).forEach(::assertResourceExists)
    }

    @Test
    fun generatesMoistFarmlandAndAquamarineEquipmentModels() {
        val farmlandModel = readJson("assets/suprememc/models/block/wet_farmland.json")
        assertEquals("minecraft:block/farmland_moist", farmlandModel.get("parent").asString)
        assertFalse(farmlandModel.has("textures"))

        val layers = readJson("assets/suprememc/equipment/aquamarine.json").getAsJsonObject("layers")
        assertTrue(layers.has("humanoid"))
        assertTrue(layers.has("humanoid_baby"))
        assertTrue(layers.has("humanoid_leggings"))
        assertEquals("suprememc:aquamarine", layers.getAsJsonArray("humanoid").get(0).asJsonObject.get("texture").asString)
    }

    @Test
    fun generatesOceanOnlyAquamarineOrePlacement() {
        val config = readJson("data/suprememc/worldgen/configured_feature/aquamarine_ore.json").getAsJsonObject("config")
        assertEquals(4, config.get("size").asInt)
        assertEquals(0.5f, config.get("discard_chance_on_air_exposure").asFloat)
        assertEquals(3, config.getAsJsonArray("targets").size())

        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json")
        assertEquals("neoforge:add_features", modifier.get("type").asString)
        assertEquals("#minecraft:is_ocean", modifier.get("biomes").asString)
        assertEquals("suprememc:aquamarine_ore", modifier.get("features").asString)
        assertEquals("underground_ores", modifier.get("step").asString)
    }

    @Test
    fun generatesCorrectProgressionRecipesLootAndTags() {
        val compacting = readJson("data/suprememc/recipe/aquamarine_block.json")
        assertEquals("minecraft:crafting_shaped", compacting.get("type").asString)
        assertEquals("AAA", compacting.getAsJsonArray("pattern").get(0).asString)
        assertEquals("suprememc:aquamarine", compacting.getAsJsonObject("key").get("A").asString)
        assertEquals("suprememc:aquamarine_block", compacting.getAsJsonObject("result").get("id").asString)

        val decompacting = readJson("data/suprememc/recipe/aquamarine_from_aquamarine_block.json")
        assertEquals("minecraft:crafting_shapeless", decompacting.get("type").asString)
        assertEquals("suprememc:aquamarine_block", decompacting.getAsJsonArray("ingredients").get(0).asString)
        assertEquals(9, decompacting.getAsJsonObject("result").get("count").asInt)

        listOf("aquamarine_ore", "deepslate_aquamarine_ore").forEach { oreId ->
            val lootJson = readJson("data/suprememc/loot_table/blocks/$oreId.json").toString()
            assertTrue("minecraft:silk_touch" in lootJson)
            assertTrue("minecraft:fortune" in lootJson)
            assertTrue("minecraft:ore_drops" in lootJson)
            assertTrue("suprememc:aquamarine" in lootJson)
            assertTrue("suprememc:$oreId" in lootJson)

            assertCookingRecipe("aquamarine_from_smelting_$oreId", "minecraft:smelting", oreId)
            assertCookingRecipe("aquamarine_from_blasting_$oreId", "minecraft:blasting", oreId)
        }

        assertTagContains("data/minecraft/tags/block/needs_iron_tool.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/block/mineable/pickaxe.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/block/beacon_base_blocks.json", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/item/beacon_payment_items.json", "suprememc:aquamarine")
    }

    @Test
    fun generatedResourcesAreValidAndBackedByRequiredTextures() {
        Files.walk(generatedResources).use { paths ->
            paths.filter { it.toString().endsWith(".json") }.forEach { path ->
                assertTrue("Empty JSON file: $path") { Files.size(path) > 0 }
            }
        }

        val textures = Path.of("..", "common", "src", "main", "resources", "assets", "suprememc", "textures")
        listOf(
            "block/aquamarine_ore.png", "block/deepslate_aquamarine_ore.png", "block/aquamarine_block.png",
            "item/aquamarine.png", "item/aquamarine_pickaxe.png", "item/aquamarine_axe.png", "item/aquamarine_shovel.png", "item/aquamarine_hoe.png", "item/aquamarine_sword.png",
            "item/aquamarine_helmet.png", "item/aquamarine_chestplate.png", "item/aquamarine_leggings.png", "item/aquamarine_boots.png",
            "entity/equipment/humanoid/aquamarine.png", "entity/equipment/humanoid_baby/aquamarine.png", "entity/equipment/humanoid_leggings/aquamarine.png"
        ).forEach { texture ->
            val texturePath = textures.resolve(texture)
            assertTrue("Missing required texture: $texture") { Files.isRegularFile(texturePath) }
            assertTrue("Empty texture: $texture") { Files.size(texturePath) > 0 }
        }
    }

    private fun assertCookingRecipe(recipeId: String, type: String, ingredient: String) {
        val recipe = readJson("data/suprememc/recipe/$recipeId.json")
        assertEquals(type, recipe.get("type").asString)
        assertEquals("suprememc:$ingredient", recipe.get("ingredient").asString)
        assertEquals("suprememc:aquamarine", recipe.getAsJsonObject("result").get("id").asString)
        assertEquals(1.0f, recipe.get("experience").asFloat)
    }

    private fun assertTagContains(relativePath: String, vararg expectedValues: String) {
        val values = readJson(relativePath).getAsJsonArray("values").toString()
        expectedValues.forEach { expectedValue ->
            assertTrue("Missing tag value $expectedValue in $relativePath") { expectedValue in values }
        }
    }

    private fun assertResourceExists(relativePath: String) {
        assertTrue("Missing generated resource: $relativePath") { Files.isRegularFile(generatedResources.resolve(relativePath)) }
    }

    private fun readJson(relativePath: String): JsonObject {
        val path = generatedResources.resolve(relativePath)
        assertResourceExists(relativePath)
        return JsonParser.parseString(Files.readString(path)).asJsonObject
    }

    private companion object {
        val generatedResources: Path = Path.of("src", "generated", "resources")
    }
}