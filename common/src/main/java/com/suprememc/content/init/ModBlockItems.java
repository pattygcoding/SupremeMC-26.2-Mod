package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;

public final class ModBlockItems {
	private static boolean registered;
	private static boolean signItemsRegistered;
	public static Item COCONUT_SEEDS;
	public static Item COTTON;

	private ModBlockItems() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		register("aquamarine_ore", ModContent.AQUAMARINE_ORE);
		register("burning_diamond_ore", ModContent.BURNING_DIAMOND_ORE);
		register("nether_anthracite_ore", ModContent.NETHER_ANTHRACITE_ORE);
		register("anthracite_block", ModContent.ANTHRACITE_BLOCK);
		register("deepslate_aquamarine_ore", ModContent.DEEPSLATE_AQUAMARINE_ORE);
		register("aquamarine_block", ModContent.AQUAMARINE_BLOCK);
		register("burning_diamond_block", ModContent.BURNING_DIAMOND_BLOCK);
		register("burning_diamond_stairs", ModContent.BURNING_DIAMOND_STAIRS);
		register("burning_diamond_slab", ModContent.BURNING_DIAMOND_SLAB);
		register("aquamarine_stairs", ModContent.AQUAMARINE_STAIRS);
		register("aquamarine_slab", ModContent.AQUAMARINE_SLAB);
		register("amber_ore", ModContent.AMBER_ORE);
		register("deepslate_amber_ore", ModContent.DEEPSLATE_AMBER_ORE);
		register("prismarine_ore", ModContent.PRISMARINE_ORE);
		register("deepslate_prismarine_ore", ModContent.DEEPSLATE_PRISMARINE_ORE);
		register("amber_block", ModContent.AMBER_BLOCK);
		register("amber_stairs", ModContent.AMBER_STAIRS);
		register("amber_slab", ModContent.AMBER_SLAB);
		register("iron_stairs", ModContent.IRON_STAIRS);
		register("iron_slab", ModContent.IRON_SLAB);
		register("lapis_stairs", ModContent.LAPIS_STAIRS);
		register("lapis_slab", ModContent.LAPIS_SLAB);
		register("gold_stairs", ModContent.GOLD_STAIRS);
		register("gold_slab", ModContent.GOLD_SLAB);
		register("diamond_stairs", ModContent.DIAMOND_STAIRS);
		register("diamond_slab", ModContent.DIAMOND_SLAB);
		register("emerald_stairs", ModContent.EMERALD_STAIRS);
		register("emerald_slab", ModContent.EMERALD_SLAB);
		register("coal_stairs", ModContent.COAL_STAIRS);
		register("coal_slab", ModContent.COAL_SLAB);
		register("obsidian_stairs", ModContent.OBSIDIAN_STAIRS);
		register("obsidian_slab", ModContent.OBSIDIAN_SLAB);
		register("netherite_stairs", ModContent.NETHERITE_STAIRS);
		register("netherite_slab", ModContent.NETHERITE_SLAB);
		register("polished_granite_wall", ModContent.POLISHED_GRANITE_WALL);
		register("polished_diorite_wall", ModContent.POLISHED_DIORITE_WALL);
		register("polished_andesite_wall", ModContent.POLISHED_ANDESITE_WALL);
		register("andesite_bricks", ModContent.ANDESITE_BRICKS);
		register("andesite_brick_stairs", ModContent.ANDESITE_BRICK_STAIRS);
		register("andesite_brick_slab", ModContent.ANDESITE_BRICK_SLAB);
		register("andesite_brick_wall", ModContent.ANDESITE_BRICK_WALL);
		register("diorite_bricks", ModContent.DIORITE_BRICKS);
		register("diorite_brick_stairs", ModContent.DIORITE_BRICK_STAIRS);
		register("diorite_brick_slab", ModContent.DIORITE_BRICK_SLAB);
		register("diorite_brick_wall", ModContent.DIORITE_BRICK_WALL);
		register("granite_bricks", ModContent.GRANITE_BRICKS);
		register("granite_brick_stairs", ModContent.GRANITE_BRICK_STAIRS);
		register("granite_brick_slab", ModContent.GRANITE_BRICK_SLAB);
		register("granite_brick_wall", ModContent.GRANITE_BRICK_WALL);
		register("wet_farmland", ModContent.WET_FARMLAND);
		register("suprememc_logo_block", ModContent.SUPREME_MC_LOGO_BLOCK);
		register("spruce_bookshelf", ModContent.SPRUCE_BOOKSHELF);
		register("birch_bookshelf", ModContent.BIRCH_BOOKSHELF);
		register("jungle_bookshelf", ModContent.JUNGLE_BOOKSHELF);
		register("acacia_bookshelf", ModContent.ACACIA_BOOKSHELF);
		register("dark_oak_bookshelf", ModContent.DARK_OAK_BOOKSHELF);
		register("mangrove_bookshelf", ModContent.MANGROVE_BOOKSHELF);
		register("cherry_bookshelf", ModContent.CHERRY_BOOKSHELF);
		register("pale_oak_bookshelf", ModContent.PALE_OAK_BOOKSHELF);
		register("bamboo_bookshelf", ModContent.BAMBOO_BOOKSHELF);
		register("crimson_bookshelf", ModContent.CRIMSON_BOOKSHELF);
		register("warped_bookshelf", ModContent.WARPED_BOOKSHELF);
		register("palm_bookshelf", ModContent.PALM_BOOKSHELF);
		register("lavender_bookshelf", ModContent.LAVENDER_BOOKSHELF);
		register("spruce_crafting_table", ModContent.SPRUCE_CRAFTING_TABLE);
		register("birch_crafting_table", ModContent.BIRCH_CRAFTING_TABLE);
		register("jungle_crafting_table", ModContent.JUNGLE_CRAFTING_TABLE);
		register("acacia_crafting_table", ModContent.ACACIA_CRAFTING_TABLE);
		register("dark_oak_crafting_table", ModContent.DARK_OAK_CRAFTING_TABLE);
		register("mangrove_crafting_table", ModContent.MANGROVE_CRAFTING_TABLE);
		register("cherry_crafting_table", ModContent.CHERRY_CRAFTING_TABLE);
		register("pale_oak_crafting_table", ModContent.PALE_OAK_CRAFTING_TABLE);
		register("bamboo_crafting_table", ModContent.BAMBOO_CRAFTING_TABLE);
		register("crimson_crafting_table", ModContent.CRIMSON_CRAFTING_TABLE);
		register("warped_crafting_table", ModContent.WARPED_CRAFTING_TABLE);
		register("palm_crafting_table", ModContent.PALM_CRAFTING_TABLE);
		register("lavender_crafting_table", ModContent.LAVENDER_CRAFTING_TABLE);
		register("lavender_wart_block", ModContent.LAVENDER_WART_BLOCK);
		register("blackstone_furnace", ModContent.BLACKSTONE_FURNACE);
		register("deepslate_furnace", ModContent.DEEPSLATE_FURNACE);
		register("atlantis_debris", ModContent.ATLANTIS_DEBRIS);
		register("abyssalite_block", ModContent.ABYSSALITE_BLOCK);
		register("abyssalite_stairs", ModContent.ABYSSALITE_STAIRS);
		register("abyssalite_slab", ModContent.ABYSSALITE_SLAB);
		register("icicle", ModContent.ICICLE);
		register("palm_log", ModContent.PALM_LOG);
		register("stripped_palm_log", ModContent.STRIPPED_PALM_LOG);
		register("palm_wood", ModContent.PALM_WOOD);
		register("stripped_palm_wood", ModContent.STRIPPED_PALM_WOOD);
		register("lavender_stem", ModContent.LAVENDER_STEM);
		register("lavender_endspar", ModContent.LAVENDER_ENDSPAR);
		register("lavender_roots", ModContent.LAVENDER_ROOTS);
		register("lavender_fungus", ModContent.LAVENDER_FUNGUS);
		register("stripped_lavender_stem", ModContent.STRIPPED_LAVENDER_STEM);
		register("lavender_hyphae", ModContent.LAVENDER_HYPHAE);
		register("stripped_lavender_hyphae", ModContent.STRIPPED_LAVENDER_HYPHAE);
		register("lavender_wood", ModContent.LAVENDER_WOOD);
		register("stripped_lavender_wood", ModContent.STRIPPED_LAVENDER_WOOD);
		register("lavender_planks", ModContent.LAVENDER_PLANKS);
		register("lavender_slab", ModContent.LAVENDER_SLAB);
		register("lavender_stairs", ModContent.LAVENDER_STAIRS);
		register("lavender_fence", ModContent.LAVENDER_FENCE);
		register("lavender_fence_gate", ModContent.LAVENDER_FENCE_GATE);
		register("lavender_door", ModContent.LAVENDER_DOOR);
		register("lavender_trapdoor", ModContent.LAVENDER_TRAPDOOR);
		register("lavender_pressure_plate", ModContent.LAVENDER_PRESSURE_PLATE);
		register("lavender_button", ModContent.LAVENDER_BUTTON);
		register("palm_planks", ModContent.PALM_PLANKS);
		register("palm_slab", ModContent.PALM_SLAB);
		register("palm_stairs", ModContent.PALM_STAIRS);
		register("palm_fence", ModContent.PALM_FENCE);
		register("palm_fence_gate", ModContent.PALM_FENCE_GATE);
		register("palm_door", ModContent.PALM_DOOR);
		register("palm_trapdoor", ModContent.PALM_TRAPDOOR);
		register("palm_pressure_plate", ModContent.PALM_PRESSURE_PLATE);
		register("palm_button", ModContent.PALM_BUTTON);
		register("palm_leaves", ModContent.PALM_LEAVES);
		register("palm_sapling", ModContent.PALM_SAPLING);
		register("potted_palm_sapling", ModContent.POTTED_PALM_SAPLING);
		register("beach_grass", ModContent.BEACH_GRASS);
		register("tall_beach_grass", ModContent.TALL_BEACH_GRASS);
		register("buttercup", ModContent.BUTTERCUP);
		register("clover", ModContent.CLOVER);
		register("snow_tnt", ModContent.SNOW_TNT);
		register("fire_tnt", ModContent.FIRE_TNT);

