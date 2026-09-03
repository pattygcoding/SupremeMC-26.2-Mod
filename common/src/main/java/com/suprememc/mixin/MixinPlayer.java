package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import com.suprememc.content.items.AbyssaliteArmorItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void suprememc$restoreUnderwaterToolSpeed(BlockState state, CallbackInfoReturnable<Float> callback) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        if (player.isEyeInFluid(net.minecraft.tags.FluidTags.WATER)
            && (held.getItem() == ModContent.ABYSSALITE_PICKAXE
            || held.getItem() == ModContent.ABYSSALITE_AXE
            || held.getItem() == ModContent.ABYSSALITE_SHOVEL
            || held.getItem() == ModContent.ABYSSALITE_HOE
            || held.getItem() == ModContent.ABYSSALITE_SWORD)) {
            callback.setReturnValue(callback.getReturnValue() * 5.0F);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void suprememc$clearArmorEffectsWhenIncomplete(CallbackInfo callback) {
        Player player = (Player) (Object) this;
        if (!AbyssaliteArmorItem.isFullSet(player)) {
            player.removeEffect(MobEffects.CONDUIT_POWER);
            player.removeEffect(MobEffects.DOLPHINS_GRACE);
        }
    }
}