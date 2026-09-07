package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EnderSpiderDataTest : GeneratedDataTestSupport() {
    @Test
    fun enderSpidersUseOverworldSpiderSpawnRatesAcrossTheEnd() {
        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_ender_spiders_end.json")

        assertEquals("neoforge:add_spawns", modifier.get("type").asString)
        assertEquals(
            setOf(
                "minecraft:the_end",
                "minecraft:end_highlands",
                "minecraft:end_midlands",
                "minecraft:small_end_islands",
                "minecraft:end_barrens"
            ),
            modifier.getAsJsonArray("biomes").map { it.asString }.toSet()
        )

        val spawner = modifier.getAsJsonObject("spawners")
        assertEquals("suprememc:ender_spider", spawner.get("type").asString)
        assertEquals(3, spawner.get("weight").asInt)
        assertEquals(1, spawner.get("minCount").asInt)
        assertEquals(4, spawner.get("maxCount").asInt)
    }
}