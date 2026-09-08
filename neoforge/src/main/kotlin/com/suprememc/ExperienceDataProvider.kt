package com.suprememc

import com.google.gson.JsonArray
import com.suprememc.loot.ModLootInjections
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ExperienceDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val items = arrayOf(
        "experience_ingot", "experience_upgrade_smithing_template",
        "experience_pickaxe", "experience_axe", "experience_shovel", "experience_hoe", "experience_sword",
        "experience_helmet", "experience_chestplate", "experience_leggings", "experience_boots"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        modelFiles(cache, writes, items)
        writes += save(cache, valuesTag("$namespace:experience_ingot"), dataPath("tags/item/experience_repair_items.json"))
        writes += save(cache, shapelessExperienceIngotRecipe(), dataPath("recipe/experience_ingot.json"))
        writes += save(cache, templateDuplicationRecipe(), dataPath("recipe/experience_upgrade_smithing_template.json"))
        writes += save(cache, templateLoot(), dataPath("loot_table/inject/experience_armorer_gift.json"))
        ModLootInjections.EXPERIENCE_INJECTIONS.forEach { injection ->
            writes += save(cache, obj {
                addProperty("type", "neoforge:add_table")
                add("conditions", JsonArray().also { it.add(obj {
                    addProperty("condition", "neoforge:loot_table_id")
                    addProperty("loot_table_id", injection.targetTable())
                }) })
                addProperty("table", injection.injectedTable())
            }, dataPath("loot_modifiers/${injection.name()}.json"))
        }

        listOf("pickaxe", "axe", "shovel", "hoe", "sword", "helmet", "chestplate", "leggings", "boots").forEach { id ->
            val recipeId = "experience_${id}_smithing"
            writes += save(cache, smithing("experience_$id", "emerald_$id"), dataPath("recipe/$recipeId.json"))
            writes += save(cache, recipeAdvancement(recipeId, "misc", "experience_ingot"), dataPath("advancement/recipes/misc/$recipeId.json"))
        }

        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun smithing(result: String, base: String) = obj {
        addProperty("type", "minecraft:smithing_transform")
        addProperty("template", ingredient("experience_upgrade_smithing_template"))
        addProperty("base", ingredient(base))
        addProperty("addition", ingredient("experience_ingot"))
        add("result", itemResult(result))
    }

    private fun shapelessExperienceIngotRecipe() = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "misc")
        add("ingredients", JsonArray().also {
            repeat(4) { _ -> it.add("minecraft:lapis_block") }
            repeat(4) { _ -> it.add("minecraft:experience_bottle") }
        })
        add("result", itemResult("experience_ingot"))
    }

    private fun templateDuplicationRecipe() = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "misc")
        add("pattern", JsonArray().also { it.add("DDD"); it.add("DLD"); it.add("DD ") })
        add("key", obj {
            addProperty("D", "minecraft:diamond")
            addProperty("L", "minecraft:lapis_block")
        })
        add("result", itemResult("experience_upgrade_smithing_template", 2))
    }

    private fun templateLoot() = obj {
        addProperty("type", "minecraft:chest")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { it.add(itemLootEntry("experience_upgrade_smithing_template")) })
        }) })
    }

    override fun getName() = "SupremeMC experience resources"
}
