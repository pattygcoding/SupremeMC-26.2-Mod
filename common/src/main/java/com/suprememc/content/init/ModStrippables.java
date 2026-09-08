package com.suprememc.content.init;

import com.suprememc.content.ModContent;
import com.suprememc.mixin.AxeItemAccessor;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public final class ModStrippables {
	private static boolean registered;

	private ModStrippables() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		Map<Block, Block> strippables = new HashMap<>(AxeItemAccessor.getStrippables());
		strippables.put(ModContent.PALM_LOG, ModContent.STRIPPED_PALM_LOG);
		strippables.put(ModContent.PALM_WOOD, ModContent.STRIPPED_PALM_WOOD);
		strippables.put(ModContent.LAVENDER_STEM, ModContent.STRIPPED_LAVENDER_STEM);
		strippables.put(ModContent.LAVENDER_WOOD, ModContent.STRIPPED_LAVENDER_WOOD);
		AxeItemAccessor.setStrippables(strippables);
	}
}
