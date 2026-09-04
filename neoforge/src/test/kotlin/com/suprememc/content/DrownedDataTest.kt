package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DrownedDataTest : GeneratedDataTestSupport() {
    @Test
    fun drownedSpawnModifiersMatchBedrockWeights() {
        val expected = mapOf(
            "add_drowned_river" to ("minecraft:river" to 100),
            "add_drowned_dripstone_caves" to ("minecraft:dripstone_caves" to 100),
            "add_drowned_ice_caves" to ("suprememc:ice_caves" to 100),
            "add_drowned_oceans" to ("#minecraft:is_ocean" to 100),
            "add_drowned_frozen_river" to ("minecraft:frozen_river" to 5)
        )
        expected.forEach { (file, targetAndWeight) ->
            val modifier = readJson("data/suprememc/neoforge/biome_modifier/$file.json")
            assertEquals("neoforge:add_spawns", modifier.get("type").asString)
            assertEquals(targetAndWeight.first, modifier.getAsJsonArray("biomes").single().asString)
            val spawner = modifier.getAsJsonObject("spawners")
            assertEquals("minecraft:drowned", spawner.get("type").asString)
            assertEquals(targetAndWeight.second, spawner.get("weight").asInt)
        }

        val removal = readJson("data/suprememc/neoforge/biome_modifier/remove_vanilla_drowned_spawns.json")
        assertEquals("neoforge:remove_spawns", removal.get("type").asString)
        assertEquals("minecraft:drowned", removal.get("entity_types").asString)
    }
}