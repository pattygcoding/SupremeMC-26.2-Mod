package com.suprememc.content.init;

import com.suprememc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class ModTreeGrowers {
	private static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE = ResourceKey.create(
		Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm"));

	public static final TreeGrower PALM = new TreeGrower(
		"palm", Optional.empty(), Optional.of(PALM_TREE), Optional.empty());

	private ModTreeGrowers() {
	}

	public static void bootstrap() {
	}
}