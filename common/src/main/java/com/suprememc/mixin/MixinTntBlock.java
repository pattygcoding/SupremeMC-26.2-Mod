package com.suprememc.mixin;

import com.suprememc.content.blocks.SnowTntBlock;
import com.suprememc.content.blocks.FireTntBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TntBlock.class)
public class MixinTntBlock {
    @Inject(method = "prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
        at = @At("HEAD"), cancellable = true, require = 0)
    private static void suprememc$primeSnowTnt(Level level, BlockPos pos,
                                               CallbackInfoReturnable<Boolean> callback) {
        if (level.getBlockState(pos).getBlock() instanceof SnowTntBlock) {
            callback.setReturnValue(SnowTntBlock.prime(level, pos, null));
        } else if (level.getBlockState(pos).getBlock() instanceof FireTntBlock) {
            callback.setReturnValue(FireTntBlock.prime(level, pos, null));
        }
    }

    @Inject(method = "prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z",
        at = @At("HEAD"), cancellable = true, require = 0)
    private static void suprememc$primeSnowTntWithIgniter(Level level, BlockPos pos, LivingEntity igniter,
                                                           CallbackInfoReturnable<Boolean> callback) {
        if (level.getBlockState(pos).getBlock() instanceof SnowTntBlock) {
            callback.setReturnValue(SnowTntBlock.prime(level, pos, igniter));
        } else if (level.getBlockState(pos).getBlock() instanceof FireTntBlock) {
            callback.setReturnValue(FireTntBlock.prime(level, pos, igniter));
        }
    }
}