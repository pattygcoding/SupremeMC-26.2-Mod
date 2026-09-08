package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class FireTntDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, blockState(), resourcePath("blockstates/fire_tnt.json"))
        writes += save(cache, blockModel(), resourcePath("models/block/fire_tnt.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/fire_tnt"), resourcePath("items/fire_tnt.json"))
        writes += save(cache, selfDropLootTable("fire_tnt"), dataPath("loot_table/blocks/fire_tnt.json"))
        writes += save(cache, recipe(), dataPath("recipe/fire_tnt.json"))
        writes += save(cache, recipeAdvancement(), dataPath("advancement/recipes/redstone/fire_tnt.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState() = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/fire_tnt") }) })
    }

    private fun blockModel() = obj {
        addProperty("parent", "minecraft:block/cube_bottom_top")
        add("textures", obj {
            addProperty("side", "$namespace:block/fire_tnt_side")
            addProperty("top", "$namespace:block/fire_tnt_top")
            addProperty("bottom", "$namespace:block/fire_tnt_bottom")
        })
    }

    private fun recipe() = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "redstone")
        add("ingredients", JsonArray().also {
            it.add(minecraftIngredient("tnt"))
            it.add(minecraftIngredient("fire_charge"))
        })
        add("result", itemResult("fire_tnt"))
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
                add("conditions", obj { addProperty("recipe", "$namespace:fire_tnt") })
            })
        })
        add("requirements", JsonArray().also {
            it.add(JsonArray().also { group -> group.add("has_tnt"); group.add("has_the_recipe") })
        })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:fire_tnt") }) })
    }

    override fun getName() = "SupremeMC fire TNT"
}
