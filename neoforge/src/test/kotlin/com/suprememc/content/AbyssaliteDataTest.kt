package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class AbyssaliteDataTest : GeneratedDataTestSupport() {
    @Test
    fun generatesAbyssaliteProgressionResources() {
        listOf("assets/suprememc/models/item/abyssalite_scrap.json", "assets/suprememc/models/item/abyssalite_upgrade_smithing_template.json", "assets/suprememc/models/block/atlantis_debris.json", "assets/suprememc/equipment/abyssalite.json", "assets/suprememc/items/abyssalite_ingot.json", "data/suprememc/worldgen/configured_feature/atlantis_debris.json", "data/suprememc/worldgen/placed_feature/atlantis_debris.json", "data/suprememc/recipe/abyssalite_upgrade_smithing_template.json", "data/suprememc/recipe/abyssalite_helmet_smithing.json", "data/suprememc/recipe/abyssalite_scrap_from_smelting_atlantis_debris.json", "data/suprememc/recipe/abyssalite_scrap_from_blasting_atlantis_debris.json").forEach(::assertResourceExists)
        val lang = readJson("assets/suprememc/lang/en_us.json")
        assertEquals("Abyssalite Upgrade", lang.get("item.suprememc.abyssalite_upgrade_smithing_template").asString)
        assertEquals("Aquamarine Equipment", lang.get("item.suprememc.smithing_template.abyssalite_upgrade.applies_to").asString)
        assertEquals("Abyssalite Ingot", lang.get("item.suprememc.smithing_template.abyssalite_upgrade.ingredients").asString)
        val feature = readJson("data/suprememc/worldgen/configured_feature/atlantis_debris.json").getAsJsonObject("config")
        assertEquals(2, feature.get("size").asInt)
        assertEquals(4, feature.getAsJsonArray("targets").size())
    }

    @Test
    fun injectsAbyssaliteTemplateIntoOceanStructureLoot() {
        val guaranteed = readJson("data/suprememc/loot_table/inject/abyssalite_template_guaranteed.json").getAsJsonArray("pools").single().asJsonObject.getAsJsonArray("entries").single().asJsonObject
        assertEquals("suprememc:abyssalite_upgrade_smithing_template", guaranteed.get("name").asString)
        assertFalse(guaranteed.has("conditions"))
        val rare = readJson("data/suprememc/loot_table/inject/abyssalite_template_rare.json").toString()
        assert("minecraft:random_chance" in rare && "0.1" in rare)
    }

    @Test
    fun generatesAbyssaliteRecipesAndCookingValues() {
        val ingot = readJson("data/suprememc/recipe/abyssalite_ingot.json")
        assertEquals(8, ingot.getAsJsonArray("ingredients").size())
        assertEquals("suprememc:abyssalite_ingot", ingot.getAsJsonObject("result").get("id").asString)
        val smithing = readJson("data/suprememc/recipe/abyssalite_helmet_smithing.json")
        assertEquals("suprememc:aquamarine_helmet", smithing.get("base").asString)
        assertEquals("suprememc:abyssalite_ingot", smithing.get("addition").asString)
    }

    @Test
    fun generatesDiamondTierAbyssaliteMiningTags() {
        assertTagContains("data/minecraft/tags/block/needs_diamond_tool.json", "suprememc:atlantis_debris", "suprememc:abyssalite_block")
        assertTagContains("data/minecraft/tags/block/mineable/pickaxe.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block", "suprememc:atlantis_debris", "suprememc:abyssalite_block")
    }
}