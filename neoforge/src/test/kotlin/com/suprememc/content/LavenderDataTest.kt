package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LavenderDataTest : GeneratedDataTestSupport() {
    @Test
    fun theEndDimensionUsesVanillaBiomeSource() {
        val file = java.nio.file.Path.of("..", "common", "src", "main", "resources", "data/minecraft/dimension/the_end.json")
        val dimension = com.google.gson.JsonParser.parseString(java.nio.file.Files.readString(file)).asJsonObject
        assertEquals("minecraft:the_end", dimension.get("type").asString)
        val generator = dimension.getAsJsonObject("generator")
        assertEquals("minecraft:noise", generator.get("type").asString)
        assertEquals("minecraft:end", generator.get("settings").asString)
        val biomeSource = generator.getAsJsonObject("biome_source")
        assertEquals("minecraft:the_end", biomeSource.get("type").asString)
    }

    @Test
    fun bonemealedLavenderFungusGrowsAHugeFungusLikeVanillaNetherFungi() {
        val feature = readJson("data/suprememc/worldgen/configured_feature/lavender_fungus_planted.json")
        assertEquals("minecraft:huge_fungus", feature.get("type").asString)

        val config = feature.getAsJsonObject("config")
        assertEquals("suprememc:lavender_stem", config.getAsJsonObject("stem_state").get("Name").asString)
        assertEquals("y", config.getAsJsonObject("stem_state").getAsJsonObject("Properties").get("axis").asString)
        assertEquals("suprememc:lavender_wart_block", config.getAsJsonObject("hat_state").get("Name").asString)
        assertEquals("minecraft:shroomlight", config.getAsJsonObject("decor_state").get("Name").asString)
        assertEquals("suprememc:lavender_endspar", config.getAsJsonObject("valid_base_block").get("Name").asString)
        assertTrue(config.get("planted").asBoolean)

        val replaceable = config.getAsJsonObject("replaceable_blocks")
        assertEquals("minecraft:matching_blocks", replaceable.get("type").asString)
        val blocks = replaceable.getAsJsonArray("blocks").map { it.asString }
        listOf(
            "suprememc:lavender_roots",
            "suprememc:lavender_fungus",
            "minecraft:oak_sapling",
            "minecraft:dandelion",
            "minecraft:warped_fungus",
            "minecraft:crimson_fungus",
        ).forEach { assertTrue("$it should be replaceable by the growing huge fungus") { it in blocks } }
    }

    @Test
    fun lavenderEndsparBonemealVegetationSpreadsRootsAndFungi() {
        val nyliumTag = readJson("data/minecraft/tags/block/nylium.json")
        assertTrue("suprememc:lavender_endspar" in nyliumTag.getAsJsonArray("values").map { it.asString })

        val feature = readJson("data/suprememc/worldgen/configured_feature/lavender_endspar_vegetation_bonemeal.json")
        assertEquals("minecraft:nether_forest_vegetation", feature.get("type").asString)
        val entries = feature.getAsJsonObject("config").getAsJsonObject("state_provider").getAsJsonArray("entries")
            .map { it.asJsonObject }
        val roots = entries.first { it.getAsJsonObject("data").get("Name").asString == "suprememc:lavender_roots" }
        val fungus = entries.first { it.getAsJsonObject("data").get("Name").asString == "suprememc:lavender_fungus" }
        assertTrue(roots.get("weight").asInt > fungus.get("weight").asInt)
    }

    @Test
    fun lavenderItemTagContainsOnlyRegisteredItemIds() {
        val tag = readJson("data/suprememc/tags/item/lavender_stems.json")
        val values = tag.getAsJsonArray("values").map { it.asString }

        assertTrue("suprememc:lavender_wall_sign" !in values)
        assertTrue("suprememc:lavender_wall_hanging_sign" !in values)
        assertTrue("suprememc:lavender_sign" in values)
        assertTrue("suprememc:lavender_hanging_sign" in values)
    }
}
