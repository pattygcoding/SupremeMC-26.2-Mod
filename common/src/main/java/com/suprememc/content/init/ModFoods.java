package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModFoods {
	private static boolean registered;
	public static Item COCONUT_ITEM;
	public static Item CALAMARI;
	public static Item COOKED_CALAMARI;
	public static Item GRAPES;
	public static Item TOMATO;
	public static Item CORN;

	private ModFoods() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		COCONUT_ITEM = register("coconut", new Item(ModContent.itemProperties("coconut").stacksTo(64)
			.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build(),
				net.minecraft.world.item.component.Consumables.defaultDrink()
					.onConsume(new net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect())
					.build())));
		CALAMARI = register("calamari", new Item(ModContent.itemProperties("calamari").stacksTo(64)
			.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
		COOKED_CALAMARI = register("cooked_calamari", new Item(ModContent.itemProperties("cooked_calamari").stacksTo(64)
			.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build())));
		GRAPES = registerBlockItem("grapes", ModContent.GRAPE_VINE,
			ModContent.itemProperties("grapes").stacksTo(64)
				.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
		TOMATO = registerBlockItem("tomato", ModContent.TOMATO_BUSH,
			ModContent.itemProperties("tomato").stacksTo(64)
				.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
		CORN = registerBlockItem("corn", ModContent.CORN_STALK,
			ModContent.itemProperties("corn").stacksTo(64)
				.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
	}

	private static <T extends Item> T register(String id, T item) {
		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), item);
		return item;
	}

	private static Item registerBlockItem(String id, Block block, Item.Properties properties) {
		return register(id, new BlockItem(block, properties.useBlockDescriptionPrefix()));
	}
}
