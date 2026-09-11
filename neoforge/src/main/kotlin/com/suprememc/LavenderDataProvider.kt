package com.suprememc

import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class LavenderDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val blocks = listOf(
        "lavender_wart_block",
        "lavender_endspar",
        "lavender_roots",
        "lavender_fungus",
        "lavender_stem",
        "stripped_lavender_stem",
        "lavender_hyphae",
        "stripped_lavender_hyphae",
        "lavender_wood",
        "stripped_lavender_wood",
        "lavender_planks",
        "lavender_slab",
        "lavender_stairs",
        "lavender_fence",
        "lavender_fence_gate",
        "lavender_door",
        "lavender_trapdoor",
        "lavender_pressure_plate",
        "lavender_button",
        "lavender_sign",
        "lavender_wall_sign",
        "lavender_hanging_sign",
        "lavender_wall_hanging_sign",
    )
    private val itemTagBlocks = blocks.filterNot {
        it == "lavender_wall_sign" || it == "lavender_wall_hanging_sign"
    }
    private val dragonImmuneBlocks = blocks + listOf("lavender_bookshelf", "lavender_crafting_table", "glendstone", "xylium_ore", "xylium_block")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        blockModels().forEach { (id, model) -> writes += save(cache, model, resourcePath("models/block/$id.json")) }

        blocks.forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, itemModel(id), resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$id"), resourcePath("items/$id.json"))
            if (id !in setOf("lavender_wall_sign", "lavender_wall_hanging_sign")) {
                writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
            }
        }
        listOf("lavender_boat", "lavender_chest_boat").forEach { id ->
            writes += save(cache, flatItemModel("$namespace:item/$id"), resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$id"), resourcePath("items/$id.json"))
        }

        writes += save(cache, slabLootTable("lavender_slab"), dataPath("loot_table/blocks/lavender_slab.json"))
        writes += save(cache, valuesTag("$namespace:lavender_stem", "$namespace:stripped_lavender_stem", "$namespace:lavender_hyphae", "$namespace:stripped_lavender_hyphae", "$namespace:lavender_wood", "$namespace:stripped_lavender_wood"), dataPath("tags/block/lavender_logs.json"))
        writes += save(cache, valuesTag("#$namespace:lavender_logs", "#${namespace}:palm_logs"), minecraftDataPath("tags/block/logs.json"))
        writes += save(cache, valuesTag("#$namespace:lavender_logs", "#${namespace}:palm_logs"), minecraftDataPath("tags/block/logs_that_burn.json"))
        writes += save(cache, valuesTag("$namespace:lavender_planks", "$namespace:palm_planks"), minecraftDataPath("tags/block/planks.json"))
        writes += save(cache, valuesTag("$namespace:lavender_slab", "$namespace:palm_slab"), minecraftDataPath("tags/block/wooden_slabs.json"))
        writes += save(cache, valuesTag("$namespace:lavender_stairs", "$namespace:palm_stairs"), minecraftDataPath("tags/block/wooden_stairs.json"))
        writes += save(cache, valuesTag("$namespace:lavender_fence", "$namespace:palm_fence"), minecraftDataPath("tags/block/wooden_fences.json"))
        writes += save(cache, valuesTag("$namespace:lavender_fence_gate", "$namespace:palm_fence_gate"), minecraftDataPath("tags/block/fence_gates.json"))
        writes += save(cache, valuesTag("$namespace:palm_door", "$namespace:lavender_door"), minecraftDataPath("tags/block/wooden_doors.json"))
        writes += save(cache, valuesTag("$namespace:lavender_trapdoor", "$namespace:palm_trapdoor"), minecraftDataPath("tags/block/wooden_trapdoors.json"))
        writes += save(cache, valuesTag("$namespace:lavender_pressure_plate", "$namespace:palm_pressure_plate"), minecraftDataPath("tags/block/wooden_pressure_plates.json"))
        writes += save(cache, valuesTag("$namespace:lavender_button", "$namespace:palm_button"), minecraftDataPath("tags/block/wooden_buttons.json"))
        writes += save(cache, valuesTag("#$namespace:lavender_stems", "#${namespace}:palm_logs"), minecraftDataPath("tags/item/logs.json"))
        writes += save(cache, valuesTag("#$namespace:lavender_stems", "#${namespace}:palm_logs"), minecraftDataPath("tags/item/logs_that_burn.json"))
        writes += save(cache, valuesTag("$namespace:lavender_boat"), minecraftDataPath("tags/item/boats.json"))
        writes += save(cache, valuesTag("$namespace:lavender_chest_boat"), minecraftDataPath("tags/item/chest_boats.json"))

        writes += save(cache, valuesTag(*blocks.map { "$namespace:$it" }.toTypedArray()), dataPath("tags/block/lavender_stems.json"))
        writes += save(cache, valuesTag(*itemTagBlocks.map { "$namespace:$it" }.toTypedArray()), dataPath("tags/item/lavender_stems.json"))
        writes += save(cache, valuesTag("$namespace:lavender_endspar"), minecraftDataPath("tags/block/nylium.json"))
        writes += save(cache, valuesTag(*dragonImmuneBlocks.map { "$namespace:$it" }.toTypedArray()), minecraftDataPath("tags/block/dragon_immune.json"))
        writes += save(cache, valuesTag("$namespace:lavender_endspar"), dataPath("tags/block/supports_lavender_roots.json"))
        writes += save(cache, valuesTag("$namespace:lavender_endspar"), dataPath("tags/block/supports_lavender_fungus.json"))
        writes += save(cache, lavenderVegetationFeature(), dataPath("worldgen/configured_feature/lavender_endspar_vegetation_bonemeal.json"))
        writes += save(cache, lavenderFungusFeature(), dataPath("worldgen/configured_feature/lavender_fungus_planted.json"))

        writes += save(cache, hyphaeRecipe("lavender_stem", "lavender_hyphae"), dataPath("recipe/lavender_hyphae.json"))
        writes += save(cache, shapedRecipe("lavender_wart_block", "building", arrayOf("###", "###", "###"), "#", "lavender_planks"), dataPath("recipe/lavender_wart_block.json"))
        writes += save(cache, recipeAdvancement("lavender_wart_block", "building", "lavender_planks"), dataPath("advancement/recipes/building/lavender_wart_block.json"))
        writes += save(cache, recipeAdvancement("lavender_hyphae", "building_blocks", "lavender_stem"), dataPath("advancement/recipes/building_blocks/lavender_hyphae.json"))
        writes += save(cache, hyphaeRecipe("stripped_lavender_stem", "stripped_lavender_hyphae"), dataPath("recipe/stripped_lavender_hyphae.json"))
        writes += save(cache, shapedRecipe("lavender_wood", "building", arrayOf("##", "##"), "#", "lavender_stem"), dataPath("recipe/lavender_wood.json"))
        writes += save(cache, shapedRecipe("stripped_lavender_wood", "building", arrayOf("##", "##"), "#", "stripped_lavender_stem"), dataPath("recipe/stripped_lavender_wood.json"))
        writes += save(cache, shapelessRecipe("lavender_planks", 4, "lavender_stem"), dataPath("recipe/lavender_planks.json"))
        writes += save(cache, shapedRecipe("lavender_slab", "building", arrayOf("###"), "#", "lavender_planks"), dataPath("recipe/lavender_slab.json"))
        writes += save(cache, shapedRecipe("lavender_stairs", "building", arrayOf("#  ", "## ", "###"), "#", "lavender_planks"), dataPath("recipe/lavender_stairs.json"))
        writes += save(cache, twoIngredientRecipe("lavender_fence", "building", arrayOf("#S#", "#S#"), "#", "lavender_planks", "S", "minecraft:stick"), dataPath("recipe/lavender_fence.json"))
        writes += save(cache, twoIngredientRecipe("lavender_fence_gate", "redstone", arrayOf("S#S", "S#S"), "#", "lavender_planks", "S", "minecraft:stick"), dataPath("recipe/lavender_fence_gate.json"))
        writes += save(cache, shapedRecipe("lavender_door", "redstone", arrayOf("##", "##", "##"), "#", "lavender_planks"), dataPath("recipe/lavender_door.json"))
        writes += save(cache, shapedRecipe("lavender_trapdoor", "redstone", arrayOf("###", "###"), "#", "lavender_planks"), dataPath("recipe/lavender_trapdoor.json"))
        writes += save(cache, shapedRecipe("lavender_pressure_plate", "redstone", arrayOf("##"), "#", "lavender_planks"), dataPath("recipe/lavender_pressure_plate.json"))
        writes += save(cache, shapedRecipe("lavender_button", "redstone", arrayOf("#"), "#", "lavender_planks"), dataPath("recipe/lavender_button.json"))
        writes += save(cache, twoIngredientRecipe("lavender_sign", "misc", arrayOf("###", "###", " S "), "#", "lavender_planks", "S", "minecraft:stick").apply { getAsJsonObject("result").addProperty("count", 3) }, dataPath("recipe/lavender_sign.json"))
        writes += save(cache, shapedRecipe("lavender_boat", "misc", arrayOf("# #", "###"), "#", "lavender_planks"), dataPath("recipe/lavender_boat.json"))
        writes += save(cache, shapelessRecipe("lavender_chest_boat", 1, "lavender_boat").apply { getAsJsonArray("ingredients").add("minecraft:chest") }, dataPath("recipe/lavender_chest_boat.json"))
        writes += save(cache, twoIngredientRecipe("lavender_hanging_sign", "misc", arrayOf("C C", "###", "###"), "#", "stripped_lavender_stem", "C", "minecraft:iron_chain").apply { getAsJsonObject("result").addProperty("count", 6) }, dataPath("recipe/lavender_hanging_sign.json"))
        writes += save(cache, recipeAdvancement("stripped_lavender_hyphae", "building_blocks", "stripped_lavender_stem"), dataPath("advancement/recipes/building_blocks/stripped_lavender_hyphae.json"))

        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String): JsonObject = when (id) {
        "lavender_wart_block" -> variants { add("", variant(id)) }
        "lavender_endspar", "lavender_roots", "lavender_fungus" -> variants { add("", variant(id)) }
        "lavender_stem", "stripped_lavender_stem", "lavender_wood", "stripped_lavender_wood" -> variants {
            add("axis=x", variant(id, x = 90, y = 90)); add("axis=y", variant(id)); add("axis=z", variant(id, x = 90))
        }
        "lavender_hyphae", "stripped_lavender_hyphae" -> variants {
            add("axis=x", variant(id, x = 90, y = 90)); add("axis=y", variant(id)); add("axis=z", variant(id, x = 90))
        }
        "lavender_slab" -> variants { add("type=bottom", variant("lavender_slab")); add("type=double", variant("lavender_planks")); add("type=top", variant("lavender_slab_top")) }
        "lavender_stairs" -> stairsBlockState()
        "lavender_fence" -> fenceBlockState()
        "lavender_fence_gate" -> fenceGateBlockState()
        "lavender_door" -> doorBlockState()
        "lavender_trapdoor" -> trapdoorBlockState()
        "lavender_pressure_plate" -> variants { add("powered=false", variant("lavender_pressure_plate")); add("powered=true", variant("lavender_pressure_plate_down")) }
        "lavender_button" -> buttonBlockState()
        "lavender_sign" -> variants { (0..15).forEach { add("rotation=$it", variant("lavender_sign_rot_${it % 4}", y = it / 4 * 90)) } }
        "lavender_hanging_sign" -> variants { listOf(false, true).forEach { attached -> (0..15).forEach { rotation -> add("attached=$attached,rotation=$rotation", variant("lavender_hanging_sign_${if (attached) "attached_" else ""}rot_${rotation % 4}", y = rotation / 4 * 90)) } } }
        "lavender_wall_sign" -> variants { FACING_Y.forEach { (facing, y) -> add("facing=$facing", variant("lavender_wall_sign", y = y)) } }
        "lavender_wall_hanging_sign" -> variants { FACING_Y.forEach { (facing, y) -> add("facing=$facing", variant("lavender_wall_hanging_sign", y = y)) } }
        else -> variants { add("", variant(id)) }
    }

    private fun variants(block: JsonObject.() -> Unit) = obj { add("variants", obj(block)) }

    private fun stairsBlockState() = variants {
        HORIZONTAL_Y.forEach { (facing, base) -> listOf("bottom" to 0, "top" to 180).forEach { (half, x) -> STAIR_SHAPES.forEach { (shape, model) -> add("facing=$facing,half=$half,shape=$shape", variant(model, x = x, y = base, uvlock = x != 0 || base != 0)) } } }
    }

    private fun fenceBlockState() = obj {
        add("multipart", com.google.gson.JsonArray().also { parts ->
            parts.add(obj { add("apply", variant("lavender_fence_post")) })
            listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (side, y) -> parts.add(obj { add("apply", variant("lavender_fence_side", y = y, uvlock = true)); add("when", obj { addProperty(side, "true") }) }) }
        })
    }

    private fun fenceGateBlockState() = variants { FACING_Y.forEach { (facing, y) -> listOf(false, true).forEach { wall -> listOf(false, true).forEach { open ->
        val suffix = (if (wall) "_wall" else "") + (if (open) "_open" else "")
        add("facing=$facing,in_wall=$wall,open=$open", variant("lavender_fence_gate$suffix", y = y, uvlock = true))
    } } } }

    private fun doorBlockState() = variants { HORIZONTAL_Y.forEach { (facing, base) -> listOf("lower" to "bottom", "upper" to "top").forEach { (half, part) -> listOf("left" to 90, "right" to 270).forEach { (hinge, offset) -> add("facing=$facing,half=$half,hinge=$hinge,open=false", variant("lavender_door_${part}_$hinge", y = base)); add("facing=$facing,half=$half,hinge=$hinge,open=true", variant("lavender_door_${part}_${hinge}_open", y = (base + offset) % 360)) } } } }

    private fun trapdoorBlockState() = variants { ROTATION_Y.forEach { (facing, y) -> listOf("bottom", "top").forEach { half -> add("facing=$facing,half=$half,open=false", variant("lavender_trapdoor_$half")); add("facing=$facing,half=$half,open=true", variant("lavender_trapdoor_open", y = y)) } } }

    private fun buttonBlockState() = variants { listOf("floor" to 0, "wall" to 90, "ceiling" to 180).forEach { (face, x) -> ROTATION_Y.forEach { (facing, y) -> listOf(false, true).forEach { powered -> add("face=$face,facing=$facing,powered=$powered", variant(if (powered) "lavender_button_pressed" else "lavender_button", x = x, y = if (face == "ceiling") (y + 180) % 360 else y, uvlock = face == "wall")) } } } }

    private fun hyphaeRecipe(stem: String, hyphae: String) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "building")
        addProperty("group", "bark")
        add("key", obj { addProperty("#", "$namespace:$stem") })
        add("pattern", array("##", "##"))
        add("result", itemResult(hyphae, 3))
    }

    private fun twoIngredientRecipe(result: String, category: String, pattern: Array<String>, key: String, ingredient: String, secondKey: String, secondIngredient: String) = shapedRecipe(result, category, pattern, key, ingredient).apply {
        getAsJsonObject("key").addProperty(secondKey, secondIngredient)
    }

    private fun itemModel(id: String) = when (id) {
        "lavender_roots", "lavender_fungus" -> obj { addProperty("parent", "$namespace:block/$id") }
        "lavender_door" -> flatItemModel("$namespace:item/lavender_door")
        "lavender_sign" -> flatItemModel("$namespace:item/lavender_sign")
        "lavender_hanging_sign" -> flatItemModel("$namespace:item/lavender_hanging_sign")
        "lavender_fence" -> obj { addProperty("parent", "$namespace:block/lavender_fence_inventory") }
        "lavender_button" -> obj { addProperty("parent", "$namespace:block/lavender_button_inventory") }
        "lavender_trapdoor" -> obj { addProperty("parent", "$namespace:block/lavender_trapdoor_bottom") }
        "lavender_boat", "lavender_chest_boat" -> flatItemModel("$namespace:item/$id")
        else -> obj { addProperty("parent", "$namespace:block/$id") }
    }

    private fun blockModels(): Map<String, JsonObject> {
        val models = linkedMapOf<String, JsonObject>()
        models["lavender_wart_block"] = model("minecraft:block/cube_all", "all" to "$namespace:block/lavender_wart_block")
        models["lavender_endspar"] = model("minecraft:block/cube_bottom_top", "bottom" to "minecraft:block/end_stone", "side" to "$namespace:block/lavender_endspar_side", "top" to "$namespace:block/lavender_endspar")
        models["lavender_roots"] = model("minecraft:block/cross", "cross" to "$namespace:block/lavender_roots")
        models["lavender_fungus"] = model("minecraft:block/cross", "cross" to "$namespace:block/lavender_fungus")
        models["lavender_stem"] = model("minecraft:block/cube_column", "end" to "$namespace:block/lavender_stem_top", "side" to "$namespace:block/lavender_stem")
        models["stripped_lavender_stem"] = model("minecraft:block/cube_column", "end" to "$namespace:block/stripped_lavender_stem_top", "side" to "$namespace:block/stripped_lavender_stem")
        models["lavender_hyphae"] = model("minecraft:block/cube_all", "all" to "$namespace:block/lavender_stem")
        models["stripped_lavender_hyphae"] = model("minecraft:block/cube_all", "all" to "$namespace:block/stripped_lavender_stem")
        models["lavender_wood"] = model("minecraft:block/cube_all", "all" to "$namespace:block/lavender_stem")
        models["stripped_lavender_wood"] = model("minecraft:block/cube_all", "all" to "$namespace:block/stripped_lavender_stem")
        models["lavender_planks"] = model("minecraft:block/cube_all", "all" to "$namespace:block/lavender_planks")
        models["lavender_slab"] = plankSided("minecraft:block/slab")
        models["lavender_slab_top"] = plankSided("minecraft:block/slab_top")
        models["lavender_stairs"] = plankSided("minecraft:block/stairs")
        models["lavender_stairs_inner"] = plankSided("minecraft:block/inner_stairs")
        models["lavender_stairs_outer"] = plankSided("minecraft:block/outer_stairs")
        models["lavender_fence_post"] = plankTextured("minecraft:block/fence_post")
        models["lavender_fence_side"] = plankTextured("minecraft:block/fence_side")
        models["lavender_fence_inventory"] = plankTextured("minecraft:block/fence_inventory")
        models["lavender_fence_gate"] = plankTextured("minecraft:block/template_fence_gate")
        models["lavender_fence_gate_open"] = plankTextured("minecraft:block/template_fence_gate_open")
        models["lavender_fence_gate_wall"] = plankTextured("minecraft:block/template_fence_gate_wall")
        models["lavender_fence_gate_wall_open"] = plankTextured("minecraft:block/template_fence_gate_wall_open")
        models["lavender_pressure_plate"] = plankTextured("minecraft:block/pressure_plate_up")
        models["lavender_pressure_plate_down"] = plankTextured("minecraft:block/pressure_plate_down")
        models["lavender_button"] = plankTextured("minecraft:block/button")
        models["lavender_button_pressed"] = plankTextured("minecraft:block/button_pressed")
        models["lavender_button_inventory"] = plankTextured("minecraft:block/button_inventory")
        listOf("bottom_left", "bottom_left_open", "bottom_right", "bottom_right_open", "top_left", "top_left_open", "top_right", "top_right_open").forEach { part ->
            models["lavender_door_$part"] = model("minecraft:block/door_$part", "bottom" to "$namespace:block/lavender_door_bottom", "top" to "$namespace:block/lavender_door_top")
        }
        listOf("bottom", "top", "open").forEach { part -> models["lavender_trapdoor_$part"] = model("minecraft:block/template_trapdoor_$part", "texture" to "$namespace:block/lavender_trapdoor") }
        (0..3).forEach { rot ->
            models["lavender_sign_rot_$rot"] = model("minecraft:block/template_sign_rot_$rot", "all" to "$namespace:block/lavender_sign", "particle" to "$namespace:block/lavender_planks")
            models["lavender_hanging_sign_rot_$rot"] = model("minecraft:block/template_hanging_sign_rot_$rot", "all" to "$namespace:block/lavender_hanging_sign", "particle" to "$namespace:block/stripped_lavender_stem")
            models["lavender_hanging_sign_attached_rot_$rot"] = model("minecraft:block/template_attached_hanging_sign_rot_$rot", "all" to "$namespace:block/lavender_hanging_sign", "particle" to "$namespace:block/stripped_lavender_stem")
        }
        models["lavender_wall_sign"] = model("minecraft:block/template_wall_sign", "all" to "$namespace:block/lavender_sign", "particle" to "$namespace:block/lavender_planks")
        models["lavender_wall_hanging_sign"] = model("minecraft:block/template_wall_hanging_sign", "all" to "$namespace:block/lavender_hanging_sign", "particle" to "$namespace:block/stripped_lavender_stem")
        return models
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (slot, texture) -> addProperty(slot, texture) } })
    }

    private fun plankTextured(parent: String) = model(parent, "texture" to "$namespace:block/lavender_planks")
    private fun plankSided(parent: String) = model(parent, "bottom" to "$namespace:block/lavender_planks", "side" to "$namespace:block/lavender_planks", "top" to "$namespace:block/lavender_planks")
    private fun flatItemModel(texture: String) = obj { addProperty("parent", "minecraft:item/generated"); add("textures", obj { addProperty("layer0", texture) }) }

    private fun lavenderVegetationFeature() = obj {
        addProperty("type", "minecraft:nether_forest_vegetation")
        add("config", obj {
            addProperty("spread_height", 1)
            addProperty("spread_width", 3)
            add("state_provider", obj {
                addProperty("type", "minecraft:weighted_state_provider")
                add("entries", com.google.gson.JsonArray().also { entries ->
                    entries.add(obj { add("data", obj { addProperty("Name", "$namespace:lavender_roots") }); addProperty("weight", 85) })
                    entries.add(obj { add("data", obj { addProperty("Name", "$namespace:lavender_fungus") }); addProperty("weight", 15) })
                })
            })
        })
    }

    private fun lavenderFungusFeature() = obj {
        addProperty("type", "minecraft:huge_fungus")
        add("config", obj {
            add("decor_state", obj { addProperty("Name", "minecraft:shroomlight") })
            add("hat_state", obj { addProperty("Name", "$namespace:lavender_wart_block") })
            addProperty("planted", true)
            add("replaceable_blocks", obj {
                addProperty("type", "minecraft:matching_blocks")
                add("blocks", array(*HUGE_FUNGUS_REPLACEABLE_BLOCKS))
            })
            add("stem_state", obj { addProperty("Name", "$namespace:lavender_stem"); add("Properties", obj { addProperty("axis", "y") }) })
            add("valid_base_block", obj { addProperty("Name", "$namespace:lavender_endspar") })
        })
    }

    // Mirrors the replaceable block list vanilla uses for huge warped/crimson fungi,
    // plus this mod's lavender vegetation that can generate next to the fungus.
    private val HUGE_FUNGUS_REPLACEABLE_BLOCKS = arrayOf(
        "$namespace:lavender_roots",
        "$namespace:lavender_fungus",
        "minecraft:oak_sapling",
        "minecraft:spruce_sapling",
        "minecraft:birch_sapling",
        "minecraft:jungle_sapling",
        "minecraft:acacia_sapling",
        "minecraft:cherry_sapling",
        "minecraft:dark_oak_sapling",
        "minecraft:pale_oak_sapling",
        "minecraft:mangrove_propagule",
        "minecraft:dandelion",
        "minecraft:torchflower",
        "minecraft:poppy",
        "minecraft:blue_orchid",
        "minecraft:allium",
        "minecraft:azure_bluet",
        "minecraft:red_tulip",
        "minecraft:orange_tulip",
        "minecraft:white_tulip",
        "minecraft:pink_tulip",
        "minecraft:oxeye_daisy",
        "minecraft:cornflower",
        "minecraft:wither_rose",
        "minecraft:lily_of_the_valley",
        "minecraft:brown_mushroom",
        "minecraft:red_mushroom",
        "minecraft:wheat",
        "minecraft:sugar_cane",
        "minecraft:attached_pumpkin_stem",
        "minecraft:attached_melon_stem",
        "minecraft:pumpkin_stem",
        "minecraft:melon_stem",
        "minecraft:lily_pad",
        "minecraft:nether_wart",
        "minecraft:cocoa",
        "minecraft:carrots",
        "minecraft:potatoes",
        "minecraft:chorus_plant",
        "minecraft:chorus_flower",
        "minecraft:torchflower_crop",
        "minecraft:pitcher_crop",
        "minecraft:beetroots",
        "minecraft:sweet_berry_bush",
        "minecraft:warped_fungus",
        "minecraft:crimson_fungus",
        "minecraft:weeping_vines",
        "minecraft:weeping_vines_plant",
        "minecraft:twisting_vines",
        "minecraft:twisting_vines_plant",
        "minecraft:cave_vines",
        "minecraft:cave_vines_plant",
        "minecraft:spore_blossom",
        "minecraft:azalea",
        "minecraft:flowering_azalea",
        "minecraft:moss_carpet",
        "minecraft:pink_petals",
        "minecraft:wildflowers",
        "minecraft:big_dripleaf",
        "minecraft:big_dripleaf_stem",
        "minecraft:small_dripleaf",
    )

    private val FACING_Y = linkedMapOf("south" to 0, "west" to 90, "north" to 180, "east" to 270)
    private val HORIZONTAL_Y = linkedMapOf("east" to 0, "south" to 90, "west" to 180, "north" to 270)
    private val ROTATION_Y = linkedMapOf("north" to 0, "east" to 90, "south" to 180, "west" to 270)
    private val STAIR_SHAPES = linkedMapOf(
        "straight" to "lavender_stairs",
        "inner_left" to "lavender_stairs_inner",
        "inner_right" to "lavender_stairs_inner",
        "outer_left" to "lavender_stairs_outer",
        "outer_right" to "lavender_stairs_outer",
    )

    private fun variant(id: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) = obj {
        addProperty("model", "$namespace:block/$id")
        if (uvlock) addProperty("uvlock", true)
        if (x != 0) addProperty("x", x)
        if (y != 0) addProperty("y", y)
    }

    override fun getName() = "SupremeMC lavender resources"
}