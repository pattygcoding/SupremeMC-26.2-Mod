package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalamariDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesCalamariModelsTagsAndCookingRecipes() {
        listOf("assets/suprememc/models/item/calamari.json", "assets/suprememc/items/cooked_calamari.json", "data/minecraft/tags/item/meat.json", "data/c/tags/item/foods/raw_meat.json", "data/c/tags/item/foods/cooked_fish.json").forEach(::assertResourceExists)
        mapOf("smelting" to 200, "smoking" to 100, "campfire_cooking" to 600).forEach { (kind, time) ->
            val recipe = readJson("data/suprememc/recipe/cooked_calamari_from_${kind}_calamari.json")
            assertEquals("minecraft:$kind", recipe.get("type").asString)
            assertEquals(time, recipe.get("cookingtime").asInt)
            assertEquals(0.35F, recipe.get("experience").asFloat)
        }
    }

    @Test
    fun injectsFortuneScaledCalamariDropsForBothSquidVariants() {
        val squid = readJson("data/suprememc/loot_table/inject/squid_calamari.json").toString()
        assertTrue("cooked_calamari" in squid && "is_on_fire" in squid && "minecraft:enchanted_count_increase" in squid)
        val glowSquid = readJson("data/suprememc/loot_table/inject/glow_squid_calamari.json").toString()
        assertTrue("calamari" in glowSquid && "cooked_calamari" !in glowSquid)
        assertResourceExists("data/suprememc/loot_modifiers/add_calamari_to_squid.json")
        assertResourceExists("data/suprememc/loot_modifiers/add_calamari_to_glow_squid.json")
    }
}