package com.suprememc.content.entity;

import com.suprememc.content.ModContent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class FireTnt extends PrimedTnt {
    private static final float EXPLOSION_POWER = 4.0F;

    public FireTnt(EntityType<? extends FireTnt> type, Level level) {
        super(type, level);
        setBlockState(ModContent.FIRE_TNT.defaultBlockState());
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            level().addParticle(ParticleTypes.FLAME,
                getX() + (random.nextDouble() - 0.5D) * 0.8D,
                getY() + random.nextDouble() * 0.8D,
                getZ() + (random.nextDouble() - 0.5D) * 0.8D,
                0.0D, 0.02D, 0.0D);
            level().addParticle(ParticleTypes.SMOKE,
                getX() + (random.nextDouble() - 0.5D) * 0.6D,
                getY() + 0.5D + random.nextDouble() * 0.5D,
                getZ() + (random.nextDouble() - 0.5D) * 0.6D,
                0.0D, 0.02D, 0.0D);
        }
    }

    public void explodeFire() {
        if (this.level() instanceof ServerLevel level) {
            level.explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER,
                true, Level.ExplosionInteraction.TNT);
        }
    }
}
