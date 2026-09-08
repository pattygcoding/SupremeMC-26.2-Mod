package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.PalmTrunkPlacer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public final class ModTrunkPlacerTypes {
	public static final TrunkPlacerType<PalmTrunkPlacer> PALM = Registry.register(
		BuiltInRegistries.TRUNK_PLACER_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_trunk_placer"),
		new TrunkPlacerType<>(PalmTrunkPlacer.CODEC));

	private ModTrunkPlacerTypes() {
	}

	public static void bootstrap() {
	}
}