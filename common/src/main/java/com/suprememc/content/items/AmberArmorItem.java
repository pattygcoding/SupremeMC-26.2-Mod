package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModArmorMaterials;
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

public class AmberArmorItem extends Item {
    public AmberArmorItem(String id, ArmorType type) { super(ModContent.itemProperties(id).humanoidArmor(ModArmorMaterials.AMBER, type)); }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof LivingEntity living && entity instanceof Player player) {
            boolean fullSet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AmberArmorItem
                && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AmberArmorItem
                && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof AmberArmorItem
                && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof AmberArmorItem;
            if (fullSet) living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 205, 0, false, false, false));
        }
    }
}