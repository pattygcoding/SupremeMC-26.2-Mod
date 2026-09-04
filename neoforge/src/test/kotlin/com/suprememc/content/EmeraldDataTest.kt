package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmeraldDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesEmeraldEquipmentRecipesAndChainmailRecipes() {
        listOf(
            "assets/suprememc/models/item/emerald_pickaxe.json",
            "assets/suprememc/models/item/emerald_helmet.json",
            "data/suprememc/recipe/emerald_pickaxe.json",
            "data/suprememc/recipe/emerald_helmet.json",
            "data/suprememc/recipe/chainmail_helmet.json",
            "data/suprememc/recipe/chainmail_chestplate.json",
            "data/suprememc/recipe/chainmail_leggings.json",
            "data/suprememc/recipe/chainmail_boots.json"
        ).forEach(::assertResourceExists)

        val emeraldPickaxe = readJson("data/suprememc/recipe/emerald_pickaxe.json")
        assertEquals("suprememc:emerald_pickaxe", emeraldPickaxe.getAsJsonObject("result").get("id").asString)
        val chestplate = readJson("data/suprememc/recipe/chainmail_chestplate.json")
        assertEquals("minecraft:iron_chain", chestplate.getAsJsonObject("key").get("C").asString)
        assertEquals("minecraft:chainmail_chestplate", chestplate.getAsJsonObject("result").get("id").asString)
    }

    @Test
    fun emitsEmeraldRepairTagAndEquipmentTranslations() {
        assertTagContains("data/suprememc/tags/item/emerald_repair_items.json", "minecraft:emerald")
        val lang = readJson("assets/suprememc/lang/en_us.json")
        listOf("emerald_pickaxe", "emerald_helmet", "emerald_chestplate", "emerald_leggings", "emerald_boots")
            .forEach { assertTrue(lang.has("item.suprememc.$it")) }
    }
}
