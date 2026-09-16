package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ColoredRedstoneLampDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val colors = arrayOf(
        "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
        "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        colors.forEach { color ->
            val id = "${color}_redstone_lamp"
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, cubeModel(id, false), resourcePath("models/block/$id.json"))
            writes += save(cache, cubeModel("${id}_on", true), resourcePath("models/block/${id}_on.json"))
            writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
            writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
            writes += save(cache, recipe(id, color), dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "redstone", "minecraft:redstone_lamp"), dataPath("advancement/recipes/redstone/$id.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj {
            add("lit=false", obj { addProperty("model", "$namespace:block/$id") })
            add("lit=true", obj { addProperty("model", "$namespace:block/${id}_on") })
        })
    }

    private fun cubeModel(id: String, lit: Boolean) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", "$namespace:block/${id.removeSuffix("_on")}${if (lit) "_on" else ""}") })
    }

    private fun recipe(id: String, color: String) = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "redstone")
        add("ingredients", array("minecraft:redstone_lamp", "minecraft:${color}_dye"))
        add("result", itemResult(id))
    }

    override fun getName() = "SupremeMC colored redstone lamps"
}