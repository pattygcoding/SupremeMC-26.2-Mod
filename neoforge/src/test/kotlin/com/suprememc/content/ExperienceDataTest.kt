package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExperienceDataTest : GeneratedDataTestSupport() {
    private val equipment = listOf("pickaxe", "axe", "shovel", "hoe", "sword", "helmet", "chestplate", "leggings", "boots")

    @Test
    fun generatesExperienceSmithingRecipesForEmeraldEquipment() {
        equipment.forEach { id ->
            val recipe = readJson("data/suprememc/recipe/experience_${id}_smithing.json")
            assertEquals("suprememc:experience_upgrade_smithing_template", recipe.get("template").asString)
            assertEquals("suprememc:emerald_$id", recipe.get("base").asString)
            assertEquals("suprememc:experience_ingot", recipe.get("addition").asString)
            assertEquals("suprememc:experience_$id", recipe.getAsJsonObject("result").get("id").asString)
        }
    }

    @Test
    fun generatesExperienceModelsTranslationsAndRepairTag() {
        assertTagContains("data/suprememc/tags/item/experience_repair_items.json", "suprememc:experience_ingot")
        val lang = readJson("assets/suprememc/lang/en_us.json")
        assertEquals("Experience Upgrade", lang.get("item.suprememc.experience_upgrade_smithing_template").asString)
        assertEquals("Emerald Equipment", lang.get("item.suprememc.smithing_template.experience_upgrade.applies_to").asString)
        assertEquals("Experience Ingot", lang.get("item.suprememc.smithing_template.experience_upgrade.ingredients").asString)
        listOf("experience_ingot", "experience_upgrade_smithing_template")
            .plus(equipment.map { "experience_$it" })
            .forEach { id ->
                assertResourceExists("assets/suprememc/models/item/$id.json")
                assertTrue(lang.has("item.suprememc.$id"))
            }
    }

        @Test
        fun generatesExperienceRecipesAndArmorerHeroGiftInjection() {
            val ingotRecipe = readJson("data/suprememc/recipe/experience_ingot.json")
            assertEquals(8, ingotRecipe.getAsJsonArray("ingredients").size())
            assertEquals(4, ingotRecipe.getAsJsonArray("ingredients").count { it.asString == "minecraft:lapis_block" })
            assertEquals(4, ingotRecipe.getAsJsonArray("ingredients").count { it.asString == "minecraft:experience_bottle" })

            val templateRecipe = readJson("data/suprememc/recipe/experience_upgrade_smithing_template.json")
            assertEquals(2, templateRecipe.getAsJsonObject("result").get("count").asInt)
            assertEquals(7, templateRecipe.getAsJsonArray("pattern").sumOf { row -> row.asString.count { it == 'D' } })
            assertEquals("minecraft:lapis_block", templateRecipe.getAsJsonObject("key").get("L").asString)

            val modifier = readJson("data/suprememc/loot_modifiers/add_experience_template_to_armorer_gift.json")
            assertEquals("minecraft:gameplay/hero_of_the_village/armorer_gift",
                modifier.getAsJsonArray("conditions").first().asJsonObject.get("loot_table_id").asString)
            assertResourceExists("data/suprememc/loot_table/inject/experience_armorer_gift.json")
        }
}
