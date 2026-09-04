package com.suprememc.mixin;

import com.suprememc.content.entity.AbyssaliteTridentEntity;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTypePredicate.class)
public abstract class MixinEntityTypePredicate {
    @Inject(method = "matches(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;)Z", at = @At("RETURN"), cancellable = true)
    private void suprememc$allowAbyssaliteTrident(Entity entity, net.minecraft.server.level.ServerLevel level,
            net.minecraft.world.phys.Vec3 position, CallbackInfoReturnable<Boolean> callback) {
        if (!callback.getReturnValue() && entity instanceof AbyssaliteTridentEntity trident
            && ((EntityTypePredicate) (Object) this).matches(EntityTypes.TRIDENT.builtInRegistryHolder())) {
            callback.setReturnValue(true);
        }
    }
}