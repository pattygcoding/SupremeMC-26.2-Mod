package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModArmorMaterials;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class AbyssaliteArmorItem extends Item {
    public AbyssaliteArmorItem(String id, ArmorType type) { super(ModContent.itemProperties(id).humanoidArmor(ModArmorMaterials.ABYSSALITE, type)); }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof Player player && isFullSet(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 25, 0, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 25, 0, false, false, true));
        }
    }

    public static boolean isFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AbyssaliteArmorItem
            && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AbyssaliteArmorItem
            && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof AbyssaliteArmorItem
            && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof AbyssaliteArmorItem;
    }
}
