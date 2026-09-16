package com.suprememc.content.entity;

import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

public class ShelterTnt extends PrimedTnt {
    private static final ResourceKey<LootTable> BONUS_CHEST_LOOT = ResourceKey.create(
        net.minecraft.core.registries.Registries.LOOT_TABLE,
        Identifier.withDefaultNamespace("chests/spawn_bonus_chest"));
    private static final ResourceKey<Biome> BIRCH_FOREST = biome("birch_forest");
    private static final ResourceKey<Biome> OLD_GROWTH_BIRCH_FOREST = biome("old_growth_birch_forest");
    private static final ResourceKey<Biome> SNOWY_TAIGA = biome("snowy_taiga");
    private static final ResourceKey<Biome> GROVE = biome("grove");
    private static final ResourceKey<Biome> SPARSE_JUNGLE = biome("sparse_jungle");
    private static final ResourceKey<Biome> BAMBOO_JUNGLE = biome("bamboo_jungle");
    private static final ResourceKey<Biome> SAVANNA_PLATEAU = biome("savanna_plateau");
    private static final ResourceKey<Biome> DARK_FOREST = biome("dark_forest");
    private static final ResourceKey<Biome> CHERRY_GROVE = biome("cherry_grove");
    private static final ResourceKey<Biome> MANGROVE_SWAMP = biome("mangrove_swamp");
    private static final ResourceKey<Biome> PALE_GARDEN = biome("pale_garden");
    private static final ResourceKey<Biome> BEACH = biome("beach");
    private static final ResourceKey<Biome> WARPED_FOREST = biome("warped_forest");

    public ShelterTnt(EntityType<? extends ShelterTnt> type, Level level) {
        super(type, level);
        setBlockState(ModContent.SHELTER_TNT.defaultBlockState());
    }

    public void explodeShelter() {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }

