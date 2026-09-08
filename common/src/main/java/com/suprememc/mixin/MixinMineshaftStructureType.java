package com.suprememc.mixin;

import com.suprememc.content.worldgen.NetherMineshaftContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Nether mineshafts (NetherMineshaftStructure) reuse vanilla's NORMAL mineshaft type as-is, but
// while one is actively placing blocks (see MixinStructureStart) its oak planks/fence become
// warped planks/fence. MESA-type mineshafts never run with the flag set, so no type check needed.
@Mixin(MineshaftStructure.Type.class)
public abstract class MixinMineshaftStructureType {

    @Inject(method = "getPlanksState", at = @At("HEAD"), cancellable = true)
    private void suprememc$netherPlanks(CallbackInfoReturnable<BlockState> callback) {
        if (NetherMineshaftContext.ACTIVE.get()) {
            callback.setReturnValue(Blocks.WARPED_PLANKS.defaultBlockState());
        }
    }

    @Inject(method = "getFenceState", at = @At("HEAD"), cancellable = true)
    private void suprememc$netherFence(CallbackInfoReturnable<BlockState> callback) {
        if (NetherMineshaftContext.ACTIVE.get()) {
            callback.setReturnValue(Blocks.WARPED_FENCE.defaultBlockState());
        }
    }
}
