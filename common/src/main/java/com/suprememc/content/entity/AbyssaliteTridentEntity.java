package com.suprememc.content.entity;

import com.suprememc.content.init.ModEntities;
import com.suprememc.mixin.ThrownTridentAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AbyssaliteTridentEntity extends ThrownTrident {
    public AbyssaliteTridentEntity(EntityType<AbyssaliteTridentEntity> type, Level level) { super(type, level); }

    public AbyssaliteTridentEntity(Level level, LivingEntity owner, ItemStack stack) {
        this(level, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), stack);
        setOwner(owner);
    }

    public AbyssaliteTridentEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(ModEntities.ABYSSALITE_TRIDENT_ENTITY, level);
        setPos(x, y, z);
        setPickupItemStack(stack);
        setBaseDamage(9.0D);
        // The vanilla spawn constructors also sync Loyalty and the enchantment glint; replicate that here.
        ThrownTridentAccessor accessor = (ThrownTridentAccessor) this;
        getEntityData().set(ThrownTridentAccessor.getIdLoyalty(), accessor.callGetLoyaltyFromItem(stack));
        getEntityData().set(ThrownTridentAccessor.getIdFoil(), stack.hasFoil());
    }

    @Override
    public boolean ignoreExplosion(net.minecraft.world.level.Explosion explosion) { return true; }
}
