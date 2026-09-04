package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class EmeraldDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val items = arrayOf(
        "emerald_pickaxe", "emerald_axe", "emerald_shovel", "emerald_hoe", "emerald_sword",
        "emerald_helmet", "emerald_chestplate", "emerald_leggings", "emerald_boots"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        modelFiles(cache, writes, items)
        writes += save(cache, valuesTag("minecraft:emerald"), dataPath("tags/item/emerald_repair_items.json"))
        writes += save(cache, emeraldRecipe("emerald_pickaxe", arrayOf("EEE", " S ", " S ")), dataPath("recipe/emerald_pickaxe.json"))
        writes += save(cache, emeraldRecipe("emerald_axe", arrayOf("EE ", "ES ", " S ")), dataPath("recipe/emerald_axe.json"))
        writes += save(cache, emeraldRecipe("emerald_shovel", arrayOf("E", "S", "S")), dataPath("recipe/emerald_shovel.json"))
        writes += save(cache, emeraldRecipe("emerald_hoe", arrayOf("EE ", " S ", " S ")), dataPath("recipe/emerald_hoe.json"))
        writes += save(cache, emeraldRecipe("emerald_sword", arrayOf("E", "E", "S")), dataPath("recipe/emerald_sword.json"))
        writes += save(cache, emeraldItemRecipe("emerald_helmet", arrayOf("EEE", "E E")), dataPath("recipe/emerald_helmet.json"))
        writes += save(cache, emeraldItemRecipe("emerald_chestplate", arrayOf("E E", "EEE", "EEE")), dataPath("recipe/emerald_chestplate.json"))
        writes += save(cache, emeraldItemRecipe("emerald_leggings", arrayOf("EEE", "E E", "E E")), dataPath("recipe/emerald_leggings.json"))
        writes += save(cache, emeraldItemRecipe("emerald_boots", arrayOf("E E", "E E")), dataPath("recipe/emerald_boots.json"))

        listOf(
            "emerald_pickaxe", "emerald_axe", "emerald_shovel", "emerald_hoe", "emerald_sword",
            "emerald_helmet", "emerald_chestplate", "emerald_leggings", "emerald_boots"
        ).forEach { id ->
            writes += save(cache, recipeAdvancement(id, "equipment", "emerald"), dataPath("advancement/recipes/equipment/$id.json"))
        }

        listOf("chainmail_helmet", "chainmail_chestplate", "chainmail_leggings", "chainmail_boots").forEach { id ->
            writes += save(cache, chainmailRecipe(id), dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "equipment", "chain"), dataPath("advancement/recipes/equipment/$id.json"))
        }

        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun emeraldRecipe(result: String, pattern: Array<String>) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "equipment")
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj {
            addProperty("E", "minecraft:emerald")
            if (pattern.joinToString("").contains('S')) addProperty("S", "minecraft:stick")
        })
        add("result", obj {
            addProperty("id", "$namespace:$result")
            addProperty("count", 1)
        })
    }

    private fun emeraldItemRecipe(result: String, pattern: Array<String>) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "equipment")
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj { addProperty("E", "minecraft:emerald") })
        add("result", obj {
            addProperty("id", "$namespace:$result")
            addProperty("count", 1)
        })
    }

    private fun chainmailRecipe(result: String) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "equipment")
        val pattern = when (result) {
            "chainmail_helmet" -> arrayOf("CCC", "C C")
            "chainmail_chestplate" -> arrayOf("C C", "CCC", "CCC")
            "chainmail_leggings" -> arrayOf("CCC", "C C", "C C")
            else -> arrayOf("C C", "C C")
        }
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj { addProperty("A", "minecraft:chain") })
        add("result", obj { addProperty("id", "minecraft:$result"); addProperty("count", 1) })
    }

    override fun getName() = "SupremeMC emerald resources"
}
