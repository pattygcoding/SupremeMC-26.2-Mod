package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JebWoolDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesAnimatedJebWoolAndRestrictsItsDropToNamedSheep() {
		assertResourceExists("assets/suprememc/blockstates/jeb_wool.json")
		assertResourceExists("assets/suprememc/models/block/jeb_wool.json")
        assertResourceExists("assets/suprememc/models/item/jeb_wool.json")
        assertResourceExists("assets/suprememc/items/jeb_wool.json")
		assertResourceExists("data/suprememc/loot_table/blocks/jeb_wool.json")
        assertResourceExists("data/suprememc/loot_modifiers/add_jeb_wool_to_sheep.json")

        val lootEntry = readJson("data/suprememc/loot_table/inject/jeb_wool.json")
            .getAsJsonArray("pools").single().asJsonObject
            .getAsJsonArray("entries").single().asJsonObject
        assertEquals("suprememc:jeb_wool", lootEntry.get("name").asString)
        val predicate = lootEntry.getAsJsonArray("conditions").single().asJsonObject
            .getAsJsonObject("predicate")
            .getAsJsonObject("minecraft:components")
        assertEquals("{\"text\":\"jeb_\"}", predicate.get("minecraft:custom_name").asString)

        val modifier = readJson("data/suprememc/loot_modifiers/add_jeb_wool_to_sheep.json")
        assertEquals("minecraft:entities/sheep", modifier.getAsJsonArray("conditions").single().asJsonObject.get("loot_table_id").asString)

		val itemModel = readJson("assets/suprememc/items/jeb_wool.json").getAsJsonObject("model")
		assertEquals("suprememc:block/jeb_wool", itemModel.get("model").asString)
    }
}