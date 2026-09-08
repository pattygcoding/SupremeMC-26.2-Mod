package com.suprememc.content.init;

import com.suprememc.Constants;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public final class ModArmorMaterials {
	public static final ArmorMaterial COTTON = new ArmorMaterial(
		5,
		Map.of(
			ArmorType.BOOTS, 1,
			ArmorType.LEGGINGS, 2,
			ArmorType.CHESTPLATE, 3,
			ArmorType.HELMET, 1,
			ArmorType.BODY, 3),
		15,
		SoundEvents.ARMOR_EQUIP_LEATHER,
		0.0F,
		0.0F,
		ModRepairItems.COTTON,
		asset("cotton"));

	public static final ArmorMaterial AQUAMARINE = new ArmorMaterial(
		33,
		Map.of(
			ArmorType.BOOTS, 3,
			ArmorType.LEGGINGS, 6,
			ArmorType.CHESTPLATE, 8,
			ArmorType.HELMET, 3,
			ArmorType.BODY, 11),
		10,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		2.0F,
		0.0F,
		ItemTags.REPAIRS_DIAMOND_ARMOR,
		asset("aquamarine"));

	public static final ArmorMaterial BURNING_DIAMOND = new ArmorMaterial(
		33,
		Map.of(
			ArmorType.BOOTS, 3,
			ArmorType.LEGGINGS, 6,
			ArmorType.CHESTPLATE, 8,
			ArmorType.HELMET, 3,
			ArmorType.BODY, 11),
		10,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		2.0F,
		0.0F,
		ItemTags.REPAIRS_DIAMOND_ARMOR,
		asset("burning_diamond"));

	public static final ArmorMaterial BURNING_NETHERITE = new ArmorMaterial(
		37,
		Map.of(
			ArmorType.BOOTS, 3,
			ArmorType.LEGGINGS, 6,
			ArmorType.CHESTPLATE, 8,
			ArmorType.HELMET, 3,
			ArmorType.BODY, 11),
		15,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		3.0F,
		0.1F,
		ItemTags.REPAIRS_NETHERITE_ARMOR,
		asset("burning_netherite"));

	public static final ArmorMaterial AMBER = new ArmorMaterial(
		33,
		Map.of(
			ArmorType.BOOTS, 3,
			ArmorType.LEGGINGS, 6,
			ArmorType.CHESTPLATE, 8,
			ArmorType.HELMET, 3,
			ArmorType.BODY, 11),
		10,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		2.0F,
		0.0F,
		ItemTags.REPAIRS_DIAMOND_ARMOR,
		asset("amber"));

	public static final ArmorMaterial ABYSSALITE = new ArmorMaterial(
		37,
		Map.of(
			ArmorType.BOOTS, 3,
			ArmorType.LEGGINGS, 6,
			ArmorType.CHESTPLATE, 8,
			ArmorType.HELMET, 3,
			ArmorType.BODY, 11),
		15,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		2.0F,
		0.1F,
		ModRepairItems.ABYSSALITE,
		asset("abyssalite"));

	public static final ArmorMaterial EMERALD = new ArmorMaterial(
		18,
		Map.of(
			ArmorType.BOOTS, 2,
			ArmorType.LEGGINGS, 5,
			ArmorType.CHESTPLATE, 7,
			ArmorType.HELMET, 2,
			ArmorType.BODY, 6),
		16,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		0.0F,
		0.0F,
		ModRepairItems.EMERALD,
		asset("emerald"));

	public static final ArmorMaterial EXPERIENCE = new ArmorMaterial(
		18,
		Map.of(
			ArmorType.BOOTS, 2,
			ArmorType.LEGGINGS, 5,
			ArmorType.CHESTPLATE, 7,
			ArmorType.HELMET, 2,
			ArmorType.BODY, 6),
		16,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		0.0F,
		0.0F,
		ModRepairItems.EXPERIENCE,
		asset("experience"));

	private ModArmorMaterials() {
	}

	private static ResourceKey<EquipmentAsset> asset(String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
	}
}
