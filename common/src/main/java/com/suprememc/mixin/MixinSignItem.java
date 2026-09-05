package com.suprememc.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SignItem.class)
public abstract class MixinSignItem {
    // SignItem's constructor chains to StandingAndWallBlockItem(sign, wallSign, direction,
    // properties), so hook that exact 4-arg super call and bump the stack size.
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/StandingAndWallBlockItem;<init>(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/Direction;Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties suprememc$increaseStackSize(Item.Properties properties) {
        return properties.stacksTo(64);
    }
}