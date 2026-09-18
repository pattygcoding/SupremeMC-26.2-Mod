package com.suprememc.content.blocks;

import com.suprememc.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class IcetherPortalBlock extends NetherPortalBlock {
    public static final ResourceKey<Level> ICETHER = ResourceKey.create(Registries.DIMENSION,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "icether"));
    public static final SavedDataType<PortalPositionCache> PORTAL_CACHE = PortalPositionCache.type("icether_portals");
    private static final int EXISTING_PORTAL_SEARCH_RADIUS = 128;

    public IcetherPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int particle = 0; particle < 4; particle++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z,
                (random.nextDouble() - 0.5D) * 0.04D, -random.nextDouble() * 0.02D, (random.nextDouble() - 0.5D) * 0.04D);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        Direction.Axis axis = state.getValue(AXIS);
        boolean wrongAxis = direction.getAxis().isHorizontal() && direction.getAxis() != axis;
        return !wrongAxis && !neighbourState.is(this) && !IcetherPortalShape.isComplete(level, pos, axis)
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        ResourceKey<Level> targetKey = currentLevel.dimension() == ICETHER ? Level.OVERWORLD : ICETHER;
        ServerLevel target = currentLevel.getServer().getLevel(targetKey);
        if (target == null) return null;
        int x = (int)Math.floor(entity.getX());
        int z = (int)Math.floor(entity.getZ());
        BlockPos approxOrigin = new BlockPos(x, (int)Math.floor(entity.getY()), z);
        PortalPositionCache cache = target.getDataStorage().computeIfAbsent(PORTAL_CACHE);
        BlockPos exit = cache.findNearby(target, this, approxOrigin, EXISTING_PORTAL_SEARCH_RADIUS).orElse(null);
        if (exit == null) {
            exit = createExitPortal(target, approxOrigin,
                Blocks.BLUE_ICE.defaultBlockState(), com.suprememc.content.ModContent.ICETHER_PORTAL.defaultBlockState().setValue(AXIS, Direction.Axis.X));
            cache.record(exit);
        }
        return new TeleportTransition(target, new Vec3(exit.getX() + 0.5D, exit.getY(), exit.getZ() + 0.5D), Vec3.ZERO,
            entity.getYRot(), entity.getXRot(), TeleportTransition.PLAY_PORTAL_SOUND);
    }

    public static BlockPos createExitPortal(ServerLevel level, BlockPos origin, BlockState frame, BlockState portal) {
        Direction direction = Direction.EAST;
        Direction sideways = direction.getClockWise();
        WorldBorder border = level.getWorldBorder();
        int maxY = Math.min(level.getMaxY(), level.getMinY() + level.getLogicalHeight() - 1);
        BlockPos best = null;
        double bestDistance = -1.0D;
        BlockPos.MutableBlockPos mutable = origin.mutable();

        for (BlockPos.MutableBlockPos column : BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
            if (!border.isWithinBounds(column) || !border.isWithinBounds(column.move(direction))) continue;
            column.move(direction.getOpposite());
            int startY = Math.min(maxY, level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, column.getX(), column.getZ()));
            for (int y = startY; y >= level.getMinY(); y--) {
                column.setY(y);
                if (!canReplace(level, column)) continue;
                int firstEmptyY = y;
                while (y > level.getMinY() && canReplace(level, column.move(Direction.DOWN))) y--;
                if (y + 4 > maxY) continue;
                int deltaY = firstEmptyY - y;
                if (deltaY > 0 && deltaY < 3) continue;
                if (!canHostPortal(level, column, mutable, direction, sideways, 0)) continue;
                if (canHostPortal(level, column, mutable, direction, sideways, -1)
                    && canHostPortal(level, column, mutable, direction, sideways, 1)) {
                    double distance = origin.distSqr(column);
                    if (bestDistance == -1.0D || distance < bestDistance) {
                        bestDistance = distance;
                        best = column.immutable();
                    }
                }
            }
        }

        if (best == null) {
            int minY = Math.max(level.getMinY() + 1, 70);
            int fallbackMaxY = maxY - 9;
            int y = Math.clamp(origin.getY(), minY, Math.max(minY, fallbackMaxY));
            best = border.clampToBounds(origin.relative(direction.getOpposite()).atY(y));
            for (int depth = -1; depth < 2; depth++) {
                for (int width = 0; width < 2; width++) {
                    for (int height = -1; height < 3; height++) {
                        mutable.setWithOffset(best, width * direction.getStepX() + depth * sideways.getStepX(), height,
                            width * direction.getStepZ() + depth * sideways.getStepZ());
                        level.setBlock(mutable, height < 0 ? frame : Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        for (int width = -1; width < 3; width++) {
            for (int height = -1; height < 4; height++) {
                if (width == -1 || width == 2 || height == -1 || height == 3) {
                    mutable.setWithOffset(best, width * direction.getStepX(), height, width * direction.getStepZ());
                    level.setBlock(mutable, frame, 3);
                }
            }
        }
        for (int width = 0; width < 2; width++) {
            for (int height = 0; height < 3; height++) {
                mutable.setWithOffset(best, width * direction.getStepX(), height, width * direction.getStepZ());
                level.setBlock(mutable, portal, 18);
            }
        }
        return best;
    }

    private static boolean canReplace(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() && state.getFluidState().isEmpty();
    }

    private static boolean canHostPortal(ServerLevel level, BlockPos origin, BlockPos.MutableBlockPos mutable,
                                         Direction direction, Direction sideways, int depth) {
        for (int width = -1; width < 3; width++) {
            for (int height = -1; height < 4; height++) {
                mutable.setWithOffset(origin, direction.getStepX() * width + sideways.getStepX() * depth, height,
                    direction.getStepZ() * width + sideways.getStepZ() * depth);
                if (height < 0 ? !level.getBlockState(mutable).isSolid() : !canReplace(level, mutable)) return false;
            }
        }
        return true;
    }
}