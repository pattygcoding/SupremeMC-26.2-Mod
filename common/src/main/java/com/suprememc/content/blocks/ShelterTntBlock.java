package com.suprememc.content.blocks;

import com.suprememc.content.entity.ShelterTnt;
import com.suprememc.content.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;

public class ShelterTntBlock extends TntBlock {
    public ShelterTntBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static boolean prime(Level level, BlockPos pos, LivingEntity igniter) {
        if (!(level instanceof ServerLevel serverLevel)
            || !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            return false;
        }

        ShelterTnt shelterTnt = new ShelterTnt(ModEntities.SHELTER_TNT_ENTITY, level);
        shelterTnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        level.addFreshEntity(shelterTnt);
        level.playSound(null, shelterTnt.getX(), shelterTnt.getY(), shelterTnt.getZ(),
            SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        return true;
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (!level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            return;
        }

        ShelterTnt shelterTnt = new ShelterTnt(ModEntities.SHELTER_TNT_ENTITY, level);
        shelterTnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        shelterTnt.setFuse(ShelterTnt.getRandomShortFuse(shelterTnt.getFuse(), level.getRandom()));
        level.addFreshEntity(shelterTnt);
    }
}