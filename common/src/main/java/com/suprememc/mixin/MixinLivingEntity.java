package com.suprememc.mixin;

import com.suprememc.content.items.AbyssaliteArmorItem;
import com.suprememc.content.ModEnchantments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Inject(method = "tick", at = @At("TAIL"))
    private void suprememc$updateCurses(CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return;
        }

        int amplifier = ModEnchantments.getCurseOfMassAmplifier(player);
        MobEffectInstance current = player.getEffect(MobEffects.SLOWNESS);
        if (amplifier >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 2, amplifier, true, false, true));
        } else if (current != null && current.isAmbient() && current.getDuration() <= 2) {
            player.removeEffect(MobEffects.SLOWNESS);
        }

        int slothAmplifier = ModEnchantments.getCurseOfSlothAmplifier(player);
        MobEffectInstance weakness = player.getEffect(MobEffects.WEAKNESS);
        if (slothAmplifier >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2, slothAmplifier, true, false, true));
        } else if (weakness != null && weakness.isAmbient() && weakness.getDuration() <= 2) {
            player.removeEffect(MobEffects.WEAKNESS);
        }
    }

    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    private void suprememc$blockMiningFatigue(MobEffectInstance effect, CallbackInfoReturnable<Boolean> callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player player && effect.getEffect() == MobEffects.MINING_FATIGUE
                && AbyssaliteArmorItem.isFullSet(player)) {
            callback.setReturnValue(false);
        }
    }
}