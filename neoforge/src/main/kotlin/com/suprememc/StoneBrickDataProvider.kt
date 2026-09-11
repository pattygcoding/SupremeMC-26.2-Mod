package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class StoneBrickDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val stones = listOf("andesite", "diorite", "granite")
    private val crackedStones = listOf(
        "andesite" to "$namespace:andesite_bricks",
        "diorite" to "$namespace:diorite_bricks",
        "granite" to "$namespace:granite_bricks",
        "end_stone" to "minecraft:end_stone_bricks",
        "quartz" to "minecraft:quartz_bricks"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        crackedStones.forEach { (stone, ingredient) ->
            val crackedBricks = "cracked_${stone}_bricks"
            val texture = "$namespace:block/$crackedBricks"
            writes += save(cache, cubeModel(crackedBricks, texture), resourcePath("models/block/$crackedBricks.json"))
            writes += save(cache, simpleBlockState(crackedBricks), resourcePath("blockstates/$crackedBricks.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$crackedBricks"), resourcePath("items/$crackedBricks.json"))
            writes += save(cache, selfDropLootTable(crackedBricks), dataPath("loot_table/blocks/$crackedBricks.json"))
            writes += save(cache, crackedBricksSmeltingRecipe(crackedBricks, ingredient), dataPath("recipe/$crackedBricks.json"))
            writes += save(cache, recipeAdvancement(crackedBricks, "building", ingredient), dataPath("advancement/recipes/building_blocks/$crackedBricks.json"))
        }
        stones.forEach { stone ->
            val bricks = "${stone}_bricks"
            val mossyBricks = "mossy_${stone}_bricks"
            val stairs = "${stone}_brick_stairs"
            val slab = "${stone}_brick_slab"
            val wall = "${stone}_brick_wall"
            val polished = "minecraft:polished_$stone"
            val texture = "$namespace:block/$bricks"
            val mossyTexture = "$namespace:block/$mossyBricks"

            writes += save(cache, cubeModel(bricks, texture), resourcePath("models/block/$bricks.json"))
            writes += save(cache, cubeModel(mossyBricks, mossyTexture), resourcePath("models/block/$mossyBricks.json"))
            writes += save(cache, simpleBlockState(bricks), resourcePath("blockstates/$bricks.json"))
            writes += save(cache, simpleBlockState(mossyBricks), resourcePath("blockstates/$mossyBricks.json"))
            writes += save(cache, stairsBlockState(stairs), resourcePath("blockstates/$stairs.json"))
            writes += save(cache, slabBlockState(slab), resourcePath("blockstates/$slab.json"))
            writes += save(cache, wallBlockState(wall), resourcePath("blockstates/$wall.json"))
            val mossyStairs = "mossy_${stone}_brick_stairs"
            val mossySlab = "mossy_${stone}_brick_slab"
            val mossyWall = "mossy_${stone}_brick_wall"
            stairsModels(stairs, texture).forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }
            writes += save(cache, slabModel(slab, false, texture), resourcePath("models/block/$slab.json"))
            writes += save(cache, slabModel("${slab}_top", true, texture), resourcePath("models/block/${slab}_top.json"))
            writes += save(cache, cubeModel("${slab}_double", texture), resourcePath("models/block/${slab}_double.json"))
            wallModels(wall, texture).forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }
            writes += save(cache, stairsBlockState(mossyStairs), resourcePath("blockstates/$mossyStairs.json"))
            writes += save(cache, slabBlockState(mossySlab), resourcePath("blockstates/$mossySlab.json"))
            writes += save(cache, wallBlockState(mossyWall), resourcePath("blockstates/$mossyWall.json"))
            stairsModels(mossyStairs, mossyTexture).forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }
            writes += save(cache, slabModel(mossySlab, false, mossyTexture), resourcePath("models/block/$mossySlab.json"))
            writes += save(cache, slabModel("${mossySlab}_top", true, mossyTexture), resourcePath("models/block/${mossySlab}_top.json"))
            writes += save(cache, cubeModel("${mossySlab}_double", mossyTexture), resourcePath("models/block/${mossySlab}_double.json"))
            wallModels(mossyWall, mossyTexture).forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }
            listOf(bricks, stairs, slab, wall).forEach { id ->
                val itemModel = if (id == wall) "$namespace:block/${wall}_inventory" else "$namespace:block/$id"
                writes += save(cache, itemModelDefinition(itemModel), resourcePath("items/$id.json"))
                writes += save(cache, if (id == slab) slabLootTable(id) else selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
            }
            writes += save(cache, itemModelDefinition("$namespace:block/$mossyBricks"), resourcePath("items/$mossyBricks.json"))
            writes += save(cache, selfDropLootTable(mossyBricks), dataPath("loot_table/blocks/$mossyBricks.json"))
            listOf(mossyStairs, mossySlab, mossyWall).forEach { id ->
                val itemModel = if (id == mossyWall) "$namespace:block/${id}_inventory" else "$namespace:block/$id"
                writes += save(cache, itemModelDefinition(itemModel), resourcePath("items/$id.json"))
                writes += save(cache, if (id == mossySlab) slabLootTable(id) else selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
            }

            writes += save(cache, shapedRecipe(bricks, 4, polished, arrayOf("##", "##")), dataPath("recipe/$bricks.json"))
            writes += save(cache, mossyBricksRecipe(mossyBricks, bricks), dataPath("recipe/${mossyBricks}_from_vine.json"))
            writes += save(cache, shapedRecipe(mossyStairs, 4, "$namespace:$mossyBricks", arrayOf("#  ", "## ", "###")), dataPath("recipe/$mossyStairs.json"))
            writes += save(cache, shapedRecipe(mossySlab, 6, "$namespace:$mossyBricks", arrayOf("###")), dataPath("recipe/$mossySlab.json"))
            writes += save(cache, shapedRecipe(mossyWall, 6, "$namespace:$mossyBricks", arrayOf("###", "###")), dataPath("recipe/$mossyWall.json"))
            writes += save(cache, shapedRecipe(stairs, 4, "$namespace:$bricks", arrayOf("#  ", "## ", "###")), dataPath("recipe/$stairs.json"))
            writes += save(cache, shapedRecipe(slab, 6, "$namespace:$bricks", arrayOf("###")), dataPath("recipe/$slab.json"))
            writes += save(cache, shapedRecipe(wall, 6, "$namespace:$bricks", arrayOf("###", "###")), dataPath("recipe/$wall.json"))
            writes += save(cache, stonecuttingRecipe(bricks, 1, polished), dataPath("recipe/${bricks}_from_polished_${stone}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(stairs, 1, polished), dataPath("recipe/${stairs}_from_polished_${stone}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(slab, 2, polished), dataPath("recipe/${slab}_from_polished_${stone}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(wall, 1, polished), dataPath("recipe/${wall}_from_polished_${stone}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(stairs, 1, "$namespace:$bricks"), dataPath("recipe/${stairs}_from_${bricks}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(slab, 2, "$namespace:$bricks"), dataPath("recipe/${slab}_from_${bricks}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(wall, 1, "$namespace:$bricks"), dataPath("recipe/${wall}_from_${bricks}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(mossyStairs, 1, "$namespace:$mossyBricks"), dataPath("recipe/${mossyStairs}_from_${mossyBricks}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(mossySlab, 1, "$namespace:$mossyBricks"), dataPath("recipe/${mossySlab}_from_${mossyBricks}_stonecutting.json"))
            writes += save(cache, stonecuttingRecipe(mossyWall, 1, "$namespace:$mossyBricks"), dataPath("recipe/${mossyWall}_from_${mossyBricks}_stonecutting.json"))
            listOf(bricks, stairs, slab).forEach { result ->
                writes += save(cache, recipeAdvancement(result, "building", "$namespace:$bricks"), dataPath("advancement/recipes/building_blocks/$result.json"))
            }
            writes += save(cache, recipeAdvancement("${mossyBricks}_from_vine", "building", "minecraft:vine"), dataPath("advancement/recipes/building_blocks/${mossyBricks}_from_vine.json"))
            listOf(mossyStairs, mossySlab).forEach { result ->
                writes += save(cache, recipeAdvancement(result, "building", "$namespace:$mossyBricks"), dataPath("advancement/recipes/building_blocks/$result.json"))
                writes += save(cache, recipeAdvancement("${result}_from_${mossyBricks}_stonecutting", "building", "$namespace:$mossyBricks"), dataPath("advancement/recipes/building_blocks/${result}_from_${mossyBricks}_stonecutting.json"))
            }
            writes += save(cache, recipeAdvancement(mossyWall, "decorations", "$namespace:$mossyBricks"), dataPath("advancement/recipes/decorations/$mossyWall.json"))
            writes += save(cache, recipeAdvancement("${mossyWall}_from_${mossyBricks}_stonecutting", "decorations", "$namespace:$mossyBricks"), dataPath("advancement/recipes/decorations/${mossyWall}_from_${mossyBricks}_stonecutting.json"))
            writes += save(cache, recipeAdvancement(wall, "decorations", "$namespace:$bricks"), dataPath("advancement/recipes/decorations/$wall.json"))
            listOf(bricks, stairs, slab, wall).forEach { result ->
                writes += save(cache, recipeAdvancement("${result}_from_polished_${stone}_stonecutting", if (result == wall) "decorations" else "building", polished), dataPath("advancement/recipes/${if (result == wall) "decorations" else "building_blocks"}/${result}_from_polished_${stone}_stonecutting.json"))
            }
            listOf(stairs, slab, wall).forEach { result ->
                writes += save(cache, recipeAdvancement("${result}_from_${bricks}_stonecutting", if (result == wall) "decorations" else "building", "$namespace:$bricks"), dataPath("advancement/recipes/${if (result == wall) "decorations" else "building_blocks"}/${result}_from_${bricks}_stonecutting.json"))
            }
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun cubeModel(id: String, texture: String) = obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", texture) }) }
    private fun simpleBlockState(id: String) = obj { add("variants", obj { add("", variant(id)) }) }
    private fun stairsModels(id: String, texture: String) = listOf("$id" to "stairs", "${id}_inner" to "inner_stairs", "${id}_outer" to "outer_stairs").associate { (model, parent) -> model to obj { addProperty("parent", "minecraft:block/$parent"); add("textures", obj { addProperty("side", texture); addProperty("top", texture); addProperty("bottom", texture) }) } }
    private fun slabModel(id: String, top: Boolean, texture: String) = obj { addProperty("parent", "minecraft:block/${if (top) "slab_top" else "slab"}"); add("textures", obj { addProperty("side", texture); addProperty("top", texture); addProperty("bottom", texture) }) }
    private fun wallModels(id: String, texture: String) = listOf("side" to "template_wall_side", "side_tall" to "template_wall_side_tall", "post" to "template_wall_post", "inventory" to "wall_inventory").associate { (suffix, parent) -> "${id}_$suffix" to obj { addProperty("parent", "minecraft:block/$parent"); add("textures", obj { addProperty("wall", texture) }) } }
    private fun stairsBlockState(id: String) = obj { add("variants", obj { listOf("east", "west", "north", "south").forEach { facing -> listOf("bottom", "top").forEach { half -> listOf("straight", "inner_left", "inner_right", "outer_left", "outer_right").forEach { shape -> listOf("false", "true").forEach { waterlogged -> add("facing=$facing,half=$half,shape=$shape,waterlogged=$waterlogged", variant(if (shape == "straight") id else "${id}_${if (shape.startsWith("inner")) "inner" else "outer"}", facing, half, shape)) } } } } }) }
    private fun slabBlockState(id: String) = obj { add("variants", obj { add("type=bottom,waterlogged=false", variant(id)); add("type=bottom,waterlogged=true", variant(id)); add("type=top,waterlogged=false", variant("${id}_top")); add("type=top,waterlogged=true", variant("${id}_top")); add("type=double,waterlogged=false", variant("${id}_double")); add("type=double,waterlogged=true", variant("${id}_double")) }) }
    private fun wallBlockState(id: String) = obj { add("multipart", JsonArray().also { parts -> parts.add(part("${id}_post", "up", "true", 0)); listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (direction, rotation) -> parts.add(part("${id}_side", direction, "low", rotation)); parts.add(part("${id}_side_tall", direction, "tall", rotation)) } }) }
    private fun part(model: String, property: String, value: String, rotation: Int) = obj { add("apply", obj { addProperty("model", "$namespace:block/$model"); addProperty("uvlock", true); if (rotation != 0) addProperty("y", rotation) }); add("when", obj { addProperty(property, value) }) }
    private fun variant(model: String, facing: String? = null, half: String? = null, shape: String? = null) = obj { addProperty("model", "$namespace:block/$model"); if (shape != null) { addProperty("uvlock", true); val base = when (facing) { "east" -> 0; "south" -> 90; "west" -> 180; else -> 270 }; val rotation = when (shape) { "straight" -> base; "inner_left", "outer_left" -> if (half == "bottom") (base + 270) % 360 else base; else -> if (half == "bottom") base else (base + 90) % 360 }; if (rotation != 0) addProperty("y", rotation) }; if (half == "top") addProperty("x", 180) }
    private fun shapedRecipe(result: String, count: Int, ingredient: String, pattern: Array<String>) = obj { addProperty("type", "minecraft:crafting_shaped"); addProperty("category", "building"); add("pattern", JsonArray().also { pattern.forEach(it::add) }); add("key", obj { addProperty("#", ingredient) }); add("result", itemResult(result, count)) }
    private fun stonecuttingRecipe(result: String, count: Int, ingredient: String) = obj { addProperty("type", "minecraft:stonecutting"); addProperty("ingredient", ingredient); add("result", itemResult(result, count)) }
    private fun mossyBricksRecipe(result: String, bricks: String) = obj { addProperty("type", "minecraft:crafting_shapeless"); addProperty("category", "building"); add("ingredients", array("$namespace:$bricks", "minecraft:vine")); add("result", itemResult(result)) }
    private fun crackedBricksSmeltingRecipe(result: String, ingredient: String) = obj { addProperty("type", "minecraft:smelting"); addProperty("category", "blocks"); addProperty("experience", 0.1f); addProperty("ingredient", ingredient); add("result", itemResult(result)) }
    override fun getName() = "SupremeMC stone bricks"
}