package com.suprememc.tabs;

import com.suprememc.content.ModContent;

import java.util.List;

public final class CreativeTab {

	private CreativeTab() {
	}

	public static void populate() {
		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.PALM_LOG, ModContent.STRIPPED_PALM_LOG, ModContent.PALM_WOOD, ModContent.STRIPPED_PALM_WOOD,
			ModContent.PALM_PLANKS, ModContent.PALM_STAIRS, ModContent.PALM_SLAB, ModContent.PALM_FENCE, ModContent.PALM_FENCE_GATE,
			ModContent.PALM_DOOR, ModContent.PALM_TRAPDOOR, ModContent.PALM_PRESSURE_PLATE, ModContent.PALM_BUTTON,
			ModContent.PALM_SIGN, ModContent.PALM_HANGING_SIGN
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.COAL_STAIRS, ModContent.COAL_SLAB,
			ModContent.POLISHED_GRANITE_WALL, ModContent.POLISHED_DIORITE_WALL, ModContent.POLISHED_ANDESITE_WALL,
			ModContent.ANDESITE_BRICKS, ModContent.ANDESITE_BRICK_STAIRS, ModContent.ANDESITE_BRICK_SLAB, ModContent.ANDESITE_BRICK_WALL,
			ModContent.DIORITE_BRICKS, ModContent.DIORITE_BRICK_STAIRS, ModContent.DIORITE_BRICK_SLAB, ModContent.DIORITE_BRICK_WALL,
			ModContent.GRANITE_BRICKS, ModContent.GRANITE_BRICK_STAIRS, ModContent.GRANITE_BRICK_SLAB, ModContent.GRANITE_BRICK_WALL,
			ModContent.IRON_STAIRS, ModContent.IRON_SLAB,
			ModContent.GOLD_STAIRS, ModContent.GOLD_SLAB,
			ModContent.LAPIS_STAIRS, ModContent.LAPIS_SLAB,
			ModContent.EMERALD_STAIRS, ModContent.EMERALD_SLAB,
			ModContent.DIAMOND_STAIRS, ModContent.DIAMOND_SLAB,
			ModContent.NETHERITE_STAIRS, ModContent.NETHERITE_SLAB,
			ModContent.OBSIDIAN_STAIRS, ModContent.OBSIDIAN_SLAB,
			ModContent.AQUAMARINE_STAIRS, ModContent.AQUAMARINE_SLAB,
			ModContent.AMBER_STAIRS, ModContent.AMBER_SLAB,
			ModContent.ABYSSALITE_STAIRS, ModContent.ABYSSALITE_SLAB
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(ModContent.GLOW_BLOCKS.values());
		ModContent.CREATIVE_TAB_ITEMS.addAll(ModContent.SLIME_BLOCKS.values());

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.AQUAMARINE_ORE, ModContent.DEEPSLATE_AQUAMARINE_ORE, ModContent.AQUAMARINE_BLOCK,
			ModContent.AMBER_ORE, ModContent.DEEPSLATE_AMBER_ORE, ModContent.AMBER_BLOCK,
			ModContent.NETHER_ANTHRACITE_ORE, ModContent.ANTHRACITE_BLOCK,
			ModContent.ATLANTIS_DEBRIS, ModContent.ABYSSALITE_BLOCK,
			ModContent.SUPREME_MC_LOGO_BLOCK,
			ModContent.WET_FARMLAND, ModContent.ICICLE,
			ModContent.PALM_LEAVES, ModContent.PALM_SAPLING,
			ModContent.BEACH_GRASS, ModContent.TALL_BEACH_GRASS
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.COCONUT_SEEDS, ModContent.COCONUT_ITEM,
			ModContent.COTTON, ModContent.TOMATO, ModContent.CORN, ModContent.GRAPES,
			ModContent.CALAMARI, ModContent.COOKED_CALAMARI
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.PALM_BOAT, ModContent.PALM_CHEST_BOAT,
			ModContent.GRIZZLY_BEAR_SPAWN_EGG, ModContent.FIRE_CREEPER_SPAWN_EGG
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.AQUAMARINE,
			ModContent.AMBER,
			ModContent.ANTHRACITE,
			ModContent.ABYSSALITE_SCRAP, ModContent.ABYSSALITE_INGOT,
			ModContent.ABYSSALITE_UPGRADE_SMITHING_TEMPLATE
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.AQUAMARINE_SWORD, ModContent.AQUAMARINE_PICKAXE, ModContent.AQUAMARINE_AXE,
			ModContent.AQUAMARINE_SHOVEL, ModContent.AQUAMARINE_HOE,
			ModContent.AQUAMARINE_HELMET, ModContent.AQUAMARINE_CHESTPLATE,
			ModContent.AQUAMARINE_LEGGINGS, ModContent.AQUAMARINE_BOOTS
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.COTTON_HELMET, ModContent.COTTON_CHESTPLATE,
			ModContent.COTTON_LEGGINGS, ModContent.COTTON_BOOTS
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.AMBER_SWORD, ModContent.AMBER_PICKAXE, ModContent.AMBER_AXE,
			ModContent.AMBER_SHOVEL, ModContent.AMBER_HOE,
			ModContent.AMBER_HELMET, ModContent.AMBER_CHESTPLATE,
			ModContent.AMBER_LEGGINGS, ModContent.AMBER_BOOTS
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.EMERALD_SWORD, ModContent.EMERALD_PICKAXE, ModContent.EMERALD_AXE,
			ModContent.EMERALD_SHOVEL, ModContent.EMERALD_HOE,
			ModContent.EMERALD_HELMET, ModContent.EMERALD_CHESTPLATE,
			ModContent.EMERALD_LEGGINGS, ModContent.EMERALD_BOOTS
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModContent.ABYSSALITE_SWORD, ModContent.ABYSSALITE_TRIDENT, ModContent.ABYSSALITE_PICKAXE,
			ModContent.ABYSSALITE_AXE, ModContent.ABYSSALITE_SHOVEL, ModContent.ABYSSALITE_HOE,
			ModContent.ABYSSALITE_HELMET, ModContent.ABYSSALITE_CHESTPLATE,
			ModContent.ABYSSALITE_LEGGINGS, ModContent.ABYSSALITE_BOOTS
		));
	}
}
