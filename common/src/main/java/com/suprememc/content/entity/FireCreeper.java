package com.suprememc.content.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * A creeper variant whose explosion always creates fire. Everything else (goals, swelling,
 * flint-and-steel ignition, lightning charging, sounds, skull drops) is inherited unchanged
 * from {@link Creeper}.
 */
public class FireCreeper extends Creeper {
    public FireCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    /**
     * Mirrors vanilla's explosion exactly, except the fire flag is forced on.
     * (Creeper.explodeCreeper/spawnLingeringCloud/explosionRadius are private in vanilla and
     * are widened via the shared access transformer/access widener.)
     */
    @Override
    public void explodeCreeper() {
        if (this.level() instanceof ServerLevel level) {
            float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            level.explode(this, this.getX(), this.getY(), this.getZ(),
                this.explosionRadius * explosionMultiplier, true, Level.ExplosionInteraction.MOB);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(level, Entity.RemovalReason.KILLED);
            this.discard();
        }
    }
}
