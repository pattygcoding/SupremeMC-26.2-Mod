package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CakeDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesSilkTouchCakeLootTableOverride() {
        val loot = readJson("data/minecraft/loot_table/blocks/cake.json")
        assertEquals("minecraft:block", loot.get("type").asString)
        assertEquals("minecraft:blocks/cake", loot.get("random_sequence").asString)
        val text = loot.toString()
        assertTrue("minecraft:silk_touch" in text, "cake loot table must require Silk Touch")
        assertTrue("\"name\":\"minecraft:cake\"" in text, "cake loot table must drop the cake item")
        assertTrue("block_state_property" in text && "\"bites\":\"0\"" in text, "cake must be whole (bites=0) to drop itself")
    }
}
