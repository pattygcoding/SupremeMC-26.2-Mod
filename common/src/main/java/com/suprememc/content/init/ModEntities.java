package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import com.suprememc.content.entity.EnderSpider;
import com.suprememc.content.entity.FireCreeper;
import com.suprememc.content.entity.FireTnt;
import com.suprememc.content.entity.GrizzlyBear;
import com.suprememc.content.entity.SnowCreeper;
import com.suprememc.content.entity.SnowTnt;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntities {
	private static boolean registered;

	public static EntityType<Boat> PALM_BOAT_ENTITY;
	public static EntityType<ChestBoat> PALM_CHEST_BOAT_ENTITY;
	public static EntityType<Boat> LAVENDER_BOAT_ENTITY;
	public static EntityType<ChestBoat> LAVENDER_CHEST_BOAT_ENTITY;
	public static EntityType<Boat> CRIMSON_BOAT_ENTITY;
	public static EntityType<ChestBoat> CRIMSON_CHEST_BOAT_ENTITY;
	public static EntityType<Boat> WARPED_BOAT_ENTITY;
	public static EntityType<ChestBoat> WARPED_CHEST_BOAT_ENTITY;
	public static EntityType<AbyssaliteTridentEntity> ABYSSALITE_TRIDENT_ENTITY;
	public static EntityType<GrizzlyBear> GRIZZLY_BEAR_ENTITY;
	public static EntityType<EnderSpider> ENDER_SPIDER_ENTITY;
	public static EntityType<FireCreeper> FIRE_CREEPER_ENTITY;
	public static EntityType<SnowCreeper> SNOW_CREEPER_ENTITY;
	public static EntityType<SnowTnt> SNOW_TNT_ENTITY;
	public static EntityType<FireTnt> FIRE_TNT_ENTITY;

	private ModEntities() {
	}

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		PALM_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("palm_boat"),
			EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> ModItems.PALM_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("palm_boat")));
		PALM_CHEST_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("palm_chest_boat"),
			EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> ModItems.PALM_CHEST_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("palm_chest_boat")));
		LAVENDER_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("lavender_boat"),
			EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> ModItems.LAVENDER_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("lavender_boat")));
		LAVENDER_CHEST_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("lavender_chest_boat"),
			EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> ModItems.LAVENDER_CHEST_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("lavender_chest_boat")));
		CRIMSON_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("crimson_boat"),
			EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> ModItems.CRIMSON_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("crimson_boat")));
		CRIMSON_CHEST_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("crimson_chest_boat"),
			EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> ModItems.CRIMSON_CHEST_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("crimson_chest_boat")));
		WARPED_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("warped_boat"),
			EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> ModItems.WARPED_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("warped_boat")));
		WARPED_CHEST_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("warped_chest_boat"),
			EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> ModItems.WARPED_CHEST_BOAT), MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build(key("warped_chest_boat")));
		GRIZZLY_BEAR_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("grizzly_bear"),
			EntityType.Builder.<GrizzlyBear>of(GrizzlyBear::new, MobCategory.CREATURE)
				.sized(1.4F, 1.4F).clientTrackingRange(10).build(key("grizzly_bear")));
		ENDER_SPIDER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("ender_spider"),
			EntityType.Builder.<EnderSpider>of(EnderSpider::new, MobCategory.MONSTER)
				.sized(1.4F, 0.9F).eyeHeight(0.65F).clientTrackingRange(8).build(key("ender_spider")));
		FIRE_CREEPER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("fire_creeper"),
			EntityType.Builder.<FireCreeper>of(FireCreeper::new, MobCategory.MONSTER)
				.sized(0.6F, 1.7F).clientTrackingRange(8).fireImmune().build(key("fire_creeper")));
		SNOW_CREEPER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("snow_creeper"),
			EntityType.Builder.<SnowCreeper>of(SnowCreeper::new, MobCategory.MONSTER)
				.sized(0.6F, 1.7F).clientTrackingRange(8).build(key("snow_creeper")));
		SNOW_TNT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("snow_tnt"),
			EntityType.Builder.<SnowTnt>of(SnowTnt::new, MobCategory.MISC)
				.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build(key("snow_tnt")));
		FIRE_TNT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("fire_tnt"),
			EntityType.Builder.<FireTnt>of(FireTnt::new, MobCategory.MISC)
				.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build(key("fire_tnt")));
		ABYSSALITE_TRIDENT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("abyssalite_trident"),
			EntityType.Builder.<AbyssaliteTridentEntity>of(AbyssaliteTridentEntity::new, MobCategory.MISC)
				.sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(key("abyssalite_trident")));

		SpawnPlacements.register(ENDER_SPIDER_ENTITY, SpawnPlacementTypes.ON_GROUND,
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(FIRE_CREEPER_ENTITY, SpawnPlacementTypes.ON_GROUND,
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(SNOW_CREEPER_ENTITY, SpawnPlacementTypes.ON_GROUND,
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

	private static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
	}

	private static ResourceKey<EntityType<?>> key(String name) {
		return ResourceKey.create(Registries.ENTITY_TYPE, id(name));
	}
}
