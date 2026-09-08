package com.suprememc.content.init;

import com.suprememc.content.ModContent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public final class ModFlammables {
	private static boolean registered;

	private ModFlammables() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		FireBlock fireBlock = (FireBlock) Blocks.FIRE;
		fireBlock.setFlammable(ModContent.PALM_LOG, 5, 5);
		fireBlock.setFlammable(ModContent.STRIPPED_PALM_LOG, 5, 5);
		fireBlock.setFlammable(ModContent.PALM_WOOD, 5, 5);
		fireBlock.setFlammable(ModContent.STRIPPED_PALM_WOOD, 5, 5);
		fireBlock.setFlammable(ModContent.PALM_PLANKS, 5, 20);
		fireBlock.setFlammable(ModContent.PALM_SLAB, 5, 20);
		fireBlock.setFlammable(ModContent.PALM_STAIRS, 5, 20);
		fireBlock.setFlammable(ModContent.PALM_FENCE, 5, 20);
		fireBlock.setFlammable(ModContent.PALM_FENCE_GATE, 5, 20);
		fireBlock.setFlammable(ModContent.PALM_LEAVES, 30, 60);
		fireBlock.setFlammable(ModContent.LAVENDER_WOOD, 5, 5);
		fireBlock.setFlammable(ModContent.LAVENDER_STEM, 5, 5);
		fireBlock.setFlammable(ModContent.STRIPPED_LAVENDER_STEM, 5, 5);
		fireBlock.setFlammable(ModContent.STRIPPED_LAVENDER_WOOD, 5, 5);
		fireBlock.setFlammable(ModContent.LAVENDER_PLANKS, 5, 20);
		fireBlock.setFlammable(ModContent.LAVENDER_SLAB, 5, 20);
		fireBlock.setFlammable(ModContent.LAVENDER_STAIRS, 5, 20);
		fireBlock.setFlammable(ModContent.LAVENDER_FENCE, 5, 20);
		fireBlock.setFlammable(ModContent.LAVENDER_FENCE_GATE, 5, 20);
	}
}
