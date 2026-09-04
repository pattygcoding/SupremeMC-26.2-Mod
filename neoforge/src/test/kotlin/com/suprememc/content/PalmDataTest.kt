package com.suprememc.content

import com.google.gson.JsonParser
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PalmDataTest : GeneratedDataTestSupport() {
    private val recipes = listOf(
        "palm_planks", "palm_log_to_wood", "stripped_palm_log_to_stripped_palm_wood", "palm_sticks",
        "palm_slab", "palm_stairs", "palm_fence", "palm_fence_gate", "palm_door", "palm_trapdoor",
        "palm_pressure_plate", "palm_button", "palm_sign", "palm_boat", "palm_chest_boat", "palm_hanging_sign"
    )

    private val advancementPaths = listOf(
        "building/palm_planks", "building/palm_log_to_wood", "building/stripped_palm_log_to_stripped_palm_wood",
        "misc/palm_sticks", "building/palm_slab", "building/palm_stairs", "building/palm_fence",
        "building/palm_fence_gate", "redstone/palm_door", "redstone/palm_trapdoor", "redstone/palm_pressure_plate",
        "redstone/palm_button", "misc/palm_sign", "misc/palm_boat", "misc/palm_chest_boat", "misc/palm_hanging_sign"
    )

    @Test
    fun generatesPalmWoodsetResources() {
        listOf("assets/suprememc/blockstates/palm_log.json", "assets/suprememc/models/block/palm_log.json", "assets/suprememc/models/block/palm_planks.json", "assets/suprememc/models/block/stripped_palm_log.json", "assets/suprememc/models/item/palm_planks.json", "assets/suprememc/models/item/palm_boat.json", "assets/suprememc/items/palm_planks.json", "assets/suprememc/items/palm_sign.json", "data/suprememc/loot_table/blocks/palm_leaves.json", "data/minecraft/tags/block/logs.json", "data/minecraft/tags/item/logs.json", "data/minecraft/tags/item/leaves.json", "data/minecraft/tags/item/saplings.json").forEach(::assertResourceExists)
        recipes.map { "data/suprememc/recipe/$it.json" }.forEach(::assertResourceExists)
        advancementPaths.map { "data/suprememc/advancement/recipes/$it.json" }.forEach(::assertResourceExists)
        assertResourceExists("assets/suprememc/models/block/palm_wood.json")
        assertResourceExists("assets/suprememc/models/block/stripped_palm_wood.json")
        assertTrue(readJson("assets/suprememc/blockstates/palm_log.json").has("variants"))
        assertEquals("suprememc:palm_sign", readJson("data/suprememc/recipe/palm_sign.json").getAsJsonObject("result").get("id").asString)
    }

    @Test
    fun palmLeavesAndSaplingsAreCompostable() {
        assertEquals("suprememc:palm_leaves", readJson("data/minecraft/tags/item/leaves.json").getAsJsonArray("values").single().asString)
        assertEquals("suprememc:palm_sapling", readJson("data/minecraft/tags/item/saplings.json").getAsJsonArray("values").single().asString)
    }

    @Test
    fun palmSlabDropsTwoWhenDouble() {
        val loot = readJson("data/suprememc/loot_table/blocks/palm_slab.json")
        val entry = loot.getAsJsonArray("pools").get(0).asJsonObject.getAsJsonArray("entries").get(0).asJsonObject
        assertEquals("suprememc:palm_slab", entry.get("name").asString)
        val setCount = entry.getAsJsonArray("functions").map { it.asJsonObject }.first { it.get("function").asString == "minecraft:set_count" }
        assertEquals(2.0, setCount.get("count").asDouble)
        val condition = setCount.getAsJsonArray("conditions").get(0).asJsonObject
        assertEquals("suprememc:palm_slab", condition.get("block").asString)
        assertEquals("double", condition.getAsJsonObject("properties").get("type").asString)
    }

    @Test
    fun palmWoodsetJoinsVanillaWoodFamilyTagsForAxeMineability() {
        assertTagContains("data/minecraft/tags/block/logs.json", "suprememc:palm_log", "suprememc:stripped_palm_log", "suprememc:palm_wood", "suprememc:stripped_palm_wood")
        assertTagContains("data/minecraft/tags/item/logs.json", "suprememc:palm_log", "suprememc:stripped_palm_log", "suprememc:palm_wood", "suprememc:stripped_palm_wood")
        assertTagContains("data/minecraft/tags/block/planks.json", "suprememc:palm_planks")
        assertTagContains("data/minecraft/tags/block/wooden_slabs.json", "suprememc:palm_slab")
        assertTagContains("data/minecraft/tags/block/wooden_stairs.json", "suprememc:palm_stairs")
        assertTagContains("data/minecraft/tags/block/wooden_fences.json", "suprememc:palm_fence")
        assertTagContains("data/minecraft/tags/block/fence_gates.json", "suprememc:palm_fence_gate")
        assertTagContains("data/minecraft/tags/block/wooden_doors.json", "suprememc:palm_door")
        assertTagContains("data/minecraft/tags/block/wooden_trapdoors.json", "suprememc:palm_trapdoor")
        assertTagContains("data/minecraft/tags/block/wooden_pressure_plates.json", "suprememc:palm_pressure_plate")
        assertTagContains("data/minecraft/tags/block/wooden_buttons.json", "suprememc:palm_button")
        assertTagContains("data/minecraft/tags/block/signs.json", "suprememc:palm_sign", "suprememc:palm_wall_sign")
        assertTagContains("data/minecraft/tags/block/all_hanging_signs.json", "suprememc:palm_hanging_sign", "suprememc:palm_wall_hanging_sign")
    }

    @Test
    fun palmWoodUsesLogSideTexturesOnAllFaces() {
        val palmWood = readJson("assets/suprememc/models/block/palm_wood.json")
        val strippedPalmWood = readJson("assets/suprememc/models/block/stripped_palm_wood.json")
        assertEquals("minecraft:block/cube_all", palmWood.get("parent").asString)
        assertEquals("suprememc:block/palm_log", palmWood.getAsJsonObject("textures").get("all").asString)
        assertEquals("minecraft:block/cube_all", strippedPalmWood.get("parent").asString)
        assertEquals("suprememc:block/stripped_palm_log", strippedPalmWood.getAsJsonObject("textures").get("all").asString)
    }

    @Test
    fun palmTreeUsesTallStraightTrunkAndCrown() {
        val palmTreePath = Path.of("..", "common", "src", "main", "resources", "data", "suprememc", "worldgen", "configured_feature", "palm.json")
        val palmTree = JsonParser.parseString(Files.readString(palmTreePath)).asJsonObject
        val config = palmTree.getAsJsonObject("config")
        val trunkPlacer = config.getAsJsonObject("trunk_placer")
        val foliagePlacer = config.getAsJsonObject("foliage_placer")

        assertEquals("suprememc:palm_trunk_placer", trunkPlacer.get("type").asString)
        assertEquals("suprememc:palm_foliage_placer", foliagePlacer.get("type").asString)
        assertEquals(10, trunkPlacer.get("base_height").asInt)
        assertEquals(7, foliagePlacer.get("frond_count").asInt)
    }

    @Test
    fun palmRecipesHaveExpectedOutputsAndAdvancementRewards() {
        val expectedOutputs = mapOf(
            "palm_planks" to "suprememc:palm_planks",
            "palm_log_to_wood" to "suprememc:palm_wood",
            "stripped_palm_log_to_stripped_palm_wood" to "suprememc:stripped_palm_wood",
            "palm_sticks" to "minecraft:stick",
            "palm_slab" to "suprememc:palm_slab",
            "palm_stairs" to "suprememc:palm_stairs",
            "palm_fence" to "suprememc:palm_fence",
            "palm_fence_gate" to "suprememc:palm_fence_gate",
            "palm_door" to "suprememc:palm_door",
            "palm_trapdoor" to "suprememc:palm_trapdoor",
            "palm_pressure_plate" to "suprememc:palm_pressure_plate",
            "palm_button" to "suprememc:palm_button",
            "palm_sign" to "suprememc:palm_sign",
            "palm_boat" to "suprememc:palm_boat",
            "palm_chest_boat" to "suprememc:palm_chest_boat",
            "palm_hanging_sign" to "suprememc:palm_hanging_sign"
        )
        expectedOutputs.forEach { (recipe, output) ->
            assertEquals(output, readJson("data/suprememc/recipe/$recipe.json").getAsJsonObject("result").get("id").asString)
        }
        advancementPaths.forEach { path ->
            val recipe = path.substringAfterLast('/')
            val advancement = readJson("data/suprememc/advancement/recipes/$path.json")
            assertTrue(advancement.getAsJsonObject("criteria").has("has_the_recipe"))
            assertEquals("suprememc:$recipe", advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").get(0).asString)
        }
    }

    @Test
    fun doorRecipesGiveCountOfTwo() {
        val woodenDoors = listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "pale_oak", "bamboo", "crimson", "warped")
        val vanillaDoors = woodenDoors + listOf("iron", "copper")
        vanillaDoors.forEach { wood ->
            val recipe = readJson("data/minecraft/recipe/${wood}_door.json")
            assertEquals("minecraft:${wood}_door", recipe.getAsJsonObject("result").get("id").asString)
            assertEquals(2, recipe.getAsJsonObject("result").get("count").asInt)
        }
        val palmDoorRecipe = readJson("data/suprememc/recipe/palm_door.json")
        assertEquals("suprememc:palm_door", palmDoorRecipe.getAsJsonObject("result").get("id").asString)
        assertEquals(2, palmDoorRecipe.getAsJsonObject("result").get("count").asInt)
    }

    @Test
    fun palmTreesGenerateOnEveryWarmBeachAndHalfOfTemperateBeaches() {
        val warm = readJson("data/suprememc/worldgen/placed_feature/palm_trees_warm.json")
        val temperate = readJson("data/suprememc/worldgen/placed_feature/palm_trees_temperate.json")
        assertEquals("suprememc:palm_coconut", warm.get("feature").asString)
        assertEquals("suprememc:palm", temperate.get("feature").asString)

        val warmPlacement = warm.getAsJsonArray("placement").map { it.asJsonObject }
        val temperatePlacement = temperate.getAsJsonArray("placement").map { it.asJsonObject }

        // Warm beaches: no rarity filter, so every eligible chunk rolls palms, at 1-3 trees per chunk.
        assertTrue(warmPlacement.none { it.get("type").asString == "minecraft:rarity_filter" })
        val warmCount = warmPlacement.first { it.get("type").asString == "minecraft:count" }.getAsJsonObject("count")
        assertEquals("minecraft:uniform", warmCount.get("type").asString)
        assertEquals(1, warmCount.get("min_inclusive").asInt)
        assertEquals(3, warmCount.get("max_inclusive").asInt)

        // Moderate beaches: an explicit 50% rarity rule and only one palm per roll, so they stay sparser than warm beaches.
        assertEquals(2, temperatePlacement.first { it.get("type").asString == "minecraft:rarity_filter" }.get("chance").asInt)
        assertEquals(1, temperatePlacement.first { it.get("type").asString == "minecraft:count" }.get("count").asInt)

        // Climate tiers key off the surface biome temperature: warm >= 0.8, moderate in [0.3, 0.8); colder coasts get no palms.
        val warmClimate = warmPlacement.first { it.get("type").asString == "suprememc:biome_temperature" }
        assertEquals(0.8F, warmClimate.get("min_temperature").asFloat)
        assertFalse(warmClimate.has("max_temperature"))
        val temperateClimate = temperatePlacement.first { it.get("type").asString == "suprememc:biome_temperature" }
        assertEquals(0.3F, temperateClimate.get("min_temperature").asFloat)
        assertEquals(0.8F, temperateClimate.get("max_temperature").asFloat)
    }

    @Test
    fun warmClimatePalmTreesBearCoconutsButSaplingGrownPalmsDoNot() {
        val featureDir = Path.of("..", "common", "src", "main", "resources", "data", "suprememc", "worldgen", "configured_feature")
        val coconutPalm = JsonParser.parseString(Files.readString(featureDir.resolve("palm_coconut.json"))).asJsonObject
        val decorators = coconutPalm.getAsJsonObject("config").getAsJsonArray("decorators").map { it.asJsonObject }
        val coconut = decorators.single { it.get("type").asString == "suprememc:palm_coconut" }
        assertEquals(1.0F, coconut.get("probability").asFloat)
        assertEquals("suprememc:palm_trunk_placer", coconutPalm.getAsJsonObject("config").getAsJsonObject("trunk_placer").get("type").asString)

        // The plain palm feature backs saplings (and moderate-climate beaches), so it must stay coconut-free.
        val plainPalm = JsonParser.parseString(Files.readString(featureDir.resolve("palm.json"))).asJsonObject
        assertTrue(plainPalm.getAsJsonObject("config").getAsJsonArray("decorators").isEmpty)
    }

    @Test
    fun palmBeachPlacementRequiresSurfaceSandTheSaplingSurvivesOn() {
        listOf("palm_trees_warm", "palm_trees_temperate").forEach { id ->
            val placement = readJson("data/suprememc/worldgen/placed_feature/$id.json").getAsJsonArray("placement").map { it.asJsonObject }
            val substrate = placement.first { it.get("type").asString == "minecraft:block_predicate_filter" }.getAsJsonObject("predicate")
            assertEquals("minecraft:all_of", substrate.get("type").asString)
            val terms = substrate.getAsJsonArray("predicates").map { it.asJsonObject }
            val sand = terms.first { it.get("type").asString == "minecraft:matching_blocks" }
            assertEquals(listOf("minecraft:sand", "minecraft:red_sand"), sand.getAsJsonArray("blocks").map { it.asString })
            assertEquals(listOf(0, -1, 0), sand.getAsJsonArray("offset").map { it.asInt })
            val survives = terms.first { it.get("type").asString == "minecraft:would_survive" }
            assertEquals("suprememc:palm_sapling", survives.getAsJsonObject("state").get("Name").asString)

            // Surface placement only: never underwater, never in caves, snapped to the surface heightmap.
            assertEquals(0, placement.first { it.get("type").asString == "minecraft:surface_water_depth_filter" }.get("max_water_depth").asInt)
            assertEquals("OCEAN_FLOOR", placement.first { it.get("type").asString == "minecraft:heightmap" }.get("heightmap").asString)
            assertEquals("minecraft:biome", placement.last().get("type").asString)
        }
    }

    @Test
    fun palmBeachBiomeModifiersTargetTheBeachTagAtVegetalDecoration() {
        mapOf(
            "add_palm_trees_warm" to "palm_trees_warm",
            "add_palm_trees_temperate" to "palm_trees_temperate",
        ).forEach { (file, feature) ->
            val modifier = readJson("data/suprememc/neoforge/biome_modifier/$file.json")
            assertEquals("neoforge:add_features", modifier.get("type").asString)
            assertEquals("#minecraft:is_beach", modifier.get("biomes").asString)
            assertEquals("suprememc:$feature", modifier.get("features").asString)
            assertEquals("vegetal_decoration", modifier.get("step").asString)
        }
    }
}