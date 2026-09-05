package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class BellDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, bellRecipe(), minecraftDataPath("recipe/bell.json"))
        writes += save(cache, bellAdvancement(), dataPath("advancement/recipes/bell.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun bellRecipe() = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "misc")
        add("pattern", JsonArray().also {
            it.add("ISI")
            it.add("IGI")
        })
        add("key", obj {
            addProperty("I", "minecraft:iron_ingot")
            addProperty("S", "minecraft:stick")
            addProperty("G", "minecraft:gold_block")
        })
        add("result", minecraftItemResult("bell"))
    }

    private fun bellAdvancement() = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_gold_block", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj {
                    add("items", JsonArray().also {
                        it.add(obj { add("items", JsonArray().also { items -> items.add("minecraft:gold_block") }) })
                    })
                })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "minecraft:bell") })
            })
        })
        add("requirements", JsonArray().also {
            it.add(JsonArray().also { group -> group.add("has_gold_block"); group.add("has_the_recipe") })
        })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("minecraft:bell") }) })
    }

    override fun getName() = "SupremeMC bell recipe"
}