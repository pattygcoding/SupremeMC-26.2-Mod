package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class MixinThrownTrident {
    @Inject(method = "tick", at = @At("TAIL"))
    private void suprememc$floatInWater(CallbackInfo callback) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        if (trident.getWeaponItem().is(ModContent.ABYSSALITE_TRIDENT) && trident.isInWater()) {
            Vec3 velocity = trident.getDeltaMovement();
            if (velocity.y() < 0.05D) {
                trident.setDeltaMovement(velocity.x(), Math.min(0.05D, velocity.y() + 0.01D), velocity.z());
            }
        }
    }
}