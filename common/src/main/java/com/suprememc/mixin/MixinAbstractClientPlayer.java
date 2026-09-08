package com.suprememc.mixin;

import com.suprememc.content.enchantment.TensionEnchantment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Redirect(method = "getFieldOfViewModifier", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;getTicksUsingItem()I"))
        private int suprememc$tensionFovTicks(AbstractClientPlayer player) {
        int ticksUsingItem = player.getTicksUsingItem();
        int drawTicks = TensionEnchantment.getBowDrawTicks(player.getUseItem());
        return Math.round(ticksUsingItem * BowItem.MAX_DRAW_DURATION / (float) drawTicks);
    }
}