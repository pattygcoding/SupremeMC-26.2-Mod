package com.suprememc.content.init;

import com.suprememc.content.ModContent;
import net.minecraft.world.level.block.ComposterBlock;

public final class ModCompostables {
	private static boolean registered;

	private ModCompostables() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		ComposterBlock.COMPOSTABLES.put(ModContent.PALM_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModContent.PALM_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModFoods.COCONUT_ITEM, 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModBlockItems.COCONUT_SEEDS, 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModBlockItems.COTTON, 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModFoods.TOMATO, 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModContent.BEACH_GRASS.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModContent.TALL_BEACH_GRASS.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModFoods.GRAPES, 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModFoods.CORN, 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModContent.BUTTERCUP.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModContent.CLOVER.asItem(), 0.3F);
	}
}
