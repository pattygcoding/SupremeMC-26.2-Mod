package com.suprememc.content

import org.junit.jupiter.api.Test

class MineralTagsDataTest : GeneratedDataTestSupport() {
    @Test
    fun mineralBlocksHavePickaxeMiningAndCorrectTiers() {
        assertTagContains(
            "data/minecraft/tags/block/mineable/pickaxe.json",
            "suprememc:iron_block", "suprememc:iron_stairs", "suprememc:iron_slab",
            "suprememc:aquamarine_ore", "suprememc:abyssalite_block", "suprememc:abyssalite_stairs", "suprememc:abyssalite_slab"
        )
        assertTagContains("data/minecraft/tags/block/needs_stone_tool.json", "suprememc:iron_block", "suprememc:iron_stairs", "suprememc:lapis_slab")
        assertTagContains("data/minecraft/tags/block/needs_iron_tool.json", "suprememc:gold_block", "suprememc:diamond_slab", "suprememc:amber_block", "suprememc:aquamarine_ore")
        assertTagContains("data/minecraft/tags/block/needs_diamond_tool.json", "suprememc:obsidian_block", "suprememc:netherite_slab", "suprememc:atlantis_debris", "suprememc:abyssalite_block")
    }

    @Test
    fun mineralBlocksHaveBeaconAndBuildingShapeTags() {
        assertTagContains("data/minecraft/tags/block/beacon_base_blocks.json", "suprememc:gold_block", "suprememc:diamond_block", "suprememc:emerald_block", "suprememc:burning_diamond_block")
        assertTagContains("data/minecraft/tags/item/beacon_payment_items.json", "suprememc:amber", "suprememc:aquamarine", "suprememc:burning_diamond", "suprememc:abyssalite_ingot")
        assertTagContains("data/minecraft/tags/block/stairs.json", "suprememc:iron_stairs", "suprememc:obsidian_stairs", "suprememc:abyssalite_stairs")
        assertTagContains("data/minecraft/tags/block/slabs.json", "suprememc:lapis_slab", "suprememc:netherite_slab", "suprememc:amber_slab")
        assertTagContains("data/minecraft/tags/item/stairs.json", "suprememc:diamond_stairs")
        assertTagContains("data/minecraft/tags/item/slabs.json", "suprememc:emerald_slab")
    }
}