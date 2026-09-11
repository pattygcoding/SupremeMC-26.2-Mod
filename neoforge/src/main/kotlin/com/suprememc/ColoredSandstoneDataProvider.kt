package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ColoredSandstoneDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private enum class SandstoneColor(val id: String) {
        WHITE("white"), BLACK("black"), PINK("pink");

        val sand = "${id}_sand"
        val sandstone = "${id}_sandstone"
        val sandstoneStairs = "${sandstone}_stairs"
        val sandstoneSlab = "${sandstone}_slab"
        val sandstoneWall = "${sandstone}_wall"
        val smooth = "smooth_${sandstone}"
        val smoothStairs = "${smooth}_stairs"
        val smoothSlab = "${smooth}_slab"
        val cut = "cut_${sandstone}"
        val cutSlab = "${cut}_slab"
        val all = listOf(sand, sandstone, sandstoneStairs, sandstoneSlab, sandstoneWall, smooth, smoothStairs, smoothSlab, cut, cutSlab)
    }

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        SandstoneColor.entries.forEach { color ->
            generateAssets(cache, writes, color)
            generateRecipes(cache, writes, color)
            generateLoot(cache, writes, color)
        }
        val sandIds = SandstoneColor.entries.map { "$namespace:${it.sand}" }.toTypedArray()
        writes += save(cache, valuesTag(*sandIds), minecraftDataPath("tags/block/sand.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun generateAssets(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, color: SandstoneColor) {
        val sandstoneTexture = "$namespace:block/${color.sandstone}"
        val topTexture = "$namespace:block/${color.sandstone}_top"
        val bottomTexture = "$namespace:block/${color.sandstone}_bottom"
        val sandTexture = "$namespace:block/${color.sand}"
        writes += save(cache, cubeModel(color.sand, sandTexture), resourcePath("models/block/${color.sand}.json"))
        writes += save(cache, simpleBlockState(color.sand), resourcePath("blockstates/${color.sand}.json"))
        writes += save(cache, sandstoneModel(color.sandstone, sandstoneTexture, topTexture, bottomTexture), resourcePath("models/block/${color.sandstone}.json"))
        writes += save(cache, simpleBlockState(color.sandstone), resourcePath("blockstates/${color.sandstone}.json"))

        listOf(color.sandstoneStairs, color.smoothStairs).forEach { id ->
            writes += save(cache, stairsBlockState(id), resourcePath("blockstates/$id.json"))
            stairsModels(id, sandstoneTexture, topTexture, bottomTexture).forEach { (modelId, model) ->
                writes += save(cache, model, resourcePath("models/block/$modelId.json"))
            }
        }
        listOf(color.sandstoneSlab, color.smoothSlab, color.cutSlab).forEach { id ->
            writes += save(cache, slabBlockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, slabModel(id, false, sandstoneTexture, topTexture, bottomTexture), resourcePath("models/block/$id.json"))
            writes += save(cache, slabModel("${id}_top", true, sandstoneTexture, topTexture, bottomTexture), resourcePath("models/block/${id}_top.json"))
            writes += save(cache, cubeModel("${id}_double", sandstoneTexture), resourcePath("models/block/${id}_double.json"))
        }
        writes += save(cache, wallBlockState(color.sandstoneWall), resourcePath("blockstates/${color.sandstoneWall}.json"))
        wallModels(color.sandstoneWall, sandstoneTexture).forEach { (modelId, model) ->
            writes += save(cache, model, resourcePath("models/block/$modelId.json"))
        }
        listOf(color.smooth, color.cut).forEach { id ->
            writes += save(cache, cubeModel(id, sandstoneTexture), resourcePath("models/block/$id.json"))
            writes += save(cache, simpleBlockState(id), resourcePath("blockstates/$id.json"))
        }
        color.all.forEach { id ->
            val model = if (id == color.sandstoneWall) "$namespace:block/${id}_inventory" else "$namespace:block/$id"
            writes += save(cache, itemModelDefinition(model), resourcePath("items/$id.json"))
        }
    }

    private fun generateRecipes(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, color: SandstoneColor) {
        writes += save(cache, shapedRecipe(color.sandstone, 1, color.sand, arrayOf("##", "##")), dataPath("recipe/${color.sandstone}.json"))
        writes += save(cache, shapedRecipe(color.cut, 4, color.sandstone, arrayOf("##", "##")), dataPath("recipe/${color.cut}.json"))
        writes += save(cache, smeltingRecipe(color.smooth, color.sandstone), dataPath("recipe/${color.smooth}.json"))
        writes += save(cache, shapedRecipe(color.sandstoneStairs, 4, color.sandstone, arrayOf("#  ", "## ", "###")), dataPath("recipe/${color.sandstoneStairs}.json"))
        writes += save(cache, shapedRecipe(color.sandstoneSlab, 6, color.sandstone, arrayOf("###")), dataPath("recipe/${color.sandstoneSlab}.json"))
        writes += save(cache, shapedRecipe(color.sandstoneWall, 6, color.sandstone, arrayOf("###", "###")), dataPath("recipe/${color.sandstoneWall}.json"))
        writes += save(cache, shapedRecipe(color.smoothStairs, 4, color.smooth, arrayOf("#  ", "## ", "###")), dataPath("recipe/${color.smoothStairs}.json"))
        writes += save(cache, shapedRecipe(color.smoothSlab, 6, color.smooth, arrayOf("###")), dataPath("recipe/${color.smoothSlab}.json"))
        writes += save(cache, shapedRecipe(color.cutSlab, 6, color.cut, arrayOf("###")), dataPath("recipe/${color.cutSlab}.json"))
        listOf(color.sandstoneStairs to 1, color.sandstoneSlab to 2, color.sandstoneWall to 1,
            color.smoothStairs to 1, color.smoothSlab to 2, color.cutSlab to 2).forEach { (result, count) ->
            writes += save(cache, stonecuttingRecipe(result, count, color.sandstone), dataPath("recipe/${result}_from_${color.sandstone}_stonecutting.json"))
        }
        listOf(color.smoothStairs to 1, color.smoothSlab to 2).forEach { (result, count) ->
            writes += save(cache, stonecuttingRecipe(result, count, color.smooth), dataPath("recipe/${result}_from_${color.smooth}_stonecutting.json"))
        }
        writes += save(cache, stonecuttingRecipe(color.cutSlab, 2, color.cut), dataPath("recipe/${color.cutSlab}_from_${color.cut}_stonecutting.json"))
    }

    private fun generateLoot(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, color: SandstoneColor) {
        color.all.forEach { id ->
            writes += save(cache, if (id.endsWith("_slab")) slabLootTable(id) else selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
        }
    }

    private fun cubeModel(id: String, texture: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", texture) })
    }

    private fun sandstoneModel(id: String, side: String, top: String, bottom: String) = obj {
        addProperty("parent", "minecraft:block/cube_bottom_top")
        add("textures", obj { addProperty("side", side); addProperty("top", top); addProperty("bottom", bottom) })
    }

    private fun simpleBlockState(id: String) = obj { add("variants", obj { add("", variant(id)) }) }

    private fun stairsModels(id: String, side: String, top: String, bottom: String) = listOf(id to "stairs", "${id}_inner" to "inner_stairs", "${id}_outer" to "outer_stairs").associate { (modelId, parent) ->
        modelId to obj {
            addProperty("parent", "minecraft:block/$parent")
            add("textures", obj { addProperty("side", side); addProperty("top", top); addProperty("bottom", bottom) })
        }
    }

    private fun slabModel(id: String, topSlab: Boolean, side: String, top: String, bottom: String) = obj {
        addProperty("parent", "minecraft:block/${if (topSlab) "slab_top" else "slab"}")
        add("textures", obj { addProperty("side", side); addProperty("top", top); addProperty("bottom", bottom) })
    }

    private fun stairsBlockState(id: String) = obj { add("variants", obj {
        listOf("east", "west", "north", "south").forEach { facing ->
            listOf("bottom", "top").forEach { half ->
                listOf("straight", "inner_left", "inner_right", "outer_left", "outer_right").forEach { shape ->
                    listOf("false", "true").forEach { waterlogged ->
                        val model = if (shape == "straight") id else "${id}_${if (shape.startsWith("inner")) "inner" else "outer"}"
                        add("facing=$facing,half=$half,shape=$shape,waterlogged=$waterlogged", variant(model, facing, half, shape))
                    }
                }
            }
        }
    }) }

    private fun slabBlockState(id: String) = obj { add("variants", obj {
        add("type=bottom,waterlogged=false", variant(id)); add("type=bottom,waterlogged=true", variant(id))
        add("type=top,waterlogged=false", variant("${id}_top")); add("type=top,waterlogged=true", variant("${id}_top"))
        add("type=double,waterlogged=false", variant("${id}_double")); add("type=double,waterlogged=true", variant("${id}_double"))
    }) }

    private fun wallBlockState(id: String) = obj { add("multipart", JsonArray().also { parts ->
        parts.add(part("${id}_post", "up", "true", 0))
        listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (direction, rotation) ->
            parts.add(part("${id}_side", direction, "low", rotation)); parts.add(part("${id}_side_tall", direction, "tall", rotation))
        }
    }) }

    private fun wallModels(id: String, texture: String) = listOf("side" to "template_wall_side", "side_tall" to "template_wall_side_tall", "post" to "template_wall_post", "inventory" to "wall_inventory").associate { (suffix, parent) ->
        "${id}_$suffix" to obj { addProperty("parent", "minecraft:block/$parent"); add("textures", obj { addProperty("wall", texture) }) }
    }

    private fun part(model: String, property: String, value: String, rotation: Int) = obj {
        add("apply", obj { addProperty("model", "$namespace:block/$model"); addProperty("uvlock", true); if (rotation != 0) addProperty("y", rotation) })
        add("when", obj { addProperty(property, value) })
    }

    private fun variant(model: String, facing: String? = null, half: String? = null, shape: String? = null) = obj {
        addProperty("model", "$namespace:block/$model")
        if (shape != null) {
            addProperty("uvlock", true)
            val base = when (facing) { "east" -> 0; "south" -> 90; "west" -> 180; else -> 270 }
            val rotation = when (shape) { "straight" -> base; "inner_left", "outer_left" -> if (half == "bottom") (base + 270) % 360 else base; else -> if (half == "bottom") base else (base + 90) % 360 }
            if (rotation != 0) addProperty("y", rotation)
        }
        if (half == "top") addProperty("x", 180)
    }

    private fun shapedRecipe(result: String, count: Int, ingredientId: String, pattern: Array<String>) = obj {
        addProperty("type", "minecraft:crafting_shaped"); addProperty("category", "building")
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj { addProperty("#", "$namespace:$ingredientId") }); add("result", itemResult(result, count))
    }

    private fun stonecuttingRecipe(result: String, count: Int, ingredientId: String) = obj {
        addProperty("type", "minecraft:stonecutting"); addProperty("ingredient", "$namespace:$ingredientId"); add("result", itemResult(result, count))
    }

    private fun smeltingRecipe(result: String, ingredientId: String) = obj {
        addProperty("type", "minecraft:smelting"); addProperty("category", "blocks"); addProperty("experience", 0.1f); addProperty("cookingtime", 200)
        addProperty("ingredient", "$namespace:$ingredientId"); add("result", itemResult(result))
    }

    override fun getName() = "SupremeMC colored sandstone families"
}
