package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import com.suprememc.content.blockentity.NetherReactorCoreBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public final class ModBlockEntities {
	private static boolean registered;

	public static BlockEntityType<NetherReactorCoreBlockEntity> NETHER_REACTOR_CORE;

	private ModBlockEntities() {
	}

	public static void bootstrap() {
		if (registered) return;
		registered = true;

		NETHER_REACTOR_CORE = register("nether_reactor_core", new BlockEntityType<>(
			NetherReactorCoreBlockEntity::new, Set.of(ModContent.NETHER_REACTOR_CORE)));
	}

	private static <T extends BlockEntityType<?>> T register(String id, T type) {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), type);
		return type;
	}
}
