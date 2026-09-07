package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class BookshelfDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val woods = listOf(
        "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry",
        "pale_oak", "bamboo", "crimson", "warped", "palm"
    )
    private val woodsWithCustomSideTexture = setOf(
        "spruce", "birch", "jungle", "acacia", "dark_oak", "cherry", "crimson", "warped", "palm"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        woods.forEach { wood ->
            val bookshelf = "${wood}_bookshelf"
            writes += save(cache, blockState(bookshelf), resourcePath("blockstates/$bookshelf.json"))
            writes += save(cache, blockModel(wood, bookshelf), resourcePath("models/block/$bookshelf.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$bookshelf"), resourcePath("items/$bookshelf.json"))
            writes += save(cache, selfDropLootTable(bookshelf), dataPath("loot_table/blocks/$bookshelf.json"))
            val planks = "${wood}_planks"
            val ingredientId = if (wood == "palm") ingredient(planks) else minecraftIngredient(planks)
            writes += save(cache, bookshelfRecipe(bookshelf, ingredientId), dataPath("recipe/$bookshelf.json"))
            writes += save(cache, recipeAdvancement(bookshelf, "building", ingredientId), dataPath("advancement/recipes/building/$bookshelf.json"))
        }

        writes += save(cache, bookshelfRecipe("bookshelf", "oak_planks", vanilla = true), minecraftDataPath("recipe/bookshelf.json"))
        writes += save(cache, vanillaRecipeAdvancement(), minecraftDataPath("advancement/recipes/building/bookshelf.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj {
            add("", obj { addProperty("model", "$namespace:block/$id") })
        })
    }

    private fun blockModel(wood: String, id: String) = obj {
        addProperty("parent", "minecraft:block/cube_column")
        add("textures", obj {
            addProperty("end", if (wood == "palm") "$namespace:block/palm_planks" else "minecraft:block/${wood}_planks")
            addProperty("side", if (wood in woodsWithCustomSideTexture) "$namespace:block/${wood}_bookshelf" else "minecraft:block/bookshelf")
        })
    }

    private fun bookshelfRecipe(result: String, planks: String, vanilla: Boolean = false): JsonObject = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("key", obj {
            addProperty("#", if (vanilla || ':' in planks) planks else "$namespace:$planks")
            addProperty("X", "minecraft:book")
        })
        add("pattern", array("###", "XXX", "###"))
        add("result", if (vanilla) minecraftItemResult(result) else itemResult(result))
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
                add("conditions", obj { addProperty("recipe", "minecraft:bookshelf") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_oak_planks") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("minecraft:bookshelf") }) })
    }

    override fun getName() = "Bookshelves"
}
