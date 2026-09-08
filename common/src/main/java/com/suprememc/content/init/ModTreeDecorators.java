package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.PalmCoconutDecorator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class ModTreeDecorators {
	public static final TreeDecoratorType<PalmCoconutDecorator> PALM_COCONUT = Registry.register(
		BuiltInRegistries.TREE_DECORATOR_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_coconut"),
		new TreeDecoratorType<>(PalmCoconutDecorator.CODEC));

	private ModTreeDecorators() {
	}

	public static void bootstrap() {
	}
}