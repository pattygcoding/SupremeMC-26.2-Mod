package com.suprememc.content;

import com.suprememc.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
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
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Map;

public final class ModContent {

    private static boolean registered = false;

    // Loader-agnostic list, shared by fabric/neoforge to populate the SupremeMC creative tab.
    public static final List<ItemLike> CREATIVE_TAB_ITEMS = new java.util.ArrayList<>();

        private static final ArmorMaterial AQUAMARINE_ARMOR_MATERIAL = new ArmorMaterial(
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

    public static Item AQUAMARINE;
    public static Block AQUAMARINE_ORE;
    public static Block DEEPSLATE_AQUAMARINE_ORE;
    public static Block AQUAMARINE_BLOCK;
    public static Block WET_FARMLAND;

    public static Item AQUAMARINE_PICKAXE;
    public static Item AQUAMARINE_AXE;
    public static Item AQUAMARINE_SHOVEL;
    public static Item AQUAMARINE_HOE;
    public static Item AQUAMARINE_SWORD;

    public static Item AQUAMARINE_HELMET;
    public static Item AQUAMARINE_CHESTPLATE;
    public static Item AQUAMARINE_LEGGINGS;
    public static Item AQUAMARINE_BOOTS;

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
        WET_FARMLAND = registerBlock("wet_farmland", new WetFarmlandBlock(blockProperties("wet_farmland").mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F).randomTicks()));

        registerBlockItem("aquamarine_ore", AQUAMARINE_ORE);
        registerBlockItem("deepslate_aquamarine_ore", DEEPSLATE_AQUAMARINE_ORE);
        registerBlockItem("aquamarine_block", AQUAMARINE_BLOCK);
        registerBlockItem("wet_farmland", WET_FARMLAND);

        AQUAMARINE_PICKAXE = registerItem("aquamarine_pickaxe", new Item(itemProperties("aquamarine_pickaxe").pickaxe(ToolMaterial.DIAMOND, 1, -2.8F)));
        AQUAMARINE_AXE = registerItem("aquamarine_axe", new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, itemProperties("aquamarine_axe")));
        AQUAMARINE_SHOVEL = registerItem("aquamarine_shovel", new AquamarineShovelItem("aquamarine_shovel"));
        AQUAMARINE_HOE = registerItem("aquamarine_hoe", new AquamarineHoeItem("aquamarine_hoe"));
        AQUAMARINE_SWORD = registerItem("aquamarine_sword", new Item(itemProperties("aquamarine_sword").sword(ToolMaterial.DIAMOND, 3, -2.4F)));

        AQUAMARINE_HELMET = registerItem("aquamarine_helmet", new AquamarineArmorItem("aquamarine_helmet", ArmorType.HELMET));
        AQUAMARINE_CHESTPLATE = registerItem("aquamarine_chestplate", new AquamarineArmorItem("aquamarine_chestplate", ArmorType.CHESTPLATE));
        AQUAMARINE_LEGGINGS = registerItem("aquamarine_leggings", new AquamarineArmorItem("aquamarine_leggings", ArmorType.LEGGINGS));
        AQUAMARINE_BOOTS = registerItem("aquamarine_boots", new AquamarineArmorItem("aquamarine_boots", ArmorType.BOOTS));

        CREATIVE_TAB_ITEMS.addAll(List.of(AQUAMARINE, AQUAMARINE_ORE, DEEPSLATE_AQUAMARINE_ORE, AQUAMARINE_BLOCK, WET_FARMLAND,
                AQUAMARINE_PICKAXE, AQUAMARINE_AXE, AQUAMARINE_SHOVEL, AQUAMARINE_HOE, AQUAMARINE_SWORD,
                AQUAMARINE_HELMET, AQUAMARINE_CHESTPLATE, AQUAMARINE_LEGGINGS, AQUAMARINE_BOOTS));

        Constants.LOG.info("Registered Aquamarine progression content");
    }

    private static Item registerBlockItem(String id, Block block) {
        return registerItem(id, new BlockItem(block, itemProperties(id).useBlockDescriptionPrefix()));
    }

    private static Item.Properties itemProperties(String id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

    private static BlockBehaviour.Properties blockProperties(String id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
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

    public static class WetFarmlandBlock extends FarmlandBlock {
        public WetFarmlandBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        }
    }

    public static class AquamarineHoeItem extends HoeItem {
        public AquamarineHoeItem(String id) {
            super(ToolMaterial.DIAMOND, 0.0F, 0.0F, itemProperties(id));
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            if ((state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) && !level.isClientSide()) {
                level.setBlock(pos, WET_FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 7), 3);
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(), EquipmentSlot.MAINHAND);
                return InteractionResult.SUCCESS;
            }
            return super.useOn(context);
        }
    }

    public static class AquamarineShovelItem extends ShovelItem {
        public AquamarineShovelItem(String id) {
            super(ToolMaterial.DIAMOND, 1.5F, -3.0F, itemProperties(id));
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            if ((state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT)) && !level.isClientSide()) {
                level.setBlock(pos, Blocks.MUD.defaultBlockState(), 3);
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(), EquipmentSlot.MAINHAND);
                return InteractionResult.SUCCESS;
            }
            return super.useOn(context);
        }
    }

    public static class AquamarineArmorItem extends Item {
        public AquamarineArmorItem(String id, ArmorType type) {
            super(itemProperties(id).humanoidArmor(AQUAMARINE_ARMOR_MATERIAL, type));
        }

        @Override
        public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
            if (entity instanceof LivingEntity living && living instanceof Player player) {
                boolean fullSet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AquamarineArmorItem
                        && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AquamarineArmorItem
                        && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof AquamarineArmorItem
                        && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof AquamarineArmorItem;

                if (fullSet) {
                    living.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 0, false, false, false));
                }
            }
        }
    }
}
