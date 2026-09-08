package com.suprememc.content.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public final class ModToolMaterials {
	public static final ToolMaterial ABYSSALITE = new ToolMaterial(
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2031, 9.0F, 4.0F, 15, ModRepairItems.ABYSSALITE);

	public static final ToolMaterial EMERALD = new ToolMaterial(
		BlockTags.INCORRECT_FOR_IRON_TOOL, 380, 6.5F, 2.5F, 18, ModRepairItems.EMERALD);

	public static final ToolMaterial EXPERIENCE = new ToolMaterial(
		BlockTags.INCORRECT_FOR_IRON_TOOL, 380, 6.5F, 2.5F, 16, ModRepairItems.EXPERIENCE);

	private ModToolMaterials() {
	}
}
