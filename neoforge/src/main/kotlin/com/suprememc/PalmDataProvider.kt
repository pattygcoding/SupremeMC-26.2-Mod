package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class PalmDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val blocks = arrayOf("palm_log", "stripped_palm_log", "palm_wood", "stripped_palm_wood", "palm_planks", "palm_slab", "palm_stairs", "palm_fence", "palm_fence_gate", "palm_door", "palm_trapdoor", "palm_pressure_plate", "palm_button", "palm_sign", "palm_wall_sign", "palm_hanging_sign", "palm_wall_hanging_sign", "palm_leaves", "palm_sapling", "potted_palm_sapling")

    private val planksTexture = "$namespace:block/palm_planks"
    private val strippedLogTexture = "$namespace:block/stripped_palm_log"
    private val signTexture = "$namespace:block/palm_sign"
    private val hangingSignTexture = "$namespace:block/palm_hanging_sign"

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        blocks.forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, itemModel(id), resourcePath("models/item/$id.json"))
            // Grayscale leaves texture needs the item icon tinted too, matching the in-hand oak leaves color.
            val tints = if (id == "palm_leaves") array(obj { addProperty("type", "minecraft:constant"); addProperty("value", -12012264) }) else null
            writes += save(cache, itemModelDefinition("$namespace:item/$id", tints), resourcePath("items/$id.json"))
        }
        blockModels().forEach { (name, model) -> writes += save(cache, model, resourcePath("models/block/$name.json")) }
        arrayOf("palm_log", "stripped_palm_log", "palm_wood", "stripped_palm_wood").forEach { id ->
            writes += save(cache, woodSmeltingLootTable(id), dataPath("loot_table/blocks/$id.json"))
        }
        arrayOf("palm_planks", "palm_stairs", "palm_fence", "palm_fence_gate", "palm_door", "palm_trapdoor", "palm_pressure_plate", "palm_button", "palm_sapling").forEach { id ->
            writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
        }
        // Slabs drop 2 when the placed block is the double variant, matching vanilla oak_slab.json.
        writes += save(cache, slabLootTable("palm_slab"), dataPath("loot_table/blocks/palm_slab.json"))
        // Wall variants have no item of their own, so they drop the standing sign like vanilla does.
        writes += save(cache, selfDropLootTable("palm_sign"), dataPath("loot_table/blocks/palm_sign.json"))
        writes += save(cache, selfDropLootTable("palm_sign"), dataPath("loot_table/blocks/palm_wall_sign.json"))
        writes += save(cache, selfDropLootTable("palm_hanging_sign"), dataPath("loot_table/blocks/palm_hanging_sign.json"))
        writes += save(cache, selfDropLootTable("palm_hanging_sign"), dataPath("loot_table/blocks/palm_wall_hanging_sign.json"))
        arrayOf("palm_boat", "palm_chest_boat").forEach { id ->
            writes += save(cache, boatItemModel(id), resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$id"), resourcePath("items/$id.json"))
        }
        writes += save(cache, planksRecipe(), dataPath("recipe/palm_planks.json"))
        writes += save(cache, recipeAdvancement("palm_planks", "building", "palm_log"), dataPath("advancement/recipes/building/palm_planks.json"))
        writes += save(cache, woodRecipe(), dataPath("recipe/palm_log_to_wood.json"))
        writes += save(cache, recipeAdvancement("palm_log_to_wood", "building", "palm_log"), dataPath("advancement/recipes/building/palm_log_to_wood.json"))
        writes += save(cache, strippedWoodRecipe(), dataPath("recipe/stripped_palm_log_to_stripped_palm_wood.json"))
        writes += save(cache, recipeAdvancement("stripped_palm_log_to_stripped_palm_wood", "building", "stripped_palm_log"), dataPath("advancement/recipes/building/stripped_palm_log_to_stripped_palm_wood.json"))
        writes += save(cache, sticksRecipe(), dataPath("recipe/palm_sticks.json"))
        writes += save(cache, recipeAdvancement("palm_sticks", "misc", "palm_planks"), dataPath("advancement/recipes/misc/palm_sticks.json"))
        writes += save(cache, slabRecipe(), dataPath("recipe/palm_slab.json"))
        writes += save(cache, recipeAdvancement("palm_slab", "building", "palm_planks"), dataPath("advancement/recipes/building/palm_slab.json"))
        writes += save(cache, stairsRecipe(), dataPath("recipe/palm_stairs.json"))
        writes += save(cache, recipeAdvancement("palm_stairs", "building", "palm_planks"), dataPath("advancement/recipes/building/palm_stairs.json"))
        writes += save(cache, fenceRecipe(), dataPath("recipe/palm_fence.json"))
        writes += save(cache, recipeAdvancement("palm_fence", "building", "palm_planks"), dataPath("advancement/recipes/building/palm_fence.json"))
        writes += save(cache, fenceGateRecipe(), dataPath("recipe/palm_fence_gate.json"))
        writes += save(cache, recipeAdvancement("palm_fence_gate", "building", "palm_planks"), dataPath("advancement/recipes/building/palm_fence_gate.json"))
        writes += save(cache, doorRecipe(), dataPath("recipe/palm_door.json"))
        writes += save(cache, recipeAdvancement("palm_door", "redstone", "palm_planks"), dataPath("advancement/recipes/redstone/palm_door.json"))
        writes += save(cache, trapdoorRecipe(), dataPath("recipe/palm_trapdoor.json"))
        writes += save(cache, recipeAdvancement("palm_trapdoor", "redstone", "palm_planks"), dataPath("advancement/recipes/redstone/palm_trapdoor.json"))
        writes += save(cache, pressurePlateRecipe(), dataPath("recipe/palm_pressure_plate.json"))
        writes += save(cache, recipeAdvancement("palm_pressure_plate", "redstone", "palm_planks"), dataPath("advancement/recipes/redstone/palm_pressure_plate.json"))
        writes += save(cache, buttonRecipe(), dataPath("recipe/palm_button.json"))
        writes += save(cache, recipeAdvancement("palm_button", "redstone", "palm_planks"), dataPath("advancement/recipes/redstone/palm_button.json"))
        writes += save(cache, signRecipe(), dataPath("recipe/palm_sign.json"))
        writes += save(cache, recipeAdvancement("palm_sign", "misc", "palm_planks"), dataPath("advancement/recipes/misc/palm_sign.json"))
        writes += save(cache, boatRecipe(), dataPath("recipe/palm_boat.json"))
        writes += save(cache, recipeAdvancement("palm_boat", "misc", "palm_planks"), dataPath("advancement/recipes/misc/palm_boat.json"))
        writes += save(cache, chestBoatRecipe(), dataPath("recipe/palm_chest_boat.json"))
        writes += save(cache, recipeAdvancement("palm_chest_boat", "misc", "palm_boat"), dataPath("advancement/recipes/misc/palm_chest_boat.json"))
        writes += save(cache, hangingSignRecipe(), dataPath("recipe/palm_hanging_sign.json"))
        writes += save(cache, recipeAdvancement("palm_hanging_sign", "misc", "stripped_palm_log"), dataPath("advancement/recipes/misc/palm_hanging_sign.json"))
        writes += save(cache, leavesLoot(), dataPath("loot_table/blocks/palm_leaves.json"))
        writeWoodFamilyTags(cache, writes)
        writeVanillaDoorRecipes(cache, writes)
        writes += save(cache, valuesTag("$namespace:palm_leaves"), minecraftDataPath("tags/item/leaves.json"))
        writes += save(cache, valuesTag("$namespace:palm_sapling"), minecraftDataPath("tags/item/saplings.json"))
        writeWorldgen(cache, writes)
        writeCoconut(cache, writes)
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    // Registers palm blocks/items into vanilla's wood-family tags (mineable/axe is built from these, not written to directly).
    private fun writeWoodFamilyTags(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        val logs = arrayOf("$namespace:palm_log", "$namespace:stripped_palm_log", "$namespace:palm_wood", "$namespace:stripped_palm_wood")
        writes += save(cache, valuesTag(*logs), dataPath("tags/block/palm_logs.json"))
        writes += save(cache, valuesTag(*logs), dataPath("tags/item/palm_logs.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/block/logs.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/block/logs_that_burn.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/block/overworld_natural_logs.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/block/completes_find_tree_tutorial.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/item/logs.json"))
        writes += save(cache, valuesTag("#$namespace:palm_logs"), minecraftDataPath("tags/item/logs_that_burn.json"))
        listOf(
            "planks" to "palm_planks",
            "wooden_slabs" to "palm_slab",
            "wooden_stairs" to "palm_stairs",
            "wooden_fences" to "palm_fence",
            "fence_gates" to "palm_fence_gate",
            "wooden_doors" to "palm_door",
            "wooden_trapdoors" to "palm_trapdoor",
            "wooden_pressure_plates" to "palm_pressure_plate",
            "wooden_buttons" to "palm_button"
        ).forEach { (tag, id) ->
            writes += save(cache, valuesTag("$namespace:$id"), minecraftDataPath("tags/block/$tag.json"))
            writes += save(cache, valuesTag("$namespace:$id"), minecraftDataPath("tags/item/$tag.json"))
        }
        writes += save(cache, valuesTag("#minecraft:wooden_slabs"), minecraftDataPath("tags/block/slabs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_slabs"), minecraftDataPath("tags/item/slabs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_stairs"), minecraftDataPath("tags/block/stairs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_stairs"), minecraftDataPath("tags/item/stairs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_fences"), minecraftDataPath("tags/block/fences.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_fences"), minecraftDataPath("tags/item/fences.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_doors"), minecraftDataPath("tags/block/doors.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_doors"), minecraftDataPath("tags/item/doors.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_trapdoors"), minecraftDataPath("tags/block/trapdoors.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_trapdoors"), minecraftDataPath("tags/item/trapdoors.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_buttons"), minecraftDataPath("tags/block/buttons.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_buttons"), minecraftDataPath("tags/item/buttons.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_pressure_plates"), minecraftDataPath("tags/block/pressure_plates.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_pressure_plates"), minecraftDataPath("tags/item/pressure_plates.json"))
        writes += save(cache, valuesTag("#minecraft:signs", "#minecraft:all_hanging_signs"), minecraftDataPath("tags/block/all_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_sign"), minecraftDataPath("tags/block/standing_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_wall_sign"), minecraftDataPath("tags/block/wall_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_sign", "$namespace:palm_wall_sign"), minecraftDataPath("tags/block/signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_sign"), minecraftDataPath("tags/item/signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_hanging_sign"), minecraftDataPath("tags/block/ceiling_hanging_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_wall_hanging_sign"), minecraftDataPath("tags/block/wall_hanging_signs.json"))
        writes += save(cache, valuesTag("#minecraft:ceiling_hanging_signs", "#minecraft:wall_hanging_signs"), minecraftDataPath("tags/block/all_hanging_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_hanging_sign"), minecraftDataPath("tags/item/hanging_signs.json"))
        writes += save(cache, valuesTag("$namespace:palm_boat"), minecraftDataPath("tags/item/boats.json"))
        writes += save(cache, valuesTag("$namespace:palm_chest_boat"), minecraftDataPath("tags/item/chest_boats.json"))
        writes += save(cache, valuesTag("$namespace:palm_bookshelf", "$namespace:palm_crafting_table"), minecraftDataPath("tags/block/mineable/axe.json"))
    }

    private fun writeVanillaDoorRecipes(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        val woodenDoors = listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "pale_oak", "bamboo", "crimson", "warped")
        woodenDoors.forEach { wood ->
            val recipe = minecraftShapedRecipe("${wood}_door", 2, "redstone", arrayOf("##", "##", "##"), "#", "${wood}_planks", group = "wooden_door")
            writes += save(cache, recipe, minecraftDataPath("recipe/${wood}_door.json"))
        }
        writes += save(cache, minecraftShapedRecipe("iron_door", 2, "redstone", arrayOf("##", "##", "##"), "#", "iron_ingot"), minecraftDataPath("recipe/iron_door.json"))
        writes += save(cache, minecraftShapedRecipe("copper_door", 2, "redstone", arrayOf("##", "##", "##"), "#", "copper_ingot"), minecraftDataPath("recipe/copper_door.json"))
    }

    private fun writeCoconut(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        writes += save(cache, coconutBlockState(), resourcePath("blockstates/coconut.json"))
        (0..2).forEach { age ->
            writes += save(cache, model("minecraft:block/cocoa_stage$age", "cocoa" to "$namespace:block/coconut_stage$age", "particle" to "$namespace:block/coconut_stage$age"), resourcePath("models/block/coconut_stage$age.json"))
        }
        writes += save(cache, flatItemModel("$namespace:item/coconut"), resourcePath("models/item/coconut.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/coconut"), resourcePath("items/coconut.json"))
        writes += save(cache, flatItemModel("$namespace:item/coconut_seeds"), resourcePath("models/item/coconut_seeds.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/coconut_seeds"), resourcePath("items/coconut_seeds.json"))
        writes += save(cache, shapelessRecipe("coconut_seeds", 4, "coconut"), dataPath("recipe/coconut_seeds.json"))
        writes += save(cache, recipeAdvancement("coconut_seeds", "misc", "coconut"), dataPath("advancement/recipes/misc/coconut_seeds.json"))
    }

    // Mirrors vanilla cocoa's facing/rotation mapping so the pod points away from its supporting log.
    private fun coconutBlockState() = variants {
        listOf("north" to 180, "south" to 0, "east" to 270, "west" to 90).forEach { (facing, rotation) ->
            (0..2).forEach { age ->
                add("age=$age,facing=$facing", variant("coconut_stage$age", y = rotation))
            }
        }
    }

    private fun writeWorldgen(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        writes += save(cache, placedFeature("palm_coconut", warmPlacement()), dataPath("worldgen/placed_feature/palm_trees_warm.json"))
        writes += save(cache, placedFeature("palm", temperatePlacement()), dataPath("worldgen/placed_feature/palm_trees_temperate.json"))
        writes += save(cache, addFeaturesBiomeModifier("palm_trees_warm"), dataPath("neoforge/biome_modifier/add_palm_trees_warm.json"))
        writes += save(cache, addFeaturesBiomeModifier("palm_trees_temperate"), dataPath("neoforge/biome_modifier/add_palm_trees_temperate.json"))
    }

    private fun placedFeature(feature: String, placement: JsonArray) = obj {
        addProperty("feature", "$namespace:$feature")
        add("placement", placement)
    }

    private fun addFeaturesBiomeModifier(feature: String) = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", array("#minecraft:is_beach"))
        addProperty("features", "$namespace:$feature")
        addProperty("step", "vegetal_decoration")
    }

    // Warm beaches: every eligible chunk rolls 1-3 palms, coconut-bearing.
    // Order matters: heightmap must snap to the surface before the temperature/substrate checks sample it.
    private fun warmPlacement() = JsonArray().also {
        it.add(obj { addProperty("type", "minecraft:count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min_inclusive", 1); addProperty("max_inclusive", 3) }) })
        it.add(obj { addProperty("type", "minecraft:in_square") })
        it.add(obj { addProperty("type", "minecraft:surface_water_depth_filter"); addProperty("max_water_depth", 0) })
        it.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "OCEAN_FLOOR") })
        it.add(obj { addProperty("type", "suprememc:biome_temperature"); addProperty("min_temperature", 0.8f) })
        it.add(beachSubstrateFilter())
        it.add(obj { addProperty("type", "minecraft:biome") })
    }

    // Moderate beaches: half the chunks roll, and only a single sapling-backed palm at a time.
    private fun temperatePlacement() = JsonArray().also {
        it.add(obj { addProperty("type", "minecraft:rarity_filter"); addProperty("chance", 2) })
        it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 1) })
        it.add(obj { addProperty("type", "minecraft:in_square") })
        it.add(obj { addProperty("type", "minecraft:surface_water_depth_filter"); addProperty("max_water_depth", 0) })
        it.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "OCEAN_FLOOR") })
        it.add(obj { addProperty("type", "suprememc:biome_temperature"); addProperty("min_temperature", 0.3f); addProperty("max_temperature", 0.8f) })
        it.add(beachSubstrateFilter())
        it.add(obj { addProperty("type", "minecraft:biome") })
    }

    // Palms only take root on sand/red sand where the sapling itself would survive.
    private fun beachSubstrateFilter() = obj {
        addProperty("type", "minecraft:block_predicate_filter")
        add("predicate", obj {
            addProperty("type", "minecraft:all_of")
            add("predicates", JsonArray().also { predicates ->
                predicates.add(obj {
                    addProperty("type", "minecraft:matching_blocks")
                    add("blocks", array("minecraft:sand", "minecraft:red_sand"))
                    add("offset", JsonArray().also { o -> o.add(0); o.add(-1); o.add(0) })
                })
                predicates.add(obj {
                    addProperty("type", "minecraft:would_survive")
                    add("state", obj { addProperty("Name", "$namespace:palm_sapling") })
                })
            })
        })
    }

    private fun blockState(id: String): JsonObject = when (id) {
        "palm_log", "stripped_palm_log", "palm_wood", "stripped_palm_wood" -> variants {
            add("axis=x", variant(id, x = 90, y = 90))
            add("axis=y", variant(id))
            add("axis=z", variant(id, x = 90))
        }
        "palm_slab" -> variants {
            add("type=bottom", variant("palm_slab"))
            add("type=double", variant("palm_planks"))
            add("type=top", variant("palm_slab_top"))
        }
        "palm_stairs" -> stairsBlockState()
        "palm_fence" -> fenceBlockState()
        "palm_fence_gate" -> fenceGateBlockState()
        "palm_door" -> doorBlockState()
        "palm_trapdoor" -> trapdoorBlockState()
        "palm_pressure_plate" -> variants {
            add("powered=false", variant("palm_pressure_plate"))
            add("powered=true", variant("palm_pressure_plate_down"))
        }
        "palm_button" -> buttonBlockState()
        "palm_sign" -> variants {
            (0..15).forEach { add("rotation=$it", variant("palm_sign_rot_${it % 4}", y = it / 4 * 90)) }
        }
        "palm_hanging_sign" -> variants {
            listOf(false, true).forEach { attached ->
                val prefix = if (attached) "palm_hanging_sign_attached_rot_" else "palm_hanging_sign_rot_"
                (0..15).forEach { add("attached=$attached,rotation=$it", variant("$prefix${it % 4}", y = it / 4 * 90)) }
            }
        }
        "palm_wall_sign" -> variants {
            FACING_Y.forEach { (facing, y) -> add("facing=$facing", variant("palm_wall_sign", y = y)) }
        }
        "palm_wall_hanging_sign" -> variants {
            FACING_Y.forEach { (facing, y) -> add("facing=$facing", variant("palm_wall_hanging_sign", y = y)) }
        }
        else -> variants { add("", variant(id)) }
    }

    private fun doorBlockState() = variants {
        HORIZONTAL_Y.forEach { (facing, base) ->
            listOf("lower" to "bottom", "upper" to "top").forEach { (half, part) ->
                listOf("left" to 90, "right" to 270).forEach { (hinge, openOffset) ->
                    add("facing=$facing,half=$half,hinge=$hinge,open=false", variant("palm_door_${part}_$hinge", y = base))
                    add("facing=$facing,half=$half,hinge=$hinge,open=true", variant("palm_door_${part}_${hinge}_open", y = (base + openOffset) % 360))
                }
            }
        }
    }

    private fun stairsBlockState() = variants {
        HORIZONTAL_Y.forEach { (facing, base) ->
            listOf("bottom", "top").forEach { half ->
                val x = if (half == "top") 180 else 0
                STAIR_SHAPES.forEach { (shape, shapeModel) ->
                    val y = when {
                        half == "bottom" && shape.endsWith("_left") -> (base + 270) % 360
                        half == "top" && shape.endsWith("_right") -> (base + 90) % 360
                        else -> base
                    }
                    add("facing=$facing,half=$half,shape=$shape", variant(shapeModel, x = x, y = y, uvlock = x != 0 || y != 0))
                }
            }
        }
    }

    private fun fenceBlockState() = obj {
        add("multipart", JsonArray().also { parts ->
            parts.add(obj { add("apply", variant("palm_fence_post")) })
            listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270).forEach { (side, y) ->
                parts.add(obj {
                    add("apply", variant("palm_fence_side", y = y, uvlock = true))
                    add("when", obj { addProperty(side, "true") })
                })
            }
        })
    }

    private fun fenceGateBlockState() = variants {
        FACING_Y.forEach { (facing, y) ->
            listOf(false, true).forEach { inWall ->
                listOf(false, true).forEach { open ->
                    val gate = "palm_fence_gate" + (if (inWall) "_wall" else "") + (if (open) "_open" else "")
                    add("facing=$facing,in_wall=$inWall,open=$open", variant(gate, y = y, uvlock = true))
                }
            }
        }
    }

    private fun trapdoorBlockState() = variants {
        ROTATION_Y.forEach { (facing, y) ->
            listOf("bottom", "top").forEach { half ->
                add("facing=$facing,half=$half,open=false", variant("palm_trapdoor_$half"))
                add("facing=$facing,half=$half,open=true", variant("palm_trapdoor_open", y = y))
            }
        }
    }

    private fun buttonBlockState() = variants {
        listOf("floor", "wall", "ceiling").forEach { face ->
            ROTATION_Y.forEach { (facing, y) ->
                val x = when (face) {
                    "ceiling" -> 180
                    "wall" -> 90
                    else -> 0
                }
                val rotation = if (face == "ceiling") (y + 180) % 360 else y
                listOf(false, true).forEach { powered ->
                    val button = if (powered) "palm_button_pressed" else "palm_button"
                    add("face=$face,facing=$facing,powered=$powered", variant(button, x = x, y = rotation, uvlock = face == "wall"))
                }
            }
        }
    }

    private fun variants(block: JsonObject.() -> Unit) = obj { add("variants", obj(block)) }

    private fun variant(blockModel: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) = obj {
        addProperty("model", "$namespace:block/$blockModel")
        if (uvlock) addProperty("uvlock", true)
        if (x != 0) addProperty("x", x)
        if (y != 0) addProperty("y", y)
    }

    private fun blockModels(): Map<String, JsonObject> {
        val models = linkedMapOf<String, JsonObject>()
        models["palm_log"] = model("minecraft:block/cube_column", "end" to "$namespace:block/palm_log_top", "side" to "$namespace:block/palm_log")
        models["stripped_palm_log"] = model("minecraft:block/cube_column", "end" to "$namespace:block/stripped_palm_log_top", "side" to strippedLogTexture)
        models["palm_wood"] = model("minecraft:block/cube_all", "all" to "$namespace:block/palm_log")
        models["stripped_palm_wood"] = model("minecraft:block/cube_all", "all" to strippedLogTexture)
        models["palm_planks"] = model("minecraft:block/cube_all", "all" to planksTexture)
        models["palm_leaves"] = model("minecraft:block/leaves", "all" to "$namespace:block/palm_leaves")
        models["palm_sapling"] = model("minecraft:block/cross", "cross" to "$namespace:block/palm_sapling")
        models["potted_palm_sapling"] = model("minecraft:block/flower_pot_cross", "plant" to "$namespace:block/palm_sapling")
        models["palm_slab"] = plankSided("minecraft:block/slab")
        models["palm_slab_top"] = plankSided("minecraft:block/slab_top")
        models["palm_stairs"] = plankSided("minecraft:block/stairs")
        models["palm_stairs_inner"] = plankSided("minecraft:block/inner_stairs")
        models["palm_stairs_outer"] = plankSided("minecraft:block/outer_stairs")
        models["palm_fence_post"] = plankTextured("minecraft:block/fence_post")
        models["palm_fence_side"] = plankTextured("minecraft:block/fence_side")
        models["palm_fence_inventory"] = plankTextured("minecraft:block/fence_inventory")
        models["palm_fence_gate"] = plankTextured("minecraft:block/template_fence_gate")
        models["palm_fence_gate_open"] = plankTextured("minecraft:block/template_fence_gate_open")
        models["palm_fence_gate_wall"] = plankTextured("minecraft:block/template_fence_gate_wall")
        models["palm_fence_gate_wall_open"] = plankTextured("minecraft:block/template_fence_gate_wall_open")
        models["palm_pressure_plate"] = plankTextured("minecraft:block/pressure_plate_up")
        models["palm_pressure_plate_down"] = plankTextured("minecraft:block/pressure_plate_down")
        models["palm_button"] = plankTextured("minecraft:block/button")
        models["palm_button_pressed"] = plankTextured("minecraft:block/button_pressed")
        models["palm_button_inventory"] = plankTextured("minecraft:block/button_inventory")
        listOf("bottom_left", "bottom_left_open", "bottom_right", "bottom_right_open", "top_left", "top_left_open", "top_right", "top_right_open").forEach { part ->
            models["palm_door_$part"] = model("minecraft:block/door_$part", "bottom" to "$namespace:block/palm_door_bottom", "top" to "$namespace:block/palm_door_top")
        }
        listOf("bottom", "top", "open").forEach { part ->
            models["palm_trapdoor_$part"] = model("minecraft:block/template_trapdoor_$part", "texture" to "$namespace:block/palm_trapdoor")
        }
        (0..3).forEach { rot ->
            models["palm_sign_rot_$rot"] = model("minecraft:block/template_sign_rot_$rot", "all" to signTexture, "particle" to planksTexture)
            models["palm_hanging_sign_rot_$rot"] = model("minecraft:block/template_hanging_sign_rot_$rot", "all" to hangingSignTexture, "particle" to strippedLogTexture)
            models["palm_hanging_sign_attached_rot_$rot"] = model("minecraft:block/template_attached_hanging_sign_rot_$rot", "all" to hangingSignTexture, "particle" to strippedLogTexture)
        }
        models["palm_wall_sign"] = model("minecraft:block/template_wall_sign", "all" to signTexture, "particle" to planksTexture)
        models["palm_wall_hanging_sign"] = model("minecraft:block/template_wall_hanging_sign", "all" to hangingSignTexture, "particle" to strippedLogTexture)
        return models
    }

    private fun itemModel(id: String) = when (id) {
        "palm_door" -> flatItemModel("$namespace:item/palm_door")
        "palm_sign" -> flatItemModel("$namespace:item/palm_sign")
        "palm_hanging_sign" -> flatItemModel("$namespace:item/palm_hanging_sign")
        "palm_sapling" -> flatItemModel("$namespace:block/palm_sapling")
        "palm_fence" -> obj { addProperty("parent", "$namespace:block/palm_fence_inventory") }
        "palm_button" -> obj { addProperty("parent", "$namespace:block/palm_button_inventory") }
        "palm_trapdoor" -> obj { addProperty("parent", "$namespace:block/palm_trapdoor_bottom") }
        else -> obj { addProperty("parent", "$namespace:block/$id") }
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (slot, texture) -> addProperty(slot, texture) } })
    }

    private fun plankTextured(parent: String) = model(parent, "texture" to planksTexture)

    private fun plankSided(parent: String) = model(parent, "bottom" to planksTexture, "side" to planksTexture, "top" to planksTexture)

    private fun flatItemModel(texture: String) = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", texture) })
    }

    private fun planksRecipe() = obj { addProperty("type", "minecraft:crafting_shapeless"); addProperty("category", "building"); add("ingredients", array("$namespace:palm_log")); add("result", itemResult("palm_planks", 4)) }
    private fun woodRecipe() = shapedRecipe("palm_wood", "building", arrayOf("II", "II"), "I", "palm_log")
    private fun strippedWoodRecipe() = shapedRecipe("stripped_palm_wood", "building", arrayOf("II", "II"), "I", "stripped_palm_log")
    private fun sticksRecipe() = shapedRecipe("stick", "misc", arrayOf("P", "P"), "P", "palm_planks").apply { getAsJsonObject("result").addProperty("count", 4); getAsJsonObject("result").addProperty("id", "minecraft:stick") }
    private fun slabRecipe() = shapedRecipe("palm_slab", "building", arrayOf("PPP"), "P", "palm_planks").apply { getAsJsonObject("result").addProperty("count", 6) }
    private fun stairsRecipe() = shapedRecipe("palm_stairs", "building", arrayOf("P  ", "PP ", "PPP"), "P", "palm_planks")
    private fun fenceRecipe() = shapedRecipe("palm_fence", "building", arrayOf("PSP", "PSP"), "P", "palm_planks").apply { getAsJsonObject("key").addProperty("S", "minecraft:stick") }
    private fun fenceGateRecipe() = shapedRecipe("palm_fence_gate", "redstone", arrayOf("SPS", "SPS"), "P", "palm_planks").apply { getAsJsonObject("key").addProperty("S", "minecraft:stick") }
    private fun doorRecipe() = shapedRecipe("palm_door", "redstone", arrayOf("PP", "PP", "PP"), "P", "palm_planks").apply { getAsJsonObject("result").addProperty("count", 2) }
    private fun trapdoorRecipe() = shapedRecipe("palm_trapdoor", "redstone", arrayOf("PPP", "PPP"), "P", "palm_planks").apply { getAsJsonObject("result").addProperty("count", 2) }
    private fun pressurePlateRecipe() = shapedRecipe("palm_pressure_plate", "redstone", arrayOf("PP"), "P", "palm_planks")
    private fun buttonRecipe() = shapedRecipe("palm_button", "redstone", arrayOf("P"), "P", "palm_planks")
    private fun signRecipe() = obj { addProperty("type", "minecraft:crafting_shaped"); addProperty("category", "misc"); add("pattern", array("AAA", "A A", " S ")); add("key", obj { addProperty("A", "$namespace:palm_planks"); addProperty("S", "minecraft:stick") }); add("result", itemResult("palm_sign", 3)) }
    private fun boatRecipe() = obj { addProperty("type", "minecraft:crafting_shaped"); addProperty("category", "misc"); add("pattern", array("# #", "###")); add("key", obj { addProperty("#", "$namespace:palm_planks") }); add("result", itemResult("palm_boat")) }
    private fun chestBoatRecipe() = obj { addProperty("type", "minecraft:crafting_shapeless"); addProperty("category", "misc"); add("ingredients", array("$namespace:palm_boat", "minecraft:chest")); add("result", itemResult("palm_chest_boat")) }
    private fun hangingSignRecipe() = obj { addProperty("type", "minecraft:crafting_shaped"); addProperty("category", "misc"); add("pattern", array("C C", "PPP", "PPP")); add("key", obj { addProperty("C", "minecraft:iron_chain"); addProperty("P", "$namespace:stripped_palm_log") }); add("result", itemResult("palm_hanging_sign", 6)) }
    private fun boatItemModel(id: String) = flatItemModel("$namespace:item/$id")
    // Matches vanilla oak_leaves.json chances - the palm sapling is a normal 1x1 grower like oak.
    private fun leavesLoot() = leavesLootTable(
        "palm_leaves", "palm_sapling",
        listOf(0.05, 0.0625, 0.083333336, 0.1),
        listOf(0.02, 0.022222223, 0.025, 0.033333335, 0.1),
    )
    private fun woodSmeltingLootTable(id: String) = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "$namespace:blocks/$id")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "$namespace:$id")
                                add("conditions", JsonArray().also { it.add(silkTouchCondition()) })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:charcoal")
                                add("conditions", JsonArray().also {
                                    it.add(smeltingCondition())
                                    it.add(survivesExplosionCondition())
                                })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "$namespace:$id")
                                add("conditions", JsonArray().also { it.add(survivesExplosionCondition()) })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun silkTouchCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj {
            add("predicates", obj {
                add("minecraft:enchantments", JsonArray().also {
                    it.add(obj {
                        addProperty("enchantments", "minecraft:silk_touch")
                        add("levels", obj { addProperty("min", 1) })
                    })
                })
            })
        })
    }

    private fun smeltingCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj {
            add("predicates", obj {
                add("minecraft:enchantments", JsonArray().also {
                    it.add(obj {
                        addProperty("enchantments", "$namespace:smelting")
                        add("levels", obj { addProperty("min", 1) })
                    })
                })
            })
        })
    }

    private fun survivesExplosionCondition() = obj {
        addProperty("condition", "minecraft:survives_explosion")
    }

    override fun getName() = "SupremeMC palm resources"

    private companion object {
        /** Closed-state rotation for models whose base orientation faces south (wall signs, fence gates). */
        val FACING_Y = linkedMapOf("south" to 0, "west" to 90, "north" to 180, "east" to 270)

        /** Closed-state rotation for doors and stairs, whose base models face east. */
        val HORIZONTAL_Y = linkedMapOf("east" to 0, "south" to 90, "west" to 180, "north" to 270)

        /** Rotation for models whose base orientation faces north (trapdoors, buttons). */
        val ROTATION_Y = linkedMapOf("north" to 0, "east" to 90, "south" to 180, "west" to 270)

        val STAIR_SHAPES = linkedMapOf(
            "straight" to "palm_stairs",
            "inner_left" to "palm_stairs_inner",
            "inner_right" to "palm_stairs_inner",
            "outer_left" to "palm_stairs_outer",
            "outer_right" to "palm_stairs_outer",
        )
    }
}
