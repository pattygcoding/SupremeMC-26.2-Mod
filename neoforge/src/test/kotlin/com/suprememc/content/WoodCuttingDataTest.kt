package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WoodCuttingDataTest : GeneratedDataTestSupport() {

    @Test
    fun standardWoodsGenerateExpectedStonecuttingRecipes() {
        listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "pale_oak").forEach { woodType ->
            val plankRecipe = readJson("data/suprememc/recipe/${woodType}_stairs_from_${woodType}_planks_stonecutting.json")
            assertEquals("minecraft:stonecutting", plankRecipe.get("type").asString)
            assertEquals("minecraft:${woodType}_planks", plankRecipe.get("ingredient").asString)
            assertEquals("minecraft:${woodType}_stairs", plankRecipe.getAsJsonObject("result").get("id").asString)
            assertEquals(1, plankRecipe.getAsJsonObject("result").get("count").asInt)

            val boatRecipe = readJson("data/suprememc/recipe/${woodType}_boat_from_${woodType}_planks_stonecutting.json")
            assertEquals("minecraft:${woodType}_boat", boatRecipe.getAsJsonObject("result").get("id").asString)
            assertEquals(1, boatRecipe.getAsJsonObject("result").get("count").asInt)

            val slabRecipe = readJson("data/suprememc/recipe/${woodType}_slab_from_${woodType}_planks_stonecutting.json")
            assertEquals("minecraft:${woodType}_slab", slabRecipe.getAsJsonObject("result").get("id").asString)
            assertEquals(2, slabRecipe.getAsJsonObject("result").get("count").asInt)

            val logToPlanks = readJson("data/suprememc/recipe/${woodType}_planks_from_${woodType}_log_stonecutting.json")
            assertEquals("minecraft:${woodType}_log", logToPlanks.get("ingredient").asString)
            assertEquals("minecraft:${woodType}_planks", logToPlanks.getAsJsonObject("result").get("id").asString)
            assertEquals(4, logToPlanks.getAsJsonObject("result").get("count").asInt)

            val logToStripped = readJson("data/suprememc/recipe/stripped_${woodType}_log_from_${woodType}_log_stonecutting.json")
            assertEquals("minecraft:stripped_${woodType}_log", logToStripped.getAsJsonObject("result").get("id").asString)
            assertEquals(1, logToStripped.getAsJsonObject("result").get("count").asInt)
        }
    }

    @Test
    fun netherFungiGenerateStemHyphaeAndPlankRecipesWithoutBoats() {
        listOf("crimson", "warped").forEach { woodType ->
            // Planks recipes
            val stairsRecipe = readJson("data/suprememc/recipe/${woodType}_stairs_from_${woodType}_planks_stonecutting.json")
            assertEquals("minecraft:stonecutting", stairsRecipe.get("type").asString)
            assertEquals("minecraft:${woodType}_planks", stairsRecipe.get("ingredient").asString)
            assertEquals("minecraft:${woodType}_stairs", stairsRecipe.getAsJsonObject("result").get("id").asString)
            assertEquals(1, stairsRecipe.getAsJsonObject("result").get("count").asInt)

            val slabRecipe = readJson("data/suprememc/recipe/${woodType}_slab_from_${woodType}_planks_stonecutting.json")
            assertEquals(2, slabRecipe.getAsJsonObject("result").get("count").asInt)

            val chestRecipe = readJson("data/suprememc/recipe/chest_from_${woodType}_planks_stonecutting.json")
            assertEquals("minecraft:chest", chestRecipe.getAsJsonObject("result").get("id").asString)
            assertEquals(1, chestRecipe.getAsJsonObject("result").get("count").asInt)

            // Verify no boat recipes
            val boatPath = generatedResources.resolve("data/suprememc/recipe/${woodType}_boat_from_${woodType}_planks_stonecutting.json")
            assertFalse(java.nio.file.Files.exists(boatPath), "Nether fungi should not have boat recipes")

            // Stem/hyphae recipes
            val stemToPlanks = readJson("data/suprememc/recipe/${woodType}_planks_from_${woodType}_stem_stonecutting.json")
            assertEquals("minecraft:${woodType}_stem", stemToPlanks.get("ingredient").asString)
            assertEquals("minecraft:${woodType}_planks", stemToPlanks.getAsJsonObject("result").get("id").asString)
            assertEquals(4, stemToPlanks.getAsJsonObject("result").get("count").asInt)

            val stemToStripped = readJson("data/suprememc/recipe/stripped_${woodType}_stem_from_${woodType}_stem_stonecutting.json")
            assertEquals("minecraft:stripped_${woodType}_stem", stemToStripped.getAsJsonObject("result").get("id").asString)
            assertEquals(1, stemToStripped.getAsJsonObject("result").get("count").asInt)

            val hyphaeToPlanks = readJson("data/suprememc/recipe/${woodType}_planks_from_${woodType}_hyphae_stonecutting.json")
            assertEquals("minecraft:${woodType}_hyphae", hyphaeToPlanks.get("ingredient").asString)
            assertEquals("minecraft:${woodType}_planks", hyphaeToPlanks.getAsJsonObject("result").get("id").asString)
            assertEquals(4, hyphaeToPlanks.getAsJsonObject("result").get("count").asInt)

            val hyphaeToStripped = readJson("data/suprememc/recipe/stripped_${woodType}_hyphae_from_${woodType}_hyphae_stonecutting.json")
            assertEquals("minecraft:stripped_${woodType}_hyphae", hyphaeToStripped.getAsJsonObject("result").get("id").asString)
            assertEquals(1, hyphaeToStripped.getAsJsonObject("result").get("count").asInt)

            val strippedStemToPlanks = readJson("data/suprememc/recipe/${woodType}_planks_from_stripped_${woodType}_stem_stonecutting.json")
            assertEquals(4, strippedStemToPlanks.getAsJsonObject("result").get("count").asInt)

            val strippedHyphaeToPlanks = readJson("data/suprememc/recipe/${woodType}_planks_from_stripped_${woodType}_hyphae_stonecutting.json")
            assertEquals(4, strippedHyphaeToPlanks.getAsJsonObject("result").get("count").asInt)
        }
    }

    @Test
    fun bambooGeneratesBlockRaftAndMosaicRecipes() {
        // Bamboo block yields 2 planks
        val blockToPlanks = readJson("data/suprememc/recipe/bamboo_planks_from_bamboo_block_stonecutting.json")
        assertEquals("minecraft:bamboo_block", blockToPlanks.get("ingredient").asString)
        assertEquals("minecraft:bamboo_planks", blockToPlanks.getAsJsonObject("result").get("id").asString)
        assertEquals(2, blockToPlanks.getAsJsonObject("result").get("count").asInt)

        val blockToStripped = readJson("data/suprememc/recipe/stripped_bamboo_block_from_bamboo_block_stonecutting.json")
        assertEquals("minecraft:stripped_bamboo_block", blockToStripped.getAsJsonObject("result").get("id").asString)
        assertEquals(1, blockToStripped.getAsJsonObject("result").get("count").asInt)

        val strippedToPlanks = readJson("data/suprememc/recipe/bamboo_planks_from_stripped_bamboo_block_stonecutting.json")
        assertEquals(2, strippedToPlanks.getAsJsonObject("result").get("count").asInt)

        // Raft instead of boat
        val raftRecipe = readJson("data/suprememc/recipe/bamboo_raft_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:bamboo_planks", raftRecipe.get("ingredient").asString)
        assertEquals("minecraft:bamboo_raft", raftRecipe.getAsJsonObject("result").get("id").asString)
        assertEquals(1, raftRecipe.getAsJsonObject("result").get("count").asInt)

        val boatPath = generatedResources.resolve("data/suprememc/recipe/bamboo_boat_from_bamboo_planks_stonecutting.json")
        assertFalse(java.nio.file.Files.exists(boatPath), "Bamboo should use raft, not boat")

        // Mosaic from planks
        val mosaicFromPlanks = readJson("data/suprememc/recipe/bamboo_mosaic_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:bamboo_planks", mosaicFromPlanks.get("ingredient").asString)
        assertEquals("minecraft:bamboo_mosaic", mosaicFromPlanks.getAsJsonObject("result").get("id").asString)
        assertEquals(1, mosaicFromPlanks.getAsJsonObject("result").get("count").asInt)

        val mosaicStairsFromPlanks = readJson("data/suprememc/recipe/bamboo_mosaic_stairs_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:bamboo_mosaic_stairs", mosaicStairsFromPlanks.getAsJsonObject("result").get("id").asString)
        assertEquals(1, mosaicStairsFromPlanks.getAsJsonObject("result").get("count").asInt)

        val mosaicSlabFromPlanks = readJson("data/suprememc/recipe/bamboo_mosaic_slab_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:bamboo_mosaic_slab", mosaicSlabFromPlanks.getAsJsonObject("result").get("id").asString)
        assertEquals(2, mosaicSlabFromPlanks.getAsJsonObject("result").get("count").asInt)

        // Mosaic from mosaic
        val mosaicStairsFromMosaic = readJson("data/suprememc/recipe/bamboo_mosaic_stairs_from_bamboo_mosaic_stonecutting.json")
        assertEquals("minecraft:bamboo_mosaic", mosaicStairsFromMosaic.get("ingredient").asString)
        assertEquals("minecraft:bamboo_mosaic_stairs", mosaicStairsFromMosaic.getAsJsonObject("result").get("id").asString)
        assertEquals(1, mosaicStairsFromMosaic.getAsJsonObject("result").get("count").asInt)

        val mosaicSlabFromMosaic = readJson("data/suprememc/recipe/bamboo_mosaic_slab_from_bamboo_mosaic_stonecutting.json")
        assertEquals("minecraft:bamboo_mosaic", mosaicSlabFromMosaic.get("ingredient").asString)
        assertEquals("minecraft:bamboo_mosaic_slab", mosaicSlabFromMosaic.getAsJsonObject("result").get("id").asString)
        assertEquals(2, mosaicSlabFromMosaic.getAsJsonObject("result").get("count").asInt)
    }

    @Test
    fun recipeAdvancementsGeneratedInCorrectCategories() {
        // building_blocks
        val stairsAdv = readJson("data/suprememc/advancement/recipes/building_blocks/oak_stairs_from_oak_planks_stonecutting.json")
        assertEquals("minecraft:recipes/root", stairsAdv.get("parent").asString)
        val criteria = stairsAdv.getAsJsonObject("criteria")
        assertTrue(criteria.has("has_oak_planks"))
        assertTrue(criteria.has("has_the_recipe"))
        assertEquals(
            "suprememc:oak_stairs_from_oak_planks_stonecutting",
            stairsAdv.getAsJsonObject("rewards").getAsJsonArray("recipes").first().asString
        )

        // decorations
        val fenceAdv = readJson("data/suprememc/advancement/recipes/decorations/crimson_fence_from_crimson_planks_stonecutting.json")
        assertEquals("minecraft:recipes/root", fenceAdv.get("parent").asString)

        // redstone
        val doorAdv = readJson("data/suprememc/advancement/recipes/redstone/bamboo_door_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:recipes/root", doorAdv.get("parent").asString)

        // transportation
        val raftAdv = readJson("data/suprememc/advancement/recipes/transportation/bamboo_raft_from_bamboo_planks_stonecutting.json")
        assertEquals("minecraft:recipes/root", raftAdv.get("parent").asString)

        // misc
        val bowlAdv = readJson("data/suprememc/advancement/recipes/misc/bowl_from_warped_planks_stonecutting.json")
        assertEquals("minecraft:recipes/root", bowlAdv.get("parent").asString)
    }
}
