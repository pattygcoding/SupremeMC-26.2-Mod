package com.suprememc.content.blocks;

import com.suprememc.content.init.ModEntities;
import com.suprememc.content.entity.SnowTnt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;

public class SnowTntBlock extends TntBlock {
    public SnowTntBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static boolean prime(Level level, BlockPos pos, LivingEntity igniter) {
        if (!(level instanceof ServerLevel serverLevel)
            || !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            return false;
        }

        SnowTnt snowTnt = new SnowTnt(ModEntities.SNOW_TNT_ENTITY, level);
        snowTnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        level.addFreshEntity(snowTnt);
        level.playSound(null, snowTnt.getX(), snowTnt.getY(), snowTnt.getZ(),
            SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        return true;
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
        if (!level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            return;
        }

        SnowTnt snowTnt = new SnowTnt(ModEntities.SNOW_TNT_ENTITY, level);
        snowTnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        snowTnt.setFuse(SnowTnt.getRandomShortFuse(snowTnt.getFuse(), level.getRandom()));
        level.addFreshEntity(snowTnt);
    }
}
