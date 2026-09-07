package com.suprememc.mixin;

import com.suprememc.content.enchantment.TensionEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// getPowerForTime is static and hardcodes the 20-tick draw, so the stack has to be supplied
// from releaseUsing's own arguments to know the Tension level.
@Mixin(BowItem.class)
public abstract class MixinBowItem {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    private float suprememc$tensionPower(int timeHeld, ItemStack itemStack, Level level,
            LivingEntity entity, int remainingTime) {
        return TensionEnchantment.getBowPowerForTime(itemStack, timeHeld);
    }
}
