package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class MineralTagsDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val materials = listOf("iron", "lapis", "gold", "diamond", "emerald", "coal", "obsidian", "netherite", "amber", "aquamarine", "abyssalite", "burning_diamond")
    private val vanillaBlockIds = mapOf(
        "iron" to "minecraft:iron_block", "lapis" to "minecraft:lapis_block", "gold" to "minecraft:gold_block",
        "diamond" to "minecraft:diamond_block", "emerald" to "minecraft:emerald_block", "coal" to "minecraft:coal_block",
        "obsidian" to "minecraft:obsidian", "netherite" to "minecraft:netherite_block"
    )
    private val oreBlocks = listOf(
        "aquamarine_ore", "deepslate_aquamarine_ore", "burning_diamond_ore", "nether_anthracite_ore",
        "amber_ore", "deepslate_amber_ore", "prismarine_ore", "deepslate_prismarine_ore", "xylium_ore"
    )
    private val additionalBlocks = listOf(
        "anthracite_block", "atlantis_debris", "abyssalite_block",
        "polished_granite_wall", "polished_diorite_wall", "polished_andesite_wall",
        "andesite_bricks", "andesite_brick_wall", "diorite_bricks", "diorite_brick_wall",
        "granite_bricks", "granite_brick_wall", "xylium_block"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        val colors = listOf("white", "black", "pink")
        val stoneBrickFamilies = listOf("andesite", "diorite", "granite")
        val stairs = materials.map { "$namespace:${it}_stairs" } + listOf("$namespace:abyssalite_stairs") + stoneBrickFamilies.flatMap { listOf("$namespace:${it}_brick_stairs", "$namespace:mossy_${it}_brick_stairs") }
        val slabs = materials.map { "$namespace:${it}_slab" } + listOf("$namespace:abyssalite_slab") + stoneBrickFamilies.flatMap { listOf("$namespace:${it}_brick_slab", "$namespace:mossy_${it}_brick_slab") } + colors.flatMap { listOf("$namespace:${it}_sandstone_slab", "$namespace:smooth_${it}_sandstone_slab", "$namespace:cut_${it}_sandstone_slab") }
        val materialBlocks = materials.map { blockId(it) }
        val sandstone = colors.flatMap { color -> listOf("$namespace:${color}_sandstone", "$namespace:smooth_${color}_sandstone", "$namespace:cut_${color}_sandstone", "$namespace:${color}_sandstone_wall") }
        val pickaxe = materialBlocks + stairs + slabs + sandstone + oreBlocks.map { "$namespace:$it" } + additionalBlocks.map { "$namespace:$it" }
        val brickBlocks = stoneBrickFamilies.flatMap { listOf("$namespace:${it}_bricks", "$namespace:mossy_${it}_bricks", "$namespace:${it}_brick_wall", "$namespace:mossy_${it}_brick_wall") }
        val stoneTier = listOf("iron", "lapis").flatMap { materialParts(it) } + brickBlocks + listOf("$namespace:xylium_ore", "$namespace:xylium_block")
        val ironTier = listOf("gold", "diamond", "emerald", "amber", "aquamarine", "burning_diamond").flatMap { materialParts(it) } + listOf(
            "$namespace:anthracite_block", "$namespace:nether_anthracite_ore", "$namespace:prismarine_ore", "$namespace:deepslate_prismarine_ore"
        ) + oreBlocks.filter { it in listOf("aquamarine_ore", "deepslate_aquamarine_ore", "burning_diamond_ore", "amber_ore", "deepslate_amber_ore") }.map { "$namespace:$it" }
        val diamondTier = listOf("obsidian", "netherite", "abyssalite").flatMap { materialParts(it) } + listOf("$namespace:atlantis_debris")

        writes += save(cache, valuesTag(*pickaxe.toTypedArray()), minecraftDataPath("tags/block/mineable/pickaxe.json"))
        writes += save(cache, valuesTag(*stoneTier.toTypedArray()), minecraftDataPath("tags/block/needs_stone_tool.json"))
        writes += save(cache, valuesTag(*ironTier.toTypedArray()), minecraftDataPath("tags/block/needs_iron_tool.json"))
        writes += save(cache, valuesTag(*diamondTier.toTypedArray()), minecraftDataPath("tags/block/needs_diamond_tool.json"))
        writes += save(cache, valuesTag(
            "minecraft:gold_block", "minecraft:diamond_block", "minecraft:emerald_block", "$namespace:amber_block",
            "$namespace:aquamarine_block", "$namespace:burning_diamond_block", "$namespace:abyssalite_block"
        ), minecraftDataPath("tags/block/beacon_base_blocks.json"))
        writes += save(cache, valuesTag(
            "$namespace:amber", "$namespace:aquamarine", "$namespace:burning_diamond", "$namespace:abyssalite_ingot"
        ), minecraftDataPath("tags/item/beacon_payment_items.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_stairs", *stairs.toTypedArray()), minecraftDataPath("tags/block/stairs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_stairs", *stairs.toTypedArray()), minecraftDataPath("tags/item/stairs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_slabs", *slabs.toTypedArray()), minecraftDataPath("tags/block/slabs.json"))
        writes += save(cache, valuesTag("#minecraft:wooden_slabs", *slabs.toTypedArray()), minecraftDataPath("tags/item/slabs.json"))
        writes += save(cache, valuesTag(*brickBlocks.filter { it.endsWith("_bricks") }.toTypedArray()), minecraftDataPath("tags/block/stone_bricks.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun materialParts(id: String) = listOf(blockId(id), "$namespace:${id}_stairs", "$namespace:${id}_slab")

    private fun blockId(id: String) = vanillaBlockIds[id] ?: "$namespace:${id}_block"

    override fun getName() = "SupremeMC mineral tags"
}