package com.suprememc.content.init;

import com.suprememc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModRepairItems {
	public static final TagKey<Item> COTTON = create("cotton_repair_items");
	public static final TagKey<Item> ABYSSALITE = create("abyssalite_repair_items");
	public static final TagKey<Item> EMERALD = create("emerald_repair_items");
	public static final TagKey<Item> EXPERIENCE = create("experience_repair_items");

	private ModRepairItems() {
	}

	private static TagKey<Item> create(String name) {
		return TagKey.create(Registries.ITEM,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
	}
}
