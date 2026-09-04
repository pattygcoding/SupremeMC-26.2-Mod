package com.suprememc.content.entity;

import com.suprememc.content.ModContent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.level.Level;

public class GrizzlyBear extends PolarBear {
    public GrizzlyBear(EntityType<? extends PolarBear> type, Level level) {
        super(type, level);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModContent.GRIZZLY_BEAR_ENTITY.create(level, EntitySpawnReason.BREEDING);
    }
}