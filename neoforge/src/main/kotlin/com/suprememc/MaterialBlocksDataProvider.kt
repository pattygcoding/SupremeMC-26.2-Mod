package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class MaterialBlocksDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private data class Material(
        val id: String,
        val textureId: String = "${id}_block",
        val ingredientId: String = "${id}_block"
    )

    private val vanillaTextureMaterials = setOf("iron", "lapis", "gold", "diamond", "emerald", "coal", "obsidian", "netherite")

    private val materials = listOf(
        Material("iron"), Material("lapis"), Material("gold"), Material("diamond"),
        Material("emerald"), Material("coal"), Material("obsidian", "obsidian", "obsidian"),
        Material("amber"),
        Material("netherite"), Material("aquamarine"), Material("abyssalite")
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        materials.forEach { material ->
            val stairs = "${material.id}_stairs"
            val slab = "${material.id}_slab"
            val texture = if (material.id in vanillaTextureMaterials) {
                "minecraft:block/${material.textureId}"
            } else {
                "$namespace:block/${material.textureId}"
            }

            writes += save(cache, stairsBlockState(stairs), resourcePath("blockstates/$stairs.json"))
            writes += save(cache, slabBlockState(slab), resourcePath("blockstates/$slab.json"))
            writes += save(cache, itemModel(stairs), resourcePath("models/item/$stairs.json"))
            writes += save(cache, itemModel(slab), resourcePath("models/item/$slab.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$stairs"), resourcePath("items/$stairs.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$slab"), resourcePath("items/$slab.json"))
            stairsModels(stairs, texture).forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }
            writes += save(cache, slabModel(slab, false, texture), resourcePath("models/block/$slab.json"))
            writes += save(cache, slabModel("${slab}_top", true, texture), resourcePath("models/block/${slab}_top.json"))
            writes += save(cache, cubeModel("${slab}_double", texture), resourcePath("models/block/${slab}_double.json"))

            writes += save(cache, selfDropLootTable(stairs), dataPath("loot_table/blocks/$stairs.json"))
            writes += save(cache, slabLootTable(slab), dataPath("loot_table/blocks/$slab.json"))
            val stairsStonecutting = "${stairs}_from_${material.ingredientId}_stonecutting"
            val slabStonecutting = "${slab}_from_${material.ingredientId}_stonecutting"
            val ingredient = if (material.id in vanillaTextureMaterials) "minecraft:${material.ingredientId}" else "$namespace:${material.ingredientId}"
            writes += save(cache, shapedRecipe(stairs, 6, ingredient, arrayOf("#  ", "## ", "###")), dataPath("recipe/$stairs.json"))
            writes += save(cache, shapedRecipe(slab, 6, ingredient, arrayOf("###")), dataPath("recipe/$slab.json"))
            writes += save(cache, stonecuttingRecipe(stairs, 1, ingredient), dataPath("recipe/$stairsStonecutting.json"))
            writes += save(cache, stonecuttingRecipe(slab, 2, ingredient), dataPath("recipe/$slabStonecutting.json"))
            writes += save(cache, recipeAdvancement(stairs, ingredient), dataPath("advancement/recipes/building_blocks/$stairs.json"))
            writes += save(cache, recipeAdvancement(slab, ingredient), dataPath("advancement/recipes/building_blocks/$slab.json"))
            writes += save(cache, recipeAdvancement(stairsStonecutting, ingredient), dataPath("advancement/recipes/building_blocks/$stairsStonecutting.json"))
            writes += save(cache, recipeAdvancement(slabStonecutting, ingredient), dataPath("advancement/recipes/building_blocks/$slabStonecutting.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun itemModel(id: String) = obj { addProperty("parent", "minecraft:block/$id") }

    private fun cubeModel(id: String, texture: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", texture) })
    }

    private fun slabModel(id: String, top: Boolean, texture: String) = obj {
        addProperty("parent", "minecraft:block/${if (top) "slab_top" else "slab"}")
        add("textures", obj { addProperty("side", texture); addProperty("top", texture); addProperty("bottom", texture) })
    }

    private fun stairsModels(id: String, texture: String) = listOf("$id", "${id}_inner", "${id}_outer").associateWith { modelId ->
        val parent = when (modelId) {
            id -> "stairs"
            "${id}_inner" -> "inner_stairs"
            else -> "outer_stairs"
        }
        obj {
            addProperty("parent", "minecraft:block/$parent")
            add("textures", obj { addProperty("side", texture); addProperty("top", texture); addProperty("bottom", texture) })
        }
    }

    private fun stairsBlockState(id: String) = obj {
        add("variants", obj {
            listOf("east", "west", "north", "south").forEach { facing ->
                listOf("bottom", "top").forEach { half ->
                    listOf("straight", "inner_left", "inner_right", "outer_left", "outer_right").forEach { shape ->
                        listOf("false", "true").forEach { waterlogged ->
                            val model = when {
                                shape == "straight" -> id
                                shape.startsWith("inner") -> "${id}_inner"
                                else -> "${id}_outer"
                            }
                            add("facing=$facing,half=$half,shape=$shape,waterlogged=$waterlogged", variant(model, facing, half, shape))
                        }
                    }
                }
            }
        })
    }

    private fun slabBlockState(id: String) = obj {
        add("variants", obj {
            add("type=bottom,waterlogged=false", variant(id))
            add("type=bottom,waterlogged=true", variant(id))
            add("type=top,waterlogged=false", variant("${id}_top"))
            add("type=top,waterlogged=true", variant("${id}_top"))
            add("type=double,waterlogged=false", variant("${id}_double"))
            add("type=double,waterlogged=true", variant("${id}_double"))
        })
    }

    private fun variant(model: String, facing: String? = null, half: String? = null, shape: String? = null) = obj {
        addProperty("model", "$namespace:block/$model")
        if (shape != null) {
            addProperty("uvlock", true)
            val base = when (facing) { "east" -> 0; "south" -> 90; "west" -> 180; else -> 270 }
            val rotation = when (shape) {
                "straight" -> base
                "inner_left", "outer_left" -> if (half == "bottom") (base + 270) % 360 else base
                else -> if (half == "bottom") base else (base + 90) % 360
            }
            if (rotation != 0) addProperty("y", rotation)
        }
        if (half == "top") addProperty("x", 180)
    }

    private fun shapedRecipe(result: String, count: Int, ingredient: String, pattern: Array<String>) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj { addProperty("#", if (ingredient.contains(':')) ingredient else "$namespace:$ingredient") })
        add("result", itemResult(result, count))
    }

    private fun stonecuttingRecipe(result: String, count: Int, ingredient: String) = obj {
        addProperty("type", "minecraft:stonecutting")
        addProperty("ingredient", if (ingredient.contains(':')) ingredient else "$namespace:$ingredient")
        add("result", itemResult(result, count))
    }

    private fun recipeAdvancement(recipe: String, ingredient: String) = obj {
        val normalizedIngredient = if (ingredient.contains(':')) ingredient else "$namespace:$ingredient"
        val material = normalizedIngredient.substringAfter(':').substringBefore('_')
        val block = normalizedIngredient
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_${material}_block", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj { add("items", JsonArray().also { it.add(obj { addProperty("items", block) }) }) })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "$namespace:$recipe") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_${material}_block") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:$recipe") }) })
    }

    override fun getName() = "SupremeMC material stairs and slabs"
}
