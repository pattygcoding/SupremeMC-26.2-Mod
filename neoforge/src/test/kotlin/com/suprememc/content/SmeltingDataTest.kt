package com.suprememc.content

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SmeltingDataTest : GeneratedDataTestSupport() {

    private val inputToOutputMap = mapOf(
        "minecraft:raw_iron" to "minecraft:iron_ingot",
        "minecraft:raw_gold" to "minecraft:gold_ingot",
        "minecraft:raw_copper" to "minecraft:copper_ingot",
        "minecraft:ancient_debris" to "minecraft:netherite_scrap",
        "minecraft:cobblestone" to "minecraft:stone",
        "minecraft:cobbled_deepslate" to "minecraft:deepslate",
        "minecraft:netherrack" to "minecraft:nether_brick",
        "minecraft:sand" to "minecraft:glass",
        "minecraft:red_sand" to "minecraft:glass",
        "minecraft:sandstone" to "minecraft:smooth_sandstone",
        "minecraft:red_sandstone" to "minecraft:smooth_red_sandstone",
        "minecraft:basalt" to "minecraft:smooth_basalt",
        "minecraft:quartz_block" to "minecraft:smooth_quartz",
        "minecraft:clay_ball" to "minecraft:brick",
        "minecraft:oak_log" to "minecraft:charcoal",
        "minecraft:spruce_log" to "minecraft:charcoal",
        "minecraft:birch_log" to "minecraft:charcoal",
        "minecraft:jungle_log" to "minecraft:charcoal",
        "minecraft:acacia_log" to "minecraft:charcoal",
        "minecraft:dark_oak_log" to "minecraft:charcoal",
        "minecraft:mangrove_log" to "minecraft:charcoal",
        "minecraft:cherry_log" to "minecraft:charcoal",
        "minecraft:pale_oak_log" to "minecraft:charcoal",
        "minecraft:oak_wood" to "minecraft:charcoal",
        "minecraft:spruce_wood" to "minecraft:charcoal",
        "minecraft:birch_wood" to "minecraft:charcoal",
        "minecraft:jungle_wood" to "minecraft:charcoal",
        "minecraft:acacia_wood" to "minecraft:charcoal",
        "minecraft:dark_oak_wood" to "minecraft:charcoal",
        "minecraft:mangrove_wood" to "minecraft:charcoal",
        "minecraft:cherry_wood" to "minecraft:charcoal",
        "minecraft:pale_oak_wood" to "minecraft:charcoal",
        "minecraft:stripped_oak_log" to "minecraft:charcoal",
        "minecraft:stripped_spruce_log" to "minecraft:charcoal",
        "minecraft:stripped_birch_log" to "minecraft:charcoal",
        "minecraft:stripped_jungle_log" to "minecraft:charcoal",
        "minecraft:stripped_acacia_log" to "minecraft:charcoal",
        "minecraft:stripped_dark_oak_log" to "minecraft:charcoal",
        "minecraft:stripped_mangrove_log" to "minecraft:charcoal",
        "minecraft:stripped_cherry_log" to "minecraft:charcoal",
        "minecraft:stripped_pale_oak_log" to "minecraft:charcoal",
        "minecraft:stripped_oak_wood" to "minecraft:charcoal",
        "minecraft:stripped_spruce_wood" to "minecraft:charcoal",
        "minecraft:stripped_birch_wood" to "minecraft:charcoal",
        "minecraft:stripped_jungle_wood" to "minecraft:charcoal",
        "minecraft:stripped_acacia_wood" to "minecraft:charcoal",
        "minecraft:stripped_dark_oak_wood" to "minecraft:charcoal",
        "minecraft:stripped_mangrove_wood" to "minecraft:charcoal",
        "minecraft:stripped_cherry_wood" to "minecraft:charcoal",
        "minecraft:stripped_pale_oak_wood" to "minecraft:charcoal",
        "minecraft:cactus" to "minecraft:green_dye",
        "minecraft:sea_pickle" to "minecraft:lime_dye",
        "minecraft:chorus_fruit" to "minecraft:popped_chorus_fruit"
    )

    private val inputToBlockMap = mapOf(
        "minecraft:raw_iron" to listOf("iron_ore", "deepslate_iron_ore"),
        "minecraft:raw_gold" to listOf("gold_ore", "deepslate_gold_ore"),
        "minecraft:raw_copper" to listOf("copper_ore", "deepslate_copper_ore"),
        "minecraft:ancient_debris" to listOf("ancient_debris"),
        "minecraft:cobblestone" to listOf("stone"),
        "minecraft:cobbled_deepslate" to listOf("deepslate"),
        "minecraft:netherrack" to listOf("netherrack"),
        "minecraft:sand" to listOf("sand"),
        "minecraft:red_sand" to listOf("red_sand"),
        "minecraft:sandstone" to listOf("sandstone"),
        "minecraft:red_sandstone" to listOf("red_sandstone"),
        "minecraft:basalt" to listOf("basalt"),
        "minecraft:quartz_block" to listOf("quartz_block"),
        "minecraft:clay_ball" to listOf("clay"),
        "minecraft:cactus" to listOf("cactus"),
        "minecraft:sea_pickle" to listOf("sea_pickle"),
        "minecraft:chorus_fruit" to listOf("chorus_plant")
    )

    @Test
    fun generatesSmeltingEnchantmentData() {
        val enchantment = readJson("data/suprememc/enchantment/smelting.json")
        assertEquals(1, enchantment.get("max_level").asInt)
        assertEquals(2, enchantment.get("weight").asInt)
        assertEquals("#minecraft:enchantable/mining", enchantment.get("supported_items").asString)
        assertEquals("#suprememc:exclusive_set/smelting", enchantment.get("exclusive_set").asString)
        assertEquals("enchantment.suprememc.smelting", enchantment.getAsJsonObject("description").get("translate").asString)

        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/smelting.json", "minecraft:silk_touch", "suprememc:smelting")
    }

    @Test
    fun generatesSmeltingLootTablesForAllMappedInputs() {
        // Verify every input in user prompt map is tested against its block loot table
        assertEquals(53, inputToOutputMap.size, "Expected 53 input-to-output mappings in total")

        inputToOutputMap.forEach { (inputItem, expectedOutputItem) ->
            val blockIds = if (inputItem in inputToBlockMap) {
                inputToBlockMap.getValue(inputItem)
            } else {
                // For log/wood items, block ID matches path without namespace
                listOf(inputItem.removePrefix("minecraft:"))
            }

            blockIds.forEach { blockId ->
                val lootTable = readJson("data/minecraft/loot_table/blocks/$blockId.json")
                val pools = lootTable.getAsJsonArray("pools")
                val pool = pools.single().asJsonObject
                val entries = pool.getAsJsonArray("entries")
                val alternatives = entries.single().asJsonObject.getAsJsonArray("children").map { it.asJsonObject }

                // Find entry that requires smelting enchantment match_tool
                val smeltingEntry = alternatives.find { child ->
                    val conditions = child.getAsJsonArray("conditions") ?: return@find false
                    conditions.any { cond ->
                        val conditionObj = cond.asJsonObject
                        if (conditionObj.get("condition")?.asString != "minecraft:match_tool") return@any false
                        val predicates = conditionObj.getAsJsonObject("predicate")?.getAsJsonObject("predicates")
                        val enchantments = predicates?.getAsJsonArray("minecraft:enchantments") ?: return@any false
                        enchantments.any { ench ->
                            ench.asJsonObject.get("enchantments")?.asString == "suprememc:smelting"
                        }
                    }
                }

                assertTrue(smeltingEntry != null, "Block '$blockId' loot table must contain a smelting entry")
                assertEquals(expectedOutputItem, smeltingEntry.get("name").asString, "Block '$blockId' smelting drop must be '$expectedOutputItem'")

                // For ore drops (raw iron, raw gold, raw copper), verify Fortune formula is preserved
                if (inputItem in listOf("minecraft:raw_iron", "minecraft:raw_gold", "minecraft:raw_copper")) {
                    val functions = smeltingEntry.getAsJsonArray("functions").map { it.asJsonObject }
                    val fortuneFunc = functions.find { it.get("function")?.asString == "minecraft:apply_bonus" }
                    assertTrue(fortuneFunc != null, "Smelting entry for ore '$blockId' must preserve Fortune apply_bonus function")
                    assertEquals("minecraft:fortune", fortuneFunc.get("enchantment").asString)
                    assertEquals("minecraft:ore_drops", fortuneFunc.get("formula").asString)
                }
            }
        }
    }
}
