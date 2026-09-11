package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.CornPatchFeature;
import com.suprememc.content.worldgen.GlendstoneClusterFeature;
import com.suprememc.content.worldgen.GrapeVineHangFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class ModFeatures {
	public static final Feature<NoneFeatureConfiguration> CORN_PATCH = Registry.register(
		BuiltInRegistries.FEATURE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "corn_patch"),
		new CornPatchFeature());

	public static final Feature<NoneFeatureConfiguration> GRAPE_VINE_HANG = Registry.register(
		BuiltInRegistries.FEATURE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grape_vine_hang"),
		new GrapeVineHangFeature(NoneFeatureConfiguration.CODEC));

	public static final Feature<NoneFeatureConfiguration> GLENDSTONE_CLUSTER = Registry.register(
		BuiltInRegistries.FEATURE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "glendstone_cluster"),
		new GlendstoneClusterFeature(NoneFeatureConfiguration.CODEC));

	private ModFeatures() {
	}

	public static void bootstrap() {
	}
}