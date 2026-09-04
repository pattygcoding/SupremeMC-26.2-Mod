package com.suprememc.content;

import com.mojang.serialization.MapCodec;
import com.suprememc.Constants;
import com.suprememc.content.blocks.*;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import com.suprememc.content.entity.FireCreeper;
import com.suprememc.content.entity.GrizzlyBear;
import com.suprememc.content.items.*;
import com.suprememc.content.worldgen.BiomeTemperaturePlacementModifier;
import com.suprememc.content.worldgen.PalmCoconutDecorator;
import com.suprememc.content.worldgen.PalmFoliagePlacer;
import com.suprememc.content.worldgen.PalmTrunkPlacer;
import com.suprememc.mixin.AxeItemAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
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
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
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
    public static Block AQUAMARINE_ORE;
    public static Block DEEPSLATE_AQUAMARINE_ORE;
    public static Block AQUAMARINE_BLOCK;
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
    public static Block WET_FARMLAND;
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
    public static Block TOMATO_BUSH;
    public static Block BEACH_GRASS;
    public static Block TALL_BEACH_GRASS;
    public static EntityType<Boat> PALM_BOAT_ENTITY;
    public static EntityType<ChestBoat> PALM_CHEST_BOAT_ENTITY;
    public static Item PALM_BOAT;
    public static Item PALM_CHEST_BOAT;
    public static Item ABYSSALITE_SCRAP;
    public static Item ABYSSALITE_INGOT;
    public static Item ABYSSALITE_UPGRADE_SMITHING_TEMPLATE;
    public static Block ATLANTIS_DEBRIS;
    public static Block ABYSSALITE_BLOCK;
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
    public static EntityType<FireCreeper> FIRE_CREEPER_ENTITY;
    public static Item FIRE_CREEPER_SPAWN_EGG;
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

        AQUAMARINE_ORE = registerBlock("aquamarine_ore",
            new DropExperienceBlock(UniformInt.of(3, 7), blockProperties("aquamarine_ore").mapColor(MapColor.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        DEEPSLATE_AQUAMARINE_ORE = registerBlock("deepslate_aquamarine_ore",
            new DropExperienceBlock(UniformInt.of(3, 7), blockProperties("deepslate_aquamarine_ore").mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops()));
        AQUAMARINE_BLOCK = registerBlock("aquamarine_block",
                new Block(blockProperties("aquamarine_block").mapColor(MapColor.COLOR_CYAN).sound(SoundType.METAL).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
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
        WET_FARMLAND = registerBlock("wet_farmland", new WetFarmlandBlock(blockProperties("wet_farmland").mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F).randomTicks()));
        ATLANTIS_DEBRIS = registerBlock("atlantis_debris", new Block(blockProperties("atlantis_debris")
            .mapColor(MapColor.COLOR_BLACK).sound(SoundType.METAL).strength(30.0F, 1200.0F).requiresCorrectToolForDrops()));
        ABYSSALITE_BLOCK = registerBlock("abyssalite_block", new Block(blockProperties("abyssalite_block")
            .mapColor(MapColor.COLOR_BLUE).sound(SoundType.METAL).strength(50.0F, 1200.0F).requiresCorrectToolForDrops()));
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
        // Same hitbox/tracking as the vanilla creeper; only its explosion is changed (see FireCreeper).
        FIRE_CREEPER_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fire_creeper"),
            EntityType.Builder.<FireCreeper>of(FireCreeper::new, MobCategory.MONSTER)
                .sized(0.6F, 1.7F).clientTrackingRange(8).fireImmune()
                .build(ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fire_creeper"))));

        registerBlockItem("aquamarine_ore", AQUAMARINE_ORE);
        registerBlockItem("deepslate_aquamarine_ore", DEEPSLATE_AQUAMARINE_ORE);
        registerBlockItem("aquamarine_block", AQUAMARINE_BLOCK);
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
        registerBlockItem("wet_farmland", WET_FARMLAND);
        registerBlockItem("atlantis_debris", ATLANTIS_DEBRIS);
        registerBlockItem("abyssalite_block", ABYSSALITE_BLOCK);
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
        registerBlockItem("beach_grass", BEACH_GRASS);
        registerBlockItem("tall_beach_grass", TALL_BEACH_GRASS);
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

        PALM_BOAT = registerItem("palm_boat", new net.minecraft.world.item.BoatItem(PALM_BOAT_ENTITY, itemProperties("palm_boat")));
        PALM_CHEST_BOAT = registerItem("palm_chest_boat", new net.minecraft.world.item.BoatItem(PALM_CHEST_BOAT_ENTITY, itemProperties("palm_chest_boat")));
        GRIZZLY_BEAR_SPAWN_EGG = registerItem("grizzly_bear_spawn_egg",
            new SpawnEggItem(itemProperties("grizzly_bear_spawn_egg").spawnEgg(GRIZZLY_BEAR_ENTITY)));
        FIRE_CREEPER_SPAWN_EGG = registerItem("fire_creeper_spawn_egg",
            new SpawnEggItem(itemProperties("fire_creeper_spawn_egg").spawnEgg(FIRE_CREEPER_ENTITY)));

        ABYSSALITE_SCRAP = registerItem("abyssalite_scrap", new Item(itemProperties("abyssalite_scrap").stacksTo(64)));
        ABYSSALITE_INGOT = registerItem("abyssalite_ingot", new Item(itemProperties("abyssalite_ingot").stacksTo(64)));
        ABYSSALITE_UPGRADE_SMITHING_TEMPLATE = registerItem("abyssalite_upgrade_smithing_template",
            new Item(itemProperties("abyssalite_upgrade_smithing_template").stacksTo(64)));

        AQUAMARINE_PICKAXE = registerItem("aquamarine_pickaxe", new Item(itemProperties("aquamarine_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
        AQUAMARINE_AXE = registerItem("aquamarine_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, itemProperties("aquamarine_axe")));
        AQUAMARINE_SHOVEL = registerItem("aquamarine_shovel", new AquamarineShovelItem("aquamarine_shovel"));
        AQUAMARINE_HOE = registerItem("aquamarine_hoe", new AquamarineHoeItem("aquamarine_hoe"));
        AQUAMARINE_SWORD = registerItem("aquamarine_sword", new Item(itemProperties("aquamarine_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));

        AQUAMARINE_HELMET = registerItem("aquamarine_helmet", new AquamarineArmorItem("aquamarine_helmet", ArmorType.HELMET));
        AQUAMARINE_CHESTPLATE = registerItem("aquamarine_chestplate", new AquamarineArmorItem("aquamarine_chestplate", ArmorType.CHESTPLATE));
        AQUAMARINE_LEGGINGS = registerItem("aquamarine_leggings", new AquamarineArmorItem("aquamarine_leggings", ArmorType.LEGGINGS));
        AQUAMARINE_BOOTS = registerItem("aquamarine_boots", new AquamarineArmorItem("aquamarine_boots", ArmorType.BOOTS));
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

        CREATIVE_TAB_ITEMS.addAll(List.of(AQUAMARINE, AQUAMARINE_ORE, DEEPSLATE_AQUAMARINE_ORE, AQUAMARINE_BLOCK, WET_FARMLAND,
            IRON_STAIRS, IRON_SLAB, LAPIS_STAIRS, LAPIS_SLAB, GOLD_STAIRS, GOLD_SLAB,
            DIAMOND_STAIRS, DIAMOND_SLAB, EMERALD_STAIRS, EMERALD_SLAB, COAL_STAIRS, COAL_SLAB,
            OBSIDIAN_STAIRS, OBSIDIAN_SLAB, NETHERITE_STAIRS, NETHERITE_SLAB,
                AQUAMARINE_PICKAXE, AQUAMARINE_AXE, AQUAMARINE_SHOVEL, AQUAMARINE_HOE, AQUAMARINE_SWORD,
                AQUAMARINE_HELMET, AQUAMARINE_CHESTPLATE, AQUAMARINE_LEGGINGS, AQUAMARINE_BOOTS));
        CREATIVE_TAB_ITEMS.addAll(List.of(EMERALD_PICKAXE, EMERALD_AXE, EMERALD_SHOVEL, EMERALD_HOE, EMERALD_SWORD,
                EMERALD_HELMET, EMERALD_CHESTPLATE, EMERALD_LEGGINGS, EMERALD_BOOTS));
        CREATIVE_TAB_ITEMS.addAll(List.of(PALM_LOG, STRIPPED_PALM_LOG, PALM_WOOD, STRIPPED_PALM_WOOD, PALM_PLANKS, PALM_SLAB, PALM_STAIRS, PALM_FENCE, PALM_FENCE_GATE,
                PALM_DOOR, PALM_TRAPDOOR, PALM_PRESSURE_PLATE, PALM_BUTTON, PALM_SIGN, PALM_HANGING_SIGN,
            PALM_BOAT, PALM_CHEST_BOAT, PALM_LEAVES, PALM_SAPLING, COCONUT_ITEM, COCONUT_SEEDS, COTTON, CALAMARI, COOKED_CALAMARI,
            GRAPES, TOMATO, CORN));
        CREATIVE_TAB_ITEMS.add(GRIZZLY_BEAR_SPAWN_EGG);
        CREATIVE_TAB_ITEMS.add(FIRE_CREEPER_SPAWN_EGG);
        CREATIVE_TAB_ITEMS.addAll(List.of(BEACH_GRASS, TALL_BEACH_GRASS));
        CREATIVE_TAB_ITEMS.addAll(GLOW_BLOCKS.values());
        CREATIVE_TAB_ITEMS.addAll(SLIME_BLOCKS.values());
        CREATIVE_TAB_ITEMS.addAll(List.of(ATLANTIS_DEBRIS, ABYSSALITE_BLOCK, ABYSSALITE_SCRAP, ABYSSALITE_INGOT,
            ICICLE,
                ABYSSALITE_UPGRADE_SMITHING_TEMPLATE, ABYSSALITE_PICKAXE, ABYSSALITE_AXE, ABYSSALITE_SHOVEL,
                ABYSSALITE_HOE, ABYSSALITE_SWORD, ABYSSALITE_HELMET, ABYSSALITE_CHESTPLATE, ABYSSALITE_LEGGINGS,
                ABYSSALITE_BOOTS, ABYSSALITE_TRIDENT));

        Constants.LOG.info("Registered SupremeMC progression content");
    }

    private static Item registerBlockItem(String id, Block block) {
        return registerItem(id, new BlockItem(block, itemProperties(id).useBlockDescriptionPrefix()));
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
