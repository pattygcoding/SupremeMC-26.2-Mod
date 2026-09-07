package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ButtercupCloverDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("buttercup", "clover").forEach { id ->
            writes += save(cache, crossBlockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, crossModel(id), resourcePath("models/block/$id.json"))
            writes += save(cache, itemModel(id), resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
            writes += save(cache, plantLoot(id), dataPath("loot_table/blocks/$id.json"))
        }
        writes += save(cache, yellowDyeRecipe(), dataPath("recipe/buttercup_yellow_dye.json"))
        writes += save(cache, recipeAdvancement("buttercup_yellow_dye", "misc", "buttercup"), dataPath("advancement/recipes/misc/buttercup_yellow_dye.json"))
        writes += save(cache, additiveTag("$namespace:buttercup"), minecraftDataPath("tags/block/small_flowers.json"))
        writes += save(cache, additiveTag("$namespace:buttercup"), minecraftDataPath("tags/item/small_flowers.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun crossBlockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun crossModel(id: String) = obj {
        addProperty("parent", "minecraft:block/cross")
        add("textures", obj { addProperty("cross", "$namespace:block/$id") })
    }

    private fun itemModel(id: String) = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", "$namespace:block/$id") })
    }

    private fun plantLoot(id: String) = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { entries -> entries.add(itemLootEntry(id)) })
            })
        })
    }

    private fun yellowDyeRecipe() = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "misc")
        add("ingredients", JsonArray().also { it.add("$namespace:buttercup") })
        add("result", minecraftItemResult("yellow_dye"))
    }

    private fun additiveTag(value: String) = obj {
        addProperty("replace", false)
        add("values", JsonArray().also { it.add(value) })
    }

    override fun getName() = "SupremeMC buttercup and clover"
}