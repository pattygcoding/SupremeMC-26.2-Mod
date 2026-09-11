package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import com.suprememc.content.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.core.cauldron.CauldronInteraction;
import com.suprememc.content.blocks.MilkCauldronBlock;
import com.suprememc.content.blocks.MilkLiquidBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.EnumMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class ModBlocks {
	public static Block AQUAMARINE_ORE;
	public static Block BURNING_DIAMOND_ORE;
	public static Block NETHER_ANTHRACITE_ORE;
	public static Block ANTHRACITE_BLOCK;
	public static Block DEEPSLATE_AQUAMARINE_ORE;
	public static Block AQUAMARINE_BLOCK;
	public static Block BURNING_DIAMOND_BLOCK;
	public static Block BURNING_DIAMOND_STAIRS;
	public static Block BURNING_DIAMOND_SLAB;
	public static Block AQUAMARINE_STAIRS;
	public static Block AQUAMARINE_SLAB;
	public static Block AMBER_ORE;
	public static Block DEEPSLATE_AMBER_ORE;
	public static Block PRISMARINE_ORE;
	public static Block DEEPSLATE_PRISMARINE_ORE;
	public static Block XYLIUM_ORE;
	public static Block XYLIUM_BLOCK;
	public static Block AMBER_BLOCK;
	public static Block AMBER_STAIRS;
	public static Block AMBER_SLAB;
	public static Block IRON_STAIRS;
	public static Block IRON_SLAB;
	public static Block LAPIS_STAIRS;
	public static Block LAPIS_SLAB;
	public static Block GOLD_STAIRS;
	public static Block GOLD_SLAB;
	public static Block DIAMOND_STAIRS;
	public static Block DIAMOND_SLAB;
	public static Block EMERALD_STAIRS;
	public static Block EMERALD_SLAB;
	public static Block COAL_STAIRS;
	public static Block COAL_SLAB;
	public static Block OBSIDIAN_STAIRS;
	public static Block OBSIDIAN_SLAB;
	public static Block NETHERITE_STAIRS;
	public static Block NETHERITE_SLAB;
	public static Block POLISHED_GRANITE_WALL;
	public static Block POLISHED_DIORITE_WALL;
	public static Block POLISHED_ANDESITE_WALL;
	public static Block ANDESITE_BRICKS;
	public static Block MOSSY_ANDESITE_BRICKS;
	public static Block CRACKED_ANDESITE_BRICKS;
	public static Block ANDESITE_BRICK_STAIRS;
	public static Block ANDESITE_BRICK_SLAB;
	public static Block ANDESITE_BRICK_WALL;
	public static Block MOSSY_ANDESITE_BRICK_STAIRS;
	public static Block MOSSY_ANDESITE_BRICK_SLAB;
	public static Block MOSSY_ANDESITE_BRICK_WALL;
	public static Block DIORITE_BRICKS;
	public static Block MOSSY_DIORITE_BRICKS;
	public static Block CRACKED_DIORITE_BRICKS;
	public static Block DIORITE_BRICK_STAIRS;
	public static Block DIORITE_BRICK_SLAB;
	public static Block DIORITE_BRICK_WALL;
	public static Block MOSSY_DIORITE_BRICK_STAIRS;
	public static Block MOSSY_DIORITE_BRICK_SLAB;
	public static Block MOSSY_DIORITE_BRICK_WALL;
	public static Block GRANITE_BRICKS;
	public static Block MOSSY_GRANITE_BRICKS;
	public static Block CRACKED_GRANITE_BRICKS;
	public static Block GRANITE_BRICK_STAIRS;
	public static Block GRANITE_BRICK_SLAB;
	public static Block GRANITE_BRICK_WALL;
	public static Block MOSSY_GRANITE_BRICK_STAIRS;
	public static Block MOSSY_GRANITE_BRICK_SLAB;
	public static Block MOSSY_GRANITE_BRICK_WALL;
	public static Block CRACKED_END_STONE_BRICKS;
	public static Block CRACKED_QUARTZ_BRICKS;
	public static Block WET_FARMLAND;
	public static Block SUPREME_MC_LOGO_BLOCK;
	public static Block SPRUCE_BOOKSHELF;
	public static Block BIRCH_BOOKSHELF;
	public static Block JUNGLE_BOOKSHELF;
	public static Block ACACIA_BOOKSHELF;
	public static Block DARK_OAK_BOOKSHELF;
	public static Block MANGROVE_BOOKSHELF;
	public static Block CHERRY_BOOKSHELF;
	public static Block PALE_OAK_BOOKSHELF;
	public static Block BAMBOO_BOOKSHELF;
	public static Block CRIMSON_BOOKSHELF;
	public static Block WARPED_BOOKSHELF;
	public static Block PALM_BOOKSHELF;
	public static Block LAVENDER_BOOKSHELF;
	public static Block SPRUCE_CRAFTING_TABLE;
	public static Block BIRCH_CRAFTING_TABLE;
	public static Block JUNGLE_CRAFTING_TABLE;
	public static Block ACACIA_CRAFTING_TABLE;
	public static Block DARK_OAK_CRAFTING_TABLE;
	public static Block MANGROVE_CRAFTING_TABLE;
	public static Block CHERRY_CRAFTING_TABLE;
	public static Block PALE_OAK_CRAFTING_TABLE;
	public static Block BAMBOO_CRAFTING_TABLE;
	public static Block CRIMSON_CRAFTING_TABLE;
	public static Block WARPED_CRAFTING_TABLE;
	public static Block PALM_CRAFTING_TABLE;
	public static Block LAVENDER_CRAFTING_TABLE;
	public static Block LAVENDER_WART_BLOCK;
	public static Block LAVENDER_ROOTS;
	public static Block LAVENDER_FUNGUS;
	public static Block BLACKSTONE_FURNACE;
	public static Block DEEPSLATE_FURNACE;
	public static Block PALM_LOG;
	public static Block STRIPPED_PALM_LOG;
	public static Block PALM_WOOD;
	public static Block STRIPPED_PALM_WOOD;
	public static Block LAVENDER_STEM;
	public static Block LAVENDER_ENDSPAR;
	public static Block STRIPPED_LAVENDER_STEM;
	public static Block LAVENDER_HYPHAE;
	public static Block STRIPPED_LAVENDER_HYPHAE;
	public static Block LAVENDER_WOOD;
	public static Block STRIPPED_LAVENDER_WOOD;
	public static Block LAVENDER_PLANKS;
	public static Block LAVENDER_SLAB;
	public static Block LAVENDER_STAIRS;
	public static Block LAVENDER_FENCE;
	public static Block LAVENDER_FENCE_GATE;
	public static Block LAVENDER_DOOR;
	public static Block LAVENDER_TRAPDOOR;
	public static Block LAVENDER_PRESSURE_PLATE;
	public static Block LAVENDER_BUTTON;
	public static Block LAVENDER_SIGN;
	public static Block LAVENDER_WALL_SIGN;
	public static Block LAVENDER_HANGING_SIGN;
	public static Block LAVENDER_WALL_HANGING_SIGN;
	public static Block PALM_PLANKS;
	public static Block PALM_SLAB;
	public static Block PALM_STAIRS;
	public static Block PALM_FENCE;
	public static Block PALM_FENCE_GATE;
	public static Block PALM_DOOR;
	public static Block PALM_TRAPDOOR;
	public static Block PALM_PRESSURE_PLATE;
	public static Block PALM_BUTTON;
	public static Block PALM_SIGN;
	public static Block PALM_WALL_SIGN;
	public static Block PALM_HANGING_SIGN;
	public static Block PALM_WALL_HANGING_SIGN;
	public static Block PALM_LEAVES;
	public static Block PALM_SAPLING;
	public static Block POTTED_PALM_SAPLING;
	public static Block COCONUT;
	public static Block COTTON_BUSH;
	public static Block TOMATO_BUSH;
	public static Block BEACH_GRASS;
	public static Block TALL_BEACH_GRASS;
	public static Block BUTTERCUP;
	public static Block CLOVER;
	public static Block ATLANTIS_DEBRIS;
	public static Block ABYSSALITE_BLOCK;
	public static Block ABYSSALITE_STAIRS;
	public static Block ABYSSALITE_SLAB;
	public static Block ICICLE;
	public static Block GRAPE_VINE;
	public static Block GRAPE_VINE_PLANT;
	public static Block CORN_STALK;
	public static Block CORN_STALK_PLANT;
	public static Block MILK_FLUID_BLOCK;
	public static Block MILK_CAULDRON;
	public static Block SNOW_TNT;
	public static Block FIRE_TNT;
	public static Block GLENDSTONE;
	public static final Map<String, Block> COLORED_SANDSTONE_BLOCKS = new LinkedHashMap<>();
	
	public static final Map<DyeColor, Block> GLOW_BLOCKS = new EnumMap<>(DyeColor.class);
	public static final Map<DyeColor, Block> SLIME_BLOCKS = new EnumMap<>(DyeColor.class);

	private static boolean registered;

	public static void bootstrap() {
		if (registered) return;
		registered = true;
		MILK_FLUID_BLOCK = register("milk", new MilkLiquidBlock(ModFluids.MILK,
			props("milk").mapColor(MapColor.SNOW).noLootTable()));
		MILK_CAULDRON = register("milk_cauldron", new MilkCauldronBlock(
			props("milk_cauldron").mapColor(MapColor.SNOW).strength(2.0F)));
		AQUAMARINE_ORE = register("aquamarine_ore", new DropExperienceBlock(UniformInt.of(3, 7), props("aquamarine_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
		BURNING_DIAMOND_ORE = register("burning_diamond_ore", new DropExperienceBlock(UniformInt.of(3, 7), props("burning_diamond_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
		NETHER_ANTHRACITE_ORE = register("nether_anthracite_ore", new DropExperienceBlock(UniformInt.of(0, 2), props("nether_anthracite_ore").mapColor(MapColor.NETHER).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
		ANTHRACITE_BLOCK = register("anthracite_block", new Block(props("anthracite_block").mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(5.0F, 6.0F)));
		DEEPSLATE_AQUAMARINE_ORE = register("deepslate_aquamarine_ore", new DropExperienceBlock(UniformInt.of(3, 7), props("deepslate_aquamarine_ore").mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()));
		XYLIUM_ORE = register("xylium_ore", new DropExperienceBlock(UniformInt.of(1, 5), endStoneProps("xylium_ore")));
		XYLIUM_BLOCK = register("xylium_block", new Block(endStoneProps("xylium_block")));
		AQUAMARINE_BLOCK = register("aquamarine_block", new Block(props("aquamarine_block").mapColor(MapColor.COLOR_CYAN).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
		BURNING_DIAMOND_BLOCK = register("burning_diamond_block", new Block(props("burning_diamond_block").mapColor(MapColor.DIAMOND).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
		AMBER_BLOCK = register("amber_block", new Block(props("amber_block").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
		ATLANTIS_DEBRIS = register("atlantis_debris", new Block(props("atlantis_debris").strength(30.0F, 1200.0F).requiresCorrectToolForDrops()));
		ABYSSALITE_BLOCK = register("abyssalite_block", new Block(props("abyssalite_block").strength(50.0F, 1200.0F).requiresCorrectToolForDrops()));
		WET_FARMLAND = register("wet_farmland", new WetFarmlandBlock(props("wet_farmland").mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F).randomTicks()));
		SUPREME_MC_LOGO_BLOCK = register("suprememc_logo_block", new Block(props("suprememc_logo_block").mapColor(MapColor.GRASS).sound(SoundType.GRASS).strength(0.6F)));
		PALM_LOG = register("palm_log", new RotatedPillarBlock(props("palm_log").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F)));
		STRIPPED_PALM_LOG = register("stripped_palm_log", new RotatedPillarBlock(props("stripped_palm_log").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F)));
		PALM_WOOD = register("palm_wood", new RotatedPillarBlock(props("palm_wood").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F)));
		STRIPPED_PALM_WOOD = register("stripped_palm_wood", new RotatedPillarBlock(props("stripped_palm_wood").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F)));
		LAVENDER_STEM = register("lavender_stem", new RotatedPillarBlock(props("lavender_stem").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		LAVENDER_ENDSPAR = register("lavender_endspar", new LavenderEndsparBlock(props("lavender_endspar").mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.4F).sound(SoundType.NYLIUM).randomTicks()));
		LAVENDER_ROOTS = register("lavender_roots", new LavenderRootsBlock(props("lavender_roots").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		LAVENDER_FUNGUS = register("lavender_fungus", new LavenderFungusBlock(props("lavender_fungus").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		STRIPPED_LAVENDER_STEM = register("stripped_lavender_stem", new RotatedPillarBlock(props("stripped_lavender_stem").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		LAVENDER_HYPHAE = register("lavender_hyphae", new RotatedPillarBlock(props("lavender_hyphae").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		STRIPPED_LAVENDER_HYPHAE = register("stripped_lavender_hyphae", new RotatedPillarBlock(props("stripped_lavender_hyphae").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		LAVENDER_WOOD = register("lavender_wood", new RotatedPillarBlock(props("lavender_wood").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		STRIPPED_LAVENDER_WOOD = register("stripped_lavender_wood", new RotatedPillarBlock(props("stripped_lavender_wood").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F)));
		LAVENDER_PLANKS = register("lavender_planks", new Block(props("lavender_planks").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		LAVENDER_SLAB = register("lavender_slab", new SlabBlock(props("lavender_slab").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		LAVENDER_STAIRS = register("lavender_stairs", new LavenderStairsBlock());
		LAVENDER_FENCE = register("lavender_fence", new FenceBlock(props("lavender_fence").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		LAVENDER_FENCE_GATE = register("lavender_fence_gate", new FenceGateBlock(ModWoodTypes.LAVENDER, props("lavender_fence_gate").mapColor(MapColor.COLOR_PURPLE).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		LAVENDER_DOOR = register("lavender_door", new LavenderDoorBlock());
		LAVENDER_TRAPDOOR = register("lavender_trapdoor", new LavenderTrapDoorBlock());
		LAVENDER_PRESSURE_PLATE = register("lavender_pressure_plate", new LavenderPressurePlateBlock());
		LAVENDER_BUTTON = register("lavender_button", new LavenderButtonBlock());
		PALM_PLANKS = register("palm_planks", new Block(props("palm_planks").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		PALM_SLAB = register("palm_slab", new SlabBlock(props("palm_slab").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
		COCONUT = register("coconut", new CoconutBlock(props("coconut").mapColor(MapColor.PLANT).sound(SoundType.WOOD).strength(0.2F, 3.0F).noCollision()));
		COTTON_BUSH = register("cotton_bush", new CottonBushBlock(props("cotton_bush").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.2F).noCollision()));
		TOMATO_BUSH = register("tomato_bush", new TomatoBushBlock(props("tomato_bush").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.2F).noCollision()));
		BEACH_GRASS = register("beach_grass", new BeachGrassBlock(props("beach_grass").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		TALL_BEACH_GRASS = register("tall_beach_grass", new TallBeachGrassBlock(props("tall_beach_grass").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		GRAPE_VINE = register("grape_vine", new GrapeVineBlock(props("grape_vine").mapColor(MapColor.PLANT).noCollision().noOcclusion().randomTicks()));
		GRAPE_VINE_PLANT = register("grape_vine_plant", new GrapeVinePlantBlock(props("grape_vine_plant").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		CORN_STALK = register("corn_stalk", new CornStalkBlock(props("corn_stalk").mapColor(MapColor.PLANT).noCollision().noOcclusion().randomTicks()));
		CORN_STALK_PLANT = register("corn_stalk_plant", new CornStalkPlantBlock(props("corn_stalk_plant").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		BUTTERCUP = register("buttercup", new FlowerBlock(MobEffects.SATURATION, 7.0F, props("buttercup").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		CLOVER = register("clover", new CloverBlock(props("clover").mapColor(MapColor.PLANT).noCollision().noOcclusion()));
		SNOW_TNT = register("snow_tnt", new SnowTntBlock(props("snow_tnt").mapColor(MapColor.SNOW).strength(0.0F)));
		FIRE_TNT = register("fire_tnt", new FireTntBlock(props("fire_tnt").mapColor(MapColor.FIRE).strength(0.0F)));
		GLENDSTONE = register("glendstone", new Block(props("glendstone").mapColor(MapColor.SAND).instrument(NoteBlockInstrument.PLING).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> 15).isRedstoneConductor((state, level, pos) -> false)));
		registerColoredSandstoneFamilies();
		registerSimple(
			"burning_diamond_stairs", 
			"burning_diamond_slab", 
			"aquamarine_stairs", 
			"aquamarine_slab", 
			"amber_ore", 
			"deepslate_amber_ore", 
			"prismarine_ore", 
			"deepslate_prismarine_ore", 
			"amber_stairs", 
			"amber_slab", 
			"iron_stairs", 
			"iron_slab", 
			"lapis_stairs", 
			"lapis_slab", 
			"gold_stairs", 
			"gold_slab", 
			"diamond_stairs", 
			"diamond_slab", 
			"emerald_stairs", 
			"emerald_slab", 
			"coal_stairs", 
			"coal_slab", 
			"obsidian_stairs", 
			"obsidian_slab", 
			"netherite_stairs", 
			"netherite_slab", 
			"icicle", 
			"abyssalite_stairs", 
			"abyssalite_slab", 
			"polished_granite_wall", 
			"polished_diorite_wall", 
			"polished_andesite_wall", 
			"andesite_bricks", 
			"mossy_andesite_bricks",
			"cracked_andesite_bricks",
			"andesite_brick_stairs", 
			"andesite_brick_slab", 
			"andesite_brick_wall", 
			"mossy_andesite_brick_stairs",
			"mossy_andesite_brick_slab",
			"mossy_andesite_brick_wall",
			"diorite_bricks", 
			"mossy_diorite_bricks",
			"cracked_diorite_bricks",
			"diorite_brick_stairs", 
			"diorite_brick_slab", 
			"diorite_brick_wall", 
			"mossy_diorite_brick_stairs",
			"mossy_diorite_brick_slab",
			"mossy_diorite_brick_wall",
			"granite_bricks", 
			"mossy_granite_bricks",
			"cracked_granite_bricks",
			"cracked_end_stone_bricks",
			"cracked_quartz_bricks",
			"granite_brick_stairs", 
			"granite_brick_slab", 
			"granite_brick_wall", 
			"mossy_granite_brick_stairs",
			"mossy_granite_brick_slab",
			"mossy_granite_brick_wall",
			"spruce_bookshelf", 
			"birch_bookshelf", 
			"jungle_bookshelf", 
			"acacia_bookshelf", 
			"dark_oak_bookshelf", 
			"mangrove_bookshelf", 
			"cherry_bookshelf", 
			"pale_oak_bookshelf", 
			"bamboo_bookshelf", 
			"crimson_bookshelf", 
			"warped_bookshelf", 
			"palm_bookshelf", 
			"lavender_bookshelf",
			"spruce_crafting_table", 
			"birch_crafting_table", 
			"jungle_crafting_table", 
			"acacia_crafting_table", 
			"dark_oak_crafting_table", 
			"mangrove_crafting_table", 
			"cherry_crafting_table", 
			"pale_oak_crafting_table", 
			"bamboo_crafting_table", 
			"crimson_crafting_table", 
			"warped_crafting_table", 
			"palm_crafting_table", 
			"lavender_crafting_table",
			"lavender_wart_block",
			"palm_stairs", 
			"palm_fence", 
			"palm_fence_gate", 
			"palm_door", 
			"palm_trapdoor", 
			"palm_pressure_plate", 
			"palm_button", 
			"palm_leaves", 
			"palm_sapling", 
			"potted_palm_sapling");

		BLACKSTONE_FURNACE = register("blackstone_furnace", new StoneFurnaceBlock(props("blackstone_furnace")));
		DEEPSLATE_FURNACE = register("deepslate_furnace", new StoneFurnaceBlock(props("deepslate_furnace")));

		PALM_SIGN = register("palm_sign", new StandingSignBlock(ModWoodTypes.PALM, props("palm_sign")));
		PALM_WALL_SIGN = register("palm_wall_sign", new WallSignBlock(ModWoodTypes.PALM, props("palm_wall_sign")));
		PALM_HANGING_SIGN = register("palm_hanging_sign", new CeilingHangingSignBlock(ModWoodTypes.PALM, props("palm_hanging_sign")));
		PALM_WALL_HANGING_SIGN = register("palm_wall_hanging_sign", new WallHangingSignBlock(ModWoodTypes.PALM, props("palm_wall_hanging_sign")));
		LAVENDER_SIGN = register("lavender_sign", new StandingSignBlock(ModWoodTypes.LAVENDER, props("lavender_sign")));
		LAVENDER_WALL_SIGN = register("lavender_wall_sign", new WallSignBlock(ModWoodTypes.LAVENDER, props("lavender_wall_sign")));
		LAVENDER_HANGING_SIGN = register("lavender_hanging_sign", new CeilingHangingSignBlock(ModWoodTypes.LAVENDER, props("lavender_hanging_sign")));
		LAVENDER_WALL_HANGING_SIGN = register("lavender_wall_hanging_sign", new WallHangingSignBlock(ModWoodTypes.LAVENDER, props("lavender_wall_hanging_sign")));
			
		for (DyeColor color : DyeColor.values()) {
			String name = color.getSerializedName();
			Block glow = register(name + "_glowblock", new Block(props(name + "_glowblock").mapColor(MapColor.SAND).instrument(NoteBlockInstrument.PLING).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> 15)));
			Block slime = register(name + "_slime_block", new ColoredSlimeBlock(color, props(name + "_slime_block").mapColor(MapColor.GRASS).sound(SoundType.SLIME_BLOCK).noOcclusion()));
			GLOW_BLOCKS.put(color, glow);
			SLIME_BLOCKS.put(color, slime);
		}
	}

	private static void registerColoredSandstoneFamilies() {
		for (String color : new String[]{"white", "black", "pink"}) {
			registerColoredSandstone(color, "sand", new SandBlock(new ColorRGBA(14406560), props(color + "_sand").mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.5F)));
			registerColoredSandstone(color, "sandstone", new Block(props(color + "_sandstone").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "sandstone_stairs", new MaterialStairsBlock(
				COLORED_SANDSTONE_BLOCKS.get(color + "_sandstone").defaultBlockState(),
				props(color + "_sandstone_stairs").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "sandstone_slab", new SlabBlock(props(color + "_sandstone_slab").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "sandstone_wall", new WallBlock(props(color + "_sandstone_wall").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "smooth_sandstone", new Block(props("smooth_" + color + "_sandstone").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "smooth_sandstone_stairs", new MaterialStairsBlock(
				COLORED_SANDSTONE_BLOCKS.get("smooth_" + color + "_sandstone").defaultBlockState(),
				props("smooth_" + color + "_sandstone_stairs").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "smooth_sandstone_slab", new SlabBlock(props("smooth_" + color + "_sandstone_slab").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "cut_sandstone", new Block(props("cut_" + color + "_sandstone").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
			registerColoredSandstone(color, "cut_sandstone_slab", new SlabBlock(props("cut_" + color + "_sandstone_slab").mapColor(MapColor.SAND).sound(SoundType.STONE).strength(0.8F, 0.8F).requiresCorrectToolForDrops()));
		}
	}

	private static void registerColoredSandstone(String color, String suffix, Block block) {
		String id = suffix.startsWith("sand") ? color + "_" + suffix : suffix.replace("sandstone", color + "_sandstone");
		COLORED_SANDSTONE_BLOCKS.put(id, register(id, block));
	}

	private static void registerSimple(String... ids) {
		for (String id : ids) {
			Block block = switch (id) {
				case "icicle" -> register(id, new PointedDripstoneBlock(net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), props(id).mapColor(MapColor.ICE).sound(SoundType.POINTED_DRIPSTONE).strength(0.5F).randomTicks()));
				case "palm_fence" -> register(id, new FenceBlock(props(id).mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
				case "palm_fence_gate" -> register(id, new FenceGateBlock(ModWoodTypes.PALM, props(id).mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
				case "palm_door" -> register(id, new PalmDoorBlock());
				case "palm_trapdoor" -> register(id, new PalmTrapDoorBlock());
				case "palm_pressure_plate" -> register(id, new PalmPressurePlateBlock());
				case "palm_button" -> register(id, new PalmButtonBlock());
				case "palm_leaves" -> register(id, new PalmLeavesBlock(props(id).mapColor(MapColor.PLANT).noCollision().noOcclusion()));
				case String s when s.endsWith("_slab") -> register(id, new SlabBlock(properties(id)));
				case String s when s.endsWith("_stairs") -> register(id, new MaterialStairsBlock(baseStateForStairs(id), properties(id)));
				case String s when s.endsWith("_wall") -> register(id, new WallBlock(properties(id)));
				default -> register(id, new Block(properties(id)));
			};
			switch (id) {
				case "burning_diamond_stairs" -> BURNING_DIAMOND_STAIRS = block;
				case "burning_diamond_slab" -> BURNING_DIAMOND_SLAB = block;
				case "aquamarine_stairs" -> AQUAMARINE_STAIRS = block;
				case "aquamarine_slab" -> AQUAMARINE_SLAB = block;
				case "amber_ore" -> AMBER_ORE = block;
				case "deepslate_amber_ore" -> DEEPSLATE_AMBER_ORE = block;
				case "prismarine_ore" -> PRISMARINE_ORE = block;
				case "deepslate_prismarine_ore" -> DEEPSLATE_PRISMARINE_ORE = block;
				case "amber_stairs" -> AMBER_STAIRS = block;
				case "amber_slab" -> AMBER_SLAB = block;
				case "iron_stairs" -> IRON_STAIRS = block;
				case "iron_slab" -> IRON_SLAB = block;
				case "lapis_stairs" -> LAPIS_STAIRS = block;
				case "lapis_slab" -> LAPIS_SLAB = block;
				case "gold_stairs" -> GOLD_STAIRS = block;
				case "gold_slab" -> GOLD_SLAB = block;
				case "diamond_stairs" -> DIAMOND_STAIRS = block;
				case "diamond_slab" -> DIAMOND_SLAB = block;
				case "emerald_stairs" -> EMERALD_STAIRS = block;
				case "emerald_slab" -> EMERALD_SLAB = block;
				case "coal_stairs" -> COAL_STAIRS = block;
				case "coal_slab" -> COAL_SLAB = block;
				case "obsidian_stairs" -> OBSIDIAN_STAIRS = block;
				case "obsidian_slab" -> OBSIDIAN_SLAB = block;
				case "netherite_stairs" -> NETHERITE_STAIRS = block;
				case "netherite_slab" -> NETHERITE_SLAB = block;
				case "icicle" -> ICICLE = block;
				case "abyssalite_stairs" -> ABYSSALITE_STAIRS = block;
				case "abyssalite_slab" -> ABYSSALITE_SLAB = block;
				case "polished_granite_wall" -> POLISHED_GRANITE_WALL = block;
				case "polished_diorite_wall" -> POLISHED_DIORITE_WALL = block;
				case "polished_andesite_wall" -> POLISHED_ANDESITE_WALL = block;
				case "andesite_bricks" -> ANDESITE_BRICKS = block;
				case "mossy_andesite_bricks" -> MOSSY_ANDESITE_BRICKS = block;
				case "cracked_andesite_bricks" -> CRACKED_ANDESITE_BRICKS = block;
				case "andesite_brick_stairs" -> ANDESITE_BRICK_STAIRS = block;
				case "andesite_brick_slab" -> ANDESITE_BRICK_SLAB = block;
				case "andesite_brick_wall" -> ANDESITE_BRICK_WALL = block;
				case "mossy_andesite_brick_stairs" -> MOSSY_ANDESITE_BRICK_STAIRS = block;
				case "mossy_andesite_brick_slab" -> MOSSY_ANDESITE_BRICK_SLAB = block;
				case "mossy_andesite_brick_wall" -> MOSSY_ANDESITE_BRICK_WALL = block;
				case "diorite_bricks" -> DIORITE_BRICKS = block;
				case "mossy_diorite_bricks" -> MOSSY_DIORITE_BRICKS = block;
				case "cracked_diorite_bricks" -> CRACKED_DIORITE_BRICKS = block;
				case "diorite_brick_stairs" -> DIORITE_BRICK_STAIRS = block;
				case "diorite_brick_slab" -> DIORITE_BRICK_SLAB = block;
				case "diorite_brick_wall" -> DIORITE_BRICK_WALL = block;
				case "mossy_diorite_brick_stairs" -> MOSSY_DIORITE_BRICK_STAIRS = block;
				case "mossy_diorite_brick_slab" -> MOSSY_DIORITE_BRICK_SLAB = block;
				case "mossy_diorite_brick_wall" -> MOSSY_DIORITE_BRICK_WALL = block;
				case "granite_bricks" -> GRANITE_BRICKS = block;
				case "mossy_granite_bricks" -> MOSSY_GRANITE_BRICKS = block;
				case "cracked_granite_bricks" -> CRACKED_GRANITE_BRICKS = block;
				case "cracked_end_stone_bricks" -> CRACKED_END_STONE_BRICKS = block;
				case "cracked_quartz_bricks" -> CRACKED_QUARTZ_BRICKS = block;
				case "granite_brick_stairs" -> GRANITE_BRICK_STAIRS = block;
				case "granite_brick_slab" -> GRANITE_BRICK_SLAB = block;
				case "granite_brick_wall" -> GRANITE_BRICK_WALL = block;
				case "mossy_granite_brick_stairs" -> MOSSY_GRANITE_BRICK_STAIRS = block;
				case "mossy_granite_brick_slab" -> MOSSY_GRANITE_BRICK_SLAB = block;
				case "mossy_granite_brick_wall" -> MOSSY_GRANITE_BRICK_WALL = block;
				case "spruce_bookshelf" -> SPRUCE_BOOKSHELF = block;
				case "birch_bookshelf" -> BIRCH_BOOKSHELF = block;
				case "jungle_bookshelf" -> JUNGLE_BOOKSHELF = block;
				case "acacia_bookshelf" -> ACACIA_BOOKSHELF = block;
				case "dark_oak_bookshelf" -> DARK_OAK_BOOKSHELF = block;
				case "mangrove_bookshelf" -> MANGROVE_BOOKSHELF = block;
				case "cherry_bookshelf" -> CHERRY_BOOKSHELF = block;
				case "pale_oak_bookshelf" -> PALE_OAK_BOOKSHELF = block;
				case "bamboo_bookshelf" -> BAMBOO_BOOKSHELF = block;
				case "crimson_bookshelf" -> CRIMSON_BOOKSHELF = block;
				case "warped_bookshelf" -> WARPED_BOOKSHELF = block;
				case "palm_bookshelf" -> PALM_BOOKSHELF = block;
				case "lavender_bookshelf" -> LAVENDER_BOOKSHELF = block;
				case "spruce_crafting_table" -> SPRUCE_CRAFTING_TABLE = block;
				case "birch_crafting_table" -> BIRCH_CRAFTING_TABLE = block;
				case "jungle_crafting_table" -> JUNGLE_CRAFTING_TABLE = block;
				case "acacia_crafting_table" -> ACACIA_CRAFTING_TABLE = block;
				case "dark_oak_crafting_table" -> DARK_OAK_CRAFTING_TABLE = block;
				case "mangrove_crafting_table" -> MANGROVE_CRAFTING_TABLE = block;
				case "cherry_crafting_table" -> CHERRY_CRAFTING_TABLE = block;
				case "pale_oak_crafting_table" -> PALE_OAK_CRAFTING_TABLE = block;
				case "bamboo_crafting_table" -> BAMBOO_CRAFTING_TABLE = block;
				case "crimson_crafting_table" -> CRIMSON_CRAFTING_TABLE = block;
				case "warped_crafting_table" -> WARPED_CRAFTING_TABLE = block;
				case "palm_crafting_table" -> PALM_CRAFTING_TABLE = block;
				case "lavender_crafting_table" -> LAVENDER_CRAFTING_TABLE = block;
				case "lavender_wart_block" -> LAVENDER_WART_BLOCK = block;
				case "palm_stairs" -> PALM_STAIRS = block;
				case "palm_fence" -> PALM_FENCE = block;
				case "palm_fence_gate" -> PALM_FENCE_GATE = block;
				case "palm_door" -> PALM_DOOR = block;
				case "palm_trapdoor" -> PALM_TRAPDOOR = block;
				case "palm_pressure_plate" -> PALM_PRESSURE_PLATE = block;
				case "palm_button" -> PALM_BUTTON = block;
				case "palm_leaves" -> PALM_LEAVES = block;
				case "palm_sapling" -> PALM_SAPLING = block;
				case "potted_palm_sapling" -> POTTED_PALM_SAPLING = block;
			}
		}
	}

	private static net.minecraft.world.level.block.state.BlockState baseStateForStairs(String id) {
		return switch (id) {
			case "burning_diamond_stairs" -> BURNING_DIAMOND_BLOCK.defaultBlockState();
			case "aquamarine_stairs" -> AQUAMARINE_BLOCK.defaultBlockState();
			case "amber_stairs" -> AMBER_BLOCK.defaultBlockState();
			case "iron_stairs" -> net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState();
			case "lapis_stairs" -> net.minecraft.world.level.block.Blocks.LAPIS_BLOCK.defaultBlockState();
			case "gold_stairs" -> net.minecraft.world.level.block.Blocks.GOLD_BLOCK.defaultBlockState();
			case "diamond_stairs" -> net.minecraft.world.level.block.Blocks.DIAMOND_BLOCK.defaultBlockState();
			case "emerald_stairs" -> net.minecraft.world.level.block.Blocks.EMERALD_BLOCK.defaultBlockState();
			case "coal_stairs" -> net.minecraft.world.level.block.Blocks.COAL_BLOCK.defaultBlockState();
			case "obsidian_stairs" -> net.minecraft.world.level.block.Blocks.OBSIDIAN.defaultBlockState();
			case "netherite_stairs" -> net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK.defaultBlockState();
			case "abyssalite_stairs" -> ABYSSALITE_BLOCK.defaultBlockState();
			case "andesite_brick_stairs" -> ANDESITE_BRICKS.defaultBlockState();
			case "mossy_andesite_brick_stairs" -> MOSSY_ANDESITE_BRICKS.defaultBlockState();
			case "diorite_brick_stairs" -> DIORITE_BRICKS.defaultBlockState();
			case "mossy_diorite_brick_stairs" -> MOSSY_DIORITE_BRICKS.defaultBlockState();
			case "granite_brick_stairs" -> GRANITE_BRICKS.defaultBlockState();
			case "mossy_granite_brick_stairs" -> MOSSY_GRANITE_BRICKS.defaultBlockState();
			default -> net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
		};
	}

	private static BlockBehaviour.Properties props(String id) { return ModContent.blockProperties(id); }

	private static BlockBehaviour.Properties properties(String id) {
		if (id.contains("_brick")) {
			return props(id).sound(SoundType.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops();
		}
		return props(id);
	}

	private static BlockBehaviour.Properties endStoneProps(String id) {
		return props(id).mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3.0F, 9.0F).requiresCorrectToolForDrops();
	}

	private static <T extends Block> T register(String id, T block) {
		Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), block);
		return block;
	}
}
