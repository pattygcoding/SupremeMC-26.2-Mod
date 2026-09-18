package com.suprememc.mixin;

import com.suprememc.content.blocks.IcetherPortalShape;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SolidBucketItem.class)
public class MixinSolidBucketItem {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void suprememc$activateIcetherPortal(UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
        SolidBucketItem bucket = (SolidBucketItem)(Object)this;
        Player player = context.getPlayer();
        if (player != null && bucket.getBlock() == Blocks.POWDER_SNOW
            && IcetherPortalShape.tryCreate(context.getLevel(), context.getClickedPos().relative(context.getClickedFace()))) {
            callback.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(
                BucketItem.getEmptySuccessItem(context.getItemInHand(), player)));
        }
    }
}