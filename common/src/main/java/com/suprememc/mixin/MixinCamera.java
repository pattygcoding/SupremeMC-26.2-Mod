package com.suprememc.mixin;

import com.suprememc.content.fluid.LiquidNitrogenFluid;
import net.minecraft.client.Camera;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jspecify.annotations.Nullable;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    @Nullable
    private Level level;

    @Inject(method = "getFluidInCamera", at = @At("RETURN"), cancellable = true)
    private void suprememc$liquidNitrogenFog(CallbackInfoReturnable<FogType> cir) {
        if (cir.getReturnValue() == FogType.LAVA && this.level != null
                && this.level.getFluidState(((Camera) (Object) this).blockPosition()).getType() instanceof LiquidNitrogenFluid) {
            cir.setReturnValue(FogType.POWDER_SNOW);
        }
    }
}