        buildShelter(level, BlockPos.containing(this.getX(), this.getY(), this.getZ()));
    }

    private static void buildShelter(ServerLevel level, BlockPos center) {
        int floorY = center.getY() - 1;
        WoodPalette wood = woodFor(level, center);
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                set(level, center.offset(x, floorY - center.getY(), z), wood.planks().defaultBlockState());
            }
        }
        for (int x = -2; x <= 2; x++) {
            for (int z = -3; z <= 3; z++) {
                set(level, center.offset(x, floorY + 4 - center.getY(), z), wood.planks().defaultBlockState());
            }
        }
        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    set(level, center.offset(x, floorY + y - center.getY(), z), Blocks.AIR.defaultBlockState());
                }
            }
        }

        for (int y = 1; y <= 3; y++) {
            for (int x = -3; x <= 3; x++) {
                set(level, center.offset(x, floorY + y - center.getY(), -3),
                    x == 0 && y <= 2 ? Blocks.AIR.defaultBlockState() : wallBlock(wood, x, y));
                set(level, center.offset(x, floorY + y - center.getY(), 3), wallBlock(wood, x, y));
            }
            for (int z = -2; z <= 2; z++) {
                set(level, center.offset(-3, floorY + y - center.getY(), z), wallBlock(wood, z, y));
                set(level, center.offset(3, floorY + y - center.getY(), z), wallBlock(wood, z, y));
            }
        }

        for (int y = 1; y <= 3; y++) {
            set(level, center.offset(-3, floorY + y - center.getY(), -3), wood.log().defaultBlockState());
            set(level, center.offset(3, floorY + y - center.getY(), -3), wood.log().defaultBlockState());
            set(level, center.offset(-3, floorY + y - center.getY(), 3), wood.log().defaultBlockState());
            set(level, center.offset(3, floorY + y - center.getY(), 3), wood.log().defaultBlockState());
        }

        BlockPos door = center.offset(0, floorY + 1 - center.getY(), -3);
        set(level, door, wood.door().defaultBlockState()
            .setValue(DoorBlock.FACING, Direction.NORTH)
            .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        set(level, door.above(), wood.door().defaultBlockState()
            .setValue(DoorBlock.FACING, Direction.NORTH)
            .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));

        BlockPos chestPos = center.offset(-2, floorY + 1 - center.getY(), 2);
        set(level, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(BONUS_CHEST_LOOT, level.getRandom().nextLong());
        }
        set(level, center.offset(2, floorY + 1 - center.getY(), 2), Blocks.CRAFTING_TABLE.defaultBlockState());
        set(level, center.offset(2, floorY + 1 - center.getY(), 1), Blocks.FURNACE.defaultBlockState()
            .setValue(FurnaceBlock.FACING, Direction.WEST));

        if (level.dimension() != Level.NETHER && level.dimension() != Level.END) {
            BlockPos bedFoot = center.offset(-1, floorY + 1 - center.getY(), -2);
            set(level, bedFoot, Blocks.BED.white().defaultBlockState()
                .setValue(BedBlock.FACING, Direction.WEST)
                .setValue(BedBlock.PART, BedPart.FOOT));
            set(level, bedFoot.west(), Blocks.BED.white().defaultBlockState()
                .setValue(BedBlock.FACING, Direction.WEST)
                .setValue(BedBlock.PART, BedPart.HEAD));
        }

        set(level, center.offset(0, floorY + 1 - center.getY(), 0), Blocks.TORCH.defaultBlockState());
    }

    private static net.minecraft.world.level.block.state.BlockState wallBlock(WoodPalette wood, int position, int height) {
        if (height == 2 && (position == -1 || position == 1)) {
            return Blocks.GLASS.defaultBlockState();
        }
        return wood.planks().defaultBlockState();
    }

    private static WoodPalette woodFor(ServerLevel level, BlockPos pos) {
        if (level.dimension() == Level.END) return new WoodPalette(ModContent.LAVENDER_PLANKS, ModContent.LAVENDER_STEM, ModContent.LAVENDER_DOOR);
        if (level.dimension() == Level.NETHER) {
            return level.getBiome(pos).is(WARPED_FOREST)
                ? new WoodPalette(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM, Blocks.WARPED_DOOR)
                : new WoodPalette(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM, Blocks.CRIMSON_DOOR);
        }
        if (level.getBiome(pos).is(BEACH)) return new WoodPalette(ModContent.PALM_PLANKS, ModContent.PALM_LOG, ModContent.PALM_DOOR);
        if (level.getBiome(pos).is(PALE_GARDEN)) return new WoodPalette(Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_DOOR);
        if (level.getBiome(pos).is(BIRCH_FOREST) || level.getBiome(pos).is(OLD_GROWTH_BIRCH_FOREST)) return new WoodPalette(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG, Blocks.BIRCH_DOOR);
        if (level.getBiome(pos).is(SNOWY_TAIGA) || level.getBiome(pos).is(GROVE) || level.getBiome(pos).is(biome("taiga"))) return new WoodPalette(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG, Blocks.SPRUCE_DOOR);
        if (level.getBiome(pos).is(SPARSE_JUNGLE) || level.getBiome(pos).is(BAMBOO_JUNGLE) || level.getBiome(pos).is(biome("jungle"))) return new WoodPalette(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG, Blocks.JUNGLE_DOOR);
        if (level.getBiome(pos).is(SAVANNA_PLATEAU) || level.getBiome(pos).is(biome("savanna"))) return new WoodPalette(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG, Blocks.ACACIA_DOOR);
        if (level.getBiome(pos).is(DARK_FOREST)) return new WoodPalette(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_DOOR);
        if (level.getBiome(pos).is(CHERRY_GROVE)) return new WoodPalette(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG, Blocks.CHERRY_DOOR);
        if (level.getBiome(pos).is(MANGROVE_SWAMP)) return new WoodPalette(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG, Blocks.MANGROVE_DOOR);
        return new WoodPalette(Blocks.OAK_PLANKS, Blocks.OAK_LOG, Blocks.OAK_DOOR);
    }

    private static ResourceKey<Biome> biome(String name) {
        return ResourceKey.create(net.minecraft.core.registries.Registries.BIOME, Identifier.withDefaultNamespace(name));
    }

    private record WoodPalette(Block planks, Block log, Block door) {
    }

    private static void set(ServerLevel level, BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        level.setBlock(pos, state, 3);
    }
}