package com.suprememc.tabs;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockItems;
import com.suprememc.content.init.ModFoods;
import com.suprememc.content.init.ModItems;
import com.suprememc.content.init.ModPotions;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public final class CreativeTab {

	private CreativeTab() {
	}

	public static List<ItemStack> enchantedBooks(HolderLookup.Provider holders) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = holders.lookupOrThrow(Registries.ENCHANTMENT);
        List<ItemStack> books = new ArrayList<>();
        for (EnchantmentEntry entry : List.of(
            new EnchantmentEntry("bounty", 3),
            new EnchantmentEntry("venom", 2),
            new EnchantmentEntry("decay", 2),
            new EnchantmentEntry("wisdom", 3),
            new EnchantmentEntry("smelting", 1),
            new EnchantmentEntry("tension", 3),
            new EnchantmentEntry("super_channeling", 1),
            new EnchantmentEntry("curse_of_mass", 1),
            new EnchantmentEntry("curse_of_sloth", 1)
        )) {
            ResourceKey<Enchantment> key = ResourceKey.create(
                Registries.ENCHANTMENT,
                Identifier.fromNamespaceAndPath("suprememc", entry.id())
            );
            for (int level = 1; level <= entry.maxLevel(); level++) {
                ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                book.enchant(enchantments.getOrThrow(key), level);
                book.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable(
                    "enchantment.suprememc." + entry.id() + ".description"))));
                books.add(book);
            }
        }
        return books;
    }

    private record EnchantmentEntry(String id, int maxLevel) {
    }

    public static List<ItemStack> potions() {
        List<ItemStack> potions = new ArrayList<>();
        for (var potion : List.of(
            ModPotions.LUCK_POTION,
            ModPotions.LONG_LUCK_POTION,
            ModPotions.STRONG_LUCK_POTION,
            ModPotions.BAD_LUCK_POTION,
            ModPotions.LONG_BAD_LUCK_POTION,
            ModPotions.STRONG_BAD_LUCK_POTION,
            ModPotions.HUNGER_POTION,
            ModPotions.LONG_HUNGER_POTION,
            ModPotions.STRONG_HUNGER_POTION,
            ModPotions.DECAY_POTION,
            ModPotions.LONG_DECAY_POTION,
            ModPotions.STRONG_DECAY_POTION
        )) {
            ItemStack stack = new ItemStack(Items.POTION);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
            potions.add(stack);
        }
        return potions;
    }

	public static void populate() {

        // Full Blocks 1
		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // Logo Block
            ModContent.SUPREME_MC_LOGO_BLOCK,

            // Natural Blocks
            ModContent.COLORED_SANDSTONE_BLOCKS.get("white_sand"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("black_sand"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("pink_sand"),
            ModContent.WET_FARMLAND,
            ModContent.LAVENDER_ENDSPAR,
            ModContent.LAVENDER_WART_BLOCK,

            // TNT
            ModContent.FIRE_TNT,
            ModContent.SNOW_TNT,

            // Non-Wood Building Blocks
            ModContent.ANDESITE_BRICKS,
            ModContent.MOSSY_ANDESITE_BRICKS,
            ModContent.CRACKED_ANDESITE_BRICKS,
            ModContent.GRANITE_BRICKS,
            ModContent.MOSSY_GRANITE_BRICKS,
            ModContent.CRACKED_GRANITE_BRICKS,
            ModContent.DIORITE_BRICKS,
            ModContent.MOSSY_DIORITE_BRICKS,
            ModContent.CRACKED_DIORITE_BRICKS,
            ModContent.COLORED_SANDSTONE_BLOCKS.get("white_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("black_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("pink_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_white_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_black_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_pink_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_white_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_black_sandstone"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_pink_sandstone"),
            ModContent.CRACKED_END_STONE_BRICKS,
            ModContent.CRACKED_QUARTZ_BRICKS,

            // Ores
            ModContent.AQUAMARINE_ORE, 
            ModContent.DEEPSLATE_AQUAMARINE_ORE, 
            ModContent.ATLANTIS_DEBRIS,
			ModContent.AMBER_ORE, 
            ModContent.DEEPSLATE_AMBER_ORE, 
			ModContent.NETHER_ANTHRACITE_ORE, 
            ModContent.BURNING_DIAMOND_ORE,
            ModContent.XYLIUM_ORE,
            
            // Mineral Blocks
            ModContent.AQUAMARINE_BLOCK,
            ModContent.ABYSSALITE_BLOCK,
            ModContent.AMBER_BLOCK,
            ModContent.ANTHRACITE_BLOCK,
            ModContent.BURNING_DIAMOND_BLOCK,
            ModContent.XYLIUM_BLOCK,

            // Logs / Stems
            ModContent.PALM_LOG,
            ModContent.LAVENDER_STEM,

            // Wood / Hyphae (6-sided bark)
            ModContent.PALM_WOOD,
            ModContent.LAVENDER_HYPHAE,
            ModContent.LAVENDER_WOOD,

            // Stripped Logs / Stems
            ModContent.STRIPPED_PALM_LOG,
            ModContent.STRIPPED_LAVENDER_STEM,

            // Stripped Wood / Hyphae
            ModContent.STRIPPED_PALM_WOOD,
            ModContent.STRIPPED_LAVENDER_HYPHAE,
            ModContent.STRIPPED_LAVENDER_WOOD,

            // Planks
            ModContent.PALM_PLANKS,
            ModContent.LAVENDER_PLANKS,

            // Leaves
            ModContent.PALM_LEAVES,

            // Decorative
            ModContent.SPRUCE_BOOKSHELF,
            ModContent.BIRCH_BOOKSHELF,
            ModContent.JUNGLE_BOOKSHELF,
            ModContent.ACACIA_BOOKSHELF,
            ModContent.DARK_OAK_BOOKSHELF,
            ModContent.MANGROVE_BOOKSHELF,
            ModContent.CHERRY_BOOKSHELF,
            ModContent.PALE_OAK_BOOKSHELF,
            ModContent.BAMBOO_BOOKSHELF,
            ModContent.CRIMSON_BOOKSHELF,
            ModContent.WARPED_BOOKSHELF,
            ModContent.PALM_BOOKSHELF,
            ModContent.LAVENDER_BOOKSHELF
        ));

        // Colored Full Blocks
        ModContent.CREATIVE_TAB_ITEMS.addAll(ModContent.GLOW_BLOCKS.values());
		ModContent.CREATIVE_TAB_ITEMS.addAll(ModContent.SLIME_BLOCKS.values());

        // Full Blocks 2
        ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // GUI Blocks
            ModContent.SPRUCE_CRAFTING_TABLE,
            ModContent.BIRCH_CRAFTING_TABLE,
            ModContent.JUNGLE_CRAFTING_TABLE,
            ModContent.ACACIA_CRAFTING_TABLE,
            ModContent.DARK_OAK_CRAFTING_TABLE,
            ModContent.MANGROVE_CRAFTING_TABLE,
            ModContent.CHERRY_CRAFTING_TABLE,
            ModContent.PALE_OAK_CRAFTING_TABLE,
            ModContent.BAMBOO_CRAFTING_TABLE,
            ModContent.CRIMSON_CRAFTING_TABLE,
            ModContent.WARPED_CRAFTING_TABLE,
            ModContent.PALM_CRAFTING_TABLE,
            ModContent.LAVENDER_CRAFTING_TABLE,
            ModContent.BLACKSTONE_FURNACE,
            ModContent.DEEPSLATE_FURNACE
        ));

        // Partial Blocks
		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // Stairs
            ModContent.ANDESITE_BRICK_STAIRS,
            ModContent.DIORITE_BRICK_STAIRS,
            ModContent.GRANITE_BRICK_STAIRS,
            ModContent.MOSSY_ANDESITE_BRICK_STAIRS,
            ModContent.MOSSY_DIORITE_BRICK_STAIRS,
            ModContent.MOSSY_GRANITE_BRICK_STAIRS,
            ModContent.PALM_STAIRS,
            ModContent.LAVENDER_STAIRS,
            ModContent.COAL_STAIRS,
            ModContent.IRON_STAIRS,
            ModContent.GOLD_STAIRS,
            ModContent.LAPIS_STAIRS,
            ModContent.EMERALD_STAIRS,
            ModContent.DIAMOND_STAIRS,
            ModContent.NETHERITE_STAIRS,
            ModContent.OBSIDIAN_STAIRS,
            ModContent.AQUAMARINE_STAIRS,
            ModContent.AMBER_STAIRS,
            ModContent.BURNING_DIAMOND_STAIRS,
            ModContent.ABYSSALITE_STAIRS,
            ModContent.COLORED_SANDSTONE_BLOCKS.get("white_sandstone_stairs"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("black_sandstone_stairs"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("pink_sandstone_stairs"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_white_sandstone_stairs"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_black_sandstone_stairs"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_pink_sandstone_stairs"),

            // Slabs
            ModContent.ANDESITE_BRICK_SLAB,
            ModContent.DIORITE_BRICK_SLAB,
            ModContent.GRANITE_BRICK_SLAB,
            ModContent.MOSSY_ANDESITE_BRICK_SLAB,
            ModContent.MOSSY_DIORITE_BRICK_SLAB,
            ModContent.MOSSY_GRANITE_BRICK_SLAB,
            ModContent.PALM_SLAB,
            ModContent.LAVENDER_SLAB,
            ModContent.COAL_SLAB,
            ModContent.IRON_SLAB,
            ModContent.GOLD_SLAB,
            ModContent.LAPIS_SLAB,
            ModContent.EMERALD_SLAB,
            ModContent.DIAMOND_SLAB,
            ModContent.NETHERITE_SLAB,
            ModContent.OBSIDIAN_SLAB,
            ModContent.AQUAMARINE_SLAB,
            ModContent.AMBER_SLAB,
            ModContent.BURNING_DIAMOND_SLAB,
            ModContent.ABYSSALITE_SLAB,
            ModContent.COLORED_SANDSTONE_BLOCKS.get("white_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("black_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("pink_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_white_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_black_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("smooth_pink_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_white_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_black_sandstone_slab"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("cut_pink_sandstone_slab"),

            // Walls
            ModContent.POLISHED_ANDESITE_WALL,
            ModContent.POLISHED_DIORITE_WALL,
            ModContent.POLISHED_GRANITE_WALL,
            ModContent.MOSSY_ANDESITE_BRICK_WALL,
            ModContent.MOSSY_DIORITE_BRICK_WALL,
            ModContent.MOSSY_GRANITE_BRICK_WALL,
            ModContent.GRANITE_BRICK_WALL,
            ModContent.DIORITE_BRICK_WALL,
            ModContent.ANDESITE_BRICK_WALL,
            ModContent.COLORED_SANDSTONE_BLOCKS.get("white_sandstone_wall"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("black_sandstone_wall"),
            ModContent.COLORED_SANDSTONE_BLOCKS.get("pink_sandstone_wall"),

                
            // Fences
            ModContent.PALM_FENCE,
            ModContent.LAVENDER_FENCE,

            // Fence Gates
            ModContent.PALM_FENCE_GATE,
            ModContent.LAVENDER_FENCE_GATE,

            // Doors
            ModContent.PALM_DOOR,
            ModContent.LAVENDER_DOOR,

            // Trapdoors
            ModContent.PALM_TRAPDOOR,
            ModContent.LAVENDER_TRAPDOOR,

            // Pressure Plates
            ModContent.PALM_PRESSURE_PLATE,
            ModContent.LAVENDER_PRESSURE_PLATE,

            // Buttons
            ModContent.PALM_BUTTON,
            ModContent.LAVENDER_BUTTON,

            // Signs
            ModContent.PALM_SIGN,
            ModContent.LAVENDER_SIGN,

            // Hanging Signs
            ModContent.PALM_HANGING_SIGN,
            ModContent.LAVENDER_HANGING_SIGN,

            // Boats
            ModItems.PALM_BOAT,
            ModItems.PALM_CHEST_BOAT,
			ModItems.LAVENDER_BOAT, 
            ModItems.LAVENDER_CHEST_BOAT,
            ModItems.CRIMSON_BOAT,
            ModItems.CRIMSON_CHEST_BOAT,
            ModItems.WARPED_BOAT,
            ModItems.WARPED_CHEST_BOAT,

            // Misc Features
            ModContent.ICICLE,

            // Plants
            ModContent.PALM_SAPLING,
            ModContent.LAVENDER_FUNGUS,
            ModContent.BEACH_GRASS, 
            ModContent.TALL_BEACH_GRASS,
			ModContent.BUTTERCUP, 
            ModContent.CLOVER,
            ModContent.LAVENDER_ROOTS,

            // Food/Plant Blocks
            ModBlockItems.COCONUT_SEEDS,
            ModBlockItems.COTTON, 
            ModFoods.TOMATO, 
            ModFoods.CORN, 
            ModFoods.GRAPES
        ));

        // True Foods
		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModFoods.COCONUT_ITEM,
			ModFoods.CALAMARI, 
            ModFoods.COOKED_CALAMARI,
            ModItems.MILK_BOTTLE
		));

		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // Mineral Items
			ModItems.BURNING_DIAMOND,
			ModItems.AQUAMARINE,
			ModItems.AMBER,
			ModItems.ANTHRACITE,
			ModItems.EXPERIENCE_DUST, 
            ModItems.XYLIUM_DUST, 
            ModItems.GLENDSTONE_DUST,
			ModItems.EXPERIENCE_INGOT, 
			ModItems.ABYSSALITE_SCRAP, 
            ModItems.ABYSSALITE_INGOT,

            // Smithing Templates
            ModItems.EXPERIENCE_UPGRADE_SMITHING_TEMPLATE,
			ModItems.ABYSSALITE_UPGRADE_SMITHING_TEMPLATE
		));

		// Tools & Weapons (Herramientas y Armas)
        ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // Burning Diamond
            ModItems.BURNING_DIAMOND_SWORD,
            ModItems.BURNING_DIAMOND_PICKAXE,
            ModItems.BURNING_DIAMOND_AXE,
            ModItems.BURNING_DIAMOND_SHOVEL,
            ModItems.BURNING_DIAMOND_HOE,
        
            // Burning Netherite
            ModItems.BURNING_NETHERITE_SWORD,
            ModItems.BURNING_NETHERITE_PICKAXE,
            ModItems.BURNING_NETHERITE_AXE,
            ModItems.BURNING_NETHERITE_SHOVEL,
            ModItems.BURNING_NETHERITE_HOE,
        
            // Aquamarine
            ModItems.AQUAMARINE_SWORD,
            ModItems.AQUAMARINE_PICKAXE,
            ModItems.AQUAMARINE_AXE,
            ModItems.AQUAMARINE_SHOVEL,
            ModItems.AQUAMARINE_HOE,
        
            // Amber
            ModItems.AMBER_SWORD,
            ModItems.AMBER_PICKAXE,
            ModItems.AMBER_AXE,
            ModItems.AMBER_SHOVEL,
            ModItems.AMBER_HOE,
        
            // Emerald
            ModItems.EMERALD_SWORD,
            ModItems.EMERALD_PICKAXE,
            ModItems.EMERALD_AXE,
            ModItems.EMERALD_SHOVEL,
            ModItems.EMERALD_HOE,
        
            // Experience
            ModItems.EXPERIENCE_SWORD,
            ModItems.EXPERIENCE_PICKAXE,
            ModItems.EXPERIENCE_AXE,
            ModItems.EXPERIENCE_SHOVEL,
            ModItems.EXPERIENCE_HOE,
        
            // Abyssalite
            ModItems.ABYSSALITE_SWORD,
            ModItems.ABYSSALITE_TRIDENT,
            ModItems.ABYSSALITE_PICKAXE,
            ModItems.ABYSSALITE_AXE,
            ModItems.ABYSSALITE_SHOVEL,
            ModItems.ABYSSALITE_HOE
        ));

        // Armor (Armadura)
        ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
            // Burning Diamond
            ModItems.BURNING_DIAMOND_HELMET,
            ModItems.BURNING_DIAMOND_CHESTPLATE,
            ModItems.BURNING_DIAMOND_LEGGINGS,
            ModItems.BURNING_DIAMOND_BOOTS,
        
            // Burning Netherite
            ModItems.BURNING_NETHERITE_HELMET,
            ModItems.BURNING_NETHERITE_CHESTPLATE,
            ModItems.BURNING_NETHERITE_LEGGINGS,
            ModItems.BURNING_NETHERITE_BOOTS,
        
            // Aquamarine
            ModItems.AQUAMARINE_HELMET,
            ModItems.AQUAMARINE_CHESTPLATE,
            ModItems.AQUAMARINE_LEGGINGS,
            ModItems.AQUAMARINE_BOOTS,
        
            // Cotton
            ModItems.COTTON_HELMET,
            ModItems.COTTON_CHESTPLATE,
            ModItems.COTTON_LEGGINGS,
            ModItems.COTTON_BOOTS,
        
            // Amber
            ModItems.AMBER_HELMET,
            ModItems.AMBER_CHESTPLATE,
            ModItems.AMBER_LEGGINGS,
            ModItems.AMBER_BOOTS,
        
            // Emerald
            ModItems.EMERALD_HELMET,
            ModItems.EMERALD_CHESTPLATE,
            ModItems.EMERALD_LEGGINGS,
            ModItems.EMERALD_BOOTS,
        
            // Experience
            ModItems.EXPERIENCE_HELMET,
            ModItems.EXPERIENCE_CHESTPLATE,
            ModItems.EXPERIENCE_LEGGINGS,
            ModItems.EXPERIENCE_BOOTS,
        
            // Abyssalite
            ModItems.ABYSSALITE_HELMET,
            ModItems.ABYSSALITE_CHESTPLATE,
            ModItems.ABYSSALITE_LEGGINGS,
            ModItems.ABYSSALITE_BOOTS
        ));

        // Spawn Eggs
		ModContent.CREATIVE_TAB_ITEMS.addAll(List.of(
			ModItems.GRIZZLY_BEAR_SPAWN_EGG, 
            ModItems.ENDER_SPIDER_SPAWN_EGG,
			ModItems.FIRE_CREEPER_SPAWN_EGG, 
            ModItems.SNOW_CREEPER_SPAWN_EGG
		));
	}
}
