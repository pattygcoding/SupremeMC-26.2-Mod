package com.suprememc.mixin;

import com.suprememc.content.entity.SnowTnt;
import com.suprememc.content.entity.FireTnt;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public class MixinPrimedTnt {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void suprememc$explodeSnowTnt(CallbackInfo callback) {
        if ((Object) this instanceof SnowTnt snowTnt) {
            snowTnt.explodeSnow();
            callback.cancel();
        } else if ((Object) this instanceof FireTnt fireTnt) {
            fireTnt.explodeFire();
            callback.cancel();
        }
    }
}