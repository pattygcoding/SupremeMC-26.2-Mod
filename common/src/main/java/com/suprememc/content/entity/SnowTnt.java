package com.suprememc.content.entity;

import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnowTnt extends PrimedTnt {
    private static final float EXPLOSION_POWER = 4.0F;

    public SnowTnt(EntityType<? extends SnowTnt> type, Level level) {
        super(type, level);
        setBlockState(ModContent.SNOW_TNT.defaultBlockState());
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            level().addParticle(ParticleTypes.SNOWFLAKE,
                getX() + (random.nextDouble() - 0.5D) * 0.8D,
                getY() + random.nextDouble() * 0.8D,
                getZ() + (random.nextDouble() - 0.5D) * 0.8D,
                0.0D, -0.02D, 0.0D);
        }
    }

    public void explodeSnow() {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }

        Map<BlockPos, BlockState> affectedStates = affectedStates(level);
        level.explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER,
            false, Level.ExplosionInteraction.TNT);
        affectedStates.forEach((pos, state) -> {
            if (!state.isAir() && !state.getFluidState().is(FluidTags.WATER)) {
                level.setBlock(pos, Blocks.POWDER_SNOW.defaultBlockState(), 3);
            }
        });
    }

    private Map<BlockPos, BlockState> affectedStates(ServerLevel level) {
        Map<BlockPos, BlockState> states = new HashMap<>();
        ServerExplosion explosion = new ServerExplosion(level, this,
            Explosion.getDefaultDamageSource(level, this), null, this.position(), EXPLOSION_POWER,
            false, Explosion.BlockInteraction.DESTROY);
        List<BlockPos> positions = explosion.calculateExplodedPositions();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                states.put(pos.immutable(), state);
            }
        }
        return states;
    }
}
