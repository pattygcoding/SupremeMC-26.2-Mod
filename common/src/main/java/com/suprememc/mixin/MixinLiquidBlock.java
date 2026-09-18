package com.suprememc.mixin;

import com.suprememc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlock.class)
public abstract class MixinLiquidBlock {
    private static final ResourceKey<Biome> ICETHER_WASTES =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "icether_wastes"));

    @Inject(method = "onPlace", at = @At("HEAD"))
    private void suprememc$freezeWater(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockState oldState,
        boolean movedByPiston,
        CallbackInfo callback
    ) {
        if (state.getFluidState().is(FluidTags.WATER) && level.getBiome(pos).is(ICETHER_WASTES)) {
            level.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);
        }
    }
}