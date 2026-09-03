package com.suprememc.content.entity;

import com.suprememc.content.ModContent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AbyssaliteTridentEntity extends ThrownTrident {
    public AbyssaliteTridentEntity(EntityType<AbyssaliteTridentEntity> type, Level level) { super(type, level); }

    public AbyssaliteTridentEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(ModContent.ABYSSALITE_TRIDENT_ENTITY, level);
        setPos(x, y, z);
        setPickupItemStack(stack);
        setBaseDamage(9.0D);
    }

    @Override
    public boolean ignoreExplosion(net.minecraft.world.level.Explosion explosion) { return true; }
}
