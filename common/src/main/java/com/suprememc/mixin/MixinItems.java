package com.suprememc.mixin;

import net.minecraft.references.BlockItemId;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class MixinItems {
    // In 26.2 cake's BlockItem is built inside Items' private registerBlock helper via a
    // factory method-ref (BlockItem::new), so there is no BlockItem.<init> call site in
    // <clinit> to hook. Cake is registered through the 3-arg registerBlock overload with a
    // fresh Item.Properties; since Item.Properties.stacksTo/component mutate that object in
    // place, mutating it at HEAD changes the registered cake item's max stack size.
    @Inject(method = "registerBlock(Lnet/minecraft/references/BlockItemId;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            at = @At("HEAD"))
    private static void suprememc$makeCakesStackable(BlockItemId id, Block block, Item.Properties properties, CallbackInfoReturnable<Item> callback) {
        if (block instanceof CakeBlock) {
            properties.stacksTo(64);
        }
    }
}