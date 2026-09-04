package com.suprememc.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThrownTrident.class)
public interface ThrownTridentAccessor {
    @Accessor("ID_LOYALTY")
    static EntityDataAccessor<Byte> getIdLoyalty() {
        throw new AssertionError();
    }

    @Accessor("ID_FOIL")
    static EntityDataAccessor<Boolean> getIdFoil() {
        throw new AssertionError();
    }

    @Invoker("getLoyaltyFromItem")
    byte callGetLoyaltyFromItem(ItemStack stack);
}
