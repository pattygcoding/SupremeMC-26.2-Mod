package com.suprememc.mixin;

import com.suprememc.content.blocks.SkylandsPortalShape;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class MixinFlintAndSteelItem {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void suprememc$activateSkylandsPortal(UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
        var level = context.getLevel();
        var portalPos = context.getClickedPos().relative(context.getClickedFace());
        if (SkylandsPortalShape.tryCreate(level, portalPos)) {
            level.playSound(context.getPlayer(), portalPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
                level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, context.getClickedPos());
            if (context.getPlayer() instanceof ServerPlayer player) context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
            callback.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}