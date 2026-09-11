package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.worldgen.EndMineshaftStructure;
import com.suprememc.content.worldgen.NetherMineshaftStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.StructureType;

public final class ModStructureTypes {
	public static final StructureType<NetherMineshaftStructure> NETHER_MINESHAFT = Registry.register(
		BuiltInRegistries.STRUCTURE_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "nether_mineshaft"),
		() -> NetherMineshaftStructure.CODEC);

	public static final StructureType<EndMineshaftStructure> END_MINESHAFT = Registry.register(
		BuiltInRegistries.STRUCTURE_TYPE,
		Identifier.fromNamespaceAndPath(Constants.MOD_ID, "end_mineshaft"),
		() -> EndMineshaftStructure.CODEC);

	private ModStructureTypes() {
	}

	public static void bootstrap() {
	}
}
