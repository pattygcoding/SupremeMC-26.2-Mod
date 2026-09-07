package com.suprememc.content;

import com.mojang.serialization.MapCodec;
import com.suprememc.Constants;
import com.suprememc.content.blocks.*;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import com.suprememc.content.entity.EnderSpider;
import com.suprememc.content.entity.FireCreeper;
import com.suprememc.content.entity.GrizzlyBear;
import com.suprememc.content.entity.SnowCreeper;
import com.suprememc.content.items.*;
import com.suprememc.content.worldgen.BiomeTemperaturePlacementModifier;
import com.suprememc.content.worldgen.CornPatchFeature;
import com.suprememc.content.worldgen.GrapeVineHangFeature;
import com.suprememc.content.worldgen.PalmCoconutDecorator;
import com.suprememc.content.worldgen.PalmFoliagePlacer;
import com.suprememc.content.worldgen.PalmTrunkPlacer;
import com.suprememc.mixin.AxeItemAccessor;
import com.suprememc.tabs.CreativeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.core.Position;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.HangingSignBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ModContent {

    private static boolean registered = false;

    // Loader-agnostic list, shared by fabric/neoforge to populate the SupremeMC creative tab.
    public static final List<ItemLike> CREATIVE_TAB_ITEMS = new java.util.ArrayList<>();

    public static final TagKey<Item> COTTON_REPAIR_ITEMS = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cotton_repair_items"));
    public static final ArmorMaterial COTTON_ARMOR_MATERIAL = new ArmorMaterial(
        5,
        Map.of(
            ArmorType.BOOTS, 1,
            ArmorType.LEGGINGS, 2,
            ArmorType.CHESTPLATE, 3,
            ArmorType.HELMET, 1,
            ArmorType.BODY, 3),
        15,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0F,
        0.0F,
        COTTON_REPAIR_ITEMS,
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cotton")));

        public static final ArmorMaterial AQUAMARINE_ARMOR_MATERIAL = new ArmorMaterial(
            33,
            Map.of(
                ArmorType.BOOTS, 3,
                ArmorType.LEGGINGS, 6,
                ArmorType.CHESTPLATE, 8,
                ArmorType.HELMET, 3,
                ArmorType.BODY, 11),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            ItemTags.REPAIRS_DIAMOND_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "aquamarine")));
        public static final ArmorMaterial BURNING_DIAMOND_ARMOR_MATERIAL = new ArmorMaterial(
            33,
            Map.of(
                ArmorType.BOOTS, 3,
                ArmorType.LEGGINGS, 6,
                ArmorType.CHESTPLATE, 8,
                ArmorType.HELMET, 3,
                ArmorType.BODY, 11),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            ItemTags.REPAIRS_DIAMOND_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "burning_diamond")));
        public static final ArmorMaterial BURNING_NETHERITE_ARMOR_MATERIAL = new ArmorMaterial(
            37,
            Map.of(
                ArmorType.BOOTS, 3,
                ArmorType.LEGGINGS, 6,
                ArmorType.CHESTPLATE, 8,
                ArmorType.HELMET, 3,
                ArmorType.BODY, 11),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F,
            0.1F,
            ItemTags.REPAIRS_NETHERITE_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "burning_netherite")));
        public static final ArmorMaterial AMBER_ARMOR_MATERIAL = new ArmorMaterial(
            33,
            Map.of(ArmorType.BOOTS, 3, ArmorType.LEGGINGS, 6, ArmorType.CHESTPLATE, 8,
                ArmorType.HELMET, 3, ArmorType.BODY, 11),
            10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, ItemTags.REPAIRS_DIAMOND_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "amber")));

            public static final TagKey<Item> ABYSSALITE_REPAIR_ITEMS = TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_repair_items"));
            public static final ToolMaterial ABYSSALITE_TOOL_MATERIAL = new ToolMaterial(
                BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2031, 9.0F, 4.0F, 15, ABYSSALITE_REPAIR_ITEMS);
            public static final ArmorMaterial ABYSSALITE_ARMOR_MATERIAL = new ArmorMaterial(
                37,
                Map.of(ArmorType.BOOTS, 3, ArmorType.LEGGINGS, 6, ArmorType.CHESTPLATE, 8,
                    ArmorType.HELMET, 3, ArmorType.BODY, 11),
                15, SoundEvents.ARMOR_EQUIP_NETHERITE, 2.0F, 0.1F, ABYSSALITE_REPAIR_ITEMS,
                ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite")));

    public static final TagKey<Item> EMERALD_REPAIR_ITEMS = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "emerald_repair_items"));
    public static final ToolMaterial EMERALD_TOOL_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL, 
        380,        // Durability (Iron: 250, Diamond: 1561)
        6.5F,       // Mining speed (Iron: 6.0F, Diamond: 8.0F)
        2.5F,       // Base attack damage bonus (Iron: 2.0F, Diamond: 3.0F, Netherite: 4.0F)
        18,         // Enchantability (Iron: 14)
        EMERALD_REPAIR_ITEMS
    );

    public static final ArmorMaterial EMERALD_ARMOR_MATERIAL = new ArmorMaterial(
        18,         // Durability multiplier (Iron: 15, Diamond: 33)
        Map.of(
            ArmorType.BOOTS, 2,
            ArmorType.LEGGINGS, 5,
            ArmorType.CHESTPLATE, 7,  // Bumped to 7 (Iron: 6, Diamond: 8)
            ArmorType.HELMET, 2,
            ArmorType.BODY, 6         // For wolf/horse body armor if used
        ),
        16,         // Enchantability (Iron: 9, Diamond: 10, Gold: 25)
        SoundEvents.ARMOR_EQUIP_DIAMOND, 
        0.0F,       // Armor Toughness (Iron: 0.0F, Diamond: 2.0F)
        0.0F,       // Knockback Resistance (Netherite: 0.1F)
        EMERALD_REPAIR_ITEMS,
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "emerald"))
    );
    
    public static Item AQUAMARINE;
    public static Item BURNING_DIAMOND;
    public static Item BURNING_NETHERITE_PICKAXE;
    public static Item BURNING_NETHERITE_AXE;
    public static Item BURNING_NETHERITE_SHOVEL;
    public static Item BURNING_NETHERITE_HOE;
    public static Item BURNING_NETHERITE_SWORD;
    public static Item BURNING_NETHERITE_HELMET;
    public static Item BURNING_NETHERITE_CHESTPLATE;
    public static Item BURNING_NETHERITE_LEGGINGS;
    public static Item BURNING_NETHERITE_BOOTS;
    public static Item AMBER;
    public static Item ANTHRACITE;
    public static Item EXPERIENCE_DUST;
    public static Item XYLIUM_DUST;
    public static Block AQUAMARINE_ORE;
    public static Block BURNING_DIAMOND_ORE;
    public static Block NETHER_ANTHRACITE_ORE;
    public static Block ANTHRACITE_BLOCK;
    public static Block DEEPSLATE_AQUAMARINE_ORE;
    public static Block AQUAMARINE_BLOCK;
    public static Block BURNING_DIAMOND_BLOCK;
    public static Block BURNING_DIAMOND_STAIRS;
    public static Block BURNING_DIAMOND_SLAB;
    public static Block AQUAMARINE_STAIRS;
    public static Block AQUAMARINE_SLAB;
    public static Block AMBER_ORE;
    public static Block DEEPSLATE_AMBER_ORE;
    public static Block PRISMARINE_ORE;
    public static Block DEEPSLATE_PRISMARINE_ORE;
    public static Block AMBER_BLOCK;
    public static Block AMBER_STAIRS;
    public static Block AMBER_SLAB;
    public static Block IRON_STAIRS;
    public static Block IRON_SLAB;
    public static Block LAPIS_STAIRS;
    public static Block LAPIS_SLAB;
    public static Block GOLD_STAIRS;
    public static Block GOLD_SLAB;
    public static Block DIAMOND_STAIRS;
    public static Block DIAMOND_SLAB;
    public static Block EMERALD_STAIRS;
    public static Block EMERALD_SLAB;
    public static Block COAL_STAIRS;
    public static Block COAL_SLAB;
    public static Block OBSIDIAN_STAIRS;
    public static Block OBSIDIAN_SLAB;
    public static Block NETHERITE_STAIRS;
    public static Block NETHERITE_SLAB;
    public static Block POLISHED_GRANITE_WALL;
    public static Block POLISHED_DIORITE_WALL;
    public static Block POLISHED_ANDESITE_WALL;
    public static Block ANDESITE_BRICKS;
    public static Block ANDESITE_BRICK_STAIRS;
    public static Block ANDESITE_BRICK_SLAB;
    public static Block ANDESITE_BRICK_WALL;
    public static Block DIORITE_BRICKS;
    public static Block DIORITE_BRICK_STAIRS;
    public static Block DIORITE_BRICK_SLAB;
    public static Block DIORITE_BRICK_WALL;
    public static Block GRANITE_BRICKS;
    public static Block GRANITE_BRICK_STAIRS;
    public static Block GRANITE_BRICK_SLAB;
    public static Block GRANITE_BRICK_WALL;
    public static Block WET_FARMLAND;
    public static Block SUPREME_MC_LOGO_BLOCK;
    public static Block SPRUCE_BOOKSHELF;
    public static Block BIRCH_BOOKSHELF;
    public static Block JUNGLE_BOOKSHELF;
    public static Block ACACIA_BOOKSHELF;
    public static Block DARK_OAK_BOOKSHELF;
    public static Block MANGROVE_BOOKSHELF;
    public static Block CHERRY_BOOKSHELF;
    public static Block PALE_OAK_BOOKSHELF;
    public static Block BAMBOO_BOOKSHELF;
    public static Block CRIMSON_BOOKSHELF;
    public static Block WARPED_BOOKSHELF;
    public static Block PALM_BOOKSHELF;
    public static Block SPRUCE_CRAFTING_TABLE;
    public static Block BIRCH_CRAFTING_TABLE;
    public static Block JUNGLE_CRAFTING_TABLE;
    public static Block ACACIA_CRAFTING_TABLE;
    public static Block DARK_OAK_CRAFTING_TABLE;
    public static Block MANGROVE_CRAFTING_TABLE;
    public static Block CHERRY_CRAFTING_TABLE;
    public static Block PALE_OAK_CRAFTING_TABLE;
    public static Block BAMBOO_CRAFTING_TABLE;
    public static Block CRIMSON_CRAFTING_TABLE;
    public static Block WARPED_CRAFTING_TABLE;
    public static Block PALM_CRAFTING_TABLE;
    public static Block BLACKSTONE_FURNACE;
    public static Block DEEPSLATE_FURNACE;
    public static final BlockSetType PALM_BLOCK_SET = new BlockSetType("palm");
    public static final WoodType PALM_WOOD_TYPE = new WoodType("palm", PALM_BLOCK_SET);
        private static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm"));
        // Constructor args are (name, megaTree, tree, flowers) - the palm is a normal 1x1 sapling tree.
        public static final TreeGrower PALM_TREE_GROWER = new TreeGrower("palm", Optional.empty(), Optional.of(PALM_TREE), Optional.empty());
        public static final FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE_PLACER_TYPE = Registry.register(
            BuiltInRegistries.FOLIAGE_PLACER_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_foliage_placer"),
            new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));
        public static final TrunkPlacerType<PalmTrunkPlacer> PALM_TRUNK_PLACER_TYPE = Registry.register(
            BuiltInRegistries.TRUNK_PLACER_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_trunk_placer"),
            new TrunkPlacerType<>(PalmTrunkPlacer.CODEC));
        public static final PlacementModifierType<BiomeTemperaturePlacementModifier> BIOME_TEMPERATURE_PLACEMENT_MODIFIER = Registry.register(
            BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "biome_temperature"),
            () -> BiomeTemperaturePlacementModifier.CODEC);
        public static final TreeDecoratorType<PalmCoconutDecorator> PALM_COCONUT_DECORATOR_TYPE = Registry.register(
            BuiltInRegistries.TREE_DECORATOR_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_coconut"),
            new TreeDecoratorType<>(PalmCoconutDecorator.CODEC));
        public static final Feature<NoneFeatureConfiguration> CORN_PATCH_FEATURE = Registry.register(
            BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "corn_patch"),
            new CornPatchFeature());
        public static final Feature<NoneFeatureConfiguration> GRAPE_VINE_HANG_FEATURE = Registry.register(
            BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grape_vine_hang"),
            new GrapeVineHangFeature(NoneFeatureConfiguration.CODEC));
    public static Block PALM_LOG;
    public static Block STRIPPED_PALM_LOG;
    public static Block PALM_WOOD;
    public static Block STRIPPED_PALM_WOOD;
    public static Block PALM_PLANKS;
    public static Block PALM_SLAB;
    public static Block PALM_STAIRS;
    public static Block PALM_FENCE;
    public static Block PALM_FENCE_GATE;
    public static Block PALM_DOOR;
    public static Block PALM_TRAPDOOR;
    public static Block PALM_PRESSURE_PLATE;
    public static Block PALM_BUTTON;
    public static Block PALM_SIGN;
    public static Block PALM_WALL_SIGN;
    public static Block PALM_HANGING_SIGN;
    public static Block PALM_WALL_HANGING_SIGN;
    public static Block PALM_LEAVES;
    public static Block PALM_SAPLING;
    public static Block POTTED_PALM_SAPLING;
    public static Block COCONUT;
    public static Item COCONUT_ITEM;
    public static Item COCONUT_SEEDS;
    public static Block COTTON_BUSH;
    public static Item COTTON;
    public static Item COTTON_HELMET;
    public static Item COTTON_CHESTPLATE;
    public static Item COTTON_LEGGINGS;
    public static Item COTTON_BOOTS;
    public static Block TOMATO_BUSH;
    public static Block BEACH_GRASS;
    public static Block TALL_BEACH_GRASS;
    public static Block BUTTERCUP;
    public static Block CLOVER;
    public static Holder<Potion> LUCK_POTION;
    public static Holder<Potion> LONG_LUCK_POTION;
    public static Holder<Potion> STRONG_LUCK_POTION;
    public static Holder<Potion> BAD_LUCK_POTION;
    public static Holder<Potion> LONG_BAD_LUCK_POTION;
    public static Holder<Potion> STRONG_BAD_LUCK_POTION;
    public static Holder<Potion> HUNGER_POTION;
    public static Holder<Potion> LONG_HUNGER_POTION;
    public static Holder<Potion> STRONG_HUNGER_POTION;
    public static Holder<Potion> DECAY_POTION;
    public static Holder<Potion> LONG_DECAY_POTION;
    public static Holder<Potion> STRONG_DECAY_POTION;
    public static EntityType<Boat> PALM_BOAT_ENTITY;
    public static EntityType<ChestBoat> PALM_CHEST_BOAT_ENTITY;
    public static Item PALM_BOAT;
    public static Item PALM_CHEST_BOAT;
    public static Item ABYSSALITE_SCRAP;
    public static Item ABYSSALITE_INGOT;
    public static Item ABYSSALITE_UPGRADE_SMITHING_TEMPLATE;
    public static Block ATLANTIS_DEBRIS;
    public static Block ABYSSALITE_BLOCK;
    public static Block ABYSSALITE_STAIRS;
    public static Block ABYSSALITE_SLAB;
    public static Block ICICLE;

    public static Item AQUAMARINE_PICKAXE;
    public static Item AQUAMARINE_AXE;
    public static Item AQUAMARINE_SHOVEL;
    public static Item AQUAMARINE_HOE;
    public static Item AQUAMARINE_SWORD;

    public static Item AQUAMARINE_HELMET;
    public static Item AQUAMARINE_CHESTPLATE;
    public static Item AQUAMARINE_LEGGINGS;
    public static Item AQUAMARINE_BOOTS;
    public static Item BURNING_DIAMOND_PICKAXE;
    public static Item BURNING_DIAMOND_AXE;
    public static Item BURNING_DIAMOND_SHOVEL;
    public static Item BURNING_DIAMOND_HOE;
    public static Item BURNING_DIAMOND_SWORD;
    public static Item BURNING_DIAMOND_HELMET;
    public static Item BURNING_DIAMOND_CHESTPLATE;
    public static Item BURNING_DIAMOND_LEGGINGS;
    public static Item BURNING_DIAMOND_BOOTS;
    public static Item AMBER_PICKAXE;
    public static Item AMBER_AXE;
    public static Item AMBER_SHOVEL;
    public static Item AMBER_HOE;
    public static Item AMBER_SWORD;
    public static Item AMBER_HELMET;
    public static Item AMBER_CHESTPLATE;
    public static Item AMBER_LEGGINGS;
    public static Item AMBER_BOOTS;
    public static Item ABYSSALITE_PICKAXE;
    public static Item ABYSSALITE_AXE;
    public static Item ABYSSALITE_SHOVEL;
    public static Item ABYSSALITE_HOE;
    public static Item ABYSSALITE_SWORD;
    public static Item ABYSSALITE_HELMET;
    public static Item ABYSSALITE_CHESTPLATE;
    public static Item ABYSSALITE_LEGGINGS;
    public static Item ABYSSALITE_BOOTS;
    public static Item ABYSSALITE_TRIDENT;
    public static EntityType<AbyssaliteTridentEntity> ABYSSALITE_TRIDENT_ENTITY;
    public static EntityType<GrizzlyBear> GRIZZLY_BEAR_ENTITY;
    public static Item GRIZZLY_BEAR_SPAWN_EGG;
    public static EntityType<EnderSpider> ENDER_SPIDER_ENTITY;
    public static Item ENDER_SPIDER_SPAWN_EGG;
    public static EntityType<FireCreeper> FIRE_CREEPER_ENTITY;
    public static Item FIRE_CREEPER_SPAWN_EGG;
    public static EntityType<SnowCreeper> SNOW_CREEPER_ENTITY;
    public static Item SNOW_CREEPER_SPAWN_EGG;
    public static Item EMERALD_PICKAXE;
    public static Item EMERALD_AXE;
    public static Item EMERALD_SHOVEL;
    public static Item EMERALD_HOE;
    public static Item EMERALD_SWORD;
    public static Item EMERALD_HELMET;
    public static Item EMERALD_CHESTPLATE;
    public static Item EMERALD_LEGGINGS;
    public static Item EMERALD_BOOTS;
    public static Item CALAMARI;
    public static Item COOKED_CALAMARI;
    public static Item GRAPES;
    public static Item TOMATO;
    public static Item CORN;
    public static Block GRAPE_VINE;
    public static Block GRAPE_VINE_PLANT;
    public static Block CORN_STALK;
    public static Block CORN_STALK_PLANT;
    // Dyed variants: one glowblock (glowstone parity) and one slime block (see MixinPistonStructureResolver
    // for the same-color-only sticky behavior) per DyeColor.
    public static final Map<DyeColor, Block> GLOW_BLOCKS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, Block> SLIME_BLOCKS = new EnumMap<>(DyeColor.class);

    public static void bootstrap() {
        if (registered) {
            return;
        }
        registered = true;

        AQUAMARINE = registerItem("aquamarine", new Item(itemProperties("aquamarine").stacksTo(64)));
        AMBER = registerItem("amber", new Item(itemProperties("amber").stacksTo(64)));
        ANTHRACITE = registerItem("anthracite", new Item(itemProperties("anthracite").stacksTo(64)));
        EXPERIENCE_DUST = registerItem("experience_dust", new Item(itemProperties("experience_dust").stacksTo(64)));
        XYLIUM_DUST = registerItem("xylium_dust", new Item(itemProperties("xylium_dust").stacksTo(64)));

        AQUAMARINE_ORE = registerBlock("aquamarine_ore",
            new DropExperienceBlock(UniformInt.of(3, 7), blockProperties("aquamarine_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        BURNING_DIAMOND_ORE = registerBlock("burning_diamond_ore",
            new DropExperienceBlock(UniformInt.of(3, 7), blockProperties("burning_diamond_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        NETHER_ANTHRACITE_ORE = registerBlock("nether_anthracite_ore",
            new DropExperienceBlock(UniformInt.of(0, 2), blockProperties("nether_anthracite_ore").mapColor(MapColor.NETHER).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        ANTHRACITE_BLOCK = registerBlock("anthracite_block",
            new Block(blockProperties("anthracite_block").mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(5.0F, 6.0F)));
        DEEPSLATE_AQUAMARINE_ORE = registerBlock("deepslate_aquamarine_ore",
            new DropExperienceBlock(UniformInt.of(3, 7), blockProperties("deepslate_aquamarine_ore").mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()));
        AQUAMARINE_BLOCK = registerBlock("aquamarine_block",
                new Block(blockProperties("aquamarine_block").mapColor(MapColor.COLOR_CYAN).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
        BURNING_DIAMOND_BLOCK = registerBlock("burning_diamond_block",
            new Block(blockProperties("burning_diamond_block").mapColor(MapColor.DIAMOND).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
        BURNING_DIAMOND_STAIRS = registerBlock("burning_diamond_stairs", new MaterialStairsBlock(BURNING_DIAMOND_BLOCK.defaultBlockState(),
            materialProperties("burning_diamond_stairs", MapColor.DIAMOND, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        BURNING_DIAMOND_SLAB = registerBlock("burning_diamond_slab", new SlabBlock(
            materialProperties("burning_diamond_slab", MapColor.DIAMOND, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        AQUAMARINE_STAIRS = registerBlock("aquamarine_stairs", new MaterialStairsBlock(AQUAMARINE_BLOCK.defaultBlockState(),
            materialProperties("aquamarine_stairs", MapColor.COLOR_CYAN, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        AQUAMARINE_SLAB = registerBlock("aquamarine_slab", new SlabBlock(
            materialProperties("aquamarine_slab", MapColor.COLOR_CYAN, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        AMBER_ORE = registerBlock("amber_ore", new DropExperienceBlock(UniformInt.of(3, 7),
            blockProperties("amber_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        DEEPSLATE_AMBER_ORE = registerBlock("deepslate_amber_ore", new DropExperienceBlock(UniformInt.of(3, 7),
            blockProperties("deepslate_amber_ore").mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()));
        PRISMARINE_ORE = registerBlock("prismarine_ore", new DropExperienceBlock(UniformInt.of(1, 5),
            blockProperties("prismarine_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        DEEPSLATE_PRISMARINE_ORE = registerBlock("deepslate_prismarine_ore", new DropExperienceBlock(UniformInt.of(1, 5),
            blockProperties("deepslate_prismarine_ore").mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()));
        AMBER_BLOCK = registerBlock("amber_block", new Block(
            blockProperties("amber_block").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
        AMBER_STAIRS = registerBlock("amber_stairs", new MaterialStairsBlock(AMBER_BLOCK.defaultBlockState(),
            materialProperties("amber_stairs", MapColor.COLOR_ORANGE, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        AMBER_SLAB = registerBlock("amber_slab", new SlabBlock(
            materialProperties("amber_slab", MapColor.COLOR_ORANGE, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        IRON_STAIRS = registerBlock("iron_stairs", new MaterialStairsBlock(Blocks.IRON_BLOCK.defaultBlockState(),
            blockProperties("iron_stairs").mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
        IRON_SLAB = registerBlock("iron_slab", new SlabBlock(
            blockProperties("iron_slab").mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
        LAPIS_STAIRS = registerBlock("lapis_stairs", new MaterialStairsBlock(Blocks.LAPIS_BLOCK.defaultBlockState(), materialProperties("lapis_stairs", MapColor.LAPIS, SoundType.METAL, 3.0F, 3.0F)));
        LAPIS_SLAB = registerBlock("lapis_slab", new SlabBlock(materialProperties("lapis_slab", MapColor.LAPIS, SoundType.METAL, 3.0F, 3.0F)));
        GOLD_STAIRS = registerBlock("gold_stairs", new MaterialStairsBlock(Blocks.GOLD_BLOCK.defaultBlockState(), materialProperties("gold_stairs", MapColor.GOLD, SoundType.METAL, 3.0F, 6.0F)));
        GOLD_SLAB = registerBlock("gold_slab", new SlabBlock(materialProperties("gold_slab", MapColor.GOLD, SoundType.METAL, 3.0F, 6.0F)));
        DIAMOND_STAIRS = registerBlock("diamond_stairs", new MaterialStairsBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(), materialProperties("diamond_stairs", MapColor.DIAMOND, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        DIAMOND_SLAB = registerBlock("diamond_slab", new SlabBlock(materialProperties("diamond_slab", MapColor.DIAMOND, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        EMERALD_STAIRS = registerBlock("emerald_stairs", new MaterialStairsBlock(Blocks.EMERALD_BLOCK.defaultBlockState(), materialProperties("emerald_stairs", MapColor.EMERALD, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        EMERALD_SLAB = registerBlock("emerald_slab", new SlabBlock(materialProperties("emerald_slab", MapColor.EMERALD, SoundType.METAL, 5.0F, 6.0F).requiresCorrectToolForDrops()));
        COAL_STAIRS = registerBlock("coal_stairs", new MaterialStairsBlock(Blocks.COAL_BLOCK.defaultBlockState(), materialProperties("coal_stairs", MapColor.COLOR_BLACK, SoundType.STONE, 5.0F, 6.0F)));
        COAL_SLAB = registerBlock("coal_slab", new SlabBlock(materialProperties("coal_slab", MapColor.COLOR_BLACK, SoundType.STONE, 5.0F, 6.0F)));
        OBSIDIAN_STAIRS = registerBlock("obsidian_stairs", new MaterialStairsBlock(Blocks.OBSIDIAN.defaultBlockState(), materialProperties("obsidian_stairs", MapColor.COLOR_BLACK, SoundType.STONE, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        OBSIDIAN_SLAB = registerBlock("obsidian_slab", new SlabBlock(materialProperties("obsidian_slab", MapColor.COLOR_BLACK, SoundType.STONE, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        NETHERITE_STAIRS = registerBlock("netherite_stairs", new MaterialStairsBlock(Blocks.NETHERITE_BLOCK.defaultBlockState(), materialProperties("netherite_stairs", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        NETHERITE_SLAB = registerBlock("netherite_slab", new SlabBlock(materialProperties("netherite_slab", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        POLISHED_GRANITE_WALL = registerBlock("polished_granite_wall", new WallBlock(materialProperties("polished_granite_wall", MapColor.COLOR_ORANGE, SoundType.STONE, 2.0F, 6.0F).requiresCorrectToolForDrops()));
        POLISHED_DIORITE_WALL = registerBlock("polished_diorite_wall", new WallBlock(materialProperties("polished_diorite_wall", MapColor.QUARTZ, SoundType.STONE, 2.0F, 6.0F).requiresCorrectToolForDrops()));
        POLISHED_ANDESITE_WALL = registerBlock("polished_andesite_wall", new WallBlock(materialProperties("polished_andesite_wall", MapColor.STONE, SoundType.STONE, 2.0F, 6.0F).requiresCorrectToolForDrops()));
        ANDESITE_BRICKS = registerBlock("andesite_bricks", new Block(materialProperties("andesite_bricks", MapColor.STONE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        ANDESITE_BRICK_STAIRS = registerBlock("andesite_brick_stairs", new MaterialStairsBlock(ANDESITE_BRICKS.defaultBlockState(), materialProperties("andesite_brick_stairs", MapColor.STONE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        ANDESITE_BRICK_SLAB = registerBlock("andesite_brick_slab", new SlabBlock(materialProperties("andesite_brick_slab", MapColor.STONE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        ANDESITE_BRICK_WALL = registerBlock("andesite_brick_wall", new WallBlock(materialProperties("andesite_brick_wall", MapColor.STONE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        DIORITE_BRICKS = registerBlock("diorite_bricks", new Block(materialProperties("diorite_bricks", MapColor.QUARTZ, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        DIORITE_BRICK_STAIRS = registerBlock("diorite_brick_stairs", new MaterialStairsBlock(DIORITE_BRICKS.defaultBlockState(), materialProperties("diorite_brick_stairs", MapColor.QUARTZ, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        DIORITE_BRICK_SLAB = registerBlock("diorite_brick_slab", new SlabBlock(materialProperties("diorite_brick_slab", MapColor.QUARTZ, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        DIORITE_BRICK_WALL = registerBlock("diorite_brick_wall", new WallBlock(materialProperties("diorite_brick_wall", MapColor.QUARTZ, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        GRANITE_BRICKS = registerBlock("granite_bricks", new Block(materialProperties("granite_bricks", MapColor.COLOR_ORANGE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        GRANITE_BRICK_STAIRS = registerBlock("granite_brick_stairs", new MaterialStairsBlock(GRANITE_BRICKS.defaultBlockState(), materialProperties("granite_brick_stairs", MapColor.COLOR_ORANGE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        GRANITE_BRICK_SLAB = registerBlock("granite_brick_slab", new SlabBlock(materialProperties("granite_brick_slab", MapColor.COLOR_ORANGE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        GRANITE_BRICK_WALL = registerBlock("granite_brick_wall", new WallBlock(materialProperties("granite_brick_wall", MapColor.COLOR_ORANGE, SoundType.STONE, 1.5F, 6.0F).requiresCorrectToolForDrops()));
        WET_FARMLAND = registerBlock("wet_farmland", new WetFarmlandBlock(blockProperties("wet_farmland").mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F).randomTicks()));
        SUPREME_MC_LOGO_BLOCK = registerBlock("suprememc_logo_block", new Block(blockProperties("suprememc_logo_block")
            .mapColor(MapColor.GRASS).sound(SoundType.GRASS).strength(0.6F).randomTicks()));
        SPRUCE_BOOKSHELF = registerBookshelf("spruce_bookshelf", MapColor.PODZOL);
        BIRCH_BOOKSHELF = registerBookshelf("birch_bookshelf", MapColor.SAND);
        JUNGLE_BOOKSHELF = registerBookshelf("jungle_bookshelf", MapColor.DIRT);
        ACACIA_BOOKSHELF = registerBookshelf("acacia_bookshelf", MapColor.COLOR_ORANGE);
        DARK_OAK_BOOKSHELF = registerBookshelf("dark_oak_bookshelf", MapColor.COLOR_BROWN);
        MANGROVE_BOOKSHELF = registerBookshelf("mangrove_bookshelf", MapColor.COLOR_RED);
        CHERRY_BOOKSHELF = registerBookshelf("cherry_bookshelf", MapColor.COLOR_PINK);
        PALE_OAK_BOOKSHELF = registerBookshelf("pale_oak_bookshelf", MapColor.COLOR_LIGHT_GRAY);
        BAMBOO_BOOKSHELF = registerBookshelf("bamboo_bookshelf", MapColor.COLOR_YELLOW);
        CRIMSON_BOOKSHELF = registerBookshelf("crimson_bookshelf", MapColor.COLOR_RED);
        WARPED_BOOKSHELF = registerBookshelf("warped_bookshelf", MapColor.COLOR_CYAN);
        PALM_BOOKSHELF = registerBookshelf("palm_bookshelf", MapColor.WOOD);
        SPRUCE_CRAFTING_TABLE = registerCraftingTable("spruce_crafting_table", MapColor.PODZOL);
        BIRCH_CRAFTING_TABLE = registerCraftingTable("birch_crafting_table", MapColor.SAND);
        JUNGLE_CRAFTING_TABLE = registerCraftingTable("jungle_crafting_table", MapColor.DIRT);
        ACACIA_CRAFTING_TABLE = registerCraftingTable("acacia_crafting_table", MapColor.COLOR_ORANGE);
        DARK_OAK_CRAFTING_TABLE = registerCraftingTable("dark_oak_crafting_table", MapColor.COLOR_BROWN);
        MANGROVE_CRAFTING_TABLE = registerCraftingTable("mangrove_crafting_table", MapColor.COLOR_RED);
        CHERRY_CRAFTING_TABLE = registerCraftingTable("cherry_crafting_table", MapColor.COLOR_PINK);
        PALE_OAK_CRAFTING_TABLE = registerCraftingTable("pale_oak_crafting_table", MapColor.COLOR_LIGHT_GRAY);
        BAMBOO_CRAFTING_TABLE = registerCraftingTable("bamboo_crafting_table", MapColor.COLOR_YELLOW);
        CRIMSON_CRAFTING_TABLE = registerCraftingTable("crimson_crafting_table", MapColor.COLOR_RED);
        WARPED_CRAFTING_TABLE = registerCraftingTable("warped_crafting_table", MapColor.COLOR_CYAN);
        PALM_CRAFTING_TABLE = registerCraftingTable("palm_crafting_table", MapColor.WOOD);
        BLACKSTONE_FURNACE = registerFurnace("blackstone_furnace", MapColor.COLOR_BLACK);
        DEEPSLATE_FURNACE = registerFurnace("deepslate_furnace", MapColor.DEEPSLATE);
        ATLANTIS_DEBRIS = registerBlock("atlantis_debris", new Block(blockProperties("atlantis_debris")
            .mapColor(MapColor.COLOR_BLACK).sound(SoundType.METAL).strength(30.0F, 1200.0F).requiresCorrectToolForDrops()));
        ABYSSALITE_BLOCK = registerBlock("abyssalite_block", new Block(blockProperties("abyssalite_block")
            .mapColor(MapColor.COLOR_BLUE).sound(SoundType.METAL).strength(50.0F, 1200.0F).requiresCorrectToolForDrops()));
        ABYSSALITE_STAIRS = registerBlock("abyssalite_stairs", new MaterialStairsBlock(ABYSSALITE_BLOCK.defaultBlockState(),
            materialProperties("abyssalite_stairs", MapColor.COLOR_BLUE, SoundType.METAL, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        ABYSSALITE_SLAB = registerBlock("abyssalite_slab", new SlabBlock(
            materialProperties("abyssalite_slab", MapColor.COLOR_BLUE, SoundType.METAL, 50.0F, 1200.0F).requiresCorrectToolForDrops()));
        ICICLE = registerBlock("icicle", new PointedDripstoneBlock(Blocks.DRIPSTONE_BLOCK.defaultBlockState(), blockProperties("icicle")
            .mapColor(MapColor.COLOR_LIGHT_BLUE).sound(SoundType.POINTED_DRIPSTONE).strength(1.5F, 3.0F).noOcclusion()));

        PALM_LOG = registerBlock("palm_log", new RotatedPillarBlock(blockProperties("palm_log").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 2.0F)));
        STRIPPED_PALM_LOG = registerBlock("stripped_palm_log", new RotatedPillarBlock(blockProperties("stripped_palm_log").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 2.0F)));
        PALM_WOOD = registerBlock("palm_wood", new RotatedPillarBlock(blockProperties("palm_wood").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 2.0F)));
        STRIPPED_PALM_WOOD = registerBlock("stripped_palm_wood", new RotatedPillarBlock(blockProperties("stripped_palm_wood").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 2.0F)));
        PALM_PLANKS = registerBlock("palm_planks", new Block(blockProperties("palm_planks").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
        PALM_SLAB = registerBlock("palm_slab", new SlabBlock(blockProperties("palm_slab").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
        PALM_STAIRS = registerBlock("palm_stairs", new PalmStairsBlock());
        PALM_FENCE = registerBlock("palm_fence", new FenceBlock(blockProperties("palm_fence").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
        PALM_FENCE_GATE = registerBlock("palm_fence_gate", new FenceGateBlock(PALM_WOOD_TYPE, blockProperties("palm_fence_gate").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
        PALM_DOOR = registerBlock("palm_door", new PalmDoorBlock());
        PALM_TRAPDOOR = registerBlock("palm_trapdoor", new PalmTrapDoorBlock());
        PALM_PRESSURE_PLATE = registerBlock("palm_pressure_plate", new PalmPressurePlateBlock());
        PALM_BUTTON = registerBlock("palm_button", new PalmButtonBlock());
        PALM_SIGN = registerBlock("palm_sign", new StandingSignBlock(PALM_WOOD_TYPE, blockProperties("palm_sign").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F).noCollision().noOcclusion()));
        PALM_WALL_SIGN = registerBlock("palm_wall_sign", new WallSignBlock(PALM_WOOD_TYPE, blockProperties("palm_wall_sign").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F).noCollision().noOcclusion()));
        PALM_HANGING_SIGN = registerBlock("palm_hanging_sign", new CeilingHangingSignBlock(PALM_WOOD_TYPE, blockProperties("palm_hanging_sign").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F).noCollision().noOcclusion()));
        PALM_WALL_HANGING_SIGN = registerBlock("palm_wall_hanging_sign", new WallHangingSignBlock(PALM_WOOD_TYPE, blockProperties("palm_wall_hanging_sign").mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F).noCollision().noOcclusion()));
        PALM_LEAVES = registerBlock("palm_leaves", new PalmLeavesBlock(blockProperties("palm_leaves").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.2F).randomTicks().noOcclusion().isValidSpawn((state, level, pos, type) -> false).isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false)));
        PALM_SAPLING = registerBlock("palm_sapling", new PalmSaplingBlock());
        POTTED_PALM_SAPLING = registerBlock("potted_palm_sapling", new FlowerPotBlock(PALM_SAPLING, blockProperties("potted_palm_sapling").mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(0.0F).noOcclusion()));
        COCONUT = registerBlock("coconut", new CoconutBlock(blockProperties("coconut").mapColor(MapColor.PLANT).sound(SoundType.WOOD).strength(0.2F, 3.0F).randomTicks().noCollision()));
        COTTON_BUSH = registerBlock("cotton_bush", new CottonBushBlock(blockProperties("cotton_bush").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.2F).randomTicks().noCollision()));
        TOMATO_BUSH = registerBlock("tomato_bush", new TomatoBushBlock(blockProperties("tomato_bush").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.2F).randomTicks().noCollision()));
        BEACH_GRASS = registerBlock("beach_grass", new BeachGrassBlock(blockProperties("beach_grass").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion()));
        TALL_BEACH_GRASS = registerBlock("tall_beach_grass", new TallBeachGrassBlock(blockProperties("tall_beach_grass").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion()));
        GRAPE_VINE = registerBlock("grape_vine", new GrapeVineBlock(blockProperties("grape_vine").mapColor(MapColor.PLANT).sound(SoundType.WEEPING_VINES).strength(0.2F).noCollision().noOcclusion().randomTicks()));
        GRAPE_VINE_PLANT = registerBlock("grape_vine_plant", new GrapeVinePlantBlock(blockProperties("grape_vine_plant").mapColor(MapColor.PLANT).sound(SoundType.WEEPING_VINES).strength(0.2F).noCollision().noOcclusion()));
        CORN_STALK = registerBlock("corn_stalk", new CornStalkBlock(blockProperties("corn_stalk").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion().randomTicks()));
        CORN_STALK_PLANT = registerBlock("corn_stalk_plant", new CornStalkPlantBlock(blockProperties("corn_stalk_plant").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion()));
        BUTTERCUP = registerBlock("buttercup", new FlowerBlock(MobEffects.SATURATION, 7.0F,
            blockProperties("buttercup").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion()));
        CLOVER = registerBlock("clover", new CloverBlock(
            blockProperties("clover").mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0.0F).noCollision().noOcclusion()));
        LUCK_POTION = registerPotion("luck", 6000, 0, MobEffects.LUCK);
        LONG_LUCK_POTION = registerPotion("long_luck", 9600, 0, MobEffects.LUCK);
        STRONG_LUCK_POTION = registerPotion("strong_luck", 1800, 1, MobEffects.LUCK);
        BAD_LUCK_POTION = registerPotion("bad_luck", 6000, 0, MobEffects.UNLUCK);
        LONG_BAD_LUCK_POTION = registerPotion("long_bad_luck", 9600, 0, MobEffects.UNLUCK);
        STRONG_BAD_LUCK_POTION = registerPotion("strong_bad_luck", 1800, 1, MobEffects.UNLUCK);
        HUNGER_POTION = registerPotion("hunger", 2700, 0, MobEffects.HUNGER);
        LONG_HUNGER_POTION = registerPotion("long_hunger", 5400, 0, MobEffects.HUNGER);
        STRONG_HUNGER_POTION = registerPotion("strong_hunger", 1200, 1, MobEffects.HUNGER);
        DECAY_POTION = registerPotion("decay", 2400, 0, MobEffects.WITHER);
        LONG_DECAY_POTION = registerPotion("long_decay", 3600, 0, MobEffects.WITHER);
        STRONG_DECAY_POTION = registerPotion("strong_decay", 1200, 1, MobEffects.WITHER);

        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            // Same properties as vanilla glowstone (mapColor/instrument/strength/sound/lightLevel/isRedstoneConductor).
            Block glowBlock = registerBlock(name + "_glowblock", new Block(blockProperties(name + "_glowblock")
                .mapColor(MapColor.SAND).instrument(NoteBlockInstrument.PLING).strength(0.3F).sound(SoundType.GLASS)
                .lightLevel(state -> 15).isRedstoneConductor((state, level, pos) -> false)));
            GLOW_BLOCKS.put(color, glowBlock);
            registerBlockItem(name + "_glowblock", glowBlock);

            // Same properties as vanilla slime block; stickiness restricted to same color via MixinPistonStructureResolver.
            Block slimeBlock = registerBlock(name + "_slime_block", new ColoredSlimeBlock(color, blockProperties(name + "_slime_block")
                .mapColor(MapColor.GRASS).friction(0.8F).bounceRestitution(1.0F).sound(SoundType.SLIME_BLOCK).noOcclusion()));
            SLIME_BLOCKS.put(color, slimeBlock);
            registerBlockItem(name + "_slime_block", slimeBlock);
        }

        Map<Block, Block> strippables = new HashMap<>(AxeItemAccessor.getStrippables());
        strippables.put(PALM_LOG, STRIPPED_PALM_LOG);
        strippables.put(PALM_WOOD, STRIPPED_PALM_WOOD);
        AxeItemAccessor.setStrippables(strippables);

        // Vanilla stores flammability in FireBlock's private per-block maps (see FireBlock.bootStrap: logs 5/5,
        // planks family 5/20, leaves 30/60; doors/trapdoors/signs/plates/buttons/saplings stay non-flammable).
        // The access transformer/widener opens setFlammable so palm wood burns like vanilla wood types.
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(PALM_LOG, 5, 5);
        fireBlock.setFlammable(STRIPPED_PALM_LOG, 5 , 5);
        fireBlock.setFlammable(PALM_WOOD, 5, 5);
        fireBlock.setFlammable(STRIPPED_PALM_WOOD, 5, 5);
        fireBlock.setFlammable(PALM_PLANKS, 5, 20);
        fireBlock.setFlammable(PALM_SLAB, 5, 20);
        fireBlock.setFlammable(PALM_STAIRS, 5, 20);
        fireBlock.setFlammable(PALM_FENCE, 5, 20);
        fireBlock.setFlammable(PALM_FENCE_GATE, 5, 20);
        fireBlock.setFlammable(PALM_LEAVES, 30, 60);

        PALM_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_boat"),
            EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> PALM_BOAT), MobCategory.MISC)
                .sized(1.375F, 0.5625F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_boat"))));
        PALM_CHEST_BOAT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_chest_boat"),
            EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> PALM_CHEST_BOAT), MobCategory.MISC)
                .sized(1.375F, 0.5625F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_chest_boat"))));

        GRIZZLY_BEAR_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grizzly_bear"),
            EntityType.Builder.<GrizzlyBear>of(GrizzlyBear::new, MobCategory.CREATURE)
                .sized(1.4F, 1.4F).clientTrackingRange(10)
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grizzly_bear"))));
        ENDER_SPIDER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ender_spider"),
            EntityType.Builder.<EnderSpider>of(EnderSpider::new, MobCategory.MONSTER)
                .sized(1.4F, 0.9F).eyeHeight(0.65F).clientTrackingRange(8)
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ender_spider"))));
        // Same hitbox/tracking as the vanilla creeper; only its explosion is changed (see FireCreeper).
        FIRE_CREEPER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fire_creeper"),
            EntityType.Builder.<FireCreeper>of(FireCreeper::new, MobCategory.MONSTER)
                .sized(0.6F, 1.7F).clientTrackingRange(8).fireImmune()
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fire_creeper"))));
        SNOW_CREEPER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow_creeper"),
            EntityType.Builder.<SnowCreeper>of(SnowCreeper::new, MobCategory.MONSTER)
                .sized(0.6F, 1.7F).clientTrackingRange(8)
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow_creeper"))));
        // Vanilla only assigns an ON_GROUND placement rule to entity types it registers itself;
        // without this, custom entity types default to NO_RESTRICTIONS and spawn floating in mid-air.
        SpawnPlacements.register(ENDER_SPIDER_ENTITY, SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(FIRE_CREEPER_ENTITY, SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacements.register(SNOW_CREEPER_ENTITY, SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        registerBlockItem("aquamarine_ore", AQUAMARINE_ORE);
        registerBlockItem("burning_diamond_ore", BURNING_DIAMOND_ORE);
        registerBlockItem("nether_anthracite_ore", NETHER_ANTHRACITE_ORE);
        registerBlockItem("anthracite_block", ANTHRACITE_BLOCK);
        registerBlockItem("deepslate_aquamarine_ore", DEEPSLATE_AQUAMARINE_ORE);
        registerBlockItem("aquamarine_block", AQUAMARINE_BLOCK);
        registerBlockItem("burning_diamond_block", BURNING_DIAMOND_BLOCK);
        registerBlockItem("burning_diamond_stairs", BURNING_DIAMOND_STAIRS);
        registerBlockItem("burning_diamond_slab", BURNING_DIAMOND_SLAB);
        registerBlockItem("aquamarine_stairs", AQUAMARINE_STAIRS);
        registerBlockItem("aquamarine_slab", AQUAMARINE_SLAB);
        registerBlockItem("amber_ore", AMBER_ORE);
        registerBlockItem("deepslate_amber_ore", DEEPSLATE_AMBER_ORE);
        registerBlockItem("prismarine_ore", PRISMARINE_ORE);
        registerBlockItem("deepslate_prismarine_ore", DEEPSLATE_PRISMARINE_ORE);
        registerBlockItem("amber_block", AMBER_BLOCK);
        registerBlockItem("amber_stairs", AMBER_STAIRS);
        registerBlockItem("amber_slab", AMBER_SLAB);
        registerBlockItem("iron_stairs", IRON_STAIRS);
        registerBlockItem("iron_slab", IRON_SLAB);
        registerBlockItem("lapis_stairs", LAPIS_STAIRS);
        registerBlockItem("lapis_slab", LAPIS_SLAB);
        registerBlockItem("gold_stairs", GOLD_STAIRS);
        registerBlockItem("gold_slab", GOLD_SLAB);
        registerBlockItem("diamond_stairs", DIAMOND_STAIRS);
        registerBlockItem("diamond_slab", DIAMOND_SLAB);
        registerBlockItem("emerald_stairs", EMERALD_STAIRS);
        registerBlockItem("emerald_slab", EMERALD_SLAB);
        registerBlockItem("coal_stairs", COAL_STAIRS);
        registerBlockItem("coal_slab", COAL_SLAB);
        registerBlockItem("obsidian_stairs", OBSIDIAN_STAIRS);
        registerBlockItem("obsidian_slab", OBSIDIAN_SLAB);
        registerBlockItem("netherite_stairs", NETHERITE_STAIRS);
        registerBlockItem("netherite_slab", NETHERITE_SLAB);
        registerBlockItem("polished_granite_wall", POLISHED_GRANITE_WALL);
        registerBlockItem("polished_diorite_wall", POLISHED_DIORITE_WALL);
        registerBlockItem("polished_andesite_wall", POLISHED_ANDESITE_WALL);
        registerBlockItem("andesite_bricks", ANDESITE_BRICKS);
        registerBlockItem("andesite_brick_stairs", ANDESITE_BRICK_STAIRS);
        registerBlockItem("andesite_brick_slab", ANDESITE_BRICK_SLAB);
        registerBlockItem("andesite_brick_wall", ANDESITE_BRICK_WALL);
        registerBlockItem("diorite_bricks", DIORITE_BRICKS);
        registerBlockItem("diorite_brick_stairs", DIORITE_BRICK_STAIRS);
        registerBlockItem("diorite_brick_slab", DIORITE_BRICK_SLAB);
        registerBlockItem("diorite_brick_wall", DIORITE_BRICK_WALL);
        registerBlockItem("granite_bricks", GRANITE_BRICKS);
        registerBlockItem("granite_brick_stairs", GRANITE_BRICK_STAIRS);
        registerBlockItem("granite_brick_slab", GRANITE_BRICK_SLAB);
        registerBlockItem("granite_brick_wall", GRANITE_BRICK_WALL);
        registerBlockItem("wet_farmland", WET_FARMLAND);
        registerBlockItem("suprememc_logo_block", SUPREME_MC_LOGO_BLOCK);
        registerBlockItem("spruce_bookshelf", SPRUCE_BOOKSHELF);
        registerBlockItem("birch_bookshelf", BIRCH_BOOKSHELF);
        registerBlockItem("jungle_bookshelf", JUNGLE_BOOKSHELF);
        registerBlockItem("acacia_bookshelf", ACACIA_BOOKSHELF);
        registerBlockItem("dark_oak_bookshelf", DARK_OAK_BOOKSHELF);
        registerBlockItem("mangrove_bookshelf", MANGROVE_BOOKSHELF);
        registerBlockItem("cherry_bookshelf", CHERRY_BOOKSHELF);
        registerBlockItem("pale_oak_bookshelf", PALE_OAK_BOOKSHELF);
        registerBlockItem("bamboo_bookshelf", BAMBOO_BOOKSHELF);
        registerBlockItem("crimson_bookshelf", CRIMSON_BOOKSHELF);
        registerBlockItem("warped_bookshelf", WARPED_BOOKSHELF);
        registerBlockItem("palm_bookshelf", PALM_BOOKSHELF);
        registerBlockItem("spruce_crafting_table", SPRUCE_CRAFTING_TABLE);
        registerBlockItem("birch_crafting_table", BIRCH_CRAFTING_TABLE);
        registerBlockItem("jungle_crafting_table", JUNGLE_CRAFTING_TABLE);
        registerBlockItem("acacia_crafting_table", ACACIA_CRAFTING_TABLE);
        registerBlockItem("dark_oak_crafting_table", DARK_OAK_CRAFTING_TABLE);
        registerBlockItem("mangrove_crafting_table", MANGROVE_CRAFTING_TABLE);
        registerBlockItem("cherry_crafting_table", CHERRY_CRAFTING_TABLE);
        registerBlockItem("pale_oak_crafting_table", PALE_OAK_CRAFTING_TABLE);
        registerBlockItem("bamboo_crafting_table", BAMBOO_CRAFTING_TABLE);
        registerBlockItem("crimson_crafting_table", CRIMSON_CRAFTING_TABLE);
        registerBlockItem("warped_crafting_table", WARPED_CRAFTING_TABLE);
        registerBlockItem("palm_crafting_table", PALM_CRAFTING_TABLE);
        registerBlockItem("blackstone_furnace", BLACKSTONE_FURNACE);
        registerBlockItem("deepslate_furnace", DEEPSLATE_FURNACE);
        registerBlockItem("atlantis_debris", ATLANTIS_DEBRIS);
        registerBlockItem("abyssalite_block", ABYSSALITE_BLOCK);
        registerBlockItem("abyssalite_stairs", ABYSSALITE_STAIRS);
        registerBlockItem("abyssalite_slab", ABYSSALITE_SLAB);
        registerBlockItem("icicle", ICICLE);
        registerBlockItem("palm_log", PALM_LOG);
        registerBlockItem("stripped_palm_log", STRIPPED_PALM_LOG);
        registerBlockItem("palm_wood", PALM_WOOD);
        registerBlockItem("stripped_palm_wood", STRIPPED_PALM_WOOD);
        registerBlockItem("palm_planks", PALM_PLANKS);
        registerBlockItem("palm_slab", PALM_SLAB);
        registerBlockItem("palm_stairs", PALM_STAIRS);
        registerBlockItem("palm_fence", PALM_FENCE);
        registerBlockItem("palm_fence_gate", PALM_FENCE_GATE);
        registerBlockItem("palm_door", PALM_DOOR);
        registerBlockItem("palm_trapdoor", PALM_TRAPDOOR);
        registerBlockItem("palm_pressure_plate", PALM_PRESSURE_PLATE);
        registerBlockItem("palm_button", PALM_BUTTON);
        registerItem("palm_sign", new SignItem(PALM_SIGN, PALM_WALL_SIGN,
            itemProperties("palm_sign").useBlockDescriptionPrefix().stacksTo(16)));
        registerItem("palm_hanging_sign", new HangingSignItem(PALM_HANGING_SIGN, PALM_WALL_HANGING_SIGN,
            itemProperties("palm_hanging_sign").useBlockDescriptionPrefix().stacksTo(16)));
        registerBlockItem("palm_leaves", PALM_LEAVES);
        registerBlockItem("palm_sapling", PALM_SAPLING);
        registerBlockItem("potted_palm_sapling", POTTED_PALM_SAPLING);
        COCONUT_ITEM = registerItem("coconut", new Item(itemProperties("coconut").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build(),
                net.minecraft.world.item.component.Consumables.defaultDrink()
                    .onConsume(new net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect())
                    .build())));
        COCONUT_SEEDS = registerItem("coconut_seeds", new BlockItem(COCONUT, itemProperties("coconut_seeds").stacksTo(64)));
        COTTON = registerItem("cotton", new BlockItem(COTTON_BUSH, itemProperties("cotton").stacksTo(64)));
        COTTON_HELMET = registerItem("cotton_helmet", new Item(itemProperties("cotton_helmet").humanoidArmor(COTTON_ARMOR_MATERIAL, ArmorType.HELMET)));
        COTTON_CHESTPLATE = registerItem("cotton_chestplate", new Item(itemProperties("cotton_chestplate").humanoidArmor(COTTON_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));
        COTTON_LEGGINGS = registerItem("cotton_leggings", new Item(itemProperties("cotton_leggings").humanoidArmor(COTTON_ARMOR_MATERIAL, ArmorType.LEGGINGS)));
        COTTON_BOOTS = registerItem("cotton_boots", new Item(itemProperties("cotton_boots").humanoidArmor(COTTON_ARMOR_MATERIAL, ArmorType.BOOTS)));
        registerBlockItem("beach_grass", BEACH_GRASS);
        registerBlockItem("tall_beach_grass", TALL_BEACH_GRASS);
        registerBlockItem("buttercup", BUTTERCUP);
        registerBlockItem("clover", CLOVER);
        CALAMARI = registerItem("calamari", new Item(itemProperties("calamari").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
        COOKED_CALAMARI = registerItem("cooked_calamari", new Item(itemProperties("cooked_calamari").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build())));
        GRAPES = registerItem("grapes", new BlockItem(GRAPE_VINE, itemProperties("grapes").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
        TOMATO = registerItem("tomato", new BlockItem(TOMATO_BUSH, itemProperties("tomato").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
        CORN = registerItem("corn", new BlockItem(CORN_STALK, itemProperties("corn").stacksTo(64)
            .food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));

        registerCompostables();

        PALM_BOAT = registerItem("palm_boat", new net.minecraft.world.item.BoatItem(PALM_BOAT_ENTITY, itemProperties("palm_boat")));
        PALM_CHEST_BOAT = registerItem("palm_chest_boat", new net.minecraft.world.item.BoatItem(PALM_CHEST_BOAT_ENTITY, itemProperties("palm_chest_boat")));
        GRIZZLY_BEAR_SPAWN_EGG = registerItem("grizzly_bear_spawn_egg",
            new SpawnEggItem(itemProperties("grizzly_bear_spawn_egg").spawnEgg(GRIZZLY_BEAR_ENTITY)));
        ENDER_SPIDER_SPAWN_EGG = registerItem("ender_spider_spawn_egg",
            new SpawnEggItem(itemProperties("ender_spider_spawn_egg").spawnEgg(ENDER_SPIDER_ENTITY)));
        FIRE_CREEPER_SPAWN_EGG = registerItem("fire_creeper_spawn_egg",
            new SpawnEggItem(itemProperties("fire_creeper_spawn_egg").spawnEgg(FIRE_CREEPER_ENTITY)));
        SNOW_CREEPER_SPAWN_EGG = registerItem("snow_creeper_spawn_egg",
            new SpawnEggItem(itemProperties("snow_creeper_spawn_egg").spawnEgg(SNOW_CREEPER_ENTITY)));

        ABYSSALITE_SCRAP = registerItem("abyssalite_scrap", new Item(itemProperties("abyssalite_scrap").stacksTo(64)));
        ABYSSALITE_INGOT = registerItem("abyssalite_ingot", new Item(itemProperties("abyssalite_ingot").stacksTo(64)));
        ABYSSALITE_UPGRADE_SMITHING_TEMPLATE = registerItem("abyssalite_upgrade_smithing_template",
            new Item(itemProperties("abyssalite_upgrade_smithing_template").stacksTo(64)));

        BURNING_DIAMOND = registerItem("burning_diamond", new Item(itemProperties("burning_diamond")));
        BURNING_DIAMOND_PICKAXE = registerItem("burning_diamond_pickaxe", new Item(itemProperties("burning_diamond_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
        BURNING_DIAMOND_AXE = registerItem("burning_diamond_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, itemProperties("burning_diamond_axe")));
        BURNING_DIAMOND_SHOVEL = registerItem("burning_diamond_shovel", new ShovelItem(ToolMaterial.DIAMOND, 1.5F, -3.0F, itemProperties("burning_diamond_shovel")));
        BURNING_DIAMOND_HOE = registerItem("burning_diamond_hoe", new HoeItem(ToolMaterial.DIAMOND, 0.0F, -3.0F, itemProperties("burning_diamond_hoe")));
        BURNING_DIAMOND_SWORD = registerItem("burning_diamond_sword", new Item(itemProperties("burning_diamond_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));
        BURNING_DIAMOND_HELMET = registerItem("burning_diamond_helmet", new BurningDiamondArmorItem("burning_diamond_helmet", ArmorType.HELMET));
        BURNING_DIAMOND_CHESTPLATE = registerItem("burning_diamond_chestplate", new BurningDiamondArmorItem("burning_diamond_chestplate", ArmorType.CHESTPLATE));
        BURNING_DIAMOND_LEGGINGS = registerItem("burning_diamond_leggings", new BurningDiamondArmorItem("burning_diamond_leggings", ArmorType.LEGGINGS));
        BURNING_DIAMOND_BOOTS = registerItem("burning_diamond_boots", new BurningDiamondArmorItem("burning_diamond_boots", ArmorType.BOOTS));

        BURNING_NETHERITE_PICKAXE = registerItem("burning_netherite_pickaxe", new Item(itemProperties("burning_netherite_pickaxe").pickaxe(ToolMaterial.NETHERITE, 1, -2.8F)));
        BURNING_NETHERITE_AXE = registerItem("burning_netherite_axe", new AxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, itemProperties("burning_netherite_axe")));
        BURNING_NETHERITE_SHOVEL = registerItem("burning_netherite_shovel", new ShovelItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, itemProperties("burning_netherite_shovel")));
        BURNING_NETHERITE_HOE = registerItem("burning_netherite_hoe", new HoeItem(ToolMaterial.NETHERITE, 0.0F, -3.0F, itemProperties("burning_netherite_hoe")));
        BURNING_NETHERITE_SWORD = registerItem("burning_netherite_sword", new Item(itemProperties("burning_netherite_sword").sword(ToolMaterial.NETHERITE, 3, -2.4F)));
        BURNING_NETHERITE_HELMET = registerItem("burning_netherite_helmet", new BurningNetheriteArmorItem("burning_netherite_helmet", ArmorType.HELMET));
        BURNING_NETHERITE_CHESTPLATE = registerItem("burning_netherite_chestplate", new BurningNetheriteArmorItem("burning_netherite_chestplate", ArmorType.CHESTPLATE));
        BURNING_NETHERITE_LEGGINGS = registerItem("burning_netherite_leggings", new BurningNetheriteArmorItem("burning_netherite_leggings", ArmorType.LEGGINGS));
        BURNING_NETHERITE_BOOTS = registerItem("burning_netherite_boots", new BurningNetheriteArmorItem("burning_netherite_boots", ArmorType.BOOTS));

        AQUAMARINE_PICKAXE = registerItem("aquamarine_pickaxe", new Item(itemProperties("aquamarine_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
        AQUAMARINE_AXE = registerItem("aquamarine_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, itemProperties("aquamarine_axe")));
        AQUAMARINE_SHOVEL = registerItem("aquamarine_shovel", new AquamarineShovelItem("aquamarine_shovel"));
        AQUAMARINE_HOE = registerItem("aquamarine_hoe", new AquamarineHoeItem("aquamarine_hoe"));
        AQUAMARINE_SWORD = registerItem("aquamarine_sword", new Item(itemProperties("aquamarine_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));

        AQUAMARINE_HELMET = registerItem("aquamarine_helmet", new AquamarineArmorItem("aquamarine_helmet", ArmorType.HELMET));
        AQUAMARINE_CHESTPLATE = registerItem("aquamarine_chestplate", new AquamarineArmorItem("aquamarine_chestplate", ArmorType.CHESTPLATE));
        AQUAMARINE_LEGGINGS = registerItem("aquamarine_leggings", new AquamarineArmorItem("aquamarine_leggings", ArmorType.LEGGINGS));
        AQUAMARINE_BOOTS = registerItem("aquamarine_boots", new AquamarineArmorItem("aquamarine_boots", ArmorType.BOOTS));
        AMBER_PICKAXE = registerItem("amber_pickaxe", new Item(itemProperties("amber_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
        AMBER_AXE = registerItem("amber_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, itemProperties("amber_axe")));
        AMBER_SHOVEL = registerItem("amber_shovel", new AmberShovelItem("amber_shovel"));
        AMBER_HOE = registerItem("amber_hoe", new AmberHoeItem("amber_hoe"));
        AMBER_SWORD = registerItem("amber_sword", new Item(itemProperties("amber_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));
        AMBER_HELMET = registerItem("amber_helmet", new AmberArmorItem("amber_helmet", ArmorType.HELMET));
        AMBER_CHESTPLATE = registerItem("amber_chestplate", new AmberArmorItem("amber_chestplate", ArmorType.CHESTPLATE));
        AMBER_LEGGINGS = registerItem("amber_leggings", new AmberArmorItem("amber_leggings", ArmorType.LEGGINGS));
        AMBER_BOOTS = registerItem("amber_boots", new AmberArmorItem("amber_boots", ArmorType.BOOTS));
        ABYSSALITE_PICKAXE = registerItem("abyssalite_pickaxe", new Item(itemProperties("abyssalite_pickaxe").pickaxe(ABYSSALITE_TOOL_MATERIAL, 1, -2.8F)));
        ABYSSALITE_AXE = registerItem("abyssalite_axe", new AxeItem(ABYSSALITE_TOOL_MATERIAL, 5.0F, -3.0F, itemProperties("abyssalite_axe")));
        ABYSSALITE_SHOVEL = registerItem("abyssalite_shovel", new AbyssaliteShovelItem());
        ABYSSALITE_HOE = registerItem("abyssalite_hoe", new AbyssaliteHoeItem());
        ABYSSALITE_SWORD = registerItem("abyssalite_sword", new Item(itemProperties("abyssalite_sword").sword(ABYSSALITE_TOOL_MATERIAL, 3, -2.4F)));
        ABYSSALITE_HELMET = registerItem("abyssalite_helmet", new AbyssaliteArmorItem("abyssalite_helmet", ArmorType.HELMET));
        ABYSSALITE_CHESTPLATE = registerItem("abyssalite_chestplate", new AbyssaliteArmorItem("abyssalite_chestplate", ArmorType.CHESTPLATE));
        ABYSSALITE_LEGGINGS = registerItem("abyssalite_leggings", new AbyssaliteArmorItem("abyssalite_leggings", ArmorType.LEGGINGS));
        ABYSSALITE_BOOTS = registerItem("abyssalite_boots", new AbyssaliteArmorItem("abyssalite_boots", ArmorType.BOOTS));
        ABYSSALITE_TRIDENT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident"),
                EntityType.Builder.<AbyssaliteTridentEntity>of(AbyssaliteTridentEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident"))));
        ABYSSALITE_TRIDENT = registerItem("abyssalite_trident", new AbyssaliteTridentItem(
            itemProperties("abyssalite_trident").durability(750).repairable(ABYSSALITE_REPAIR_ITEMS)));

        EMERALD_PICKAXE = registerItem("emerald_pickaxe", new Item(itemProperties("emerald_pickaxe").pickaxe(EMERALD_TOOL_MATERIAL, 1.0F, -2.8F)));
        EMERALD_AXE = registerItem("emerald_axe", new AxeItem(EMERALD_TOOL_MATERIAL, 6.0F, -3.1F, itemProperties("emerald_axe")));
        EMERALD_SHOVEL = registerItem("emerald_shovel", new ShovelItem(EMERALD_TOOL_MATERIAL, 1.5F, -3.0F, itemProperties("emerald_shovel")));
        EMERALD_HOE = registerItem("emerald_hoe", new HoeItem(EMERALD_TOOL_MATERIAL, -2.0F, -1.0F, itemProperties("emerald_hoe")));
        EMERALD_SWORD = registerItem("emerald_sword", new Item(itemProperties("emerald_sword").sword(EMERALD_TOOL_MATERIAL, 3, -2.4F)));
        EMERALD_HELMET = registerItem("emerald_helmet", new EmeraldArmorItem("emerald_helmet", ArmorType.HELMET));
        EMERALD_CHESTPLATE = registerItem("emerald_chestplate", new EmeraldArmorItem("emerald_chestplate", ArmorType.CHESTPLATE));
        EMERALD_LEGGINGS = registerItem("emerald_leggings", new EmeraldArmorItem("emerald_leggings", ArmorType.LEGGINGS));
        EMERALD_BOOTS = registerItem("emerald_boots", new EmeraldArmorItem("emerald_boots", ArmorType.BOOTS));

        CreativeTab.populate();

        Constants.LOG.info("Registered SupremeMC progression content");
    }

    private static Holder<Potion> registerPotion(String id, int duration, int amplifier,
                                                  Holder<net.minecraft.world.effect.MobEffect> effect) {
        return Registry.registerForHolder(
            BuiltInRegistries.POTION,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, id),
            new Potion(id, new MobEffectInstance(effect, duration, amplifier))
        );
    }

    private static Item registerBlockItem(String id, Block block) {
        return registerItem(id, new BlockItem(block, itemProperties(id).useBlockDescriptionPrefix()));
    }

    private static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(PALM_LEAVES.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(PALM_SAPLING.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(COCONUT_ITEM, 0.65F);
        ComposterBlock.COMPOSTABLES.put(COCONUT_SEEDS, 0.3F);
        ComposterBlock.COMPOSTABLES.put(COTTON, 0.65F);
        ComposterBlock.COMPOSTABLES.put(TOMATO, 0.65F);
        ComposterBlock.COMPOSTABLES.put(BEACH_GRASS.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(TALL_BEACH_GRASS.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(GRAPES, 0.65F);
        ComposterBlock.COMPOSTABLES.put(CORN, 0.65F);
        ComposterBlock.COMPOSTABLES.put(BUTTERCUP.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CLOVER.asItem(), 0.3F);
    }

    public static Item.Properties itemProperties(String id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

    public static BlockBehaviour.Properties blockProperties(String id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

    private static BlockBehaviour.Properties materialProperties(String id, MapColor mapColor, SoundType sound, float strength, float resistance) {
        return blockProperties(id).mapColor(mapColor).sound(sound).strength(strength, resistance);
    }

    private static <T extends Block> T registerBlock(String id, T block) {
        RegistryHelper.registerBlock(id, block);
        return block;
    }

    private static Block registerBookshelf(String id, MapColor mapColor) {
        return registerBlock(id, new BookshelfBlock(blockProperties(id)
            .mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(1.5F).sound(SoundType.WOOD).ignitedByLava()));
    }

    private static Block registerCraftingTable(String id, MapColor mapColor) {
        return registerBlock(id, new WoodCraftingTableBlock(blockProperties(id)
            .mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()));
    }

    private static Block registerFurnace(String id, MapColor mapColor) {
        return registerBlock(id, new StoneFurnaceBlock(blockProperties(id)
            .mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(3.5F).sound(SoundType.STONE)));
    }

    private static <T extends Item> T registerItem(String id, T item) {
        RegistryHelper.registerItem(id, item);
        return item;
    }

    private static final class RegistryHelper {
        private static void registerItem(String id, Item item) {
            Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), item);
        }

        private static void registerBlock(String id, Block block) {
            Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), block);
        }
    }

}
