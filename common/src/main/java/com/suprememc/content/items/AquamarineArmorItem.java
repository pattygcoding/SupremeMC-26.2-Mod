package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class AquamarineArmorItem extends Item {
    public AquamarineArmorItem(String id, ArmorType type) { super(ModContent.itemProperties(id).humanoidArmor(ModContent.AQUAMARINE_ARMOR_MATERIAL, type)); }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof LivingEntity living && entity instanceof Player player) {
            boolean fullSet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AquamarineArmorItem
                && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AquamarineArmorItem
                && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof AquamarineArmorItem
                && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof AquamarineArmorItem;
            if (fullSet) living.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 25, 0, false, false, false));
        }
    }
}
