package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SupremeMCLogoBlockDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        val id = "suprememc_logo_block"
        val textureTop = "$namespace:block/${id}_top"
        val textureSide = "$namespace:block/${id}_side"
        val textureBottom = "$namespace:block/${id}_bottom"

        writes += save(cache, obj {
            add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
        }, resourcePath("blockstates/$id.json"))
        writes += save(cache, obj {
            addProperty("parent", "minecraft:block/cube_bottom_top")
            add("textures", obj {
                addProperty("top", textureTop)
                addProperty("side", textureSide)
                addProperty("bottom", textureBottom)
            })
        }, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "minecraft:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        writes += save(cache, logoBlockLootTable(id), dataPath("loot_table/blocks/$id.json"))
        writes += save(cache, obj {
            addProperty("type", "minecraft:crafting_shapeless")
            addProperty("category", "building")
            add("ingredients", JsonArray().also {
                it.add("minecraft:grass_block")
                it.add("minecraft:magenta_dye")
                it.add("minecraft:pink_dye")
            })
            add("result", itemResult(id))
        }, dataPath("recipe/$id.json"))
        writes += save(cache, grassBlockRecipeAdvancement(id), dataPath("advancement/recipes/building_blocks/$id.json"))
        writes += save(cache, valuesTag("$namespace:$id"), minecraftDataPath("tags/block/mineable/shovel.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun grassBlockRecipeAdvancement(recipe: String) = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_grass_block", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj { add("items", JsonArray().also { it.add(obj { addProperty("items", "minecraft:grass_block") }) }) })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "$namespace:$recipe") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_grass_block") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:$recipe") }) })
    }

    private fun logoBlockLootTable(id: String) = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "minecraft:survives_explosion") }) })
                add("entries", JsonArray().also { it.add(itemLootEntry(id)) })
            })
        })
        addProperty("random_sequence", "$namespace:blocks/$id")
    }

    override fun getName() = "SupremeMC logo block"
}