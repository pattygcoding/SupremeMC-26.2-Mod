package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import com.suprememc.content.blockentity.NetherReactorCoreBlockEntity;
import com.suprememc.content.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Recreates the Pocket Edition Nether Reactor: a 3x3x3 gold/cobblestone multiblock that, when
 * right-clicked, seals the player inside a hollow netherrack spire for 45 seconds.
 */
public class NetherReactorCoreBlock extends Block implements EntityBlock {
    public static final MapCodec<NetherReactorCoreBlock> CODEC = simpleCodec(NetherReactorCoreBlock::new);
    public static final EnumProperty<ReactorState> STATE = EnumProperty.create("reactor_state", ReactorState.class);

    /** Half-extent of the widest tier, so the base footprint is 17x17. */
    public static final int SPIRE_RADIUS = 8;
    /** Levels above the floor; the tower is 17 blocks tall including floor and roof. */
    public static final int SPIRE_HEIGHT = 16;

    private static final int[][] CORNER_OFFSETS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private static final int[][] CROSS_OFFSETS = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public NetherReactorCoreBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, ReactorState.UNUSED));
    }

    @Override
    public MapCodec<NetherReactorCoreBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetherReactorCoreBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != ModBlockEntities.NETHER_REACTOR_CORE) {
            return null;
        }
        return (tickLevel, pos, tickState, blockEntity) ->
            NetherReactorCoreBlockEntity.serverTick(tickLevel, pos, tickState, (NetherReactorCoreBlockEntity) blockEntity);
    }

    // Also handled with an item in hand, otherwise a creative player holding blocks just places them.
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(STATE) == ReactorState.UNUSED && findStructureFault(level, pos) == null) {
            if (!level.isClientSide()) {
                activate(level, pos, state);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(STATE) != ReactorState.UNUSED) {
            return InteractionResult.PASS;
        }
        StructureFault fault = findStructureFault(level, pos);
        if (fault != null) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.translatable("message.suprememc.nether_reactor.incomplete",
                    fault.expected(), fault.pos().getX(), fault.pos().getY(), fault.pos().getZ(),
                    level.getBlockState(fault.pos()).getBlock().getName()));
            }
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide()) {
            activate(level, pos, state);
        }
        return InteractionResult.SUCCESS;
    }

    /** The first position that does not match the required pattern, with what was expected there. */
    public record StructureFault(BlockPos pos, Component expected) {
    }

    public static boolean isStructureComplete(Level level, BlockPos core) {
        return findStructureFault(level, core) == null;
    }

    /**
     * Bottom layer: gold corners + cobblestone cross. Middle: cobblestone in the corners around the
     * core with empty sides. Top: cobblestone cross.
     */
    public static StructureFault findStructureFault(Level level, BlockPos core) {
        for (int[] offset : CORNER_OFFSETS) {
            BlockPos bottom = core.offset(offset[0], -1, offset[1]);
            if (!level.getBlockState(bottom).is(Blocks.GOLD_BLOCK)) {
                return new StructureFault(bottom, Blocks.GOLD_BLOCK.getName());
            }
            BlockPos middle = core.offset(offset[0], 0, offset[1]);
            if (!level.getBlockState(middle).is(Blocks.COBBLESTONE)) {
                return new StructureFault(middle, Blocks.COBBLESTONE.getName());
            }
        }
        for (int[] offset : CROSS_OFFSETS) {
            BlockPos bottom = core.offset(offset[0], -1, offset[1]);
            if (!level.getBlockState(bottom).is(Blocks.COBBLESTONE)) {
                return new StructureFault(bottom, Blocks.COBBLESTONE.getName());
            }
            BlockPos top = core.offset(offset[0], 1, offset[1]);
            if (!level.getBlockState(top).is(Blocks.COBBLESTONE)) {
                return new StructureFault(top, Blocks.COBBLESTONE.getName());
            }
            boolean center = offset[0] == 0 && offset[1] == 0;
            BlockPos middle = core.offset(offset[0], 0, offset[1]);
            if (!center && !level.getBlockState(middle).isAir()) {
                return new StructureFault(middle, Blocks.AIR.getName());
            }
        }
        return null;
    }

    private static void activate(Level level, BlockPos core, BlockState state) {
        List<BlockPos> obsidian = structurePositions(core);
        for (BlockPos pos : obsidian) {
            level.setBlockAndUpdate(pos, ModContent.GLOWING_OBSIDIAN.defaultBlockState());
        }
        level.setBlockAndUpdate(core, state.setValue(STATE, ReactorState.ACTIVE));

        generateSpire(level, core);

        if (level.getServer() != null) {
            level.dimensionTypeRegistration().value().defaultClock().ifPresent(clock ->
                level.getServer().clockManager().moveToTimeMarker(clock, ClockTimeMarkers.NIGHT));
        }

        if (level.getBlockEntity(core) instanceof NetherReactorCoreBlockEntity reactor) {
            reactor.startActivePhase(obsidian);
        }
    }

    /** The netherrack floor sits directly under the bottom layer of the activation structure. */
    public static int floorY(BlockPos core) {
        return core.getY() - 2;
    }

    /** Hollow, flat-walled tiered tower of netherrack centred on the core. */
    private static void generateSpire(Level level, BlockPos core) {
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        int floorY = floorY(core);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int layer = 0; layer <= SPIRE_HEIGHT; layer++) {
            int extent = extentAtLayer(layer);
            int extentAbove = layer == SPIRE_HEIGHT ? -1 : extentAtLayer(layer + 1);
            for (int dx = -extent; dx <= extent; dx++) {
                for (int dz = -extent; dz <= extent; dz++) {
                    int ring = Math.max(Math.abs(dx), Math.abs(dz));
                    boolean wall = ring == extent;
                    boolean floor = layer == 0;
                    // Roofs the step wherever the tier above has pulled in, and caps the very top.
                    boolean ledge = ring > extentAbove;
                    if (!wall && !floor && !ledge) {
                        continue;
                    }
                    cursor.set(core.getX() + dx, floorY + layer, core.getZ() + dz);
                    if (isProtected(level, core, cursor)) {
                        continue;
                    }
                    level.setBlock(cursor, netherrack, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    private static int extentAtLayer(int layer) {
        if (layer <= 11) {
            return SPIRE_RADIUS;
        }
        return layer <= 14 ? 6 : 4;
    }

    private static boolean isProtected(Level level, BlockPos core, BlockPos pos) {
        // Leave the whole 3x3x3 activation structure (cobblestone, obsidian, core) alone.
        if (Math.abs(pos.getX() - core.getX()) <= 1 && Math.abs(pos.getZ() - core.getZ()) <= 1
            && Math.abs(pos.getY() - core.getY()) <= 1) {
            return true;
        }
        BlockState existing = level.getBlockState(pos);
        if (existing.is(ModContent.GLOWING_OBSIDIAN) || existing.getBlock() instanceof NetherReactorCoreBlock) {
            return true;
        }
        // Never chew through bedrock / other indestructible terrain.
        return existing.getDestroySpeed(level, pos) < 0.0F;
    }

    /** Every block of the activation structure except the core itself. */
    public static List<BlockPos> structurePositions(BlockPos core) {
        List<BlockPos> positions = new ArrayList<>(18);
        for (int[] offset : CORNER_OFFSETS) {
            positions.add(core.offset(offset[0], -1, offset[1]));
            positions.add(core.offset(offset[0], 0, offset[1]));
        }
        for (int[] offset : CROSS_OFFSETS) {
            positions.add(core.offset(offset[0], -1, offset[1]));
            positions.add(core.offset(offset[0], 1, offset[1]));
        }
        return positions;
    }

    /** Reverts the tracked activation obsidian blocks once the reactor burns out. */
    public static void extinguishGlowingObsidian(Level level, List<BlockPos> tracked) {
        for (BlockPos pos : tracked) {
            if (level.getBlockState(pos).is(ModContent.GLOWING_OBSIDIAN)) {
                level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
            }
        }
    }

    public enum ReactorState implements StringRepresentable {
        UNUSED("unused"),
        ACTIVE("active"),
        USED("used");

        private final String name;

        ReactorState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
