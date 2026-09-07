package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CraftingTableDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val woods = listOf(
        "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry",
        "pale_oak", "bamboo", "crimson", "warped", "palm"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        woods.forEach { wood ->
            val table = "${wood}_crafting_table"
            writes += save(cache, blockState(table), resourcePath("blockstates/$table.json"))
            writes += save(cache, blockModel(wood), resourcePath("models/block/$table.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$table"), resourcePath("items/$table.json"))
            writes += save(cache, selfDropLootTable(table), dataPath("loot_table/blocks/$table.json"))
            val planks = "${wood}_planks"
            val ingredientId = if (wood == "palm") ingredient(planks) else minecraftIngredient(planks)
            writes += save(cache, craftingTableRecipe(table, ingredientId), dataPath("recipe/$table.json"))
            writes += save(cache, recipeAdvancement(table, "building", ingredientId), dataPath("advancement/recipes/building/$table.json"))
        }

        writes += save(cache, craftingTableRecipe("crafting_table", "oak_planks", vanilla = true), minecraftDataPath("recipe/crafting_table.json"))
        writes += save(cache, vanillaRecipeAdvancement(), minecraftDataPath("advancement/recipes/decorations/crafting_table.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj {
            add("", obj { addProperty("model", "$namespace:block/$id") })
        })
    }

    private fun blockModel(wood: String) = obj {
        val sideTexture = "$namespace:block/${wood}_crafting_table_side"
        val frontTexture = "$namespace:block/${wood}_crafting_table_front"
        val topTexture = "$namespace:block/${wood}_crafting_table_top"
        addProperty("parent", "minecraft:block/cube")
        add("textures", obj {
            addProperty("down", if (wood == "palm") "$namespace:block/palm_planks" else "minecraft:block/${wood}_planks")
            addProperty("east", sideTexture)
            addProperty("north", frontTexture)
            addProperty("particle", frontTexture)
            addProperty("south", sideTexture)
            addProperty("up", topTexture)
            addProperty("west", frontTexture)
        })
    }

    private fun craftingTableRecipe(result: String, planks: String, vanilla: Boolean = false): JsonObject = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("key", obj { addProperty("#", if (vanilla || ':' in planks) planks else "$namespace:$planks") })
        add("pattern", array("##", "##"))
        add("result", if (vanilla) minecraftItemResult(result) else itemResult(result))
        if (vanilla) addProperty("show_notification", false)
    }

    private fun vanillaRecipeAdvancement() = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_oak_planks", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj { add("items", JsonArray().also { it.add(obj { add("items", JsonArray().also { ids -> ids.add("minecraft:oak_planks") }) }) }) })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "minecraft:crafting_table") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_oak_planks") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("minecraft:crafting_table") }) })
    }

    override fun getName() = "Crafting tables"
}