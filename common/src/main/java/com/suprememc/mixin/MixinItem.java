package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.item.Item.class)
public abstract class MixinItem {
    @Inject(method = "hurtEnemy", at = @At("HEAD"))
    private void suprememc$burningFireAspect(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo callback) {
        net.minecraft.world.item.Item item = stack.getItem();
        if (item == ModContent.BURNING_NETHERITE_PICKAXE
                || item == ModContent.BURNING_NETHERITE_AXE
                || item == ModContent.BURNING_NETHERITE_SHOVEL
                || item == ModContent.BURNING_NETHERITE_HOE
                || item == ModContent.BURNING_NETHERITE_SWORD) {
            target.igniteForSeconds(8.0F);
        } else if (item == ModContent.BURNING_DIAMOND_PICKAXE
                || item == ModContent.BURNING_DIAMOND_AXE
                || item == ModContent.BURNING_DIAMOND_SHOVEL
                || item == ModContent.BURNING_DIAMOND_HOE
                || item == ModContent.BURNING_DIAMOND_SWORD) {
            target.igniteForSeconds(4.0F);
        }
    }
}