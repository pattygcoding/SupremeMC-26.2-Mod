package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.PalmFoliagePlacer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public final class ModFoliagePlacerTypes {
	public static final FoliagePlacerType<PalmFoliagePlacer> PALM = Registry.register(
		BuiltInRegistries.FOLIAGE_PLACER_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_foliage_placer"),
		new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));

	private ModFoliagePlacerTypes() {
	}

	public static void bootstrap() {
	}
}