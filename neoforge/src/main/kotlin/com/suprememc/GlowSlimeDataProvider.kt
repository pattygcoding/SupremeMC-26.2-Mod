package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class GlowSlimeDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val dyeColors = arrayOf(
        "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
        "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, valuesTag(
            "minecraft:slime_block",
            *dyeColors.map { "$namespace:${it}_slime_block" }.toTypedArray()
        ), dataPath("tags/item/slime_blocks.json"))
        dyeColors.forEach { color ->
            writeCubeBlock(cache, writes, "${color}_glowblock")
            writeSlimeBlock(cache, writes, "${color}_slime_block")
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun writeCubeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        writes += save(cache, obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
    }

    // Vanilla slime_block model is NOT a plain cube_all: it layers an always-visible inset 10x10x10 cube
    // under a full-size cullface cube, which is what makes adjacent slime blocks blend seamlessly at their
    // borders (see ZReference-/assets/minecraft/models/block/slime_block.json). Colored variants reuse it as-is.
    private fun writeSlimeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        writes += save(cache, slimeModel(id), resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
        val color = id.removeSuffix("_slime_block")
        writes += save(cache, obj {
            addProperty("type", "minecraft:crafting_shapeless")
            addProperty("category", "misc")
            add("ingredients", array("#$namespace:slime_blocks", "minecraft:${color}_dye"))
            add("result", itemResult(id))
        }, dataPath("recipe/$id.json"))
        writes += save(cache, recipeAdvancement(id, "misc", "minecraft:slime_block"), dataPath("advancement/recipes/misc/$id.json"))
    }

    private fun slimeModel(id: String): JsonObject {
        fun ints(vararg values: Int) = JsonArray().also { arr -> values.forEach(arr::add) }
        fun face(uv: IntArray, cullface: String? = null) = obj {
            add("uv", ints(*uv))
            addProperty("texture", "#texture")
            if (cullface != null) addProperty("cullface", cullface)
        }
        fun element(from: IntArray, to: IntArray, cull: Boolean) = obj {
            add("from", ints(*from))
            add("to", ints(*to))
            add("faces", obj {
                val uv = if (from[0] == 0) intArrayOf(0, 0, 16, 16) else intArrayOf(3, 3, 13, 13)
                listOf("down", "up", "north", "south", "west", "east").forEach { dir ->
                    add(dir, face(uv, if (cull) dir else null))
                }
            })
        }
        return obj {
            addProperty("parent", "block/block")
            add("textures", obj { addProperty("particle", "$namespace:block/$id"); addProperty("texture", "$namespace:block/$id") })
            add("elements", JsonArray().also {
                it.add(element(intArrayOf(3, 3, 3), intArrayOf(13, 13, 13), false))
                it.add(element(intArrayOf(0, 0, 0), intArrayOf(16, 16, 16), true))
            })
        }
    }

    override fun getName() = "SupremeMC dyed glowblocks and slime blocks"
}
