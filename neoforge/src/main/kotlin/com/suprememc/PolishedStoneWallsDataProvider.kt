package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class PolishedStoneWallsDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private data class Wall(val id: String, val base: String, val texture: String)

    private val walls = listOf(
        Wall("polished_granite_wall", "granite", "polished_granite"),
        Wall("polished_diorite_wall", "diorite", "polished_diorite"),
        Wall("polished_andesite_wall", "andesite", "polished_andesite")
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        walls.forEach { wall ->
            writes += save(cache, wallBlockState(wall.id), resourcePath("blockstates/${wall.id}.json"))
            writes += save(cache, wallModel("${wall.id}_side", "template_wall_side", wall.texture), resourcePath("models/block/${wall.id}_side.json"))
            writes += save(cache, wallModel("${wall.id}_side_tall", "template_wall_side_tall", wall.texture), resourcePath("models/block/${wall.id}_side_tall.json"))
            writes += save(cache, wallModel("${wall.id}_post", "template_wall_post", wall.texture), resourcePath("models/block/${wall.id}_post.json"))
            writes += save(cache, wallModel("${wall.id}_inventory", "wall_inventory", wall.texture), resourcePath("models/block/${wall.id}_inventory.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/${wall.id}_inventory"), resourcePath("items/${wall.id}.json"))
            writes += save(cache, selfDropLootTable(wall.id), dataPath("loot_table/blocks/${wall.id}.json"))
            writes += save(cache, wallCraftingRecipe(wall), dataPath("recipe/${wall.id}.json"))
            writes += save(cache, wallStonecuttingRecipe(wall, wall.base), dataPath("recipe/${wall.id}_from_${wall.base}_stonecutting.json"))
            writes += save(cache, wallStonecuttingRecipe(wall, wall.texture), dataPath("recipe/${wall.id}_from_${wall.texture}_stonecutting.json"))
            writes += save(cache, recipeAdvancement(wall.id, "minecraft:${wall.texture}"), dataPath("advancement/recipes/decorations/${wall.id}.json"))
            writes += save(cache, recipeAdvancement("${wall.id}_from_${wall.base}_stonecutting", "minecraft:${wall.base}"), dataPath("advancement/recipes/decorations/${wall.id}_from_${wall.base}_stonecutting.json"))
            writes += save(cache, recipeAdvancement("${wall.id}_from_${wall.texture}_stonecutting", "minecraft:${wall.texture}"), dataPath("advancement/recipes/decorations/${wall.id}_from_${wall.texture}_stonecutting.json"))
        }
        writes += save(cache, valuesTag(
            "$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:aquamarine_block",
            "$namespace:atlantis_debris", "$namespace:abyssalite_block", "$namespace:nether_anthracite_ore",
            "$namespace:anthracite_block", *walls.map { "$namespace:${it.id}" }.toTypedArray()
            , "$namespace:andesite_bricks", "$namespace:andesite_brick_stairs", "$namespace:andesite_brick_slab", "$namespace:andesite_brick_wall"
            , "$namespace:diorite_bricks", "$namespace:diorite_brick_stairs", "$namespace:diorite_brick_slab", "$namespace:diorite_brick_wall"
            , "$namespace:granite_bricks", "$namespace:granite_brick_stairs", "$namespace:granite_brick_slab", "$namespace:granite_brick_wall"
        ), minecraftDataPath("tags/block/mineable/pickaxe.json"))
        writes += save(cache, valuesTag(
            *walls.map { "$namespace:${it.id}" }.toTypedArray(),
            "$namespace:andesite_brick_wall", "$namespace:diorite_brick_wall", "$namespace:granite_brick_wall"
        ), minecraftDataPath("tags/block/walls.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun wallBlockState(id: String) = obj {
        add("multipart", JsonArray().also { parts ->
            parts.add(part("${id}_post", "up", "true"))
            listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (direction, rotation) ->
                parts.add(part("${id}_side", direction, "low", rotation))
            }
            listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (direction, rotation) ->
                parts.add(part("${id}_side_tall", direction, "tall", rotation))
            }
        })
    }

    private fun part(model: String, property: String, value: String, rotation: Int = 0) = obj {
        add("apply", obj {
            addProperty("model", "$namespace:block/$model")
            if (model.endsWith("side") || model.endsWith("side_tall")) {
                addProperty("uvlock", true)
                if (rotation != 0) addProperty("y", rotation)
            }
        })
        add("when", obj { addProperty(property, value) })
    }

    private fun wallModel(parentId: String, parent: String, texture: String) = obj {
        addProperty("parent", "minecraft:block/$parent")
        add("textures", obj { addProperty("wall", "minecraft:block/$texture") })
    }

    private fun wallCraftingRecipe(wall: Wall) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        add("pattern", JsonArray().also { it.add("###"); it.add("###") })
        add("key", obj { addProperty("#", "minecraft:${wall.texture}") })
        add("result", itemResult(wall.id, 6))
    }

    private fun wallStonecuttingRecipe(wall: Wall, ingredientId: String) = obj {
        addProperty("type", "minecraft:stonecutting")
        addProperty("ingredient", "minecraft:$ingredientId")
        add("result", itemResult(wall.id))
    }

    private fun recipeAdvancement(recipe: String, unlock: String) = obj {
        val unlockId = unlock.substringAfter(':')
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_$unlockId", obj {
                addProperty("trigger", "minecraft:inventory_changed")
                add("conditions", obj { add("items", JsonArray().also { it.add(obj { addProperty("items", unlock) }) }) })
            })
            add("has_the_recipe", obj {
                addProperty("trigger", "minecraft:recipe_unlocked")
                add("conditions", obj { addProperty("recipe", "$namespace:$recipe") })
            })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_the_recipe"); group.add("has_$unlockId") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:$recipe") }) })
    }

    override fun getName() = "SupremeMC polished stone walls"
}