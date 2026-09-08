package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SnowTntDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, blockState(), resourcePath("blockstates/snow_tnt.json"))
        writes += save(cache, blockModel(), resourcePath("models/block/snow_tnt.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/snow_tnt"), resourcePath("items/snow_tnt.json"))
        writes += save(cache, selfDropLootTable("snow_tnt"), dataPath("loot_table/blocks/snow_tnt.json"))
        writes += save(cache, recipe(), dataPath("recipe/snow_tnt.json"))
        writes += save(cache, recipeAdvancement(), dataPath("advancement/recipes/redstone/snow_tnt.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState() = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/snow_tnt") }) })
    }

    private fun blockModel() = obj {
        addProperty("parent", "minecraft:block/cube_bottom_top")
        add("textures", obj {
            addProperty("side", "$namespace:block/snow_tnt_side")
            addProperty("top", "$namespace:block/snow_tnt_top")
            addProperty("bottom", "$namespace:block/snow_tnt_bottom")
        })
    }

    private fun recipe() = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "redstone")
        add("ingredients", JsonArray().also {
            it.add(minecraftIngredient("tnt"))
            it.add(minecraftIngredient("snow_block"))
        })
        add("result", itemResult("snow_tnt"))
    }

    private fun recipeAdvancement() = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_tnt", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj {
                    add("items", JsonArray().also {
                        it.add(obj { add("items", JsonArray().also { items -> items.add(minecraftIngredient("tnt")) }) })
                    })
                })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "$namespace:snow_tnt") })
            })
        })
        add("requirements", JsonArray().also {
            it.add(JsonArray().also { group -> group.add("has_tnt"); group.add("has_the_recipe") })
        })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:snow_tnt") }) })
    }

    override fun getName() = "SupremeMC snow TNT"
}
