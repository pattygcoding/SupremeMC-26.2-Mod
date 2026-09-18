package com.suprememc.content.blocks;

import com.suprememc.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SkylandsPortalBlock extends NetherPortalBlock {
    public static final ResourceKey<Level> SKYLANDS = ResourceKey.create(Registries.DIMENSION,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "skylands"));
    public static final SavedDataType<PortalPositionCache> PORTAL_CACHE = PortalPositionCache.type("skylands_portals");
    private static final int EXISTING_PORTAL_SEARCH_RADIUS = 128;

    public SkylandsPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        Direction.Axis axis = state.getValue(AXIS);
        boolean wrongAxis = direction.getAxis().isHorizontal() && direction.getAxis() != axis;
        return !wrongAxis && !neighbourState.is(this) && !SkylandsPortalShape.isComplete(level, pos, axis)
            ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        ServerLevel target = currentLevel.getServer().getLevel(currentLevel.dimension() == SKYLANDS ? Level.OVERWORLD : SKYLANDS);
        if (target == null) return null;
        PortalPositionCache cache = target.getDataStorage().computeIfAbsent(PORTAL_CACHE);
        BlockPos exit = cache.findNearby(target, this, entity.blockPosition(), EXISTING_PORTAL_SEARCH_RADIUS).orElse(null);
        if (exit == null) {
            exit = IcetherPortalBlock.createExitPortal(target, entity.blockPosition(), com.suprememc.content.ModContent.GLOWING_OBSIDIAN.defaultBlockState(),
                com.suprememc.content.ModContent.SKYLANDS_PORTAL.defaultBlockState().setValue(AXIS, Direction.Axis.X));
            cache.record(exit);
        }
        return new TeleportTransition(target, new Vec3(exit.getX() + 0.5D, exit.getY(), exit.getZ() + 0.5D), Vec3.ZERO,
            entity.getYRot(), entity.getXRot(), TeleportTransition.PLAY_PORTAL_SOUND);
    }
}