package com.suprememc.mixin;

import com.suprememc.content.items.MilkBottlePlacement;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.BottleItem.class)
public abstract class MixinBottleItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void suprememc$fillMilkBottle(Level level, Player player, InteractionHand hand,
                                          CallbackInfoReturnable<InteractionResult> callback) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.GLASS_BOTTLE)) {
            return;
        }

        HitResult hit = player.pick(5.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK
            && MilkBottlePlacement.fill(player, hand, level, ((BlockHitResult) hit).getBlockPos()) != InteractionResult.PASS) {
            callback.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}