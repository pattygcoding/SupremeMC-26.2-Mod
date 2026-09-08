package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.BiomeTemperaturePlacementModifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public final class ModPlacementModifiers {
	public static final PlacementModifierType<BiomeTemperaturePlacementModifier> BIOME_TEMPERATURE = Registry.register(
		BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "biome_temperature"),
		() -> BiomeTemperaturePlacementModifier.CODEC);

	private ModPlacementModifiers() {
	}

	public static void bootstrap() {
	}
}