package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CottonDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesCottonBushStagesAndPlaceableCottonItem() {
        assertResourceExists("assets/suprememc/blockstates/cotton_bush.json")
        (0..3).forEach { age -> assertResourceExists("assets/suprememc/models/block/cotton_bush_stage$age.json") }
        assertEquals(
            "suprememc:item/cotton",
            readJson("assets/suprememc/items/cotton.json").getAsJsonObject("model").get("model").asString
        )
    }

    @Test
    fun cottonBushLootMatchesSweetBerryGrowthStages() {
        listOf("blocks/cotton_bush", "harvest/cotton_bush").forEach { path ->
            val pools = readJson("data/suprememc/loot_table/$path.json").getAsJsonArray("pools").map { it.asJsonObject }
            assertEquals(listOf("2", "3"), pools.map { pool ->
                pool.getAsJsonArray("conditions")[0].asJsonObject
                    .getAsJsonObject("properties").get("age").asString
            })
            assertTrue(pools.all { "suprememc:cotton" in it.toString() && "minecraft:fortune" in it.toString() })
        }
    }

    @Test
    fun cottonBushesGenerateOnlyInPlainsVariants() {
        val configured = readJson("data/suprememc/worldgen/configured_feature/cotton_bushes.json")
        assertEquals("minecraft:simple_block", configured.get("type").asString)
        assertEquals(
            "suprememc:cotton_bush",
            configured.getAsJsonObject("config").getAsJsonObject("to_place")
                .getAsJsonObject("state").get("Name").asString
        )

        val placed = readJson("data/suprememc/worldgen/placed_feature/cotton_bushes.json")
        assertEquals("suprememc:cotton_bushes", placed.get("feature").asString)
        val placements = placed.getAsJsonArray("placement").map { it.asJsonObject }
        // ~half as common as vanilla plains flower patches (see CottonDataProvider comment).
        assertTrue(placements.any { it.get("type").asString == "minecraft:rarity_filter" && it.get("chance").asInt == 4 })
        assertTrue(placements.any { it.get("type").asString == "minecraft:count" && it.get("count").asInt == 32 })
        assertTrue(placements.any { it.get("type").asString == "minecraft:random_offset" })
        assertTrue(placements.any {
            it.get("type").asString == "minecraft:block_predicate_filter" && "minecraft:air" in it.toString()
        })

        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_cotton_bushes.json")
        assertEquals(listOf("minecraft:plains", "minecraft:sunflower_plains"), modifier.getAsJsonArray("biomes").map { it.asString })
        assertEquals("suprememc:cotton_bushes", modifier.get("features").asString)
        assertEquals("vegetal_decoration", modifier.get("step").asString)
    }
}