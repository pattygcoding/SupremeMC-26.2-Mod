package com.suprememc.mixin;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SnowballItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({BucketItem.class, EnderpearlItem.class, SnowballItem.class})
public abstract class MixinStackableItem {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties suprememc$increaseStackSize(Item.Properties properties) {
        return properties.stacksTo(64);
    }
}