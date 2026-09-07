package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public abstract class MixinPotionBrewing {
    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private void suprememc$requireAwkwardSplashPotion(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> cir) {
        if (input.is(Items.SPLASH_POTION) && ingredient.is(ModContent.EXPERIENCE_DUST)) {
            PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents.is(Potions.AWKWARD)) {
                cir.setReturnValue(new ItemStack(Items.EXPERIENCE_BOTTLE));
            } else {
                cir.setReturnValue(ItemStack.EMPTY);
            }
            return;
        }
    }
}