		COCONUT_SEEDS = register("coconut_seeds", ModContent.COCONUT,
			ModContent.itemProperties("coconut_seeds").stacksTo(64));
		COTTON = register("cotton", ModContent.COTTON_BUSH,
			ModContent.itemProperties("cotton").stacksTo(64));

		for (DyeColor color : DyeColor.values()) {
			String name = color.getSerializedName();
			register(name + "_glowblock", ModBlocks.GLOW_BLOCKS.get(color));
			register(name + "_slime_block", ModBlocks.SLIME_BLOCKS.get(color));
		}
	}

	public static void bootstrapSignItems() {
		if (signItemsRegistered) return;
		signItemsRegistered = true;
		register("palm_sign", new SignItem(ModContent.PALM_SIGN, ModContent.PALM_WALL_SIGN,
			ModContent.itemProperties("palm_sign").useBlockDescriptionPrefix().stacksTo(16)));
		register("palm_hanging_sign", new HangingSignItem(ModContent.PALM_HANGING_SIGN, ModContent.PALM_WALL_HANGING_SIGN,
			ModContent.itemProperties("palm_hanging_sign").useBlockDescriptionPrefix().stacksTo(16)));
		register("lavender_sign", new SignItem(ModContent.LAVENDER_SIGN, ModContent.LAVENDER_WALL_SIGN,
			ModContent.itemProperties("lavender_sign").useBlockDescriptionPrefix().stacksTo(16)));
		register("lavender_hanging_sign", new HangingSignItem(ModContent.LAVENDER_HANGING_SIGN, ModContent.LAVENDER_WALL_HANGING_SIGN,
			ModContent.itemProperties("lavender_hanging_sign").useBlockDescriptionPrefix().stacksTo(16)));
	}

	public static Item register(String id, Block block) {
		return register(id, block, ModContent.itemProperties(id));
	}

	private static Item register(String id, Block block, Item.Properties properties) {
		Item item = new BlockItem(block, properties.useBlockDescriptionPrefix());
		Registry.register(BuiltInRegistries.ITEM,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), item);
		return item;
	}

	private static Item register(String id, Item item) {
		Registry.register(BuiltInRegistries.ITEM,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), item);
		return item;
	}
}
