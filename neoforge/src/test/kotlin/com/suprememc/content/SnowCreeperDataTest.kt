package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SnowCreeperDataTest : GeneratedDataTestSupport() {

    @Test
    fun generatesSpawnEggModelAndItemDefinition() {
        val model = readJson("assets/suprememc/models/item/snow_creeper_spawn_egg.json")
        assertEquals("minecraft:item/generated", model.get("parent").asString)
        assertEquals("suprememc:item/snow_creeper_spawn_egg", model.getAsJsonObject("textures").get("layer0").asString)

        val definition = readJson("assets/suprememc/items/snow_creeper_spawn_egg.json")
        assertEquals("minecraft:model", definition.getAsJsonObject("model").get("type").asString)
        assertEquals("suprememc:item/snow_creeper_spawn_egg", definition.getAsJsonObject("model").get("model").asString)
    }

    @Test
    fun dropsGunpowderAndMusicDiscsLikeAVanillaCreeper() {
        val loot = readJson("data/suprememc/loot_table/entities/snow_creeper.json").toString()
        assertTrue("minecraft:gunpowder" in loot)
        assertTrue("minecraft:snowball" in loot)
        assertTrue("\"min\":4.0" in loot)
        assertTrue("\"max\":8.0" in loot)
        assertTrue("minecraft:enchanted_count_increase" in loot)
        assertTrue("#minecraft:skeletons" in loot)
        assertTrue("minecraft:creeper_drop_music_discs" in loot)
    }

    @Test
    fun replacesVanillaCreeperSpawnsInColdBiomes() {
        val removal = readJson("data/suprememc/neoforge/biome_modifier/remove_vanilla_creepers_cold.json")
        assertEquals("neoforge:remove_spawns", removal.get("type").asString)
        assertEquals("minecraft:creeper", removal.get("entity_types").asString)
        val removedBiomes = removal.getAsJsonArray("biomes").map { it.asString }.toSet()
        assertTrue("minecraft:snowy_plains" in removedBiomes)
        assertTrue("minecraft:snowy_taiga" in removedBiomes)
        assertTrue("minecraft:grove" in removedBiomes)

        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_snow_creepers_cold.json")
        assertEquals("neoforge:add_spawns", modifier.get("type").asString)
        assertEquals(removedBiomes, modifier.getAsJsonArray("biomes").map { it.asString }.toSet())
        val spawner = modifier.getAsJsonObject("spawners")
        assertEquals("suprememc:snow_creeper", spawner.get("type").asString)
        assertEquals(100, spawner.get("weight").asInt)
        assertEquals(4, spawner.get("minCount").asInt)
        assertEquals(4, spawner.get("maxCount").asInt)
    }

    @Test
    fun localizesEntityAndSpawnEgg() {
        val lang = readJson("assets/suprememc/lang/en_us.json")
        assertTrue(lang.has("entity.suprememc.snow_creeper"), "Missing entity lang key")
        assertTrue(lang.has("item.suprememc.snow_creeper_spawn_egg"), "Missing spawn egg lang key")
    }
}