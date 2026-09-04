package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FireCreeperDataTest : GeneratedDataTestSupport() {

    @Test
    fun generatesSpawnEggModelAndItemDefinition() {
        val model = readJson("assets/suprememc/models/item/fire_creeper_spawn_egg.json")
        assertEquals("minecraft:item/generated", model.get("parent").asString)
        assertEquals("suprememc:item/fire_creeper_spawn_egg", model.getAsJsonObject("textures").get("layer0").asString)

        val definition = readJson("assets/suprememc/items/fire_creeper_spawn_egg.json")
        assertEquals("minecraft:model", definition.getAsJsonObject("model").get("type").asString)
        assertEquals("suprememc:item/fire_creeper_spawn_egg", definition.getAsJsonObject("model").get("model").asString)
    }

    @Test
    fun dropsGunpowderAndMusicDiscsLikeAVanillaCreeper() {
        val loot = readJson("data/suprememc/loot_table/entities/fire_creeper.json").toString()
        assertTrue("minecraft:gunpowder" in loot)
        assertTrue("minecraft:enchanted_count_increase" in loot)
        assertTrue("#minecraft:skeletons" in loot)
        assertTrue("minecraft:creeper_drop_music_discs" in loot)
    }

    @Test
    fun spawnsInNetherBiomesExceptWarpedForest() {
        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_fire_creepers_nether.json")
        assertEquals("neoforge:add_spawns", modifier.get("type").asString)
        val biomes = modifier.getAsJsonArray("biomes").map { it.asString }.toSet()
        assertEquals(
            setOf(
                "minecraft:nether_wastes",
                "minecraft:soul_sand_valley",
                "minecraft:crimson_forest",
                "minecraft:basalt_deltas"
            ),
            biomes
        )

        val spawner = modifier.getAsJsonObject("spawners")
        assertEquals("suprememc:fire_creeper", spawner.get("type").asString)
        assertEquals(25, spawner.get("weight").asInt)
        assertEquals(1, spawner.get("minCount").asInt)
        assertEquals(2, spawner.get("maxCount").asInt)
    }

    @Test
    fun localizesEntityAndSpawnEgg() {
        val lang = readJson("assets/suprememc/lang/en_us.json")
        assertTrue(lang.has("entity.suprememc.fire_creeper"), "Missing entity lang key")
        assertTrue(lang.has("item.suprememc.fire_creeper_spawn_egg"), "Missing spawn egg lang key")
    }
}
