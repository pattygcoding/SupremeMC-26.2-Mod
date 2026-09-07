package com.suprememc.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Spider.class)
public abstract class MixinSpiderSpawn {
    @Redirect(
            method = "finalizeSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int suprememc$useDifficultyJockeyChance(
            RandomSource random, int bound, ServerLevelAccessor level, DifficultyInstance difficulty,
            EntitySpawnReason spawnReason, SpawnGroupData groupData) {
        int rollRange = switch (difficulty.getDifficulty()) {
            case NORMAL -> 50;
            case HARD -> 25;
            default -> 100;
        };
        return random.nextInt(rollRange);
    }
}