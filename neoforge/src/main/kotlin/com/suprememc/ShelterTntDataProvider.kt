package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ShelterTntDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, blockState(), resourcePath("blockstates/shelter_tnt.json"))
        writes += save(cache, blockModel(), resourcePath("models/block/shelter_tnt.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/shelter_tnt"), resourcePath("items/shelter_tnt.json"))
        writes += save(cache, selfDropLootTable("shelter_tnt"), dataPath("loot_table/blocks/shelter_tnt.json"))
        writes += save(cache, recipe(), dataPath("recipe/shelter_tnt.json"))
        writes += save(cache, recipeAdvancement(), dataPath("advancement/recipes/redstone/shelter_tnt.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState() = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/shelter_tnt") }) })
    }

    private fun blockModel() = obj {
        addProperty("parent", "minecraft:block/cube_bottom_top")
        add("textures", obj {
            addProperty("side", "$namespace:block/shelter_tnt_side")
            addProperty("top", "$namespace:block/shelter_tnt_top")
            addProperty("bottom", "$namespace:block/shelter_tnt_side")
        })
    }

    private fun recipe() = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "redstone")
        add("ingredients", JsonArray().also {
            it.add(minecraftIngredient("tnt"))
            it.add(minecraftIngredient("oak_planks"))
        })
        add("result", itemResult("shelter_tnt"))
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
                add("conditions", obj { addProperty("recipe", "$namespace:shelter_tnt") })
            })
        })
        add("requirements", JsonArray().also {
            it.add(JsonArray().also { group -> group.add("has_tnt"); group.add("has_the_recipe") })
        })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:shelter_tnt") }) })
    }

    override fun getName() = "SupremeMC shelter TNT"
}