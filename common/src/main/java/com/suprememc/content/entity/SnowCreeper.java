package com.suprememc.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnowCreeper extends Creeper {
    public SnowCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    @Override
    public void explodeCreeper() {
        if (this.level() instanceof ServerLevel level) {
            float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
            float radius = this.explosionRadius * explosionMultiplier;
            Map<BlockPos, BlockState> affectedStates = affectedStates(level, radius);
            this.dead = true;
            level.explode(this, this.getX(), this.getY(), this.getZ(), radius, false, Level.ExplosionInteraction.MOB);
            freezeAffectedBlocks(level, affectedStates);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(level, Entity.RemovalReason.KILLED);
            this.discard();
        }
    }

    private Map<BlockPos, BlockState> affectedStates(ServerLevel level, float radius) {
        Map<BlockPos, BlockState> states = new HashMap<>();
        if (!level.getGameRules().get(GameRules.MOB_GRIEFING)) {
            return states;
        }

        ServerExplosion explosion = new ServerExplosion(level, this,
            Explosion.getDefaultDamageSource(level, this), null, this.position(), radius, false,
            Explosion.BlockInteraction.DESTROY);
        List<BlockPos> positions = explosion.calculateExplodedPositions();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                states.put(pos.immutable(), state);
            }
        }
        return states;
    }

    private static void freezeAffectedBlocks(ServerLevel level, Map<BlockPos, BlockState> affectedStates) {
        affectedStates.forEach((pos, state) -> {
            BlockState replacement = state.getFluidState().is(FluidTags.WATER)
                ? Blocks.ICE.defaultBlockState()
                : Blocks.POWDER_SNOW.defaultBlockState();
            level.setBlock(pos, replacement, 3);
        });
    }
}