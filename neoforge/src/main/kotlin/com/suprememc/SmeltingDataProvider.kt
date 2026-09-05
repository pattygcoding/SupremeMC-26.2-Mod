package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SmeltingDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()

        // Raw Ores -> Smelted Ingots
        writes += save(cache, oreSmeltingLoot("iron_ore", "minecraft:iron_ingot", "minecraft:raw_iron"), minecraftDataPath("loot_table/blocks/iron_ore.json"))
        writes += save(cache, oreSmeltingLoot("deepslate_iron_ore", "minecraft:iron_ingot", "minecraft:raw_iron"), minecraftDataPath("loot_table/blocks/deepslate_iron_ore.json"))
        writes += save(cache, oreSmeltingLoot("gold_ore", "minecraft:gold_ingot", "minecraft:raw_gold"), minecraftDataPath("loot_table/blocks/gold_ore.json"))
        writes += save(cache, oreSmeltingLoot("deepslate_gold_ore", "minecraft:gold_ingot", "minecraft:raw_gold"), minecraftDataPath("loot_table/blocks/deepslate_gold_ore.json"))
        writes += save(cache, oreSmeltingLoot("copper_ore", "minecraft:copper_ingot", "minecraft:raw_copper", countMin = 2.0f, countMax = 5.0f), minecraftDataPath("loot_table/blocks/copper_ore.json"))
        writes += save(cache, oreSmeltingLoot("deepslate_copper_ore", "minecraft:copper_ingot", "minecraft:raw_copper", countMin = 2.0f, countMax = 5.0f), minecraftDataPath("loot_table/blocks/deepslate_copper_ore.json"))

        // Ancient Debris -> Netherite Scrap
        writes += save(cache, simpleSmeltingLoot("ancient_debris", "minecraft:netherite_scrap", "minecraft:ancient_debris"), minecraftDataPath("loot_table/blocks/ancient_debris.json"))

        // Stone & Deepslate
        writes += save(cache, simpleSmeltingLoot("stone", "minecraft:stone", "minecraft:cobblestone", "minecraft:stone"), minecraftDataPath("loot_table/blocks/stone.json"))
        writes += save(cache, simpleSmeltingLoot("deepslate", "minecraft:deepslate", "minecraft:cobbled_deepslate", "minecraft:deepslate"), minecraftDataPath("loot_table/blocks/deepslate.json"))

        // Netherrack & Sand blocks
        writes += save(cache, simpleSmeltingLoot("netherrack", "minecraft:nether_brick", "minecraft:netherrack"), minecraftDataPath("loot_table/blocks/netherrack.json"))
        writes += save(cache, simpleSmeltingLoot("sand", "minecraft:glass", "minecraft:sand"), minecraftDataPath("loot_table/blocks/sand.json"))
        writes += save(cache, simpleSmeltingLoot("red_sand", "minecraft:glass", "minecraft:red_sand"), minecraftDataPath("loot_table/blocks/red_sand.json"))
        writes += save(cache, simpleSmeltingLoot("sandstone", "minecraft:smooth_sandstone", "minecraft:sandstone"), minecraftDataPath("loot_table/blocks/sandstone.json"))
        writes += save(cache, simpleSmeltingLoot("red_sandstone", "minecraft:smooth_red_sandstone", "minecraft:red_sandstone"), minecraftDataPath("loot_table/blocks/red_sandstone.json"))
        writes += save(cache, simpleSmeltingLoot("basalt", "minecraft:smooth_basalt", "minecraft:basalt"), minecraftDataPath("loot_table/blocks/basalt.json"))
        writes += save(cache, simpleSmeltingLoot("quartz_block", "minecraft:smooth_quartz", "minecraft:quartz_block"), minecraftDataPath("loot_table/blocks/quartz_block.json"))

        // Clay
        writes += save(cache, claySmeltingLoot(), minecraftDataPath("loot_table/blocks/clay.json"))

        // Logs and Woods -> Charcoal
        val treeTypes = arrayOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "pale_oak")
        treeTypes.forEach { tree ->
            arrayOf("${tree}_log", "${tree}_wood", "stripped_${tree}_log", "stripped_${tree}_wood").forEach { block ->
                writes += save(cache, simpleSmeltingLoot(block, "minecraft:charcoal", "minecraft:$block"), minecraftDataPath("loot_table/blocks/$block.json"))
            }
        }

        // Special Flora
        writes += save(cache, simpleSmeltingLoot("cactus", "minecraft:green_dye", "minecraft:cactus"), minecraftDataPath("loot_table/blocks/cactus.json"))
        writes += save(cache, seaPickleSmeltingLoot(), minecraftDataPath("loot_table/blocks/sea_pickle.json"))
        writes += save(cache, chorusPlantSmeltingLoot(), minecraftDataPath("loot_table/blocks/chorus_plant.json"))

        return CompletableFuture.allOf(*writes.toTypedArray())
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

    private fun simpleSmeltingLoot(blockId: String, smeltedItem: String, normalItem: String, silkItem: String = normalItem) = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/$blockId")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", silkItem)
                                add("conditions", JsonArray().also { it.add(silkTouchCondition()) })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", smeltedItem)
                                add("conditions", JsonArray().also {
                                    it.add(smeltingCondition())
                                    it.add(survivesExplosionCondition())
                                })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", normalItem)
                                add("conditions", JsonArray().also { it.add(survivesExplosionCondition()) })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun oreSmeltingLoot(blockId: String, smeltedItem: String, normalItem: String, silkItem: String = "minecraft:$blockId", countMin: Float = 1.0f, countMax: Float = 1.0f) = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/$blockId")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", silkItem)
                                add("conditions", JsonArray().also { it.add(silkTouchCondition()) })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", smeltedItem)
                                add("conditions", JsonArray().also { it.add(smeltingCondition()) })
                                add("functions", JsonArray().also { funcs ->
                                    if (countMax > 1.0f) {
                                        funcs.add(obj {
                                            addProperty("function", "minecraft:set_count")
                                            add("count", obj {
                                                addProperty("type", "minecraft:uniform")
                                                addProperty("min", countMin)
                                                addProperty("max", countMax)
                                            })
                                        })
                                    }
                                    funcs.add(obj {
                                        addProperty("function", "minecraft:apply_bonus")
                                        addProperty("enchantment", "minecraft:fortune")
                                        addProperty("formula", "minecraft:ore_drops")
                                    })
                                    funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", normalItem)
                                add("functions", JsonArray().also { funcs ->
                                    if (countMax > 1.0f) {
                                        funcs.add(obj {
                                            addProperty("function", "minecraft:set_count")
                                            add("count", obj {
                                                addProperty("type", "minecraft:uniform")
                                                addProperty("min", countMin)
                                                addProperty("max", countMax)
                                            })
                                        })
                                    }
                                    funcs.add(obj {
                                        addProperty("function", "minecraft:apply_bonus")
                                        addProperty("enchantment", "minecraft:fortune")
                                        addProperty("formula", "minecraft:ore_drops")
                                    })
                                    funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun claySmeltingLoot() = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/clay")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:clay")
                                add("conditions", JsonArray().also { it.add(silkTouchCondition()) })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:brick")
                                add("conditions", JsonArray().also { it.add(smeltingCondition()) })
                                add("functions", JsonArray().also { funcs ->
                                    funcs.add(obj {
                                        addProperty("function", "minecraft:set_count")
                                        addProperty("count", 4.0)
                                    })
                                    funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:clay_ball")
                                add("functions", JsonArray().also { funcs ->
                                    funcs.add(obj {
                                        addProperty("function", "minecraft:set_count")
                                        addProperty("count", 4.0)
                                    })
                                    funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun seaPickleSmeltingLoot() = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/sea_pickle")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:sea_pickle")
                                add("conditions", JsonArray().also { it.add(silkTouchCondition()) })
                                add("functions", pickleCountFunctions())
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:lime_dye")
                                add("conditions", JsonArray().also { it.add(smeltingCondition()) })
                                add("functions", pickleCountFunctions())
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:sea_pickle")
                                add("functions", pickleCountFunctions())
                            })
                        })
                    })
                })
            })
        })
    }

    private fun pickleCountFunctions() = JsonArray().also { funcs ->
        for (count in 2..4) {
            funcs.add(obj {
                addProperty("function", "minecraft:set_count")
                addProperty("count", count.toDouble())
                add("conditions", JsonArray().also {
                    it.add(obj {
                        addProperty("condition", "minecraft:block_state_property")
                        addProperty("block", "minecraft:sea_pickle")
                        add("properties", obj { addProperty("pickles", count.toString()) })
                    })
                })
            })
        }
        funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
    }

    private fun chorusPlantSmeltingLoot() = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/chorus_plant")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:popped_chorus_fruit")
                                add("conditions", JsonArray().also { it.add(smeltingCondition()) })
                                add("functions", chorusPlantFunctions())
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                addProperty("name", "minecraft:chorus_fruit")
                                add("functions", chorusPlantFunctions())
                            })
                        })
                    })
                })
            })
        })
    }

    private fun chorusPlantFunctions() = JsonArray().also { funcs ->
        funcs.add(obj {
            addProperty("function", "minecraft:set_count")
            add("count", obj {
                addProperty("type", "minecraft:uniform")
                addProperty("min", 0.0)
                addProperty("max", 1.0)
            })
        })
        funcs.add(obj { addProperty("function", "minecraft:explosion_decay") })
    }

    override fun getName() = "SupremeMC smelting loot"
}
