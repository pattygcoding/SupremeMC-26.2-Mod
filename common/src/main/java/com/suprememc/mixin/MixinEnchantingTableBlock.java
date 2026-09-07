package com.suprememc.mixin;

import com.suprememc.content.blocks.BookshelfBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
public abstract class MixinEnchantingTableBlock {
    @Inject(method = "isValidBookShelf", at = @At("HEAD"), cancellable = true)
    private static void suprememc$customBookshelf(Level level, BlockPos enchantingTablePos, BlockPos offset,
            CallbackInfoReturnable<Boolean> callback) {
        BlockPos bookshelfPos = enchantingTablePos.offset(offset);
        if (level.getBlockState(bookshelfPos).getBlock() instanceof BookshelfBlock) {
            BlockPos betweenPos = enchantingTablePos.offset(offset.getX() / 2, offset.getY() / 2, offset.getZ() / 2);
            if (level.getBlockState(betweenPos).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
                callback.setReturnValue(true);
            }
        }
    }
}