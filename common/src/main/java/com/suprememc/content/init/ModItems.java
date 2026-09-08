package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import com.suprememc.content.items.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public final class ModItems {
	private static boolean initialRegistered;
	private static boolean armorRegistered;
	private static boolean remainingRegistered;

	public static Item AQUAMARINE;
	public static Item AMBER;
	public static Item ANTHRACITE;
	public static Item EXPERIENCE_DUST;
	public static Item EXPERIENCE_INGOT;
	public static Item EXPERIENCE_UPGRADE_SMITHING_TEMPLATE;
	public static Item XYLIUM_DUST;
	public static Item COTTON_HELMET;
	public static Item COTTON_CHESTPLATE;
	public static Item COTTON_LEGGINGS;
	public static Item COTTON_BOOTS;
	public static Item PALM_BOAT;
	public static Item PALM_CHEST_BOAT;
	public static Item LAVENDER_BOAT;
	public static Item LAVENDER_CHEST_BOAT;
	public static Item GRIZZLY_BEAR_SPAWN_EGG;
	public static Item ENDER_SPIDER_SPAWN_EGG;
	public static Item FIRE_CREEPER_SPAWN_EGG;
	public static Item SNOW_CREEPER_SPAWN_EGG;
	public static Item ABYSSALITE_SCRAP;
	public static Item ABYSSALITE_INGOT;
	public static Item ABYSSALITE_UPGRADE_SMITHING_TEMPLATE;
	public static Item BURNING_DIAMOND;
	public static Item BURNING_DIAMOND_PICKAXE;
	public static Item BURNING_DIAMOND_AXE;
	public static Item BURNING_DIAMOND_SHOVEL;
	public static Item BURNING_DIAMOND_HOE;
	public static Item BURNING_DIAMOND_SWORD;
	public static Item BURNING_DIAMOND_HELMET;
	public static Item BURNING_DIAMOND_CHESTPLATE;
	public static Item BURNING_DIAMOND_LEGGINGS;
	public static Item BURNING_DIAMOND_BOOTS;
	public static Item BURNING_NETHERITE_PICKAXE;
	public static Item BURNING_NETHERITE_AXE;
	public static Item BURNING_NETHERITE_SHOVEL;
	public static Item BURNING_NETHERITE_HOE;
	public static Item BURNING_NETHERITE_SWORD;
	public static Item BURNING_NETHERITE_HELMET;
	public static Item BURNING_NETHERITE_CHESTPLATE;
	public static Item BURNING_NETHERITE_LEGGINGS;
	public static Item BURNING_NETHERITE_BOOTS;
	public static Item AQUAMARINE_PICKAXE;
	public static Item AQUAMARINE_AXE;
	public static Item AQUAMARINE_SHOVEL;
	public static Item AQUAMARINE_HOE;
	public static Item AQUAMARINE_SWORD;
	public static Item AQUAMARINE_HELMET;
	public static Item AQUAMARINE_CHESTPLATE;
	public static Item AQUAMARINE_LEGGINGS;
	public static Item AQUAMARINE_BOOTS;
	public static Item AMBER_PICKAXE;
	public static Item AMBER_AXE;
	public static Item AMBER_SHOVEL;
	public static Item AMBER_HOE;
	public static Item AMBER_SWORD;
	public static Item AMBER_HELMET;
	public static Item AMBER_CHESTPLATE;
	public static Item AMBER_LEGGINGS;
	public static Item AMBER_BOOTS;
	public static Item ABYSSALITE_PICKAXE;
	public static Item ABYSSALITE_AXE;
	public static Item ABYSSALITE_SHOVEL;
	public static Item ABYSSALITE_HOE;
	public static Item ABYSSALITE_SWORD;
	public static Item ABYSSALITE_HELMET;
	public static Item ABYSSALITE_CHESTPLATE;
	public static Item ABYSSALITE_LEGGINGS;
	public static Item ABYSSALITE_BOOTS;
	public static Item ABYSSALITE_TRIDENT;
	public static Item EMERALD_PICKAXE;
	public static Item EMERALD_AXE;
	public static Item EMERALD_SHOVEL;
	public static Item EMERALD_HOE;
	public static Item EMERALD_SWORD;
	public static Item EMERALD_HELMET;
	public static Item EMERALD_CHESTPLATE;
	public static Item EMERALD_LEGGINGS;
	public static Item EMERALD_BOOTS;
	public static Item EXPERIENCE_PICKAXE;
	public static Item EXPERIENCE_AXE;
	public static Item EXPERIENCE_SHOVEL;
	public static Item EXPERIENCE_HOE;
	public static Item EXPERIENCE_SWORD;
	public static Item EXPERIENCE_HELMET;
	public static Item EXPERIENCE_CHESTPLATE;
	public static Item EXPERIENCE_LEGGINGS;
	public static Item EXPERIENCE_BOOTS;

	private ModItems() {
	}

	public static void bootstrapInitial() {
		if (initialRegistered) return;
		initialRegistered = true;
		AQUAMARINE = register("aquamarine", new Item(ModContent.itemProperties("aquamarine").stacksTo(64)));
		AMBER = register("amber", new Item(ModContent.itemProperties("amber").stacksTo(64)));
		ANTHRACITE = register("anthracite", new Item(ModContent.itemProperties("anthracite").stacksTo(64)));
		EXPERIENCE_DUST = register("experience_dust", new GlintItem(ModContent.itemProperties("experience_dust").stacksTo(64)));
		EXPERIENCE_INGOT = register("experience_ingot", new GlintItem(ModContent.itemProperties("experience_ingot").stacksTo(64)));
		EXPERIENCE_UPGRADE_SMITHING_TEMPLATE = register("experience_upgrade_smithing_template",
			ModContent.smithingTemplateItem("experience_upgrade", "experience", "emerald", "experience_ingot"));
		XYLIUM_DUST = register("xylium_dust", new Item(ModContent.itemProperties("xylium_dust").stacksTo(64)));
	}

	public static void bootstrapArmor() {
		if (armorRegistered) return;
		armorRegistered = true;
		COTTON_HELMET = register("cotton_helmet", new Item(ModContent.itemProperties("cotton_helmet").humanoidArmor(ModArmorMaterials.COTTON, ArmorType.HELMET)));
		COTTON_CHESTPLATE = register("cotton_chestplate", new Item(ModContent.itemProperties("cotton_chestplate").humanoidArmor(ModArmorMaterials.COTTON, ArmorType.CHESTPLATE)));
		COTTON_LEGGINGS = register("cotton_leggings", new Item(ModContent.itemProperties("cotton_leggings").humanoidArmor(ModArmorMaterials.COTTON, ArmorType.LEGGINGS)));
		COTTON_BOOTS = register("cotton_boots", new Item(ModContent.itemProperties("cotton_boots").humanoidArmor(ModArmorMaterials.COTTON, ArmorType.BOOTS)));
	}

	public static void bootstrapRemaining() {
		if (remainingRegistered) return;
		remainingRegistered = true;
		PALM_BOAT = register("palm_boat", new net.minecraft.world.item.BoatItem(ModEntities.PALM_BOAT_ENTITY, ModContent.itemProperties("palm_boat")));
		PALM_CHEST_BOAT = register("palm_chest_boat", new net.minecraft.world.item.BoatItem(ModEntities.PALM_CHEST_BOAT_ENTITY, ModContent.itemProperties("palm_chest_boat")));
		LAVENDER_BOAT = register("lavender_boat", new net.minecraft.world.item.BoatItem(ModEntities.LAVENDER_BOAT_ENTITY, ModContent.itemProperties("lavender_boat")));
		LAVENDER_CHEST_BOAT = register("lavender_chest_boat", new net.minecraft.world.item.BoatItem(ModEntities.LAVENDER_CHEST_BOAT_ENTITY, ModContent.itemProperties("lavender_chest_boat")));
		GRIZZLY_BEAR_SPAWN_EGG = register("grizzly_bear_spawn_egg", new SpawnEggItem(ModContent.itemProperties("grizzly_bear_spawn_egg").spawnEgg(ModEntities.GRIZZLY_BEAR_ENTITY)));
		ENDER_SPIDER_SPAWN_EGG = register("ender_spider_spawn_egg", new SpawnEggItem(ModContent.itemProperties("ender_spider_spawn_egg").spawnEgg(ModEntities.ENDER_SPIDER_ENTITY)));
		FIRE_CREEPER_SPAWN_EGG = register("fire_creeper_spawn_egg", new SpawnEggItem(ModContent.itemProperties("fire_creeper_spawn_egg").spawnEgg(ModEntities.FIRE_CREEPER_ENTITY)));
		SNOW_CREEPER_SPAWN_EGG = register("snow_creeper_spawn_egg", new SpawnEggItem(ModContent.itemProperties("snow_creeper_spawn_egg").spawnEgg(ModEntities.SNOW_CREEPER_ENTITY)));
		ABYSSALITE_SCRAP = register("abyssalite_scrap", new Item(ModContent.itemProperties("abyssalite_scrap").stacksTo(64)));
		ABYSSALITE_INGOT = register("abyssalite_ingot", new Item(ModContent.itemProperties("abyssalite_ingot").stacksTo(64)));
		ABYSSALITE_UPGRADE_SMITHING_TEMPLATE = register("abyssalite_upgrade_smithing_template",
			ModContent.smithingTemplateItem("abyssalite_upgrade", "abyssalite", "aquamarine", "abyssalite_ingot"));
		BURNING_DIAMOND = register("burning_diamond", new Item(ModContent.itemProperties("burning_diamond")));
		BURNING_DIAMOND_PICKAXE = register("burning_diamond_pickaxe", new Item(ModContent.itemProperties("burning_diamond_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
		BURNING_DIAMOND_AXE = register("burning_diamond_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, ModContent.itemProperties("burning_diamond_axe")));
		BURNING_DIAMOND_SHOVEL = register("burning_diamond_shovel", new ShovelItem(ToolMaterial.DIAMOND, 1.5F, -3.0F, ModContent.itemProperties("burning_diamond_shovel")));
		BURNING_DIAMOND_HOE = register("burning_diamond_hoe", new HoeItem(ToolMaterial.DIAMOND, 0.0F, -3.0F, ModContent.itemProperties("burning_diamond_hoe")));
		BURNING_DIAMOND_SWORD = register("burning_diamond_sword", new Item(ModContent.itemProperties("burning_diamond_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));
		BURNING_DIAMOND_HELMET = register("burning_diamond_helmet", new BurningDiamondArmorItem("burning_diamond_helmet", ArmorType.HELMET));
		BURNING_DIAMOND_CHESTPLATE = register("burning_diamond_chestplate", new BurningDiamondArmorItem("burning_diamond_chestplate", ArmorType.CHESTPLATE));
		BURNING_DIAMOND_LEGGINGS = register("burning_diamond_leggings", new BurningDiamondArmorItem("burning_diamond_leggings", ArmorType.LEGGINGS));
		BURNING_DIAMOND_BOOTS = register("burning_diamond_boots", new BurningDiamondArmorItem("burning_diamond_boots", ArmorType.BOOTS));
		BURNING_NETHERITE_PICKAXE = register("burning_netherite_pickaxe", new Item(ModContent.itemProperties("burning_netherite_pickaxe").pickaxe(ToolMaterial.NETHERITE, 1, -2.8F)));
		BURNING_NETHERITE_AXE = register("burning_netherite_axe", new AxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, ModContent.itemProperties("burning_netherite_axe")));
		BURNING_NETHERITE_SHOVEL = register("burning_netherite_shovel", new ShovelItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, ModContent.itemProperties("burning_netherite_shovel")));
		BURNING_NETHERITE_HOE = register("burning_netherite_hoe", new HoeItem(ToolMaterial.NETHERITE, 0.0F, -3.0F, ModContent.itemProperties("burning_netherite_hoe")));
		BURNING_NETHERITE_SWORD = register("burning_netherite_sword", new Item(ModContent.itemProperties("burning_netherite_sword").sword(ToolMaterial.NETHERITE, 3, -2.4F)));
		BURNING_NETHERITE_HELMET = register("burning_netherite_helmet", new BurningNetheriteArmorItem("burning_netherite_helmet", ArmorType.HELMET));
		BURNING_NETHERITE_CHESTPLATE = register("burning_netherite_chestplate", new BurningNetheriteArmorItem("burning_netherite_chestplate", ArmorType.CHESTPLATE));
		BURNING_NETHERITE_LEGGINGS = register("burning_netherite_leggings", new BurningNetheriteArmorItem("burning_netherite_leggings", ArmorType.LEGGINGS));
		BURNING_NETHERITE_BOOTS = register("burning_netherite_boots", new BurningNetheriteArmorItem("burning_netherite_boots", ArmorType.BOOTS));
		AQUAMARINE_PICKAXE = register("aquamarine_pickaxe", new Item(ModContent.itemProperties("aquamarine_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
		AQUAMARINE_AXE = register("aquamarine_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, ModContent.itemProperties("aquamarine_axe")));
		AQUAMARINE_SHOVEL = register("aquamarine_shovel", new AquamarineShovelItem("aquamarine_shovel"));
		AQUAMARINE_HOE = register("aquamarine_hoe", new AquamarineHoeItem("aquamarine_hoe"));
		AQUAMARINE_SWORD = register("aquamarine_sword", new Item(ModContent.itemProperties("aquamarine_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));
		AQUAMARINE_HELMET = register("aquamarine_helmet", new AquamarineArmorItem("aquamarine_helmet", ArmorType.HELMET));
		AQUAMARINE_CHESTPLATE = register("aquamarine_chestplate", new AquamarineArmorItem("aquamarine_chestplate", ArmorType.CHESTPLATE));
		AQUAMARINE_LEGGINGS = register("aquamarine_leggings", new AquamarineArmorItem("aquamarine_leggings", ArmorType.LEGGINGS));
		AQUAMARINE_BOOTS = register("aquamarine_boots", new AquamarineArmorItem("aquamarine_boots", ArmorType.BOOTS));
		AMBER_PICKAXE = register("amber_pickaxe", new Item(ModContent.itemProperties("amber_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
		AMBER_AXE = register("amber_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, ModContent.itemProperties("amber_axe")));
		AMBER_SHOVEL = register("amber_shovel", new AmberShovelItem("amber_shovel"));
		AMBER_HOE = register("amber_hoe", new AmberHoeItem("amber_hoe"));
		AMBER_SWORD = register("amber_sword", new Item(ModContent.itemProperties("amber_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));
		AMBER_HELMET = register("amber_helmet", new AmberArmorItem("amber_helmet", ArmorType.HELMET));
		AMBER_CHESTPLATE = register("amber_chestplate", new AmberArmorItem("amber_chestplate", ArmorType.CHESTPLATE));
		AMBER_LEGGINGS = register("amber_leggings", new AmberArmorItem("amber_leggings", ArmorType.LEGGINGS));
		AMBER_BOOTS = register("amber_boots", new AmberArmorItem("amber_boots", ArmorType.BOOTS));
		ABYSSALITE_PICKAXE = register("abyssalite_pickaxe", new Item(ModContent.itemProperties("abyssalite_pickaxe").pickaxe(ModToolMaterials.ABYSSALITE, 1, -2.8F)));
		ABYSSALITE_AXE = register("abyssalite_axe", new AxeItem(ModToolMaterials.ABYSSALITE, 5.0F, -3.0F, ModContent.itemProperties("abyssalite_axe")));
		ABYSSALITE_SHOVEL = register("abyssalite_shovel", new AbyssaliteShovelItem());
		ABYSSALITE_HOE = register("abyssalite_hoe", new AbyssaliteHoeItem());
		ABYSSALITE_SWORD = register("abyssalite_sword", new Item(ModContent.itemProperties("abyssalite_sword").sword(ModToolMaterials.ABYSSALITE, 3, -2.4F)));
		ABYSSALITE_HELMET = register("abyssalite_helmet", new AbyssaliteArmorItem("abyssalite_helmet", ArmorType.HELMET));
		ABYSSALITE_CHESTPLATE = register("abyssalite_chestplate", new AbyssaliteArmorItem("abyssalite_chestplate", ArmorType.CHESTPLATE));
		ABYSSALITE_LEGGINGS = register("abyssalite_leggings", new AbyssaliteArmorItem("abyssalite_leggings", ArmorType.LEGGINGS));
		ABYSSALITE_BOOTS = register("abyssalite_boots", new AbyssaliteArmorItem("abyssalite_boots", ArmorType.BOOTS));
		ABYSSALITE_TRIDENT = register("abyssalite_trident", new AbyssaliteTridentItem(ModContent.itemProperties("abyssalite_trident").durability(750).enchantable(1).repairable(ModRepairItems.ABYSSALITE)));
		EMERALD_PICKAXE = register("emerald_pickaxe", new Item(ModContent.itemProperties("emerald_pickaxe").pickaxe(ModToolMaterials.EMERALD, 1.0F, -2.8F)));
		EMERALD_AXE = register("emerald_axe", new AxeItem(ModToolMaterials.EMERALD, 6.0F, -3.1F, ModContent.itemProperties("emerald_axe")));
		EMERALD_SHOVEL = register("emerald_shovel", new ShovelItem(ModToolMaterials.EMERALD, 1.5F, -3.0F, ModContent.itemProperties("emerald_shovel")));
		EMERALD_HOE = register("emerald_hoe", new HoeItem(ModToolMaterials.EMERALD, -2.0F, -1.0F, ModContent.itemProperties("emerald_hoe")));
		EMERALD_SWORD = register("emerald_sword", new Item(ModContent.itemProperties("emerald_sword").sword(ModToolMaterials.EMERALD, 3, -2.4F)));
		EMERALD_HELMET = register("emerald_helmet", new EmeraldArmorItem("emerald_helmet", ArmorType.HELMET));
		EMERALD_CHESTPLATE = register("emerald_chestplate", new EmeraldArmorItem("emerald_chestplate", ArmorType.CHESTPLATE));
		EMERALD_LEGGINGS = register("emerald_leggings", new EmeraldArmorItem("emerald_leggings", ArmorType.LEGGINGS));
		EMERALD_BOOTS = register("emerald_boots", new EmeraldArmorItem("emerald_boots", ArmorType.BOOTS));
		EXPERIENCE_PICKAXE = register("experience_pickaxe", new Item(ModContent.itemProperties("experience_pickaxe").pickaxe(ModToolMaterials.EXPERIENCE, 1.0F, -2.8F)));
		EXPERIENCE_AXE = register("experience_axe", new AxeItem(ModToolMaterials.EXPERIENCE, 6.0F, -3.1F, ModContent.itemProperties("experience_axe")));
		EXPERIENCE_SHOVEL = register("experience_shovel", new ShovelItem(ModToolMaterials.EXPERIENCE, 1.5F, -3.0F, ModContent.itemProperties("experience_shovel")));
		EXPERIENCE_HOE = register("experience_hoe", new HoeItem(ModToolMaterials.EXPERIENCE, -2.0F, -1.0F, ModContent.itemProperties("experience_hoe")));
		EXPERIENCE_SWORD = register("experience_sword", new Item(ModContent.itemProperties("experience_sword").sword(ModToolMaterials.EXPERIENCE, 3, -2.4F)));
		EXPERIENCE_HELMET = register("experience_helmet", new ExperienceArmorItem("experience_helmet", ArmorType.HELMET));
		EXPERIENCE_CHESTPLATE = register("experience_chestplate", new ExperienceArmorItem("experience_chestplate", ArmorType.CHESTPLATE));
		EXPERIENCE_LEGGINGS = register("experience_leggings", new ExperienceArmorItem("experience_leggings", ArmorType.LEGGINGS));
		EXPERIENCE_BOOTS = register("experience_boots", new ExperienceArmorItem("experience_boots", ArmorType.BOOTS));
	}

	private static <T extends Item> T register(String id, T item) {
		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), item);
		return item;
	}
}
