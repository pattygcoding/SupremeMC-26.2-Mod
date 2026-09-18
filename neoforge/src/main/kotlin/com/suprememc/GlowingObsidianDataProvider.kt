package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class GlowingObsidianDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val id = "glowing_obsidian"
        val writes = listOf(
            save(cache, simpleBlockState(id), resourcePath("blockstates/$id.json")),
            save(cache, cubeModel(id), resourcePath("models/block/$id.json")),
            save(cache, itemBlockModel(id), resourcePath("models/item/$id.json")),
            save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json")),
            save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json")),
            save(cache, glowingObsidianRecipe(), dataPath("recipe/$id.json"))
        )
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun simpleBlockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun cubeModel(id: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", "$namespace:block/$id") })
    }

    private fun itemBlockModel(id: String) = obj {
        addProperty("parent", "$namespace:block/$id")
    }

    private fun glowingObsidianRecipe() = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("pattern", array(" G ", "GOG", " G "))
        add("key", obj {
            addProperty("G", "minecraft:glowstone_dust")
            addProperty("O", "minecraft:obsidian")
        })
        add("result", itemResult("glowing_obsidian"))
    }

    override fun getName() = "Glowing Obsidian"
}