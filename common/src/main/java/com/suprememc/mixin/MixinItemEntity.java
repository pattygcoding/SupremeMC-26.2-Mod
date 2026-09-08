package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {
    @Shadow
    public abstract net.minecraft.world.item.ItemStack getItem();

    @Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
    private void suprememc$ignoreExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> callback) {
        if (isAbyssaliteItem()) {
            callback.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void suprememc$floatInWater(CallbackInfo callback) {
        if (isAbyssaliteItem() && ((ItemEntity) (Object) this).isInWater()) {
            ItemEntity entity = (ItemEntity) (Object) this;
            net.minecraft.world.phys.Vec3 velocity = entity.getDeltaMovement();
            if (velocity.y() < 0.05D) {
                entity.setDeltaMovement(velocity.x(), Math.min(0.05D, velocity.y() + 0.01D), velocity.z());
            }
        }
    }

    private boolean isAbyssaliteItem() {
        net.minecraft.world.item.Item item = getItem().getItem();
        return item == ModContent.ATLANTIS_DEBRIS.asItem() || item == ModItems.ABYSSALITE_SCRAP
            || item == ModItems.ABYSSALITE_INGOT || item == ModContent.ABYSSALITE_BLOCK.asItem()
                || item == ModItems.ABYSSALITE_TRIDENT;
    }
}