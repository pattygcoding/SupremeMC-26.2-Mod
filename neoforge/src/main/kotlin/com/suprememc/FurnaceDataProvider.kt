package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class FurnaceDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val furnaces = listOf(
        "blackstone" to "blackstone",
        "deepslate" to "cobbled_deepslate"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        furnaces.forEach { (name, ingredient) ->
            val furnace = "${name}_furnace"
            writes += save(cache, blockState(furnace), resourcePath("blockstates/$furnace.json"))
            writes += save(cache, blockModel(furnace, false), resourcePath("models/block/$furnace.json"))
            writes += save(cache, blockModel(furnace, true), resourcePath("models/block/${furnace}_on.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$furnace"), resourcePath("items/$furnace.json"))
            writes += save(cache, selfDropLootTable(furnace), dataPath("loot_table/blocks/$furnace.json"))
            writes += save(cache, furnaceRecipe(furnace, ingredient), dataPath("recipe/$furnace.json"))
            writes += save(cache, recipeAdvancement(furnace, "decorations", "minecraft:$ingredient"), dataPath("advancement/recipes/decorations/$furnace.json"))
        }

        writes += save(cache, furnaceRecipe("furnace", "cobblestone", vanilla = true), minecraftDataPath("recipe/furnace.json"))
        writes += save(cache, vanillaRecipeAdvancement(), minecraftDataPath("advancement/recipes/decorations/furnace.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj {
            listOf("east" to 90, "north" to 0, "south" to 180, "west" to 270).forEach { (facing, rotation) ->
                add("facing=$facing,lit=false", modelVariant(id, false, rotation))
                add("facing=$facing,lit=true", modelVariant(id, true, rotation))
            }
        })
    }

    private fun modelVariant(id: String, lit: Boolean, rotation: Int) = obj {
        addProperty("model", "$namespace:block/$id${if (lit) "_on" else ""}")
        if (rotation != 0) addProperty("y", rotation)
    }

    private fun blockModel(id: String, lit: Boolean) = obj {
        addProperty("parent", "minecraft:block/orientable")
        add("textures", obj {
            addProperty("front", "$namespace:block/${id}_front${if (lit) "_on" else ""}")
            addProperty("side", "$namespace:block/${id}_side")
            addProperty("top", "$namespace:block/${id}_top")
        })
    }

    private fun furnaceRecipe(result: String, ingredient: String, vanilla: Boolean = false): JsonObject = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("key", obj { addProperty("#", if (vanilla) "minecraft:$ingredient" else "minecraft:$ingredient") })
        add("pattern", array("###", "# #", "###"))
        add("result", if (vanilla) minecraftItemResult(result) else itemResult(result))
    }

    private fun vanillaRecipeAdvancement() = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_cobblestone", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj { add("items", JsonArray().also { it.add(obj { add("items", JsonArray().also { ids -> ids.add("minecraft:cobblestone") }) }) }) })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "minecraft:furnace") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_cobblestone") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("minecraft:furnace") }) })
    }

    override fun getName() = "Furnaces"
}