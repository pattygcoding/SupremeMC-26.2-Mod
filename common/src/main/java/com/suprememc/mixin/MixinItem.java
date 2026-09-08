package com.suprememc.mixin;

import com.suprememc.content.items.MilkBucketPlacement;
import com.suprememc.content.init.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.Item.class)
public abstract class MixinItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void suprememc$placeMilkBucket(Level level, Player player, InteractionHand hand,
                                            CallbackInfoReturnable<InteractionResult> callback) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.MILK_BUCKET)) {
            return;
        }

        HitResult hit = player.pick(5.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK
            && MilkBucketPlacement.place(player, level, (BlockHitResult) hit)) {
            callback.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"))
    private void suprememc$burningFireAspect(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo callback) {
        net.minecraft.world.item.Item item = stack.getItem();
        if (item == ModItems.BURNING_NETHERITE_PICKAXE
            || item == ModItems.BURNING_NETHERITE_AXE
            || item == ModItems.BURNING_NETHERITE_SHOVEL
            || item == ModItems.BURNING_NETHERITE_HOE
            || item == ModItems.BURNING_NETHERITE_SWORD) {
            target.igniteForSeconds(8.0F);
        } else if (item == ModItems.BURNING_DIAMOND_PICKAXE
            || item == ModItems.BURNING_DIAMOND_AXE
            || item == ModItems.BURNING_DIAMOND_SHOVEL
            || item == ModItems.BURNING_DIAMOND_HOE
            || item == ModItems.BURNING_DIAMOND_SWORD) {
            target.igniteForSeconds(4.0F);
        }
    }
}