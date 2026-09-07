package com.suprememc.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Zombie.class)
public abstract class MixinZombieSpawn {
    @Redirect(
            method = "finalizeSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;getSpawnAsBabyOdds(Lnet/minecraft/util/RandomSource;)Z"))
    private boolean suprememc$useDifficultyBabyChance(
            RandomSource random, ServerLevelAccessor level, DifficultyInstance difficulty,
            EntitySpawnReason spawnReason, SpawnGroupData groupData) {
        float chance = switch (difficulty.getDifficulty()) {
            case EASY -> 0.05F;
            case NORMAL -> 0.10F;
            case HARD -> 0.15F;
            default -> 0.05F;
        };
        return random.nextFloat() < chance;
    }
}